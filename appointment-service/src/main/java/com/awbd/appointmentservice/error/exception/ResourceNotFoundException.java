package com.awbd.appointmentservice.error.exception;

import com.awbd.appointmentservice.error.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractApiException {
    public ResourceNotFoundException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.NOT_FOUND, errorMessage, params);
    }
}
