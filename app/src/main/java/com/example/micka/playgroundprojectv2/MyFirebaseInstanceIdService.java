package com.example.micka.playgroundprojectv2;

import android.content.Intent;
import android.util.Log;

import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by micka on 5/6/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";

    String TAG = "MYFIREBASEID";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.wtf(TAG, "Refreshed token: " + refreshedToken);

        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));

        SharedPrefUser.getInstance(getApplicationContext()).saveNewDiviceToken(refreshedToken);
    }

}
