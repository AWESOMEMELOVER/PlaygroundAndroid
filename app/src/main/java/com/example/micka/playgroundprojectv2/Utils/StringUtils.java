package com.example.micka.playgroundprojectv2.Utils;

/**
 * Created by micka on 3/16/2018.
 */

public class StringUtils {

    public static String getStringValue(String responce){
        String result="";
        String [] bufStrings = responce.split("\\:");
        result=bufStrings[1].substring(1,bufStrings[1].length()-2);

        return result;
    }

}
