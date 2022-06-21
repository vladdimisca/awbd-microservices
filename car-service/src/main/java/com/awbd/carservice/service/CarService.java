package com.awbd.carservice.service;

import com.awbd.carservice.model.Car;

import java.util.List;

public interface CarService {
    Car create(Car car);

    Car update(Long id, Car car);

    Car getById(Long id);

    List<Car> getAll();

    void deleteById(Long id);
}
