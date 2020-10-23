package com.drfin.drfin_amoney.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.LastApplicationNumber;
import com.drfin.drfin_amoney.models.LoginHelperModel;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.CaptureSignatureView;
import com.drfin.drfin_amoney.utils.ImageConversionClass;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanAmountDetailsFragment extends Fragment implements View.OnClickListener {
    private TextView principal_amount, interest_amount, processing_fees,
            processing_fees_gst, total_processing_fees, disbursement_amount,
            interest_rate, overdue_rate, loan_term, loan_installment_num,
            name, phone, accout_no, account_ifsc,
            repayment_amount, repayment_status;

    private PrefrenceHandler pref;
    private LinearLayout progress_bar, btn,progress, repayment_layout,details;
    private CaptureSignatureView mSig;
    private String appliaction_no;
    private Bundle bundle;

    public LoanAmountDetailsFragment(String applicationNo, Bundle bundle) {
        this.appliaction_no = applicationNo;
        this.bundle=bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amout_details, container, false);
        init(view);
        mSig = new CaptureSignatureView(getContext(), null);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        getLoanAmount();
        return view;
    }

    private void init(View view) {
        principal_amount = view.findViewById(R.id.principal_amount);
        interest_amount = view.findViewById(R.id.interest_amount);
        processing_fees = view.findViewById(R.id.processing_fees);
        processing_fees_gst = view.findViewById(R.id.processing_fees_gst);
        total_processing_fees = view.findViewById(R.id.total_processing_fees);
        disbursement_amount = view.findViewById(R.id.disbursement_amount);
        interest_rate = view.findViewById(R.id.interest_rate);
        overdue_rate = view.findViewById(R.id.overdue_rate);
        loan_term = view.findViewById(R.id.loan_term);
        btn = view.findViewById(R.id.btn);
        loan_installment_num = view.findViewById(R.id.loan_installment_num);
        Button acceptBtn = view.findViewById(R.id.acceptBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        progress_bar = view.findViewById(R.id.progress_bar);

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        accout_no = view.findViewById(R.id.account_no);
        account_ifsc = view.findViewById(R.id.account_ifsc);
        repayment_amount = view.findViewById(R.id.repayment_amount);
        repayment_status= view.findViewById(R.id.repayment_status);
        repayment_layout= view.findViewById(R.id.repayment_layout);
        details= view.findViewById(R.id.details);

        btn.setVisibility(appliaction_no == null ? View.VISIBLE : View.GONE);
        details.setVisibility(bundle==null?View.GONE:View.VISIBLE);
        repayment_layout.setVisibility(bundle==null?View.GONE:View.VISIBLE);

        acceptBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void getLoanAmount() {
        progress_bar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);

        Call<LastApplicationNumber> call1 = apiInterface.getLoanAmount(
                appliaction_no == null ? pref.getLastApplicationNo() : appliaction_no,
                pref.getAuth_Token(),
                "1");

        call1.enqueue(new Callback<LastApplicationNumber>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<LastApplicationNumber> call, @NonNull Response<LastApplicationNumber> response) {
                if (response.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        principal_amount.setText(response.body().getDisbursement_details().getPrincipal());
                        interest_amount.setText(response.body().getDisbursement_details().getInterest());
                        processing_fees.setText(response.body().getDisbursement_details().getProcessing_fees());
                        processing_fees_gst.setText(response.body().getDisbursement_details().getProcessing_fees_gst());
                        total_processing_fees.setText(response.body().getDisbursement_details().getTotal_processing_fees());
                        disbursement_amount.setText(response.body().getDisbursement_details().getDisbursement_amount());
                        interest_rate.setText(response.body().getDisbursement_details().getInterest_rate() + " %");
                        overdue_rate.setText(response.body().getDisbursement_details().getOverdue_rate() + " %");
                        loan_term.setText(response.body().getDisbursement_details().getLoan_term() + " days");
                        loan_installment_num.setText(response.body().getDisbursement_details().getLoan_installment_num());

                        if (bundle!=null){
                            name.setText(bundle.getString("name"));
                            phone.setText(bundle.getString("phone"));
                            accout_no.setText(bundle.getString("accountNo"));
                            account_ifsc.setText(bundle.getString("accountIFSC"));
                            repayment_amount.setText(bundle.getString("repaymentAmount"));
                            repayment_status.setText(bundle.getString("repaymentStatus"));

                            repayment_amount.setVisibility(bundle.getString("repaymentAmount")==null?View.GONE:View.VISIBLE);
                            repayment_status.setVisibility(bundle.getString("repaymentStatus")==null?View.GONE:View.VISIBLE);
                            btn.setVisibility(Objects.equals(bundle.getString("checkStatus"), "pass") ?View.VISIBLE:View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LastApplicationNumber> call, @NonNull Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdatLoanStatus(String encodedImage, final String status) {
        progress_bar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.upadteLoanStatus(pref.getAuth_Token(), status, encodedImage);
        call1.enqueue(new Callback<LoginHelperModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                Log.d("Tag",response.message());
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        pref.setLastApplicationStatus(status);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        LayoutInflater inflater = getLayoutInflater();
                        builder.setCancelable(false);
                        View dialogLayout = inflater.inflate(R.layout.loan_approve_activity, null);
                        builder.setView(dialogLayout);
                        Button continueButton = dialogLayout.findViewById(R.id.view_detail);
                        TextView title = dialogLayout.findViewById(R.id.title);
                        TextView message = dialogLayout.findViewById(R.id.message);
                        ImageView image = dialogLayout.findViewById(R.id.image);
                        title.setText("Loan\n Status");
                        if (!status.equals("accepted")) {
                            message.setText(getResources().getString(R.string.loan_reject_approved));
                            image.setImageDrawable(getResources().getDrawable(R.drawable.no_found));
                        }
                        continueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        });

                        builder.show();
                    }else if (response.body().getResult_code().equals("201")) {
                        pref.setLastApplicationStatus(status);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        LayoutInflater inflater = getLayoutInflater();
                        builder.setCancelable(false);
                        @SuppressLint("InflateParams")
                        final View dialogLayout = inflater.inflate(R.layout.loan_approve_activity, null);
                        builder.setView(dialogLayout);
                        Button continueButton = dialogLayout.findViewById(R.id.view_detail);
                        TextView message = dialogLayout.findViewById(R.id.message);
                        TextView title = dialogLayout.findViewById(R.id.title);
                        title.setText("Loan\n Status");
                        message.setText(getResources().getString(R.string.pendingStatus));
                        ImageView image = dialogLayout.findViewById(R.id.image);
                        title.setText("Loan\n Status");
                        if (!status.equals("accepted")) {
                            message.setText(getResources().getString(R.string.loan_reject_approved));
                            image.setImageDrawable(getResources().getDrawable(R.drawable.no_found));
                        }
                        continueButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.acceptBtn:
                showSignature();
                break;

            case R.id.cancelBtn:
                UpdatLoanStatus("","declined");
                break;
        }
    }

    private void showSignature() {
        final AlertDialog.Builder signatureBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = getLayoutInflater();
        signatureBuilder.setCancelable(false);
        @SuppressLint("InflateParams")
        final View dialogLayout = inflater.inflate(R.layout.signature_layout, null, false);
        signatureBuilder.setView(dialogLayout);
        LinearLayout mContent = dialogLayout.findViewById(R.id.linearLayout);
        ImageView refresh_image = dialogLayout.findViewById(R.id.refresh_image);
        final TextView continueBtn = dialogLayout.findViewById(R.id.continueBtn);
        progress = dialogLayout.findViewById(R.id.progress);

        mContent.addView(mSig, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        refresh_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSig.ClearCanvas();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueBtn.setEnabled(false);
                Bitmap signature = mSig.getBitmap();
                if (signature != null) {
                    progress.setVisibility(View.VISIBLE);
                    String encodedImage = ImageConversionClass.BitmapToString(signature);
                    UpdatLoanStatus(encodedImage,"accepted");
                } else {
                    continueBtn.setEnabled(true);
                    Toast.makeText(getContext(), "Signature is mendatory", Toast.LENGTH_LONG).show();
                }
            }
        });
        signatureBuilder.show();
    }
}
