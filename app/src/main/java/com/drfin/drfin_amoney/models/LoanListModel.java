package com.drfin.drfin_amoney.models;

import com.google.gson.annotations.SerializedName;

public class LoanListModel {

    @SerializedName("id")
    private String id;
    @SerializedName("loan_id")
    private String loanId;
    @SerializedName("cin")
    private String cin;
    @SerializedName("application_number")
    private String applicationNumber;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("loan_account_no")
    private String loanAccountNo;
    @SerializedName("bank_ifsc")
    private String bankIfsc;
    @SerializedName("bank_account")
    private String bankAccount;
    @SerializedName("loan_amount")
    private String loanAmount;
    @SerializedName("principal")
    private String principal;
    @SerializedName("interest")
    private String interest;
    @SerializedName("period_end_date")
    private String periodEndDate;
    @SerializedName("paid_amount")
    private Object paidAmount;
    @SerializedName("period_repayment_amount")
    private String periodRepaymentNum;
    @SerializedName("overdue_rate")
    private String overdueRate;
    @SerializedName("application_status")
    private String application_status;
    @SerializedName("customer_status")
    private String customer_status;
    @SerializedName("loan_status")
    private String loanStatus;
    @SerializedName("customer_loan_status")
    private String customerLoanStatus;
    @SerializedName("sanction_letter")
    private String sanctionLetter;
    @SerializedName("repayment_status")
    private String repayment_status;
    @SerializedName("apply_date")
    private String apply_date;

    public String getApply_date() {
        return apply_date;
    }

    public void setApply_date(String apply_date) {
        this.apply_date = apply_date;
    }

    public String getRepayment_status() {
        return repayment_status;
    }

    public void setRepayment_status(String repayment_status) {
        this.repayment_status = repayment_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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

    public String getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(String periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Object getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Object paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPeriodRepaymentNum() {
        return periodRepaymentNum;
    }

    public void setPeriodRepaymentNum(String periodRepaymentNum) {
        this.periodRepaymentNum = periodRepaymentNum;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public String getCustomerLoanStatus() {
        return customerLoanStatus;
    }

    public void setCustomerLoanStatus(String customerLoanStatus) {
        this.customerLoanStatus = customerLoanStatus;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getSanctionLetter() {
        return sanctionLetter;
    }

    public void setSanctionLetter(String sanctionLetter) {
        this.sanctionLetter = sanctionLetter;
    }
}
