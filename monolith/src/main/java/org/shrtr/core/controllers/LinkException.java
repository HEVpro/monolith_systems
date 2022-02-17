package org.shrtr.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LinkException extends Exception {
    public LinkException(String msg) {
        super(msg);
    }
}
