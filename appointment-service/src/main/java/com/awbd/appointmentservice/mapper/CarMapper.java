package com.awbd.appointmentservice.mapper;

import com.awbd.appointmentservice.dto.CarDto;
import com.awbd.appointmentservice.model.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car mapToEntity(CarDto carDto);
}
