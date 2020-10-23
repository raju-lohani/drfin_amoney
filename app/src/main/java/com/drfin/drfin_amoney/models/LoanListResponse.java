package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoanListResponse {

    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("info")
    private String info;
    @SerializedName("application_list")
    private ArrayList<LoanListModel> applicationList = null;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<LoanListModel> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(ArrayList<LoanListModel> applicationList) {
        this.applicationList = applicationList;
    }
}
