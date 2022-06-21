package com.awbd.employeeservice.error;

import lombok.Value;

@Value
public class ErrorEntity {
    int errorCode;
    String errorMessage;
}
