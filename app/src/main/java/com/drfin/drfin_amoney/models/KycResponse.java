package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pulkit Kumar
 */
public class KycResponse {
    @SerializedName("info")
    private String info;

    @SerializedName("result_code")
    private String result_code;

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
}
