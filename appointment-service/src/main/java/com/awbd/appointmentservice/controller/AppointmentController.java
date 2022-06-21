package com.awbd.appointmentservice.controller;

import com.awbd.appointmentservice.dto.AppointmentDto;
import com.awbd.appointmentservice.error.ErrorMessage;
import com.awbd.appointmentservice.error.exception.AbstractApiException;
import com.awbd.appointmentservice.error.exception.TemporaryUnavailableException;
import com.awbd.appointmentservice.mapper.AppointmentMapper;
import com.awbd.appointmentservice.model.Appointment;
import com.awbd.appointmentservice.service.AppointmentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @Operation(summary = "Create appointment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = AppointmentDto.class)
                            )
                    }
            )
    })
    @PostMapping(produces = {"application/hal+json"})
    @CircuitBreaker(name = "appointment", fallbackMethod = "createAppointmentFallback")
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.create(appointmentMapper.mapToEntity(appointmentDto));
        AppointmentDto result = appointmentMapper.mapToDto(appointment);
        Link selfLink = linkTo(methodOn(AppointmentController.class).create(appointmentDto)).withSelfRel();
        result.add(selfLink);
        Link getLink = linkTo(methodOn(AppointmentController.class).getById(appointment.getId())).withRel("getAppointment");
        result.add(getLink);
        return ResponseEntity
                .created(URI.create("/appointments/" + appointment.getId()))
                .body(result);
    }

    private ResponseEntity<AppointmentDto> createAppointmentFallback(AppointmentDto appointmentDto, Throwable t) {
        log.info("[Circuit breaker fallback] Request body: {}", appointmentDto);

        if (t instanceof AbstractApiException) {
            throw (AbstractApiException) t;
        }

        log.error(t.getMessage(), t);
        throw new TemporaryUnavailableException(ErrorMessage.TEMPORARY_UNAVAILABLE);
    }

    @Operation(summary = "Update appointment by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment updated",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = AppointmentDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Appointment not found", content = @Content)
    })
    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    @CircuitBreaker(name = "appointment", fallbackMethod = "updateAppointmentFallback")
    public ResponseEntity<AppointmentDto> update(@PathVariable("id") Long id, @Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.update(id, appointmentMapper.mapToEntity(appointmentDto));
        AppointmentDto result = appointmentMapper.mapToDto(appointment);
        Link selfLink = linkTo(methodOn(AppointmentController.class).getById(id)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    private ResponseEntity<AppointmentDto> updateAppointmentFallback(Long id, AppointmentDto appointmentDto, Throwable t) {
        log.info("[Circuit breaker fallback] Id: {}. Request body: {}", id, appointmentDto);

        if (t instanceof AbstractApiException) {
            throw (AbstractApiException) t;
        }

        log.error(t.getMessage(), t);
        throw new TemporaryUnavailableException(ErrorMessage.TEMPORARY_UNAVAILABLE);
    }

    @Operation(summary = "Get all appointments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointments found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class))
                            )
                    }
            )})
    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<AppointmentDto>> getAll() {
        Link link = linkTo(methodOn(AppointmentController.class).getAll()).withSelfRel();
        List<AppointmentDto> appointments = appointmentService.getAll().stream().
                map((appointment) -> {
                    AppointmentDto appointmentDto = appointmentMapper.mapToDto(appointment);
                    Link selfLink = linkTo(methodOn(AppointmentController.class).getById(appointment.getId())).withSelfRel();
                    appointmentDto.add(selfLink);
                    return appointmentDto;
                }).collect(Collectors.toList());
        CollectionModel<AppointmentDto> result = CollectionModel.of(appointments, link);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get appointment by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Appointment found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = AppointmentDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Appointment not found", content = @Content)})
    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<AppointmentDto> getById(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.getById(id);
        AppointmentDto result = appointmentMapper.mapToDto(appointment);
        Link selfLink = linkTo(methodOn(AppointmentController.class).getById(id)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Appointment not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
