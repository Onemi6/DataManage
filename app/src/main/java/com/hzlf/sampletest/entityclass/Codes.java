package com.hzlf.sampletest.entityclass;

public class Codes {

    private String codes;
    private String status;
    private String message;

    public Codes() {
    }

    public Codes(String codes, String status, String message) {
        this.codes = codes;
        this.status = status;
        this.message = message;
    }

    public String getCodes() {
        return codes;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
