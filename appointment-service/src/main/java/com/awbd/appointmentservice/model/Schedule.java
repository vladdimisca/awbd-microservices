package com.awbd.appointmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Schedule {
    private Hours start;
    private Hours stop;
    private String instanceId;
}
