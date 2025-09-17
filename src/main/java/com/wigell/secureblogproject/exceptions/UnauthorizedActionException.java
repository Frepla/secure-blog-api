package com.wigell.secureblogproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedActionException extends RuntimeException {

    private final String object;
    private final String action;

    public UnauthorizedActionException(String object, String action) {
        super(String.format("Not allowed to %s this %s", action, object));
        this.object = object;
        this.action = action;
    }

    public String getObject() {
        return object;
    }

    public String getAction() {
        return action;
    }
}