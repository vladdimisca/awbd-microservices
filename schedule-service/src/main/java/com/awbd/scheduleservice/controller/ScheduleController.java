package com.awbd.scheduleservice.controller;

import com.awbd.scheduleservice.config.PropertiesConfig;
import com.awbd.scheduleservice.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final PropertiesConfig propertiesConfig;
    private final Environment environment;

    @GetMapping
    public Schedule getSchedule() {
        return new Schedule(propertiesConfig.getStart(), propertiesConfig.getStop(), environment.getProperty("info.app.version"));
    }
}
