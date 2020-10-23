package com.drfin.drfin_amoney.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckConnection {

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
