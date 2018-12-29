package com.hzlf.sampletest.model;

public class Status {
    private String NO;
    private String NAME;
    private String token;
    private String status;
    private String message;

    public Status(String NO, String NAME, String token, String status, String message) {
        this.NO = NO;
        this.NAME = NAME;
        this.token = token;
        this.status = status;
        this.message = message;
    }

    public String getNo() {
        return NO;
    }

    public String getName() {
        return NAME;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
