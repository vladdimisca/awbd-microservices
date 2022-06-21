package com.awbd.appointmentservice.dto;

import com.awbd.appointmentservice.model.CarType;
import com.awbd.appointmentservice.model.JobType;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
public class JobDto extends RepresentationModel<JobDto> {

    Long id;
    JobType type;
    Double price;
    Long durationMinutes;
    CarType carType;
    Integer numberOfEmployees;
}
