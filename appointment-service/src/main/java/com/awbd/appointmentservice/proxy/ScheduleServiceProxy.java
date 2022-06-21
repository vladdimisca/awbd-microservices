package com.awbd.appointmentservice.proxy;

import com.awbd.appointmentservice.model.Schedule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "scheduleservice")
public interface ScheduleServiceProxy {

    @GetMapping("/schedule")
    Schedule getSchedule();
}
