package com.awbd.jobservice.error.exception;

import com.awbd.jobservice.error.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractApiException {
    public ConflictException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.CONFLICT, errorMessage, params);
    }
}
