package com.drfin.drfin_amoney.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.drfin.drfin_amoney.CommonDetailsActivity;
import com.drfin.drfin_amoney.HomeScreenActivity;
import com.drfin.drfin_amoney.R;
import com.drfin.drfin_amoney.utils.PrefrenceHandler;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManagerCompat notificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TAG", "Refreshed token: " + token);
        PrefrenceHandler pref=new PrefrenceHandler(this);
        pref.setFCM_TOKEN(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager= NotificationManagerCompat.from(getApplicationContext());
        Intent notificationIntent = new Intent(this, CommonDetailsActivity.class);
        notificationIntent.putExtra("valid","PreviousLoanList");

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001")
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.rzp_config))
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(intent);
        notificationManager.notify(1, builder.build());
    }
}
