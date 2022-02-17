package org.shrtr.core.controllers;

public class LinkResponse {

    private String error;

    public LinkResponse() {

    }

    public LinkResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
