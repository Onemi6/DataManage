package com.hzlf.sampletest.model;

public class User {
    private String NO;

    private String NAME;

    private String LOGIN_NAME;

    private String PASSWORD;

    private String TIME_STAMP;

    public User(String NO, String NAME, String LOGIN_NAME, String PASSWORD,
                String TIME_STAMP) {
        super();
        this.NO = NO;
        this.NAME = NAME;
        this.LOGIN_NAME = LOGIN_NAME;
        this.PASSWORD = PASSWORD;
        this.TIME_STAMP = TIME_STAMP;
    }

    public String getNO() {
        return this.NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getNAME() {
        return this.NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLOGIN_NAME() {
        return this.LOGIN_NAME;
    }

    public void setLOGIN_NAME(String LOGIN_NAME) {
        this.LOGIN_NAME = LOGIN_NAME;
    }

    public String getPASSWORD() {
        return this.PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getTIME_STAMP() {
        return this.TIME_STAMP;
    }

    public void setTIME_STAMP(String TIME_STAMP) {
        this.TIME_STAMP = TIME_STAMP;
    }

}
