package com.hzlf.sampletest.entityclass;

public class MainInfo {

    private String status;
    private String no;
    private String number;
    private String addtime;

    public MainInfo() {
    }

    public MainInfo(String status, String no, String number, String addtime) {
        this.status = status;
        this.no = no;
        this.number = number;
        this.addtime = addtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public String getNumber() {
        return number;
    }

    public String getAddtime() {
        return addtime;
    }
}
