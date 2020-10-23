package com.drfin.drfin_amoney.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.drfin.drfin_amoney.CommonDetailsActivity;
import com.drfin.drfin_amoney.HomeScreenActivity;
import com.drfin.drfin_amoney.MainActivity;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.Services.UserAllInformationService;
import com.drfin.drfin_amoney.models.LoginHelperModel;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.CustomeDatePicker;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.drfin.drfin_amoney.utils.SelectFragmentCallbacks;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanFragment extends Fragment implements View.OnClickListener {
    private PrefrenceHandler pref;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 2;
    private Dialog Logindialog, VerifyOTPDialog, EnterPasswordDialog;
    private LinearLayout progress_bar;
    private SelectFragmentCallbacks selectFragmentCallbacks;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private int diff = 0;
    private Button applyButton;
    private TextView message;
    private EditText otpET;
    private String otp;

    public LoanFragment(SelectFragmentCallbacks selectFragmentCallbacks) {
        this.selectFragmentCallbacks = selectFragmentCallbacks;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    final String sender = intent.getStringExtra("sender");
                    assert sender != null;
                    if (sender.split("-")[1].equals("DRFINA")) {
//                    Handler h = new Handler();
//                    h.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (otpET != null) {
//                                otpET.setText(message);
//                            }
//                        }
//                    }, 1000);
                        otp = message;
                    }
                }
            }catch (Exception ignored){

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loan, container, false);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        calculateDiffrence();
        init(v);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        mAuth.addAuthStateListener(mAuthListner);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(Objects.requireNonNull(getActivity()), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(Objects.requireNonNull(getActivity()));
        mGoogleApiClient.disconnect();

        try {
            Objects.requireNonNull(getContext()).unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.d("TAG", "onPause: ");
        }
    }

    @SuppressLint("SetTextI18n")
    private void init(View v) {
        applyButton = v.findViewById(R.id.applyButton);
        message = v.findViewById(R.id.message);
        applyButton.setOnClickListener(this);

        if (!pref.getLastApplicationNo().equals("") && !pref.getLastApplicationStatus().equals("") && !pref.getAuth_Token().equals("")) {

            if (pref.getLastApplicationStatus().equals("reject") || pref.getLastApplicationStatus().equals("pass") || pref.getLastApplicationStatus().equals("accepted")) {
                if (diff < 30) {
                    message.setText("You can re-apply after " + (30 - diff) + " days");
                    applyButton.setText(getResources().getString(R.string.previous_loan_detail));
                } else {
                    selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
                }
            } else if (pref.getLastApplicationStatus().equals("pending")) {
                message.setText("Wait for last application's approval");
                applyButton.setText(getResources().getString(R.string.previous_loan_detail));
            } else {
                selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (pref.getLastApplicationNo() != null && pref.getLastApplicationStatus() == null) {
            selectFragmentCallbacks.onActionChange(getContext(), "kyc");
        } else if (pref.getLastApplicationNo() == null && pref.getLastApplicationStatus() == null && !pref.getAuth_Token().equals("")) {
            selectFragmentCallbacks.onActionChange(getContext(), "login");
        } else if (!pref.getLastApplicationNo().equals("") && !pref.getLastApplicationStatus().equals("") && !pref.getAuth_Token().equals("")) {
            if (diff < 30) {
                startActivity(new Intent(getContext(), CommonDetailsActivity.class).putExtra("valid", "PreviousLoanList"));
            } else {
                selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
            }
        } else if (!pref.getAuth_Token().equals("")) {
            selectFragmentCallbacks.onActionChange(getContext(), "login");
        } else if (pref.getAuth_Token().equals("")) {
            showLoginDialog();
        }
    }

    private void calculateDiffrence() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        try {
            Date currentDate = df.parse(formattedDate);
            Date storeDate = df.parse(pref.getLastApplicationDate());
            diff = CustomeDatePicker.getDaysDifference(storeDate, currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showLoginDialog() {
        pref.setUSER_OTP("");
        pref.setSOCIAL_NAME("");
        pref.setGemail("");
        pref.setMobileNo("");
        Logindialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        mCallbackManager = CallbackManager.Factory.create();
        Logindialog.setContentView(R.layout.login_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(Logindialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        Logindialog.getWindow().setAttributes(lp);
        Button SendOtp = Logindialog.findViewById(R.id.Sendffff);
        SignInButton button = Logindialog.findViewById(R.id.Googlesign);
        final EditText mobileNo = Logindialog.findViewById(R.id.PhoneNumber);
        progress_bar = Logindialog.findViewById(R.id.progress_bar);

        SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNo.getText().length() < 10) {
                    mobileNo.setError(getResources().getString(R.string.enter_valid_mobileno));
                } else {
                    pref.setMobileNo("");
                    progress_bar.setVisibility(View.VISIBLE);
                    sendOTP(mobileNo.getText().toString().trim(), pref.getDeviceId());
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_bar.setVisibility(View.VISIBLE);
                mGoogleApiClient.connect();
                if (mGoogleApiClient.isConnected()) {
                    pref.setMobileNo("");
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    pref.setMobileNo("");
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });

        LoginButton loginButton = Logindialog.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                pref.setMobileNo("");
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });
        Logindialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showLoginForgetDialog() {
        pref.setUSER_OTP("");
        Logindialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        mCallbackManager = CallbackManager.Factory.create();
        Logindialog.setContentView(R.layout.login_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(Logindialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        Logindialog.getWindow().setAttributes(lp);
        Button SendOtp = Logindialog.findViewById(R.id.Sendffff);
        Logindialog.findViewById(R.id.login_button).setVisibility(View.GONE);
        Logindialog.findViewById(R.id.Googlesign).setVisibility(View.GONE);
        SendOtp.setText("Send OTP");
        final EditText mobileNo = Logindialog.findViewById(R.id.PhoneNumber);
        mobileNo.setHint("Enter Register Mobile No");
        progress_bar = Logindialog.findViewById(R.id.progress_bar);

        SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNo.getText().length() < 10) {
                    mobileNo.setError(getResources().getString(R.string.enter_valid_mobileno));
                } else {
                    progress_bar.setVisibility(View.VISIBLE);
                    forgetOTP(mobileNo.getText().toString().trim(), pref.getDeviceId());
                }
            }
        });


        Logindialog.show();
    }

    private void handleFacebookAccessToken(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");

                            pref.setGemail(email);
                            pref.setSOCIAL_NAME(first_name+" "+last_name);
                            facebookLogin(first_name + " " + last_name, email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showVerifyOTPDialog(final String mobileNo) {
        VerifyOTPDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        VerifyOTPDialog.setContentView(R.layout.dialog_verfication_code);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(VerifyOTPDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        VerifyOTPDialog.getWindow().setAttributes(lp);
        otpET = VerifyOTPDialog.findViewById(R.id.otp);
        final TextView resendotpTV = VerifyOTPDialog.findViewById(R.id.resendotp);
        final EditText enterPasswordET = VerifyOTPDialog.findViewById(R.id.enterPasswordET);
        final EditText ConfirmpasswordET = VerifyOTPDialog.findViewById(R.id.ConfirmpasswordET);
        enterPasswordET.setVisibility(View.GONE);
        ConfirmpasswordET.setVisibility(View.GONE);
        progress_bar = VerifyOTPDialog.findViewById(R.id.progress_bar);
        final Button registerbtn = VerifyOTPDialog.findViewById(R.id.registerbtn);
        count(resendotpTV);
        //To resend otp
        resendotpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count(resendotpTV);
                pref.setUSER_OTP("");
                otpET.setText("");
                resendOTP(mobileNo, pref.getDeviceId());
            }
        });
        //Button to register user
//        registerbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (otpET.getText().length() == 0) {
//                    otpET.setError("Enter valid otp");
//                } else {
//                    progress_bar.setVisibility(View.VISIBLE);
//                    verifyOtpApi(mobileNo, otpET.getText().toString().trim(), otpET);
//                }
//            }
//        });

        registerbtn.setVisibility(View.GONE);

        otpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!otp.equals("")) {
                    if (otpET.getText().length() == 0) {
//                        otpET.setError("Enter valid otp");
                    } else if (!otp.equals(otpET.getText().toString())) {
                        if (otpET.length() >= 4) {
                            Toast.makeText(getContext(), "Invalid OTP or Sim card is not in same device.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progress_bar.setVisibility(View.VISIBLE);
                        verifyOtpApi(mobileNo, otpET.getText().toString().trim(), otpET);
                    }
                }
            }
        });
        VerifyOTPDialog.show();
    }

    private void showEnterPasswordDialog(final String mobileNo) {
        EnterPasswordDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        EnterPasswordDialog.setContentView(R.layout.dialog_verfication_code);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(EnterPasswordDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        EnterPasswordDialog.getWindow().setAttributes(lp);
        EnterPasswordDialog.findViewById(R.id.ConfirmpasswordET).setVisibility(View.GONE);
        EnterPasswordDialog.findViewById(R.id.linearLayout).setVisibility(View.GONE);
        EnterPasswordDialog.findViewById(R.id.forgetTextView).setVisibility(View.VISIBLE);
        final EditText enterPasswordET = EnterPasswordDialog.findViewById(R.id.enterPasswordET);
        progress_bar = EnterPasswordDialog.findViewById(R.id.progress_bar);
        Button registerbtn = EnterPasswordDialog.findViewById(R.id.registerbtn);
        TextView forgetTextView = EnterPasswordDialog.findViewById(R.id.forgetTextView);
        registerbtn.setText(getResources().getString(R.string.login));
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterPasswordET.getText().length() == 0) {
                    enterPasswordET.setError(getResources().getString(R.string.blank_password_error));
                } else {
                    progress_bar.setVisibility(View.VISIBLE);
                    LoginByPassword(mobileNo, enterPasswordET.getText().toString().trim(), enterPasswordET);
                }
            }
        });

        forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logindialog.dismiss();
                EnterPasswordDialog.dismiss();
                showLoginForgetDialog();
            }
        });
        EnterPasswordDialog.show();
    }

    private void count(final TextView resend) {
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                resend.setClickable(false);
                resend.setText(millisUntilFinished / 1000 + "s");
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                resend.setClickable(true);
                resend.setText("Resend OTP");
            }
        };
        timer.start();
    }

    private void sendOTP(String mobileNo, String deviceId) {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.sendOTP(mobileNo, deviceId, pref.getFCM_TOKEN(), "");
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        pref.setMobileNo(response.body().getMobile());
                        Logindialog.dismiss();
                        showVerifyOTPDialog(response.body().getMobile());
                    }
//                    else if (response.body().getResult_code().equals("201")) {
//                        pref.setMobileNo(response.body().getMobile());
//                        showEnterPasswordDialog(response.body().getMobile());
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void forgetOTP(String mobileNo, String deviceId) {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.sendOTP(mobileNo, deviceId, pref.getFCM_TOKEN(), "1");
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        pref.setMobileNo(response.body().getMobile());
                        Logindialog.dismiss();
                        showVerifyOTPDialog(response.body().getMobile());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOTP(String mobileNo, String deviceId) {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.sendOTP(mobileNo, deviceId, pref.getFCM_TOKEN(), "");
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                assert response.body() != null;
                Log.d("TAG", "onResponse: " + response.body().getInfo());
                Toast.makeText(getContext(), "Otp Send Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                Log.d("TAG", "onResponse: ");
            }
        });
    }

    private void verifyOtpApi(String mobileNo, String otp, final EditText otpEditText) {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.verifyOTP(mobileNo, otp);
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("TAG", "responce body to verify otp: " + response.body().getResult_code());
                    if (response.body().getResult_code().equals("200")) {
                        pref.setAuth_Token(response.body().getAuth_token());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationStatus(response.body().getLoan_status());
                        if (diff < 30) {
                            applyButton.setText(getResources().getString(R.string.previous_loan_detail));
                        } else {
                            selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
                        }
                        selectFragmentCallbacks.onActionChange(getContext(), "login");
                        Logindialog.dismiss();
                        VerifyOTPDialog.dismiss();
                        Logindialog.dismiss();
                        progress_bar.setVisibility(View.GONE);
                    } else {
                        otpEditText.setError(getResources().getString(R.string.invalid_otp));
                        progress_bar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginHelperModel> call, @NonNull Throwable t) {
                Log.d("TAG", "onResponse: ");
            }
        });
    }

    private void LoginByPassword(String mobileNo, String password, final EditText enterPasswordET) {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.loginByPassword(mobileNo, password);
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("TAG", "responce body: " + response.body().getResult_code());
                    if (response.body().getResult_code().equals("200")) {
                        pref.setAuth_Token(response.body().getAuth_token());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationStatus(response.body().getLoan_status());
                        if (!pref.getLastApplicationNo().equals("") && !pref.getLastApplicationStatus().equals("") && !pref.getAuth_Token().equals("")) {
                            if (diff < 30) {
                                applyButton.setText(getResources().getString(R.string.previous_loan_detail));
                            } else {
                                selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
                            }
                        }
                        selectFragmentCallbacks.onActionChange(getContext(), "login");
                        Logindialog.dismiss();
                        EnterPasswordDialog.dismiss();
                        progress_bar.setVisibility(View.GONE);
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        enterPasswordET.setError(getResources().getString(R.string.enter_valid_password));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            super.onActivityResult(requestCode, resultCode, data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("TAG", "onActivityResult:Testing " + result.getStatus());
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                firebaseAuthWithGoogle(account);
            } else {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Signed in Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String mobile = user.getPhoneNumber();
            String email = user.getEmail();
            String name = user.getDisplayName();

            pref.setGmobile(mobile);
            pref.setGemail(email);
            pref.setSOCIAL_NAME(name);
            gmailLogin();
        } else {
            pref.setGmobile(null);
            pref.setGemail(null);
            pref.setSOCIAL_NAME(null);
        }
    }

    private void gmailLogin() {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.googleSignIn(pref.getGmobile(),
                pref.getGemail(),
                pref.getDeviceId(),
                "G",
                pref.getSOCIAL_NAME(),
                pref.getFCM_TOKEN());
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("TAG", "responce body: " + response.body().getResult_code());
                    if (response.body().getResult_code().equals("200")) {
                        pref.setAuth_Token(response.body().getAuth_token());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationStatus(response.body().getLoan_status());
                        if (!pref.getLastApplicationNo().equals("") && !pref.getLastApplicationStatus().equals("") && !pref.getAuth_Token().equals("")) {
                            if (diff < 30) {
                                applyButton.setText(getResources().getString(R.string.previous_loan_detail));
                            } else {
                                selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
                            }
                        }
                        selectFragmentCallbacks.onActionChange(getContext(), "login");
                        Logindialog.dismiss();
                        progress_bar.setVisibility(View.GONE);
                    } else {
                        progress_bar.setVisibility(View.GONE);
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

    private void facebookLogin(String userName, String email) {
        progress_bar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LoginHelperModel> call1 = apiInterface.googleSignIn("",
                email,
                pref.getDeviceId(),
                "F",
                userName,
                pref.getFCM_TOKEN());
        call1.enqueue(new Callback<LoginHelperModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginHelperModel> call, @NonNull Response<LoginHelperModel> response) {
                if (response.isSuccessful()) {
//                    Log.d("TAG", "onResponse facebook: " + response.body().getAuth_token());
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        pref.setAuth_Token(response.body().getAuth_token());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationStatus(response.body().getLoan_status());
                        Logindialog.dismiss();
                        progress_bar.setVisibility(View.GONE);
                        if (diff < 30) {
                            applyButton.setText(getResources().getString(R.string.previous_loan_detail));
                        } else {
                            selectFragmentCallbacks.onActionChange(getContext(), "data_fill");
                        }
                        selectFragmentCallbacks.onActionChange(getContext(), "login");
                    } else {
                        progress_bar.setVisibility(View.GONE);
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

}
