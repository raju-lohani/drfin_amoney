package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

public class LoginHelperModel {

    @SerializedName("info")
    private String info;
    @SerializedName("result_code")
    private String result_code;
    @SerializedName("otp")
    private String otp;
    @SerializedName("auth_token")
    private String auth_token;
    @SerializedName("is_active")
    private String is_active;
    @SerializedName("id")
    private String id;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("application_number")
    private String application_number;
    @SerializedName("loan_status")
    private String loan_status;

    public LoginHelperModel(String mobile, String device_id) {
        this.mobile = mobile;
        this.device_id = device_id;
    }

    public String getApplication_number() {
        return application_number;
    }

    public void setApplication_number(String application_number) {
        this.application_number = application_number;
    }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
