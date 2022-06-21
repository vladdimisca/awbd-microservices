package com.awbd.employeeservice.mapper;

import com.awbd.employeeservice.dto.EmployeeDto;
import com.awbd.employeeservice.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto mapToDto(Employee employee);

    Employee mapToEntity(EmployeeDto employeeDto);
}
