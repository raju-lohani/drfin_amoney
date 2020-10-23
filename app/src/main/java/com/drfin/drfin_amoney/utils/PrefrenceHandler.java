package com.drfin.drfin_amoney.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PrefrenceHandler {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PrefrenceHandler(Context context) {
        sharedPreferences= context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private String DeviceName="DeviceName";
    private String DeviceId="DeviceId";
    private String DeviceIMEI="DeviceIMEI";
    private String DeviceModel="DeviceModel";
    private String Auth_Token="Auth_Token";
    private String Call_log="Call_log";
    private String Latitude="latitude";
    private String Longitude="longitude";
    private String InstalledApp="InstalledApp";
    private String ContactList="ContactList";
    private String MobileNo="MobileNo";
    private String RelativesName="RelativesName";
    private String Gmobile="Gmobile";
    private String Gemail="Gemail";
    private String Gname="Gname";
    private String LastApplicationNo="LastApplicationNo";
    private String LastApplicationDate="LastApplicationDate";
    private String LastApplicationStatus="LastApplicationStatus";
    private String FCM_TOKEN="FCM_TOKEN";
    private String USER_OTP="USER_OTP";
    private String USER_Address="USER_Address";
    private String USER_Bank="USER_Bank";
    private String USER_PAN="USER_PAN";
    private String USER_AADHAR="USER_AADHAR";
    private String SOCIAL_NAME="SOCIAL_NAME";
    private String NOTIFY_DIALOG="NOTIFY_DIALOG";

    public String getNOTIFY_DIALOG() {
        return sharedPreferences.getString(NOTIFY_DIALOG,"");
    }

    public void setNOTIFY_DIALOG(String notify_dialog) {
        editor.putString(NOTIFY_DIALOG,notify_dialog);
        editor.commit();
    }

    public String getSOCIAL_NAME() {
        return sharedPreferences.getString(SOCIAL_NAME,"");
    }

    public void setSOCIAL_NAME(String social_name) {
        editor.putString(SOCIAL_NAME,social_name);
        editor.commit();
    }

    public String getUSER_PAN() {
        return sharedPreferences.getString(USER_PAN,"");
    }

    public void setUSER_PAN(String user_pan) {
        editor.putString(USER_PAN,user_pan);
        editor.commit();
    }

    public String getUSER_AADHAR() {
        return sharedPreferences.getString(USER_AADHAR,"");
    }

    public void setUSER_AADHAR(String user_aadhar) {
        editor.putString(USER_AADHAR,user_aadhar);
        editor.commit();
    }

    public String getUSER_Bank() {
        return sharedPreferences.getString(USER_Bank,"");
    }

    public void setUSER_Bank(String user_bank) {
        editor.putString(USER_Bank,user_bank);
        editor.commit();
    }

    public String getUSER_Address() {
        return sharedPreferences.getString(USER_Address,"");
    }

    public void setUSER_Address(String user_address) {
        editor.putString(USER_Address,user_address);
        editor.commit();
    }

    public String getUSER_OTP() {
        return sharedPreferences.getString(USER_OTP,"");
    }

    public void setUSER_OTP(String user_otp) {
        editor.putString(USER_OTP,user_otp);
        editor.commit();
    }

    public String getFCM_TOKEN() {
        return sharedPreferences.getString(FCM_TOKEN,"");
    }

    public void setFCM_TOKEN(String fcm_token) {
        editor.putString(FCM_TOKEN,fcm_token);
        editor.commit();
    }

    public String getLastApplicationStatus() {
        return sharedPreferences.getString(LastApplicationStatus,"");
    }

    public void setLastApplicationStatus(String lastApplicationStatus) {
        editor.putString(LastApplicationStatus,lastApplicationStatus);
        editor.commit();
    }

    public String getLastApplicationNo() {
        return sharedPreferences.getString(LastApplicationNo,"");
    }

    public void setLastApplicationNo(String lastApplicationNo) {
        editor.putString(LastApplicationNo,lastApplicationNo);
        editor.commit();
    }

    public String getLastApplicationDate() {
        return sharedPreferences.getString(LastApplicationDate,"");
    }

    public void setLastApplicationDate(String lastApplicationDate) {
        editor.putString(LastApplicationDate,lastApplicationDate);
        editor.commit();
    }


    public String getDeviceId() {
        return sharedPreferences.getString(DeviceId,"");
    }

    public void setDeviceId(String deviceId) {
        editor.putString(DeviceId,deviceId);
        editor.commit();
    }

    public String getRelativesName() {
        return sharedPreferences.getString(RelativesName,"");
    }

    public void setRelativesName(String relativesName) {
        editor.putString(RelativesName,relativesName);
        editor.commit();
    }

    public String getMobileNo() {
        return sharedPreferences.getString(MobileNo,"");
    }

    public void setMobileNo(String mobileNo) {
        editor.putString(MobileNo,mobileNo);
        editor.commit();
    }

    public String getContactList() {
        return sharedPreferences.getString(ContactList,"");
    }

    public void setContactList(String contactList) {
        editor.putString(ContactList,contactList);
        editor.commit();
    }

    public String getInstalledApp() {
        return sharedPreferences.getString(InstalledApp,"");
    }

    public void setInstalledApp(String installedApp) {
        editor.putString(InstalledApp,installedApp);
        editor.commit();
    }

    public String getCall_log() {
        return sharedPreferences.getString(Call_log,"");
    }

    public void setCall_log(String call_log) {
        editor.putString(Call_log,call_log);
        editor.commit();
    }

    public String getLatitude() {
        return sharedPreferences.getString(Latitude,"");
    }

    public void setLatitude(String latitude) {
        editor.putString(Latitude,latitude);
        editor.commit();
    }

    public String getLongitude() {
        return sharedPreferences.getString(Longitude,"");
    }

    public void setLongitude(String longitude) {
        editor.putString(Longitude,longitude);
        editor.commit();
    }


    public String getAuth_Token() {
        return sharedPreferences.getString(Auth_Token,"");
    }

    public void setAuth_Token(String auth_Token) {
        editor.putString(Auth_Token,auth_Token);
        editor.commit();
    }

    public String getDeviceName() {
        return sharedPreferences.getString(DeviceName,"");
    }

    public void setDeviceName(String deviceId) {
        editor.putString(DeviceName,deviceId);
        editor.commit();
    }

    public String getDeviceIMEI() {
        return sharedPreferences.getString(DeviceIMEI,"");
    }

    public void setDeviceIMEI(String deviceIMEI) {
        editor.putString(DeviceIMEI,deviceIMEI);
        editor.commit();
    }

    public String getDeviceModel() {
        return sharedPreferences.getString(DeviceModel,"");
    }

    public void setDeviceModel(String deviceModel) {
        editor.putString(DeviceModel,deviceModel);
        editor.commit();
    }

    public String getGmobile() {
        return sharedPreferences.getString(Gmobile,"");
    }

    public void setGmobile(String gmobile) {
        editor.putString(Gmobile,gmobile);
        editor.commit();
    }

    public String getGemail() {
        return sharedPreferences.getString(Gemail,"");
    }

    public void setGemail(String gemail) {
        editor.putString(Gemail,gemail);
        editor.commit();
    }

    public String getGname() {
        return sharedPreferences.getString(Gname,"");
    }

    public void setGname(String gname) {
        editor.putString(Gname,gname);
        editor.commit();    }
}
