package com.example.micka.playgroundprojectv2.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.micka.playgroundprojectv2.R;

/**
 * Created by micka on 5/6/2018.
 */

public class MyNotificationManager {

    private Context mCntx;
    public static final int NOTIFICATION_ID = 234;
    public MyNotificationManager(Context context){
        this.mCntx = context;
    }

    public void showNotification(String from , String notification, Intent intent){

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mCntx,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCntx);

        Notification myNotification = builder.setSmallIcon(R.drawable.ava)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentTitle(notification)
                .build();

        myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager)
                mCntx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID,myNotification);
    }

}
