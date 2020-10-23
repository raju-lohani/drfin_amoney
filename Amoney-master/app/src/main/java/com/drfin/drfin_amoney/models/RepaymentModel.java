package com.drfin.drfin_amoney.models;

public class RepaymentModel {

    private String result_code;
    private String info;
    private String period_end_date;
    private String period_repayment_amount;
    private String overdue_rate;
    private  String overdue_fee;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPeriod_end_date() {
        return period_end_date;
    }

    public void setPeriod_end_date(String period_end_date) {
        this.period_end_date = period_end_date;
    }

    public String getPeriod_repayment_amount() {
        return period_repayment_amount;
    }

    public void setPeriod_repayment_amount(String period_repayment_amount) {
        this.period_repayment_amount = period_repayment_amount;
    }

    public String getOverdue_rate() {
        return overdue_rate;
    }

    public void setOverdue_rate(String overdue_rate) {
        this.overdue_rate = overdue_rate;
    }

    public String getOverdue_fee() {
        return overdue_fee;
    }

    public void setOverdue_fee(String overdue_fee) {
        this.overdue_fee = overdue_fee;
    }
}
