package com.gtech.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiException apiError) {
        apiError.setStackTrace(new StackTraceElement[0]);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Handling a generic exception of type: {}. Error: {}", ex.getClass().getSimpleName(), ExceptionUtils.getRootCause(ex).getMessage());
        log.error(ExceptionUtils.getStackTrace(ex));

        return buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex));
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(ApiException ex, WebRequest request) {
        log.error("Handling an api exception of type: {}. Error: {}", ex.getClass().getSimpleName(), ExceptionUtils.getRootCause(ex).getMessage());

        return buildResponseEntity(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Handling an api exception of type: {}. Error: {}", ex.getClass().getSimpleName(), ExceptionUtils.getRootCause(ex).getMessage());

        return buildResponseEntity(new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}