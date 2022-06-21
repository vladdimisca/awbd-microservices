package com.awbd.carservice.controller;

import com.awbd.carservice.dto.CarDto;
import com.awbd.carservice.mapper.CarMapper;
import com.awbd.carservice.model.Car;
import com.awbd.carservice.service.CarService;
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
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    @Operation(summary = "Create car")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Car created",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = CarDto.class)
                            )
                    }
            )
    })
    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<CarDto> create(@Valid @RequestBody CarDto carDto) {
        Car car = carService.create(carMapper.mapToEntity(carDto));
        CarDto result = carMapper.mapToDto(car);
        Link selfLink = linkTo(methodOn(CarController.class).create(carDto)).withSelfRel();
        result.add(selfLink);
        Link getLink = linkTo(methodOn(CarController.class).getById(car.getId())).withRel("getCar");
        result.add(getLink);
        return ResponseEntity
                .created(URI.create("/cars/" + car.getId()))
                .body(result);
    }

    @Operation(summary = "Update car by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "car updated",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = CarDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)
    })
    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<CarDto> update(@PathVariable("id") Long id, @Valid @RequestBody CarDto carDto) {
        Car car = carService.update(id, carMapper.mapToEntity(carDto));
        CarDto result = carMapper.mapToDto(car);
        Link selfLink = linkTo(methodOn(CarController.class).update(id, carDto)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cars found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarDto.class))
                            )
                    }
            )})
    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<CarDto>> getAll() {
        Link link = linkTo(methodOn(CarController.class).getAll()).withSelfRel();
        List<CarDto> carsDto = carService.getAll().stream().
                map((car) -> {
                    CarDto carDto = carMapper.mapToDto(car);
                    Link selfLink = linkTo(methodOn(CarController.class).getById(car.getId())).withSelfRel();
                    carDto.add(selfLink);
                    return carDto;
                }).collect(Collectors.toList());
        CollectionModel<CarDto> result = CollectionModel.of(carsDto, link);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get car by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "car found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = CarDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)})
    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<CarDto> getById(@PathVariable("id") Long id) {
        Car car = carService.getById(id);
        CarDto result = carMapper.mapToDto(car);
        Link selfLink = linkTo(methodOn(CarController.class).getById(id)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found", content = @Content)})
    @DeleteMapping(value ="/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
