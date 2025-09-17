package com.wigell.secureblogproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPostException extends RuntimeException {

    private final String object;
    private final String field;
    private final Object value;

    public InvalidPostException(String object, String field, Object value) {
        super(String.format("%s is invalid: %s = '%s'", object, field, value));
        this.object = object;
        this.field = field;
        this.value = value;
    }

    public String getObject() {
        return object;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}