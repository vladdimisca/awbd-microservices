package com.awbd.appointmentservice.dto;

import com.awbd.appointmentservice.model.validator.AppointmentTimeConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class AppointmentDto extends RepresentationModel<AppointmentDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @AppointmentTimeConstraint
    @NotNull(message = "Start time is mandatory.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime startTime;

    @NotNull(message = "Car is mandatory.")
    Long carId;

    @NotNull(message = "Job is mandatory.")
    Long jobId;
}
