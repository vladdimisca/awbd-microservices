package com.awbd.appointmentservice.proxy;

import com.awbd.appointmentservice.dto.JobDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "jobservice")
public interface JobServiceProxy {

    @GetMapping("/jobs/{id}")
    JobDto getById(@PathVariable("id") Long id);
}
