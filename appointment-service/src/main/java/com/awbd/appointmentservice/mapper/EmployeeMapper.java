package com.awbd.appointmentservice.mapper;

import com.awbd.appointmentservice.dto.EmployeeDto;
import com.awbd.appointmentservice.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee mapToEntity(EmployeeDto employeeDto);
}
