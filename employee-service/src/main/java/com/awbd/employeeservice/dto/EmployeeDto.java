package com.awbd.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Value
public class EmployeeDto extends RepresentationModel<EmployeeDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @NotNull(message = "First name is mandatory.")
    @Size(min = 1, max = 150, message = "First name must be between 1 and 100 characters long.")
    String firstName;

    @NotNull(message = "Last name is mandatory.")
    @Size(min = 1, max = 150, message = "Last name must be between 1 and 100 characters long.")
    String lastName;

    @NotEmpty(message="Email cannot be empty.")
    @Email(message = "Email has an invalid format.")
    String email;

    @NotNull(message = "Phone number is mandatory.")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Phone number is invalid.")
    String phoneNumber;

    @NotNull(message = "Salary is mandatory.")
    @Min(value = 1500, message = "Salary must be at least 1500.")
    @Max(value = 50000, message = "Salary must be at most 50000.")
    Double salary;

    @NotNull(message = "Hire date is mandatory.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate hireDate;
}
