package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AadharStateListResponseModel {
    @SerializedName("info")
    private String info;
    @SerializedName("result_code")
    private String result_code;
    @SerializedName("district")
    private String district;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private List<AadharCityListModel> AadharCityListModel;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public List<AadharCityListModel> getAadharCityListModel() {
        return AadharCityListModel;
    }

    public void setAadharCityListModel(List<AadharCityListModel> cityListModels) {
        this.AadharCityListModel = cityListModels;
    }
}
