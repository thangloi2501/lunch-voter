package com.gtech.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gtech.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiException extends RuntimeException {

    @Builder.Default
    @Getter
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @Getter
    private long timestamp;
    @Getter
    private String message;
    @Getter
    private String debugMessage;

    @Builder.Default
    @Getter
    private String localizedMessage = null;

    public ApiException() {
        timestamp = System.currentTimeMillis();
    }

    public ApiException(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiException(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ApiException(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiException(HttpStatus status, String message, String debugMessage) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiException(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public static ApiException from(HttpStatus httpStatus, String messageKey, Object... args) {
        return new ApiException(httpStatus, AppUtils.getMessage(messageKey, args));
    }

    public static ApiException notFound(String subject, String key, Object value) {
        return new ApiException(HttpStatus.NOT_FOUND, AppUtils.getMessage("common.error.not-found", subject, key, value));
    }
}
