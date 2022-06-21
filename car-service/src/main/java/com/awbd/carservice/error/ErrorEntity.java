package com.awbd.carservice.error;

import lombok.Value;

@Value
public class ErrorEntity {
    int errorCode;
    String errorMessage;
}
