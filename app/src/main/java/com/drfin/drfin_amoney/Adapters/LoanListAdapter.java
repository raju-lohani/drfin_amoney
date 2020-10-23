package com.drfin.drfin_amoney.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drfin.drfin_amoney.CommonDetailsActivity;
import com.drfin.drfin_amoney.PaymentActivity;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.LoanListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class LoanListAdapter extends RecyclerView.Adapter<LoanListAdapter.ViewHolder> {
    private List<LoanListModel> loanListModels;
    private Context context;
    private Bundle bundle;

    public LoanListAdapter(List<LoanListModel> loanListModels, Context context) {
        this.loanListModels = loanListModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final LoanListModel loanListModel = loanListModels.get(position);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");

        holder.loan_or_application_no.setText(loanListModel.getLoanAccountNo() == null ? "Application No: " + loanListModel.getApplicationNumber() : "Loan No: " + loanListModel.getLoanAccountNo());
        holder.apply_date.setText("Apply Date: " + loanListModel.getApply_date());

        //---Show and hide start--//
        if (loanListModel.getLoanStatus() == null) {
            holder.loanStatus.setText(loanListModel.getApplication_status() == null ? "Reviewing"
                    : loanListModel.getApplication_status().toLowerCase().equals("pass") ? "Approved" : "Rejected");
        }

        holder.repaymnetLayout.setVisibility(loanListModel.getPeriodEndDate() == null ? View.GONE : View.VISIBLE);

        holder.repayment_status.setText(loanListModel.getRepayment_status() == null ? "Pending"
                : loanListModel.getRepayment_status().toLowerCase().equals("closed") ? "Closed"
                : loanListModel.getRepayment_status().toLowerCase().equals("overdues") ? "Overdues" : "");

        if (loanListModel.getLoanStatus() != null && loanListModel.getLoanStatus().toLowerCase().equals("success")) {
            holder.payBtn.setVisibility(loanListModel.getRepayment_status() != null
                    ? (loanListModel.getRepayment_status().equals("closed") ? View.GONE : View.VISIBLE)
                    : View.VISIBLE);
            holder.closeImage.setVisibility(loanListModel.getRepayment_status() != null
                    ? (loanListModel.getRepayment_status().equals("closed") ? View.VISIBLE : View.GONE)
                    : View.GONE);
        } else {
            holder.payBtn.setVisibility(View.GONE);
        }

        holder.repayment_status.setTextColor(loanListModel.getRepayment_status() == null ? (context.getResources().getColor(R.color.mainAppColor))
                : loanListModel.getRepayment_status().toLowerCase().equals("closed") ? (context.getResources().getColor(R.color.GREEN))
                : loanListModel.getRepayment_status().toLowerCase().equals("closed") ? (context.getResources().getColor(R.color.GREEN))
                : (context.getResources().getColor(R.color.RED)));

        holder.downloadPdf.setVisibility(loanListModel.getSanctionLetter() == null ? View.GONE : View.VISIBLE);
        holder.buttonLayout.setVisibility(holder.loanStatus.getText().equals("Rejected") ? View.GONE : View.VISIBLE);
        holder.buttonLayout.setVisibility(loanListModel.getApplication_status() == null ? View.GONE : View.VISIBLE);
        holder.view.setVisibility(holder.loanStatus.getText().equals("Rejected") ? View.GONE : View.VISIBLE);
        holder.viewDetails.setVisibility(holder.loanStatus.getText().equals("Rejected") ? View.GONE : View.VISIBLE);
        holder.buttonLayout.setGravity(holder.downloadPdf.getVisibility() == View.GONE ? Gravity.CENTER_HORIZONTAL : Gravity.NO_GRAVITY);
        //---End hide and show view---//


        if (loanListModel.getPeriodEndDate()!=null) {
            try {
                holder.repayment_amount.setText("Rs. " + loanListModel.getPeriodRepaymentNum());
                holder.overdue_rate.setText("Overdue Rate: " + loanListModel.getOverdueRate());
                holder.last_date.setText("Last Date: " + output.format(Objects.requireNonNull(input.parse(loanListModel.getPeriodEndDate()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (loanListModel.getApplication_status() != null && loanListModel.getApplication_status().toLowerCase().equals("pass")) {
            holder.principal_amount.setText("Rs. " + loanListModel.getPrincipal());
        }
        if (loanListModel.getCustomer_status() != null && loanListModel.getCustomer_status().toLowerCase().equals("accepted")) {
            holder.principal_amount.setText("Rs. " + loanListModel.getPrincipal());
            holder.loanStatus.setText("Pending");
        }
        if (loanListModel.getCustomer_status() != null && loanListModel.getCustomer_status().toLowerCase().equals("declined")) {
            holder.loanStatus.setText("Declined");
            holder.viewDetails.setVisibility(View.GONE);
        }

        if (loanListModel.getLoanStatus() != null && loanListModel.getLoanStatus().toLowerCase().equals("success")) {
            holder.loanStatus.setText("Success");
            holder.principal_amount.setText("Rs. " + loanListModel.getPrincipal());
        }
        if (loanListModel.getLoanStatus() != null && loanListModel.getLoanStatus().toLowerCase().equals("fail")) {
            holder.loanStatus.setText("Payment Pending");
            holder.principal_amount.setText("Rs. " + loanListModel.getPrincipal());
        }

        final String pdfUri = loanListModel.getSanctionLetter();
        final String appliactionNo = loanListModel.getApplicationNumber();
        final String name = loanListModel.getFullName();
        final String email = loanListModel.getEmail();
        final String phone = loanListModel.getPhone();
        final String accountNo = loanListModel.getBankAccount();
        final String accountIFSC = loanListModel.getBankIfsc();
        final String repaymentAmount = loanListModel.getPeriodRepaymentNum();
        final String repaymentStatus = loanListModel.getRepayment_status();
        final String checkStatus = loanListModel.getLoanStatus() != null ? "" : loanListModel.getApplication_status() == null ? ""
                : loanListModel.getApplication_status().toLowerCase().equals("pass") ? "pass" : "";
        final String loanStatus=loanListModel.getLoanStatus();

        holder.downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUri));
                context.startActivity(browserIntent);
            }
        });


        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("phone", phone);
                bundle.putString("accountNo", accountNo);
                bundle.putString("accountIFSC", accountIFSC);
                bundle.putString("repaymentAmount", repaymentAmount);
                bundle.putString("repaymentStatus", repaymentStatus);
                bundle.putString("checkStatus", checkStatus);
                context.startActivity(new Intent(context, CommonDetailsActivity.class)
                        .putExtra("valid", "LoanAmountDetails")
                        .putExtra("bundle", bundle)
                        .putExtra("application_no", appliactionNo));
            }
        });

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PaymentActivity.class).putExtra("loan_no", loanListModel.getLoanAccountNo()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return loanListModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView loan_or_application_no, loanStatus,
                principal_amount, apply_date, overdue_rate, viewDetails, repayment_status, repayment_amount, last_date, downloadPdf;
        private LinearLayout repaymnetLayout, buttonLayout;
        private View view;
        private Button payBtn;
        private ImageView closeImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            loan_or_application_no = itemView.findViewById(R.id.loan_or_application_no);
            loanStatus = itemView.findViewById(R.id.loanStatus);
            principal_amount = itemView.findViewById(R.id.principal_amount);
            apply_date = itemView.findViewById(R.id.apply_date);
            repayment_amount = itemView.findViewById(R.id.repayment_amount);
            last_date = itemView.findViewById(R.id.last_date);
            overdue_rate = itemView.findViewById(R.id.overdue_rate);
            repayment_status = itemView.findViewById(R.id.repayment_status);
            downloadPdf = itemView.findViewById(R.id.downloadPdf);
            viewDetails = itemView.findViewById(R.id.viewDetails);
            repaymnetLayout = itemView.findViewById(R.id.repaymnetLayout);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            view = itemView.findViewById(R.id.view1);
            payBtn = itemView.findViewById(R.id.payBtn);
            closeImage = itemView.findViewById(R.id.closeImage);
        }
    }
}
