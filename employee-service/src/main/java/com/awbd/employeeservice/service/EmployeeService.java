package com.awbd.employeeservice.service;

import com.awbd.employeeservice.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);

    Employee update(Long id, Employee employee);

    Employee getById(Long id);

    List<Employee> getAll();

    void deleteById(Long id);
}
