package com.awbd.jobservice.dto;

import com.awbd.jobservice.model.CarType;
import com.awbd.jobservice.model.JobType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class JobDto extends RepresentationModel<JobDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotNull(message = "Type is mandatory.")
    @Enumerated(EnumType.STRING)
    JobType type;

    @NotNull(message = "Price is mandatory.")
    @Positive(message = "Price must be a positive number.")
    @Max(value = 1000, message = "Price must be at most 1000.")
    Double price;

    @NotNull(message = "Duration minutes is mandatory.")
    @Positive(message = "Duration minutes must be a positive number.")
    @Max(value = 90, message = "Duration minutes must be at most 90.")
    Long durationMinutes;

    @NotNull(message = "Car type is mandatory.")
    CarType carType;

    @NotNull(message = "Number of employees is mandatory.")
    @Positive(message = "Number of employees must be a positive number.")
    @Max(value = 5, message = "Number of employees must be at most 5.")
    Integer numberOfEmployees;
}
