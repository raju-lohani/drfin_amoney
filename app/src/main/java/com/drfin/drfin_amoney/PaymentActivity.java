package com.drfin.drfin_amoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drfin.drfin_amoney.models.KycResponse;
import com.drfin.drfin_amoney.models.LoginHelperModel;
import com.drfin.drfin_amoney.models.RepaymentModel;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Button paymentBtn;
    Checkout checkout;
    PrefrenceHandler pref;
    LinearLayout progress_bar;
    String loanNo;
    String paidAmount;

    private TextView repayment_amount,last_date,overdue_rate,overdue_fees,total_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentBtn = findViewById(R.id.paymentBtn);
        repayment_amount = findViewById(R.id.repayment_amount);
        last_date = findViewById(R.id.last_date);
        overdue_rate = findViewById(R.id.overdue_rate);
        overdue_fees = findViewById(R.id.overdue_fees);
        progress_bar = findViewById(R.id.progress_bar);
        total_pay=findViewById(R.id.total_pay);

        pref=new PrefrenceHandler(this);
        Checkout.preload(getApplicationContext());
        checkout = new Checkout();

        loanNo=getIntent().getStringExtra("loan_no");
        fetchRepaymentDetails(loanNo);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double Amount=Double.parseDouble(total_pay.getText().toString())*100;
                int i= (int) Amount;
                startPayment(String.valueOf(i));
            }
        });
    }

    public void startPayment(String Amount) {
        Checkout checkout = new Checkout();  /**   * Set your logo here   */
        checkout.setImage(R.drawable.logo);  /**   * Reference to current activity   */
        final Activity activity = this;  /**   * Pass your payment options to the Razorpay Checkout as a JSONObject   */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Amoney");
            options.put("image", "https://vralogicalconsultants.com/aglowcredit/assets/img/app-logo.png");
            options.put("currency", "INR");
            options.put("amount", Amount);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPaymentSuccess(String s) {
        updateTransaction(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogLayout = inflater.inflate(R.layout.loan_reject_activity, null);
        builder.setView(dialogLayout);
        TextView message = dialogLayout.findViewById(R.id.message);
        TextView title = dialogLayout.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Payment \nStatus");
        Button continueButton = dialogLayout.findViewById(R.id.continueButton);
        dialogLayout.findViewById(R.id.continueButtonLayout).setVisibility(View.VISIBLE);
        dialogLayout.findViewById(R.id.statusLayout).setVisibility(View.GONE);
        ImageView imageView = dialogLayout.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.no_found);
        message.setText(s);
        final AlertDialog d = builder.show();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                startActivity(new Intent(PaymentActivity.this,HomeScreenActivity.class));
                finish();
            }
        });
    }

    private void fetchRepaymentDetails(String loanNo) {
        progress_bar.setVisibility(View.VISIBLE);
        ApiClientService apiClientService = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<RepaymentModel> callkyc = apiClientService.repaymentDetails(
                pref.getAuth_Token(),
                loanNo);
        callkyc.enqueue(new Callback<RepaymentModel>() {
            @Override
            public void onResponse(@NonNull Call<RepaymentModel> call, @NonNull Response<RepaymentModel> response) {
                if (response.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        paidAmount=response.body().getPeriod_repayment_amount();
                        repayment_amount.setText(response.body().getPeriod_repayment_amount());
                        last_date.setText(response.body().getPeriod_end_date());
                        overdue_rate.setText(response.body().getOverdue_rate());
                        overdue_fees.setText(response.body().getOverdue_fee()==null?"0":response.body().getOverdue_fee());
                        String Totalfee=overdue_fees.getText().toString();
                        double fees=Double.parseDouble(paidAmount)+Double.parseDouble(Totalfee);
                        total_pay.setText(String.valueOf(fees));
                    }
                } else {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(PaymentActivity.this, "Some Error try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RepaymentModel> call, @NonNull Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(PaymentActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTransaction(final String transactionNo) {
        progress_bar.setVisibility(View.VISIBLE);
        ApiClientService apiClientService = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> callkyc = apiClientService.updateTransaction(
                pref.getAuth_Token(),
                loanNo,
                paidAmount,
                transactionNo);
        callkyc.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        showPaymentSuccessDialog(transactionNo);
                    }
                } else {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(PaymentActivity.this, "Some Error try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(PaymentActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showPaymentSuccessDialog(String s){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogLayout = inflater.inflate(R.layout.fragment_done_loan_layout, null);
        builder.setView(dialogLayout);
        TextView message = dialogLayout.findViewById(R.id.message);
        TextView title = dialogLayout.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Payment\n Status");
        Button continueButton = dialogLayout.findViewById(R.id.continueButton);
        dialogLayout.findViewById(R.id.continueButtonLayout).setVisibility(View.VISIBLE);
        dialogLayout.findViewById(R.id.statusLayout).setVisibility(View.GONE);
        continueButton.setText("Ok");
        message.setText("Transaction Id: "+s);
        final AlertDialog d = builder.show();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                finish();
            }
        });
    }
}
