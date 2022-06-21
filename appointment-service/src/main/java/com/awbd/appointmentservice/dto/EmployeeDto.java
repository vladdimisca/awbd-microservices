package com.awbd.appointmentservice.dto;

import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Value
public class EmployeeDto extends RepresentationModel<EmployeeDto> {

    Long id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Double salary;
    LocalDate hireDate;
}
