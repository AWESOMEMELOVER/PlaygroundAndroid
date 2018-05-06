package com.example.micka.playgroundprojectv2;

import android.content.Intent;
import android.util.Log;

import com.example.micka.playgroundprojectv2.Activities.MainActivity;
import com.example.micka.playgroundprojectv2.Activities.MyNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by micka on 5/6/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "fcmexamplemessage";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.wtf(TAG,"Fom: "+remoteMessage.getFrom());
        Log.wtf(TAG,"Notification Message Body: "+remoteMessage.getNotification());

        notifyUser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
    }

    public void notifyUser(String from, String notif){
        MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());
        myNotificationManager.showNotification(from,notif,new Intent(getApplicationContext(),MainActivity.class));
    }
}
