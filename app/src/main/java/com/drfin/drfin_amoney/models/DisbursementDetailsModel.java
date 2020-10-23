package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

public class DisbursementDetailsModel {

    @SerializedName("principal")
    private String principal;
    @SerializedName("interest")
    private String interest;
    @SerializedName("processing_fees")
    private String processing_fees;
    @SerializedName("processing_fees_gst")
    private String processing_fees_gst;
    @SerializedName("total_processing_fees")
    private String total_processing_fees;
    @SerializedName("amt_disbursed")
    private String disbursement_amount;
    @SerializedName("interest_rate")
    private String interest_rate;
    @SerializedName("overdue_rate")
    private String overdue_rate;
    @SerializedName("loan_term")
    private String loan_term;
    @SerializedName("loan_installment_num")
    private String loan_installment_num;

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public String getOverdue_rate() {
        return overdue_rate;
    }

    public void setOverdue_rate(String overdue_rate) {
        this.overdue_rate = overdue_rate;
    }

    public String getLoan_term() {
        return loan_term;
    }

    public void setLoan_term(String loan_term) {
        this.loan_term = loan_term;
    }

    public String getLoan_installment_num() {
        return loan_installment_num;
    }

    public void setLoan_installment_num(String loan_installment_num) {
        this.loan_installment_num = loan_installment_num;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getProcessing_fees() {
        return processing_fees;
    }

    public void setProcessing_fees(String processing_fees) {
        this.processing_fees = processing_fees;
    }

    public String getProcessing_fees_gst() {
        return processing_fees_gst;
    }

    public void setProcessing_fees_gst(String processing_fees_gst) {
        this.processing_fees_gst = processing_fees_gst;
    }

    public String getTotal_processing_fees() {
        return total_processing_fees;
    }

    public void setTotal_processing_fees(String total_processing_fees) {
        this.total_processing_fees = total_processing_fees;
    }

    public String getDisbursement_amount() {
        return disbursement_amount;
    }

    public void setDisbursement_amount(String disbursement_amount) {
        this.disbursement_amount = disbursement_amount;
    }
}
