package com.awbd.carservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CarType {
    REGULAR("Regular car"),
    VAN("Van"),
    MINIBUS("Minibus");

    private String description;
}
