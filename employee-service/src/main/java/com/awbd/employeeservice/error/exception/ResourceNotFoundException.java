package com.awbd.employeeservice.error.exception;

import com.awbd.employeeservice.error.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractApiException {
    public ResourceNotFoundException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.NOT_FOUND, errorMessage, params);
    }
}
