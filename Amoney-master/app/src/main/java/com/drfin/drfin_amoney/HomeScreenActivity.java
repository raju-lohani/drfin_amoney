package com.drfin.drfin_amoney;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.drfin.drfin_amoney.Services.GPSService;
import com.drfin.drfin_amoney.Services.UserAllInformationService;
import com.drfin.drfin_amoney.fragments.KycFragment;
import com.drfin.drfin_amoney.fragments.LoanApplyDoneFragment;
import com.drfin.drfin_amoney.fragments.LoanFragment;
import com.drfin.drfin_amoney.fragments.ProfileFragment;
import com.drfin.drfin_amoney.fragments.UserDetailsFillUpFragment;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.SelectFragmentCallbacks;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.security.AccessController.getContext;

/**
 * Created by Pulkit
 */

public class HomeScreenActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomMenuNavigation;
    private int selected_item = 0;
    private PrefrenceHandler pref;
    private SelectFragmentCallbacks selectFragmentCallbacks;
    private LocationManager mylocationmanager;
    boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        pref = new PrefrenceHandler(this);

        init();
        selectFragmentCallbacks = new SelectFragmentCallbacks() {
            @Override
            public void onActionChange(Context context, String flag) {
                switch (flag) {
                    case "login":
                        Intent intent = new Intent(HomeScreenActivity.this, UserAllInformationService.class);
                        startService(intent);
                        if (pref.getLastApplicationNo().equals("") && pref.getLastApplicationStatus().equals("")) {
                            selectFragment(new UserDetailsFillUpFragment(selectFragmentCallbacks));
                        } else if (!pref.getLastApplicationNo().equals("") && pref.getLastApplicationStatus().equals("")) {
                            selectFragment(new KycFragment(selectFragmentCallbacks));
                        }else if (pref.getLastApplicationStatus().equals("reject")) {
                            selectFragment(new LoanFragment(selectFragmentCallbacks));
                        }
                        break;
                    case "logout":
                        Toast.makeText(context, "Log Out Successful.", Toast.LENGTH_SHORT).show();
                        bottomMenuNavigation.setSelectedItemId(bottomMenuNavigation.getMenu().getItem(0).getItemId());
                        break;
                    case "selectLogin":
                        bottomMenuNavigation.setSelectedItemId(bottomMenuNavigation.getMenu().getItem(0).getItemId());
                        break;
                    case "kyc":
                        selectFragment(new KycFragment(selectFragmentCallbacks));
                        break;

                    case "kyc_done":
                        selectFragment(new LoanApplyDoneFragment("kyc_done"));
                        break;

                    case "kyc_reject":
                        selectFragment(new LoanApplyDoneFragment("kyc_reject"));
                        break;

                    case "data_fill":
                        intent = new Intent(HomeScreenActivity.this, UserAllInformationService.class);
                        startService(intent);
                        selectFragment(new UserDetailsFillUpFragment(selectFragmentCallbacks));
                        break;
                }
            }
        };
        selectFragment(new LoanFragment(selectFragmentCallbacks));
    }

    private void init() {
        bottomMenuNavigation = findViewById(R.id.bottomMenuNavigation);
        bottomMenuNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mGpsSwitchStateReceiver);
        } catch (Exception e) {
            Log.d("TAG", "onPause: ");
        }
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(mGpsSwitchStateReceiver, filter);
        gpsprovidercheck();
        super.onResume();
    }

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGpsEnabled || !isNetworkEnabled) {
                    gpsprovidercheck(); // Handle Location turned OFF
                }
            }
        }
    };

    private void gpsprovidercheck() {
        mylocationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = mylocationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!GpsStatus) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning..");
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            alert.setMessage(getResources().getString(R.string.enable_gps));
            alert.setCancelable(false);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(action));
                }
            });
            alert.show();
        } else {
            PhoneMiMe();
            getLocationFromGPS();
            if (pref.getLastApplicationStatus().equals("pass")) {
                if (pref.getNOTIFY_DIALOG().equals("")) {
                    showLoanConfirmDialog();
                }
            } else if (pref.getLastApplicationStatus().equals("reject")) {
                if (pref.getNOTIFY_DIALOG().equals("")) {
                    showLoanRejectDialog();
                }
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.loan) {
            selectFragment(new LoanFragment(selectFragmentCallbacks));
            selected_item = 0;
            return true;
        } else if (menuItem.getItemId() == R.id.profile) {
            selectFragment(new ProfileFragment(selectFragmentCallbacks));
            selected_item = 1;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (R.id.loan != bottomMenuNavigation.getSelectedItemId()) {
            bottomMenuNavigation.getMenu().getItem(0).setChecked(true);
            if (pref.getAuth_Token().equals("")) {
                selectFragment(new LoanFragment(selectFragmentCallbacks));
            } else {
                selectFragment(new LoanFragment(selectFragmentCallbacks));
            }
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void PhoneMiMe() {
        TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            assert telephonyManager != null;
            Log.d("TAG", "Get sim information: " + telephonyManager.getNetworkOperatorName());
            String imeiNumber1, imeiNumber2;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                imeiNumber1 = telephonyManager.getDeviceId(0);
                imeiNumber2 = telephonyManager.getDeviceId(1);
            } else {
                imeiNumber1 = telephonyManager.getDeviceId();
                imeiNumber2 = "";
            }
            String DeviceModel = android.os.Build.MODEL;
            String DeviceName = android.os.Build.MANUFACTURER;
            pref.setDeviceIMEI("Sim operator: " + telephonyManager.getNetworkOperatorName()
                    + ",Sim 1 IMEI: " + imeiNumber1
                    + ",Sim 2 IMEI: " + imeiNumber2);
            pref.setDeviceModel(DeviceModel);
            pref.setDeviceName(DeviceName);
            pref.setDeviceId(imeiNumber1);
            Log.d("Tag", "Device Information: " + pref.getDeviceIMEI() + " " + pref.getDeviceModel() + " " + pref.getDeviceName());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void getLocationFromGPS() {
        Intent i = new Intent(HomeScreenActivity.this, GPSService.class);
        startService(i);
    }

    private void selectFragment(Fragment fr) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fr);
        ft.commit();
    }

    private void showLoanConfirmDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogLayout = inflater.inflate(R.layout.fragment_done_loan_layout, null);
        builder.setView(dialogLayout);
        TextView message = dialogLayout.findViewById(R.id.message);
        TextView title = dialogLayout.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Loan \n Approved");
        Button continueButton = dialogLayout.findViewById(R.id.continueButton);
        dialogLayout.findViewById(R.id.continueButtonLayout).setVisibility(View.VISIBLE);
        dialogLayout.findViewById(R.id.statusLayout).setVisibility(View.GONE);
        message.setText(getResources().getString(R.string.loan_application_approved));
        final AlertDialog d = builder.show();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                startActivity(new Intent(HomeScreenActivity.this, CommonDetailsActivity.class).putExtra("valid", "LoanAmountDetails"));
            }
        });

    }

    private void showLoanRejectDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("Loan Status");
        @SuppressLint("InflateParams")
        final View dialogLayout = inflater.inflate(R.layout.loan_reject_activity, null);
        builder.setView(dialogLayout);
        TextView message = dialogLayout.findViewById(R.id.message);
        Button continueButton = dialogLayout.findViewById(R.id.continueButton);
        dialogLayout.findViewById(R.id.continueButtonLayout).setVisibility(View.VISIBLE);
        dialogLayout.findViewById(R.id.statusLayout).setVisibility(View.GONE);
        ImageView imageView = dialogLayout.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.no_found);
        message.setText(getResources().getString(R.string.loan_reject_approved));
        final AlertDialog d = builder.show();
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

}
