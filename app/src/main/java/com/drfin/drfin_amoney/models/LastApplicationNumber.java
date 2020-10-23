package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

public class LastApplicationNumber {

    @SerializedName("info")
    private String info;
    @SerializedName("result_code")
    private String result_code;
    @SerializedName("application_number")
    private String application_number;
    @SerializedName("apply_date")
    private String apply_date;
    @SerializedName("loan_status")
    private String loan_status;

    @SerializedName("loan_amount")
    private String loan_amount;

    @SerializedName("disbursement_details")
    private DisbursementDetailsModel disbursement_details;

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public DisbursementDetailsModel getDisbursement_details() {
        return disbursement_details;
    }

    public void setDisbursement_details(DisbursementDetailsModel disbursement_details) {
        this.disbursement_details = disbursement_details;
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

    public String getApplication_number() {
        return application_number;
    }

    public void setApplication_number(String application_number) {
        this.application_number = application_number;
    }

    public String getApply_date() {
        return apply_date;
    }

    public void setApply_date(String apply_date) {
        this.apply_date = apply_date;
    }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }
}
