package com.awbd.jobservice.controller;

import com.awbd.jobservice.dto.JobDto;
import com.awbd.jobservice.mapper.JobMapper;
import com.awbd.jobservice.model.Job;
import com.awbd.jobservice.service.JobService;
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
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @Operation(summary = "Create job")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Job created",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = JobDto.class)
                            )
                    }
            )
    })
    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<JobDto> create(@Valid @RequestBody JobDto jobDto) {
        Job job = jobService.create(jobMapper.mapToEntity(jobDto));
        JobDto result = jobMapper.mapToDto(job);
        Link selfLink = linkTo(methodOn(JobController.class).create(jobDto)).withSelfRel();
        result.add(selfLink);
        Link getLink = linkTo(methodOn(JobController.class).getById(job.getId())).withRel("getJob");
        result.add(getLink);
        return ResponseEntity
                .created(URI.create("/jobs/" + job.getId()))
                .body(result);
    }

    @Operation(summary = "Update job by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Job updated",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = JobDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "403", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)
    })
    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<JobDto> update(@PathVariable("id") Long id, @Valid @RequestBody JobDto jobDto) {
        Job job = jobService.update(id, jobMapper.mapToEntity(jobDto));
        JobDto result = jobMapper.mapToDto(job);
        Link selfLink = linkTo(methodOn(JobController.class).update(id, jobDto)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get all jobs")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Jobs found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    array = @ArraySchema(schema = @Schema(implementation = JobDto.class))
                            )
                    }
            )})
    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<JobDto>> getAll() {
        Link link = linkTo(methodOn(JobController.class).getAll()).withSelfRel();
        List<JobDto> jobDtos = jobService.getAll().stream().
                map((job) -> {
                    JobDto jobDto = jobMapper.mapToDto(job);
                    Link selfLink = linkTo(methodOn(JobController.class).getById(job.getId())).withSelfRel();
                    jobDto.add(selfLink);
                    return jobDto;
                }).collect(Collectors.toList());
        CollectionModel<JobDto> result = CollectionModel.of(jobDtos, link);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get job by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Job found",
                    content = {
                            @Content(
                                    mediaType = "application/hal+json",
                                    schema = @Schema(implementation = JobDto.class)
                            )
                    }
            ),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)})
    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<JobDto> getById(@PathVariable("id") Long id) {
        Job job = jobService.getById(id);
        JobDto result = jobMapper.mapToDto(job);
        Link selfLink = linkTo(methodOn(JobController.class).getById(id)).withSelfRel();
        result.add(selfLink);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete job by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content)})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        jobService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
