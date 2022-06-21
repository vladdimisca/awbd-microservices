package com.awbd.carservice.mapper;

import com.awbd.carservice.dto.CarDto;
import com.awbd.carservice.model.Car;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDto mapToDto(Car car);

    Car mapToEntity(CarDto carDto);
}
