package com.awbd.carservice.error;

import lombok.Value;

import java.util.List;

@Value
public class ErrorResponse {
    List<ErrorEntity> errorMessages;
}
