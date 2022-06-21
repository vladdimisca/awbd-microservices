package com.awbd.employeeservice.controller;

import com.awbd.employeeservice.dto.EmployeeDto;
import com.awbd.employeeservice.mapper.EmployeeMapper;
import com.awbd.employeeservice.model.Employee;
import com.awbd.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Operation(summary = "Create employee")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = EmployeeDto.class)
                            )
                    }
            )
    })
    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeService.create(employeeMapper.mapToEntity(employeeDto));
        EmployeeDto result = employeeMapper.mapToDto(employee);
        Link selfLink = linkTo(methodOn(EmployeeController.class).create(employeeDto)).withSelfRel();
        result.add(selfLink);
        Link getLink = linkTo(methodOn(EmployeeController.class).getById(employee.getId())).withRel("getEmployee");
        result.add(getLink);
        return ResponseEntity
                .created(URI.create("/employees/" + employee.getId()))
                .body(result);
    }

    @Operation(summary = "Update employee by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = EmployeeDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EmployeeDto> update(@PathVariable("id") Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeService.update(id, employeeMapper.mapToEntity(employeeDto));
        EmployeeDto result = employeeMapper.mapToDto(employee);
        Link selfLink = linkTo(methodOn(EmployeeController.class).update(id, employeeDto)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    array = @ArraySchema(schema = @Schema(implementation = EmployeeDto.class))
                            )
                    }
            )})
    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<EmployeeDto>> getAll() {
        Link link = linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel();
        List<EmployeeDto> employeeDtos = employeeService.getAll().stream().
                map((employee) -> {
                    EmployeeDto employeeDto = employeeMapper.mapToDto(employee);
                    Link selfLink = linkTo(methodOn(EmployeeController.class).getById(employee.getId())).withSelfRel();
                    employeeDto.add(selfLink);
                    return employeeDto;
                }).collect(Collectors.toList());
        CollectionModel<EmployeeDto> result = CollectionModel.of(employeeDtos, link);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get employee by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = EmployeeDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)})
    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EmployeeDto> getById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        EmployeeDto result = employeeMapper.mapToDto(employee);
        Link selfLink = linkTo(methodOn(EmployeeController.class).getById(id)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete employee by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
