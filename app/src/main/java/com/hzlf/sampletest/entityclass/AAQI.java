package com.hzlf.sampletest.entityclass;

public class AAQI {

    private String status;
    private Message message;

    public AAQI(String status, Message message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    // 重写toString方法
    @Override
    public String toString() {
        return "{status = " + status + ", message = " + message + "}";
    }

    public class Message {
        private String GOODS_NAME;
        private String TRADEMARK;
        private String SAMPLE_MODEL;
        private String CONTACTNAME;
        private String ADDR;

        public Message() {
        }

        public Message(String GOODS_NAME, String TRADEMARK,
                       String SAMPLE_MODEL, String CONTACTNAME, String ADDR) {
            this.GOODS_NAME = GOODS_NAME;
            this.TRADEMARK = TRADEMARK;
            this.SAMPLE_MODEL = SAMPLE_MODEL;
            this.CONTACTNAME = CONTACTNAME;
            this.ADDR = ADDR;
        }

        public String getGOODS_NAME() {
            return GOODS_NAME;
        }

        public void setGOODS_NAME(String GOODS_NAME) {
            this.GOODS_NAME = GOODS_NAME;
        }

        public String getTRADEMARK() {
            return TRADEMARK;
        }

        public void setTRADEMARK(String TRADEMARK) {
            this.TRADEMARK = TRADEMARK;
        }

        public String getSAMPLE_MODEL() {
            return SAMPLE_MODEL;
        }

        public void setSAMPLE_MODEL(String SAMPLE_MODEL) {
            this.SAMPLE_MODEL = SAMPLE_MODEL;
        }

        public String getCONTACTNAME() {
            return CONTACTNAME;
        }

        public void setCONTACTNAME(String CONTACTNAME) {
            this.CONTACTNAME = CONTACTNAME;
        }

        public String getADDR() {
            return ADDR;
        }

        public void setADDR(String ADDR) {
            this.ADDR = ADDR;
        }

        // 重写toString方法
        @Override
        public String toString() {
            return "message:{GOODS_NAME = " + GOODS_NAME + ", TRADEMARK = "
                    + TRADEMARK + ", SAMPLE_MODEL = " + SAMPLE_MODEL
                    + ", CONTACTNAME = " + CONTACTNAME + ", ADDR = " + ADDR
                    + "}";
        }

    }

}
