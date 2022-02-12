package org.shrtr.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(LinkException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public LinkResponse handleRateLimitException(LinkException se) {
        LinkResponse response = new LinkResponse(se.getMessage());
        return response;
    }
}
