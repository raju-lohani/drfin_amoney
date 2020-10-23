package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

public class PanCardVerificationData {

    @SerializedName("info")
    private String info;
    @SerializedName("result_code")
    private String result_code;
    @SerializedName("verification_status")
    private String verification_status;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }
}
