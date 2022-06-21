package com.awbd.jobservice.mapper;

import com.awbd.jobservice.dto.JobDto;
import com.awbd.jobservice.model.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobDto mapToDto(Job job);

    Job mapToEntity(JobDto jobDto);
}
