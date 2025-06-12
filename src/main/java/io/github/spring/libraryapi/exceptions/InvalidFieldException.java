package io.github.spring.libraryapi.exceptions;

import lombok.Getter;

public class InvalidFieldException extends RuntimeException{
    @Getter
    private String errorField;

    public InvalidFieldException(String field, String message){
        super(message);
        this.errorField = field;
    }
}
