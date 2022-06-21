package com.awbd.carservice.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR(1, "Internal server error. Oops, something went wrong!"),
    ALREADY_EXISTS(2, "A {0} with the same {1} already exists!"),
    RESOURCE_NOT_FOUND(3, "The {0} with identifier={1} was not found!");

    private final int errorCode;
    private final String errorMessage;
}
