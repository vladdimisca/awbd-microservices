package com.awbd.appointmentservice.service.impl;

import com.awbd.appointmentservice.dto.CarDto;
import com.awbd.appointmentservice.dto.EmployeeDto;
import com.awbd.appointmentservice.dto.JobDto;
import com.awbd.appointmentservice.error.ErrorMessage;
import com.awbd.appointmentservice.error.ErrorResponse;
import com.awbd.appointmentservice.error.exception.BadRequestException;
import com.awbd.appointmentservice.error.exception.ConflictException;
import com.awbd.appointmentservice.error.exception.ResourceNotFoundException;
import com.awbd.appointmentservice.mapper.CarMapper;
import com.awbd.appointmentservice.mapper.EmployeeMapper;
import com.awbd.appointmentservice.mapper.JobMapper;
import com.awbd.appointmentservice.model.Appointment;
import com.awbd.appointmentservice.model.Employee;
import com.awbd.appointmentservice.model.Schedule;
import com.awbd.appointmentservice.proxy.CarServiceProxy;
import com.awbd.appointmentservice.proxy.EmployeeServiceProxy;
import com.awbd.appointmentservice.proxy.JobServiceProxy;
import com.awbd.appointmentservice.proxy.ScheduleServiceProxy;
import com.awbd.appointmentservice.repository.AppointmentRepository;
import com.awbd.appointmentservice.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final JobServiceProxy jobServiceProxy;
    private final CarServiceProxy carServiceProxy;
    private final EmployeeServiceProxy employeeServiceProxy;
    private final ScheduleServiceProxy scheduleServiceProxy;
    private final CarMapper carMapper;
    private final JobMapper jobMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public Appointment create(Appointment appointment) {
        JobDto job;
        CarDto car;
        try {
            job = jobServiceProxy.getById(appointment.getJob().getId());
            car = carServiceProxy.getById(appointment.getCar().getId());
        } catch (FeignException.FeignClientException e) {
            throw getThrowableException(e);
        }

        if (!car.getType().equals(job.getCarType())) {
            throw new BadRequestException(ErrorMessage.NOT_MATCHING, "Car type", "job");
        }

        checkSchedule(appointment.getStartTime(), job.getDurationMinutes());

        List<Employee> employees =
                getFreeEmployees(job.getNumberOfEmployees(), appointment.getStartTime(), job.getDurationMinutes());

        appointment.setEmployees(employees);
        appointment.setCar(carMapper.mapToEntity(car));
        appointment.setJob(jobMapper.mapToEntity(job));

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(Long id, Appointment appointment) {
        Appointment existingAppointment = getById(id);
        CarDto car;
        JobDto job;
        try {
            car = carServiceProxy.getById(appointment.getCar().getId());
            job = jobServiceProxy.getById(appointment.getJob().getId());
        } catch (FeignException.FeignClientException e) {
            throw getThrowableException(e);
        }

        if (!car.getType().equals(job.getCarType())) {
            throw new BadRequestException(ErrorMessage.NOT_MATCHING, "car type", "job");
        }

        checkSchedule(appointment.getStartTime(), job.getDurationMinutes());

        Appointment[] obsoleteAppointments = existingAppointment.getEmployees().stream()
                .flatMap(employee -> employee.getAppointments().stream())
                .toArray(Appointment[]::new);

        List<Employee> employees = getFreeEmployees(
                job.getNumberOfEmployees(), appointment.getStartTime(), job.getDurationMinutes(), obsoleteAppointments);

        existingAppointment.setEmployees(employees);
        existingAppointment.setStartTime(appointment.getStartTime());
        existingAppointment.setCar(carMapper.mapToEntity(car));
        existingAppointment.setJob(jobMapper.mapToEntity(job));

        return appointmentRepository.save(existingAppointment);
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND, "appointment", id));
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Appointment appointment = getById(id);
        appointmentRepository.delete(appointment);
    }

    private void checkSchedule(LocalDateTime startTime, Long durationMinutes) {
        Schedule schedule = scheduleServiceProxy.getSchedule();
        log.info("Schedule app instance: {}", schedule.getInstanceId());

        LocalTime appointmentTime = startTime.toLocalTime();

        switch (startTime.getDayOfWeek()) {
            case MONDAY:
                checkTime(schedule.getStart().getMonday(), schedule.getStop().getMonday(), appointmentTime, durationMinutes);
                break;
            case TUESDAY:
                checkTime(schedule.getStart().getTuesday(), schedule.getStop().getTuesday(), appointmentTime, durationMinutes);
                break;
            case WEDNESDAY:
                checkTime(schedule.getStart().getWednesday(), schedule.getStop().getWednesday(), appointmentTime, durationMinutes);
                break;
            case THURSDAY:
                checkTime(schedule.getStart().getThursday(), schedule.getStop().getThursday(), appointmentTime, durationMinutes);
                break;
            case FRIDAY:
                checkTime(schedule.getStart().getFriday(), schedule.getStop().getFriday(), appointmentTime, durationMinutes);
                break;
            case SATURDAY:
                checkTime(schedule.getStart().getSaturday(), schedule.getStop().getSaturday(), appointmentTime, durationMinutes);
                break;
            case SUNDAY:
                checkTime(schedule.getStart().getSunday(), schedule.getStop().getSunday(), appointmentTime, durationMinutes);
        }
    }

    private void checkTime(LocalTime scheduleStart, LocalTime scheduleStop, LocalTime appointmentTime, Long durationMinutes) {
        if (scheduleStart == null || scheduleStop == null) {
            throw new BadRequestException(ErrorMessage.OFF_SCHEDULE);
        }
        if (scheduleStart.isAfter(appointmentTime) || scheduleStop.isBefore(appointmentTime.plusMinutes(durationMinutes))) {
            throw new BadRequestException(ErrorMessage.OFF_SCHEDULE);
        }
    }

    private List<Employee> getFreeEmployees(Integer numberOfEmployees, LocalDateTime startTime,
                                            Long durationMinutes, Appointment... obsoleteAppointments) {
        List<Employee> freeEmployees = new ArrayList<>();
        Collection<EmployeeDto> allEmployees = employeeServiceProxy.getAll().getContent();
        log.info(allEmployees.toString());
        for (EmployeeDto employee : allEmployees) {
            if (numberOfEmployees.equals(freeEmployees.size())) {
                return freeEmployees;
            }

            boolean isFree = true;
            List<Appointment> appointments = appointmentRepository.getAppointmentsByEmployees_Id(employee.getId());

            appointments.removeAll(Arrays.asList(obsoleteAppointments));
            for (Appointment appointment : appointments) {
                LocalDateTime start = appointment.getStartTime().plusMinutes(1);
                LocalDateTime end = appointment.getStartTime().plusMinutes(appointment.getJob().getDurationMinutes() - 1);
                if ((start.isAfter(startTime) && start.isBefore(startTime.plusMinutes(durationMinutes))) ||
                        (end.isAfter(startTime) && end.isBefore(startTime.plusMinutes(durationMinutes)))) {
                    isFree = false;
                    break;
                }
            }

            if (isFree) {
                freeEmployees.add(employeeMapper.mapToEntity(employee));
            }
        }
        if (freeEmployees.size() < numberOfEmployees) {
            throw new ConflictException(ErrorMessage.NOT_ENOUGH_RESOURCES, "employees");
        }
        return freeEmployees;
    }

    private RuntimeException getThrowableException(FeignException.FeignClientException e) {
        if (e.status() == 404 && e.responseBody().isPresent()) {
            ErrorResponse errorResponse;
            try {
                errorResponse = new ObjectMapper().readValue(e.responseBody().get().array(), ErrorResponse.class);
                return new BadRequestException(errorResponse);
            } catch (IOException ex) {
                return e;
            }
        }
        return e;
    }
}
