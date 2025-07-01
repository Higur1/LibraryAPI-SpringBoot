package io.github.spring.libraryapi.common;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseError(int status, String message, List<CustomFieldError> errors) {

    public static ResponseError genericResponse(String message) {
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseError conflict(String message) {
        return new ResponseError(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
