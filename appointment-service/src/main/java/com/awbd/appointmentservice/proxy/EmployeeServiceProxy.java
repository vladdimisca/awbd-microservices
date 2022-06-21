package com.awbd.appointmentservice.proxy;

import com.awbd.appointmentservice.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "employeeservice")
public interface EmployeeServiceProxy {

    @GetMapping("/employees")
    CollectionModel<EmployeeDto> getAll();
}
