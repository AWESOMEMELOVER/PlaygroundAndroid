package com.example.micka.playgroundprojectv2.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by micka on 5/1/2018.
 */

public class SharedPrefUser {

    private static SharedPrefUser sharedPrefUser = new SharedPrefUser();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    private final String sPref_userId = "sPref_userId";
    private final String sPref_trackingId= "sPref_trackingId";
    private final String sPref_beaconId= "sPref_beaconId";
    private final String sPref_divice_token = "sPref_divice_token";
    private SharedPrefUser(){}

    public static SharedPrefUser getInstance(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharedPrefUser;
    }

    public void saveNewId(String id){
        editor.putString(sPref_userId,id);
        editor.commit();
    }
    public String getUserId(){
        return sharedPreferences.getString(sPref_userId,null);
    }

    public void saveNewTrackingId(String id){
        editor.putString(sPref_trackingId,id);
        editor.commit();
    }

    public String getTrackingId(){
        return sharedPreferences.getString(sPref_trackingId,null);
    }

    public void saveNewBeaconId(String id){
        editor.putString(sPref_beaconId,id);
        editor.commit();
    }
    public String getBeaconId(){
        return sharedPreferences.getString(sPref_beaconId,null);
    }
    public void saveNewDiviceToken(String diviceToken){
        editor.putString(sPref_divice_token,diviceToken);
        editor.commit();
    }
    public String getDiviceToken(){
        return sharedPreferences.getString(sPref_divice_token,null);
    }
}
