package com.awbd.carservice.dto;

import com.awbd.carservice.model.CarType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
public class CarDto extends RepresentationModel<CarDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotNull(message = "Type is mandatory.")
    CarType type;

    @NotNull(message = "License plate is mandatory.")
    @Size(min = 1, max = 100, message = "License plate must be between 1 and 100 characters long.")
    String licensePlate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime createdAt;
}
