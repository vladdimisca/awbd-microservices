package com.awbd.appointmentservice.dto;

import com.awbd.appointmentservice.model.CarType;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Value
public class CarDto extends RepresentationModel<CarDto> {

    Long id;
    CarType type;
    String licensePlate;
    LocalDateTime createdAt;
}
