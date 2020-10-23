package com.drfin.drfin_amoney.models;


public class SmsListModel {
    private String SMSID;
    private String Contacct_No;
    private String SMS_BODY;
    private String SMS_DATE;

    public SmsListModel(String SMSID, String contacct_No, String SMS_BODY,String SMS_DATE) {
        this.SMSID = SMSID;
        Contacct_No = contacct_No;
        this.SMS_BODY = SMS_BODY;
        this.SMS_DATE=SMS_DATE;
    }

    public String getSMS_DATE() {
        return SMS_DATE;
    }

    public void setSMS_DATE(String SMS_DATE) {
        this.SMS_DATE = SMS_DATE;
    }

    public String getSMSID() {
        return SMSID;
    }

    public void setSMSID(String SMSID) {
        this.SMSID = SMSID;
    }

    public String getContacct_No() {
        return Contacct_No;
    }

    public void setContacct_No(String contacct_No) {
        Contacct_No = contacct_No;
    }

    public String getSMS_BODY() {
        return SMS_BODY;
    }

    public void setSMS_BODY(String SMS_BODY) {
        this.SMS_BODY = SMS_BODY;
    }

}
