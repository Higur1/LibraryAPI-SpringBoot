package io.github.spring.libraryapi.common;

import io.github.spring.libraryapi.exceptions.DuplicateRecordException;
import io.github.spring.libraryapi.exceptions.InvalidFieldException;
import io.github.spring.libraryapi.exceptions.OperationNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice //capture exceptions and return Rest response
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // no need to use response entity to send status error
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getFieldErrors();

        List<CustomFieldError> listCustomFieldErrors =
                fieldErrors
                        .stream()
                        .map(fieldError -> new CustomFieldError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                        .collect(
                                Collectors.toList());

        return new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                listCustomFieldErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomFieldError handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new CustomFieldError(e.getName(), "Invalid UUID");
    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicateRecordException(DuplicateRecordException e) {
        return ResponseError.conflict(e.getMessage());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleOperationNotAllowedException(OperationNotAllowedException e) {
        return ResponseError.genericResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleInvalidFieldException(InvalidFieldException e) {
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", List.of(new CustomFieldError(e.getErrorField(), e.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleDefaultError(RuntimeException e) {
        log.error("An unexpected error", e);
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please contact the system administrator.",
                List.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(AccessDeniedException e){
        return new ResponseError(HttpStatus.FORBIDDEN.value(), "Access Denied", List.of());
    }
}
