package com.drfin.drfin_amoney.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drfin.drfin_amoney.Adapters.AllAadharCityAdapters;
import com.drfin.drfin_amoney.Adapters.AllCityAdapters;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.models.AadharCityListModel;
import com.drfin.drfin_amoney.models.AadharStateListResponseModel;
import com.drfin.drfin_amoney.models.AadharVerificationData;
import com.drfin.drfin_amoney.models.BankIFSCCodeModel;
import com.drfin.drfin_amoney.models.CityListModel;
import com.drfin.drfin_amoney.models.Contacts;
import com.drfin.drfin_amoney.models.LoanApplyResponseModel;
import com.drfin.drfin_amoney.models.OtpVerifyModel;
import com.drfin.drfin_amoney.models.PanCardVerificationData;
import com.drfin.drfin_amoney.models.StateListResponseModel;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.CustomeDatePicker;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.drfin.drfin_amoney.utils.SelectCityStateInterface;
import com.drfin.drfin_amoney.utils.SelectFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pulkit Kumar
 */

public class UserDetailsFillUpFragment extends Fragment implements View.OnClickListener {
    private PrefrenceHandler pref;
    private EditText date_of_birth, isStudent, educationStatus, pancardNo, aadharNo,
            gender, mobileNo, relativRelation, relativeName, relativMobile,
            marritalstatus, rStateName, rCityName, adharState,
            adharCity, fname, emailid, rDetailAddress, zipcode,
            detailAdharAddress, zipCode, industry, company, msalary, account_ifsc, cnf_account_no, account_no, otpData;
    private TextView bank_branch, message;
    private SelectCityStateInterface selectCityStateInterface;
    private List<CityListModel> cityListModels;
    private List<AadharCityListModel> aadharCityListModels;
    private Dialog dialog;
    private LinearLayout progressBar;
    private SelectFragmentCallbacks selectFragmentCallbacks;
    private int AadharError = 1, PanError = 1, BankError = 1, BankIfsc = 1;
    private String bank, pan, aadhar;

    public UserDetailsFillUpFragment(SelectFragmentCallbacks selectFragmentCallbacks) {
        this.selectFragmentCallbacks = selectFragmentCallbacks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_details_fillup_fragment, container, false);
        pref = new PrefrenceHandler(Objects.requireNonNull(getContext()));
        init(view);
        setInterface();

        if (!pref.getMobileNo().equals("")) {
            mobileNo.setText(pref.getMobileNo());
            mobileNo.setClickable(false);
            mobileNo.setCursorVisible(false);
            mobileNo.setFocusable(false);
        }
        if (!pref.getGemail().equals("")) {
            emailid.setText(pref.getGemail());
            emailid.setClickable(false);
            emailid.setCursorVisible(false);
            emailid.setFocusable(false);
            if (!pref.getSOCIAL_NAME().equals("")) {
                fname.setText(pref.getSOCIAL_NAME());
                emailid.setClickable(false);
                emailid.setCursorVisible(false);
                emailid.setFocusable(false);
            }
        }

        aadharNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (aadharNo.length() == 12) {
                    checkAadharCardData();
                }
            }
        });

        pancardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pancardNo.length() == 10) {
                    checkPanCardData();
                }
            }
        });

        account_ifsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (account_ifsc.length() == 11) {
                    checkifsccodedata();
                }
            }
        });

        account_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cnf_account_no.length() > 0) {
                    if (!account_no.getText().toString().equals(cnf_account_no.getText().toString())) {
                        BankError = 1;
                        cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
                    } else {
                        BankError = 0;
                        cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                    }
                }
            }
        });

        cnf_account_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (account_no.length() > 0) {
                    if (!account_no.getText().toString().equals(cnf_account_no.getText().toString())) {
                        BankError = 1;
                        cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
                    } else {
                        BankError = 0;
                        cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                    }
                } else {
                    BankError = 1;
                    cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
                }
            }
        });

        relativMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("Recycle")
            @Override
            public void afterTextChanged(Editable s) {
                pref.setRelativesName("");
                if (relativMobile.length() > 9) {
//                    new ContactsNoValidatore().checkExistanceofNumber(getContext(), relativMobile.getText().toString(), relativMobile);
//                    Toast.makeText(getContext(), ""+pref.getRelativesName(), Toast.LENGTH_SHORT).show();
                    Cursor phones;
                    ArrayList<Contacts> selectUsers = new ArrayList<>();

                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(relativMobile.getText().toString()));
                    String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
                    phones = Objects.requireNonNull(getContext()).getContentResolver().query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    assert phones != null;
                    if (phones.getCount() != 0) {
                        while (phones.moveToNext()) {
                            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            Contacts selectUser = new Contacts();
                            selectUser.setName(name);
                            selectUsers.add(selectUser);
                        }
                        if (selectUsers.size() <= 0) {
                            Log.d("TAG", "No contact details access");
                            pref.setRelativesName("");
                        } else {
                            Log.d("TAG", "contact data new: " + selectUsers.get(0).getName());
                            pref.setRelativesName(selectUsers.get(0).getName());
                        }
                    }
                }
            }
        });

        zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("Recycle")
            @Override
            public void afterTextChanged(Editable s) {
                if (zipcode.length() > 5) {
                    rStateName.setText("");
                    rCityName.setText("");
                    getDataFromPincode(zipcode.getText().toString(), rStateName, rCityName, zipcode);
                } else {
                    rStateName.setText("");
                    rCityName.setText("");
                }
            }
        });

        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("Recycle")
            @Override
            public void afterTextChanged(Editable s) {
                if (zipCode.length() > 5) {
                    adharCity.setText("");
                    adharState.setText("");
                    getAadharDataFromPincode(zipCode.getText().toString(), adharState, adharCity, zipCode);
                } else {
                    adharCity.setText("");
                    adharState.setText("");
                }
            }
        });

        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("Recycle")
            @Override
            public void afterTextChanged(Editable s) {
                if (mobileNo.length() > 9) {
                    if (!pref.getGemail().equals("")) {
                        pref.setUSER_OTP("");
                        otpData.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        checkMobileVerify(mobileNo.getText().toString());
                    }
                } else {
                    pref.setUSER_OTP("");
                    otpData.setText("");
                    otpData.setVisibility(View.GONE);
                    message.setVisibility(View.GONE);
                }
            }
        });

        otpData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otpData.length() == 4) {
                    message.setVisibility(View.GONE);
                    mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            Objects.requireNonNull(getContext()).unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.d("TAG", "onPause: ");
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String sender = intent.getStringExtra("sender");
                assert sender != null;
                if (sender.split("-")[1].equals("DRFINA")) {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            otpData.setText(message);
                        }
                    }, 1000);
                }
            }
        }
    };

    private void init(View view) {
        date_of_birth = view.findViewById(R.id.date_of_birth);
        message = view.findViewById(R.id.message);
        marritalstatus = view.findViewById(R.id.marritalstatus);
        rStateName = view.findViewById(R.id.rStateName);
        rCityName = view.findViewById(R.id.rCityName);
        progressBar = view.findViewById(R.id.progress_bar_lay);
        adharState = view.findViewById(R.id.adharState);
        adharCity = view.findViewById(R.id.adharCity);
        Button registerbtn = view.findViewById(R.id.registerbtn);
        gender = view.findViewById(R.id.gender);
        pancardNo = view.findViewById(R.id.pancardNo);
        aadharNo = view.findViewById(R.id.aadharNo);
        relativRelation = view.findViewById(R.id.relativRelation);
        relativeName = view.findViewById(R.id.relativeName);
        relativMobile = view.findViewById(R.id.relativMobile);
        fname = view.findViewById(R.id.fname);
        emailid = view.findViewById(R.id.emailid);
        rDetailAddress = view.findViewById(R.id.rDetailAddress);
        zipcode = view.findViewById(R.id.zipcode);
        detailAdharAddress = view.findViewById(R.id.detailAdharAddress);
        zipCode = view.findViewById(R.id.zipCode);
        industry = view.findViewById(R.id.industry);
        company = view.findViewById(R.id.company);
        msalary = view.findViewById(R.id.msalary);
        isStudent = view.findViewById(R.id.isStudent);
        educationStatus = view.findViewById(R.id.educationStatus);
        mobileNo = view.findViewById(R.id.mobileNo);
        account_ifsc = view.findViewById(R.id.account_ifsc);
        cnf_account_no = view.findViewById(R.id.cnf_account_no);
        account_no = view.findViewById(R.id.account_no);
        bank_branch = view.findViewById(R.id.bank_branch);
        otpData = view.findViewById(R.id.otpData);

        date_of_birth.setOnClickListener(this);
        marritalstatus.setOnClickListener(this);
        rStateName.setOnClickListener(this);
        rCityName.setOnClickListener(this);
        adharState.setOnClickListener(this);
        adharCity.setOnClickListener(this);
        registerbtn.setOnClickListener(this);
        industry.setOnClickListener(this);
        isStudent.setOnClickListener(this);
        educationStatus.setOnClickListener(this);
        mobileNo.setOnClickListener(this);
        gender.setOnClickListener(this);
        pancardNo.setOnClickListener(this);
        aadharNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_of_birth:
                CustomeDatePicker.showPicker(getContext(), date_of_birth);
                break;

            case R.id.marritalstatus:
                showMarritalstatusAlert(marritalstatus);
                break;

            case R.id.industry:
                showIndustryDialog(industry);
                break;

            case R.id.rCityName:
                if (rStateName.getText().length() == 0) {
                    rStateName.setError("Select State");
                } else {
                    showCityModel(1);
                }
                break;

            case R.id.adharCity:
                if (adharState.getText().length() == 0) {
                    adharState.setError("Select State");
                } else {
                    showCityModel(2);
                }
                break;

            case R.id.registerbtn:
                checkValidation();
                break;

            case R.id.educationStatus:
                showEducationDialog(educationStatus);
                break;

            case R.id.isStudent:
                showStudentStatusDialog(isStudent);
                break;

            case R.id.gender:
                showSelectGenderDialog(gender);
                break;

        }
    }

    private void setInterface() {
        selectCityStateInterface = new SelectCityStateInterface() {
            @Override
            public void onGetStateOrCity(Context context, String flag, String name, String id) {
                switch (flag) {
                    case "city":
                        rCityName.setText(name);
                        dialog.dismiss();
                        break;
                    case "aadhar_city":
                        adharCity.setText(name);
                        dialog.dismiss();
                        break;
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void showStudentStatusDialog(final EditText et) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_select_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText(getResources().getString(R.string.select_student));

        RadioButton rb1, rb2;
        rb1 = dialog.findViewById(R.id.radio1);
        rb1.setText("Yes");

        rb2 = dialog.findViewById(R.id.radio2);
        rb2.setText("No");

        dialog.findViewById(R.id.radio3).setVisibility(View.GONE);
        dialog.findViewById(R.id.radio4).setVisibility(View.GONE);

        final RadioGroup radioGroup = dialog.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                et.setText(radioButton.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showSelectGenderDialog(final EditText et) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_select_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText(getResources().getString(R.string.gender));

        RadioButton rb1, rb2, rb3;
        rb1 = dialog.findViewById(R.id.radio1);
        rb1.setText("Male");

        rb2 = dialog.findViewById(R.id.radio2);
        rb2.setText("Female");

        rb3 = dialog.findViewById(R.id.radio3);
        rb3.setText("Other");


        dialog.findViewById(R.id.radio3).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.radio4).setVisibility(View.GONE);

        final RadioGroup radioGroup = dialog.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                et.setText(radioButton.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showIndustryDialog(final EditText et) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_select_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12, rb13, rb14, rb15, rb16, rb17, rb18, rb19, rb20;

        rb1 = dialog.findViewById(R.id.radio1);
        rb2 = dialog.findViewById(R.id.radio2);
        rb3 = dialog.findViewById(R.id.radio3);
        rb4 = dialog.findViewById(R.id.radio4);
        rb5 = dialog.findViewById(R.id.radio5);

        rb6 = dialog.findViewById(R.id.radio6);
        rb7 = dialog.findViewById(R.id.radio7);
        rb8 = dialog.findViewById(R.id.radio8);
        rb9 = dialog.findViewById(R.id.radio9);
        rb10 = dialog.findViewById(R.id.radio10);

        rb11 = dialog.findViewById(R.id.radio11);
        rb12 = dialog.findViewById(R.id.radio12);
        rb13 = dialog.findViewById(R.id.radio13);
        rb14 = dialog.findViewById(R.id.radio14);
        rb15 = dialog.findViewById(R.id.radio15);

        rb16 = dialog.findViewById(R.id.radio16);
        rb17 = dialog.findViewById(R.id.radio17);
        rb18 = dialog.findViewById(R.id.radio18);
        rb19 = dialog.findViewById(R.id.radio19);
        rb20 = dialog.findViewById(R.id.radio20);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText("Select Industry");

        rb1.setText("Waiter/Waitress");
        rb2.setText("Farmer");
        rb3.setText("Worker");
        rb4.setText("Customer Service");
        rb5.setText("Accounting");
        rb5.setVisibility(View.VISIBLE);
        rb6.setVisibility(View.VISIBLE);
        rb6.setText("Teacher");
        rb7.setVisibility(View.VISIBLE);
        rb7.setText("Shipping courier");
        rb8.setVisibility(View.VISIBLE);
        rb8.setText("Self-employed");
        rb9.setVisibility(View.VISIBLE);
        rb9.setText("Financial Staff");
        rb10.setVisibility(View.VISIBLE);
        rb10.setText("Advertisement");
        rb11.setVisibility(View.VISIBLE);
        rb11.setText("Reporter");
        rb12.setVisibility(View.VISIBLE);
        rb12.setText("Editor");
        rb13.setVisibility(View.VISIBLE);
        rb13.setText("Air hostes");
        rb14.setVisibility(View.VISIBLE);
        rb14.setText("Lawyer");
        rb15.setVisibility(View.VISIBLE);
        rb15.setText("Soldier");
        rb16.setVisibility(View.VISIBLE);
        rb16.setText("Policeman");
        rb17.setVisibility(View.VISIBLE);
        rb17.setText("Sailor");
        rb18.setVisibility(View.VISIBLE);
        rb18.setText("Part-time Job");
        rb19.setVisibility(View.VISIBLE);
        rb19.setText("Entrepreneur");
        rb20.setVisibility(View.VISIBLE);
        rb20.setText("Other");


        final RadioGroup radioGroup = dialog.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                et.setText(radioButton.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showMarritalstatusAlert(final EditText et) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_select_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText(getResources().getString(R.string.marrital_select));

        final RadioGroup radioGroup = dialog.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                et.setText(radioButton.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showEducationDialog(final EditText et) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.status_select_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8;

//        rb9, rb10, rb11, rb12, rb13, rb14, rb15, rb16, rb17, rb18, rb19, rb20

        rb1 = dialog.findViewById(R.id.radio1);
        rb2 = dialog.findViewById(R.id.radio2);
        rb3 = dialog.findViewById(R.id.radio3);
        rb4 = dialog.findViewById(R.id.radio4);
        rb5 = dialog.findViewById(R.id.radio5);

        rb6 = dialog.findViewById(R.id.radio6);
        rb7 = dialog.findViewById(R.id.radio7);
        rb8 = dialog.findViewById(R.id.radio8);

        TextView title = dialog.findViewById(R.id.dialogTitle);
        title.setText("Select Education");

        rb1.setText("Primary");
        rb2.setText("Middle School");
        rb3.setText("High school");
        rb4.setText("Senior Secondary");
        rb5.setVisibility(View.VISIBLE);
        rb5.setText("Under Graduate");
        rb6.setVisibility(View.VISIBLE);
        rb6.setText("Post Graduate");
        rb7.setVisibility(View.VISIBLE);
        rb7.setText("Doctor");
        rb8.setVisibility(View.VISIBLE);
        rb8.setText("Below Primary School");


        final RadioGroup radioGroup = dialog.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialog.findViewById(selectedId);
                et.setText(radioButton.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDataFromPincode(String s, final EditText et, final EditText et1, final EditText et3) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<StateListResponseModel> call1 = apiInterface.getAllDataFromPincode(s);
        call1.enqueue(new Callback<StateListResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<StateListResponseModel> call, @NonNull Response<StateListResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        et.setText(response.body().getState());
                        cityListModels = response.body().getCityListModels();
                    } else {
                        et3.setError("Invalid Pincode");
                        et3.setText("");
                        Toast.makeText(getContext(), "Enter valid Pincode", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StateListResponseModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAadharDataFromPincode(String s, final EditText et, final EditText et1, final EditText et3) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<AadharStateListResponseModel> call1 = apiInterface.getAllAadharDataFromPincode(s);
        call1.enqueue(new Callback<AadharStateListResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AadharStateListResponseModel> call, @NonNull Response<AadharStateListResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        et.setText(response.body().getState());
                        aadharCityListModels = response.body().getAadharCityListModel();
                    } else {
                        et3.setError("Invalid Pincode");
                        et3.setText("");
                        Toast.makeText(getContext(), "Enter valid Pincode", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AadharStateListResponseModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCityModel(int code) {
        dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.recycler_view_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewstate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (code == 2) {
            AllAadharCityAdapters adapter = new AllAadharCityAdapters(getContext(), aadharCityListModels, selectCityStateInterface, code);
            recyclerView.setAdapter(adapter);
        } else {
            AllCityAdapters adapter = new AllCityAdapters(getContext(), cityListModels, selectCityStateInterface, code);
            recyclerView.setAdapter(adapter);
        }

        dialog.show();
    }

    private void checkValidation() {
        String relativeNameInDeice = pref.getRelativesName();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
        if (fname.getText().length() <= 0) {
            fname.setError("Enter Name");
            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
        } else if (date_of_birth.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Date of Birth", Toast.LENGTH_SHORT).show();
            date_of_birth.setError("Select Date of Birth");
        } else if (emailid.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter email id", Toast.LENGTH_SHORT).show();
            emailid.setError("Enter Email Id");
        } else if (!emailid.getText().toString().trim().matches(emailPattern)) {
            emailid.setError("Enter Valid Email Id");
            Toast.makeText(getContext(), "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
        } else if (mobileNo.getText().length() != 10) {
            Toast.makeText(getContext(), "Enter Valid Mobile no", Toast.LENGTH_SHORT).show();
            mobileNo.setError("Enter Valid Mobile No");
        } else if (pancardNo.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Pancard Number", Toast.LENGTH_SHORT).show();
            pancardNo.setError("Enter Pancard No");
        } else if (!pattern.matcher(pancardNo.getText().toString().trim()).matches()) {
            Toast.makeText(getContext(), "Enter Valid Pancard Number", Toast.LENGTH_SHORT).show();
            pancardNo.setError("Enter Valid Pancard No");
        } else if (aadharNo.getText().length() != 12) {
            Toast.makeText(getContext(), "Enter Valid Aadhar Card Number", Toast.LENGTH_SHORT).show();
            aadharNo.setError("Enter Valid Aadhar Card No");
        } else if (gender.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
            gender.setError("Select Gender");
        } else if (rStateName.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Current State", Toast.LENGTH_SHORT).show();
            rStateName.setError("Select Current State");
        } else if (rCityName.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Current City", Toast.LENGTH_SHORT).show();
            rCityName.setError("Select Current City");
        } else if (rDetailAddress.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Current Address", Toast.LENGTH_SHORT).show();
            rDetailAddress.setError("Enter Current Address");
        } else if (zipcode.length() != 6) {
            Toast.makeText(getContext(), "Enter Pincode", Toast.LENGTH_SHORT).show();
            zipcode.setError("Enter Pincode");
        } else if (adharState.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Aadhar State", Toast.LENGTH_SHORT).show();
            adharState.setError("Select Aadhar State");
        } else if (adharCity.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Aadhar City", Toast.LENGTH_SHORT).show();
            adharCity.setError("Select Aadhar City");
        } else if (detailAdharAddress.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Aadhar Address", Toast.LENGTH_SHORT).show();
            detailAdharAddress.setError("Enter Aadhar Address");
        } else if (zipCode.length() != 6) {
            Toast.makeText(getContext(), "Enter Pin Code", Toast.LENGTH_SHORT).show();
            zipCode.setError("Enter Pin Code");
        } else if (relativRelation.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Relative's relation", Toast.LENGTH_SHORT).show();
            relativRelation.setError("Enter Relative's relation");
        } else if (relativeName.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Relative Name", Toast.LENGTH_SHORT).show();
            relativeName.setError("Enter Relative Name");
        } else if (relativMobile.getText().length() != 10) {
            Toast.makeText(getContext(), "Enter Valid Relative's Mobile No", Toast.LENGTH_SHORT).show();
            relativMobile.setError("Enter Valid Relative's Mobile No");
        } else if (relativMobile.getText().toString().equals(mobileNo.getText().toString())) {
            Toast.makeText(getContext(), "Relative's Mobile No and Your mobile no are same", Toast.LENGTH_SHORT).show();
            relativMobile.setError("Relative's Mobile No and Your mobile no are same");
        } else if (educationStatus.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Education", Toast.LENGTH_SHORT).show();
            educationStatus.setError("Select Education");
        } else if (marritalstatus.getText().length() <= 0) {
            Toast.makeText(getContext(), "Select Marrital Status", Toast.LENGTH_SHORT).show();
            marritalstatus.setError("Select Marrital Status");
        } else if (company.getText().length() <= 0) {
            Toast.makeText(getContext(), "Enter Company Name", Toast.LENGTH_SHORT).show();
            marritalstatus.setError("Enter Company Name");
        } else if (Integer.parseInt(msalary.getText().toString()) <= 0) {
            Toast.makeText(getContext(), "Monthly salary should be greater then 0", Toast.LENGTH_SHORT).show();
            msalary.setError("Monthly salary should be greater then 0");
        } else if (account_no.getText().length() <= 0) {
            account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        } else if (cnf_account_no.getText().length() <= 0) {
            cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        } else if (account_ifsc.getText().length() <= 0) {
            account_ifsc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        } else if (!account_no.getText().toString().equals(cnf_account_no.getText().toString())) {
            account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
            cnf_account_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
        } else if (BankError == 1 || AadharError == 1 || PanError == 1) {
            Toast.makeText(getContext(), "Check and fill correct data", Toast.LENGTH_SHORT).show();
        } else if (!pref.getGemail().equals("") && otpData.length() == 0) {
            Toast.makeText(getContext(), "wait for otp, sim card should be in same device", Toast.LENGTH_LONG).show();
        } else {
            ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
            bank = account_no.getText().toString();
            pan = pancardNo.getText().toString();
            aadhar = aadharNo.getText().toString();
            Call<LoanApplyResponseModel> call1 = apiInterface.loan_apply_detail(fname.getText().toString().trim(),
                    emailid.getText().toString().trim(),
                    mobileNo.getText().toString().trim(),
                    date_of_birth.getText().toString().trim(),
                    gender.getText().toString().trim(),
                    pancardNo.getText().toString().trim().toUpperCase(),
                    aadharNo.getText().toString().trim(),
                    rStateName.getText().toString().trim() + "," + rCityName.getText().toString().trim(),
                    rDetailAddress.getText().toString().trim(),
                    zipcode.getText().toString().trim(),
                    adharState.getText().toString().trim() + "," + adharCity.getText().toString().trim(),
                    detailAdharAddress.getText().toString().trim(),
                    zipCode.getText().toString().trim(),
                    educationStatus.getText().toString().trim(),
                    marritalstatus.getText().toString().trim(),
                    industry.getText().toString().trim(),
                    company.getText().toString().trim(),
                    msalary.getText().toString().trim(),
                    relativRelation.getText().toString().trim(),
                    relativeName.getText().toString().trim(),
                    relativMobile.getText().toString().trim(),
                    CustomeDatePicker.getCurrentDate(),
                    pref.getDeviceIMEI(),
                    pref.getAuth_Token(),
                    relativeNameInDeice,
                    account_ifsc.getText().toString(),
                    bank);
            loan_apply_api(call1);
        }
    }

    private void loan_apply_api(Call<LoanApplyResponseModel> call1) {
        progressBar.setVisibility(View.VISIBLE);
        call1.enqueue(new Callback<LoanApplyResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LoanApplyResponseModel> call, @NonNull Response<LoanApplyResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    progressBar.setVisibility(View.GONE);
                    if (response.body().getResult_code().equals("200")) {
                        pref.setUSER_Bank(bank);
                        pref.setUSER_PAN(pan);
                        pref.setUSER_AADHAR(aadhar);
                        Log.d("TAG", "loan apply data: " + response.body().getInfo());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        selectFragmentCallbacks.onActionChange(getContext(), "kyc");
                    }
                    if (response.body().getResult_code().equals("201")) {
                        pref.setUSER_Bank("");
                        pref.setUSER_PAN("");
                        pref.setUSER_AADHAR("");
                        Log.d("TAG", "loan apply data: " + response.body().getInfo());
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationStatus("reject");
                        selectFragmentCallbacks.onActionChange(getContext(), "kyc_reject");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoanApplyResponseModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAadharCardData() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<AadharVerificationData> call1 = apiInterface.aadharCardVarification(pref.getAuth_Token(),
                aadharNo.getText().toString().trim());
        call1.enqueue(new Callback<AadharVerificationData>() {
            @Override
            public void onResponse(@NonNull Call<AadharVerificationData> call, @NonNull Response<AadharVerificationData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        AadharError = 0;
                        aadharNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                        aadharNo.requestFocus();
                        aadharNo.setNextFocusDownId(R.id.pancardNo);
                        aadharNo.setCursorVisible(false);
                        aadharNo.setFocusableInTouchMode(false);
                    } else {
                        AadharError = 1;
                        aadharNo.setCursorVisible(true);
                        aadharNo.setFocusableInTouchMode(true);
                        aadharNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AadharVerificationData> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPanCardData() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<PanCardVerificationData> call1 = apiInterface.panCardVarification(pref.getAuth_Token(),
                fname.getText().toString().trim(),
                pancardNo.getText().toString().trim(),
                date_of_birth.getText().toString().trim());
        call1.enqueue(new Callback<PanCardVerificationData>() {
            @Override
            public void onResponse(@NonNull Call<PanCardVerificationData> call, @NonNull Response<PanCardVerificationData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        PanError = 0;
                        pancardNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                        pancardNo.setCursorVisible(false);
                        pancardNo.setFocusableInTouchMode(false);
                    } else {
                        PanError = 0;
                        pancardNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PanCardVerificationData> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkifsccodedata() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<BankIFSCCodeModel> call1 = apiInterface.ifscCodeVerification(account_ifsc.getText().toString());
        call1.enqueue(new Callback<BankIFSCCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<BankIFSCCodeModel> call, @NonNull Response<BankIFSCCodeModel> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        BankIfsc = 0;
                        bank_branch.setVisibility(View.VISIBLE);
                        bank_branch.setText(response.body().getBranch());
                        account_ifsc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green_24dp, 0);
                        account_ifsc.setCursorVisible(false);
                        account_ifsc.setFocusableInTouchMode(false);
                    } else {
                        BankIfsc = 1;
                        bank_branch.setVisibility(View.GONE);
                        account_ifsc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_warning_black_24dp, 0);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BankIFSCCodeModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkMobileVerify(String s) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<OtpVerifyModel> call1 = apiInterface.mobileVerify(pref.getGemail(),
                s,
                "0",
                "1",
                pref.getAuth_Token());
        call1.enqueue(new Callback<OtpVerifyModel>() {

            @Override
            public void onResponse(Call<OtpVerifyModel> call, Response<OtpVerifyModel> response) {
                progressBar.setVisibility(View.GONE);
                assert response.body() != null;
                pref.setUSER_OTP(response.body().getOtp());
            }

            @Override
            public void onFailure(Call<OtpVerifyModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
