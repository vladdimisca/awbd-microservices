package com.awbd.employeeservice.error.exception;

import com.awbd.employeeservice.error.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractApiException {
    public ConflictException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.CONFLICT, errorMessage, params);
    }
}
