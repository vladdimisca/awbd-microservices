package com.awbd.appointmentservice.error.exception;

import com.awbd.appointmentservice.error.ErrorMessage;
import org.springframework.http.HttpStatus;

public class TemporaryUnavailableException extends AbstractApiException {
    public TemporaryUnavailableException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.SERVICE_UNAVAILABLE, errorMessage, params);
    }
}
