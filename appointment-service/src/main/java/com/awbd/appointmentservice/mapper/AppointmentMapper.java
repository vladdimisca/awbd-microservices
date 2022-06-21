package com.awbd.appointmentservice.mapper;

import com.awbd.appointmentservice.dto.AppointmentDto;
import com.awbd.appointmentservice.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "jobId", source = "job.id")
    AppointmentDto mapToDto(Appointment appointment);

    @Mapping(target = "car.id", source = "carId")
    @Mapping(target = "job.id", source = "jobId")
    Appointment mapToEntity(AppointmentDto appointmentDto);
}
