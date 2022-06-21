package com.awbd.appointmentservice.error.exception;

import com.awbd.appointmentservice.error.ErrorMessage;
import com.awbd.appointmentservice.error.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@Getter
public abstract class AbstractApiException extends RuntimeException {
    private final int errorCode;
    private final HttpStatus httpStatus;

    public AbstractApiException(HttpStatus httpStatus, ErrorMessage errorMessage, Object... params) {
        super(formatMessage(errorMessage.getErrorMessage(), params));
        this.errorCode = errorMessage.getErrorCode();
        this.httpStatus = httpStatus;
    }

    public AbstractApiException(HttpStatus httpStatus, ErrorResponse errorResponse, Object... params) {
        super(formatMessage(errorResponse.getErrorMessages().stream().findFirst().get().getErrorMessage(), params));
        this.errorCode = errorResponse.getErrorMessages().stream().findFirst().get().getErrorCode();
        this.httpStatus = httpStatus;
    }

    private static String formatMessage(String message, Object... messageArgs) {
        return MessageFormat.format(message, messageArgs);
    }
}