package com.hzlf.sampletest.model;

public class Template {

    private String status;
    private String message;

    public Template(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
