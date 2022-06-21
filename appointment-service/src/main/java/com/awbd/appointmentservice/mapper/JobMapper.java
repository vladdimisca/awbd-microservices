package com.awbd.appointmentservice.mapper;

import com.awbd.appointmentservice.dto.JobDto;
import com.awbd.appointmentservice.model.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    Job mapToEntity(JobDto jobDto);
}
