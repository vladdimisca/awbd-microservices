package com.awbd.jobservice.error;

import lombok.Value;

@Value
public class ErrorEntity {
    int errorCode;
    String errorMessage;
}
