package com.drfin.drfin_amoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.drfin.drfin_amoney.models.LastApplicationNumber;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.CheckConnection;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pulkit
 */

public class MainActivity extends AppCompatActivity {
    private PrefrenceHandler pref;
    private ConstraintLayout constraintLayout;
    private String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        pref=new PrefrenceHandler(this);
        constraintLayout=findViewById(R.id.constraaintLayout);
        ConnectionChecking();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void ConnectionChecking(){
        if (CheckConnection.isConnected(this)) {
            get_last_application_no();
            checkAllPermission();
        }else {
            Snackbar snackbar = Snackbar.make(constraintLayout, getResources().getString(R.string.connection_error), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        ConnectionChecking();
                }
            });
            snackbar.show();
        }
    }

    private void splashOut() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,HomeScreenActivity.class));
                finish();
            }
        },2000);
    }


    private void get_last_application_no() {
        ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
        Call<LastApplicationNumber> call1 = apiInterface.getLoanApplicationNo(pref.getAuth_Token());
        call1.enqueue(new Callback<LastApplicationNumber>() {
            @Override
            public void onResponse(@NonNull Call<LastApplicationNumber> call,@NonNull Response<LastApplicationNumber> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("TAG", "responce body: " + response.body().getResult_code());
                    assert response.body() != null;
                    if (response.body().getResult_code().equals("200")) {
                        pref.setLastApplicationNo(response.body().getApplication_number());
                        pref.setLastApplicationDate(response.body().getApply_date());
                        pref.setLastApplicationStatus(response.body().getLoan_status());
                    }else {
                        pref.setLastApplicationNo("");
                        pref.setLastApplicationDate("");
                        pref.setLastApplicationStatus("");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LastApplicationNumber> call,@NonNull Throwable t) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPolicy() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("Privacy Policy");
        @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_policy, null);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ActivityCompat.requestPermissions(MainActivity.this, permission, 100);
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_DENIED
                || grantResults[1] == PackageManager.PERMISSION_DENIED
                || grantResults[2] == PackageManager.PERMISSION_DENIED
                || grantResults[3] == PackageManager.PERMISSION_DENIED
                || grantResults[4] == PackageManager.PERMISSION_DENIED
                || grantResults[5] == PackageManager.PERMISSION_DENIED
                || grantResults[6] == PackageManager.PERMISSION_DENIED) {
            checkAllPermission();
        } else {
            splashOut();
        }
    }

    private void checkAllPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)) {
            showPolicy();
        } else {
            splashOut();
        }
    }

}
