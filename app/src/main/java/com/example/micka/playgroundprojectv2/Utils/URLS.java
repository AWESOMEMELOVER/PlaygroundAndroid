package com.example.micka.playgroundprojectv2.Utils;

/**
 * Created by micka on 5/1/2018.
 */

public class URLS {

    public static final String  LOGIN_URL = "http://unix.trosha.dev.lumination.com.ua/login";
    public static final String REGISTRY_URL = "http://unix.trosha.dev.lumination.com.ua/user";
    public static  final String UPLOAD_URL = "http://unix.trosha.dev.lumination.com.ua/uploads";

    public static String getLoginUrl(){
        return LOGIN_URL;
    }

    public static String getBeaconsByIdURL(String id){
        return "http://unix.trosha.dev.lumination.com.ua/user/"+id+"/beacon";
    }

    public static String createTrackingByUserId(String id){
        return "http://unix.trosha.dev.lumination.com.ua/user/"+id+"/tracking";
    }
    public static String getTrackingsByUserID(String id){
        return "http://unix.trosha.dev.lumination.com.ua/tracking/"+id;
    }
    public static String editBeaconByUserId(String userId,String beaconId){
        return "http://unix.trosha.dev.lumination.com.ua/user/"+userId+"/beacon/"+beaconId;
    }
    public static String confirmSmsByUserId(String id){
        return "http://unix.trosha.dev.lumination.com.ua/login/"+id;
    }
    public static String addBeaconToTracking(String trackingId){
        return "http://unix.trosha.dev.lumination.com.ua/tracking/"+trackingId+"/to/beacon";
    }

}
