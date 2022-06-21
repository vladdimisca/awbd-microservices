package com.awbd.appointmentservice.error.exception;

import com.awbd.appointmentservice.error.ErrorMessage;
import com.awbd.appointmentservice.error.ErrorResponse;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractApiException {
    public BadRequestException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.BAD_REQUEST, errorMessage, params);
    }

    public BadRequestException(ErrorResponse errorResponse, Object... params) {
        super(HttpStatus.BAD_REQUEST, errorResponse, params);
    }
}
