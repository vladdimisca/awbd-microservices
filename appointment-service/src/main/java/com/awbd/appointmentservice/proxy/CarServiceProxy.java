package com.awbd.appointmentservice.proxy;

import com.awbd.appointmentservice.dto.CarDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "carservice")
public interface CarServiceProxy {

    @GetMapping("/cars/{id}")
    CarDto getById(@PathVariable("id") Long id);
}
