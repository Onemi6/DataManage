package com.hzlf.sampletest.model;

public class Source {
    private String SOURCE_NAME;
    private String ADDR;

    public Source(String SOURCE_NAME, String ADDR) {
        super();
        this.SOURCE_NAME = SOURCE_NAME;
        this.ADDR = ADDR;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getSOURCE_NAME() {
        return SOURCE_NAME;
    }

    public void setSOURCE_NAME(String sOURCE_NAME) {
        SOURCE_NAME = sOURCE_NAME;
    }

}
