package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pulkit Kumar
 */
public class LoanApplyResponseModel {

    @SerializedName("info")
    private String info;

    @SerializedName("result_code")
    private String result_code;

    @SerializedName("id")
    private String id;

    @SerializedName("cin")
    private String cin;

    @SerializedName("application_number")
    private String application_number;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getApplication_number() {
        return application_number;
    }

    public void setApplication_number(String application_number) {
        this.application_number = application_number;
    }
}
