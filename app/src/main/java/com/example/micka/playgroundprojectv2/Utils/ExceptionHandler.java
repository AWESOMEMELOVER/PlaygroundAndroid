package com.example.micka.playgroundprojectv2.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by micka on 4/29/2018.
 */

public class ExceptionHandler {

    public enum ExceptionType{
        TELEPHONE("Can't find user with this phone number.");

        private String exception;

        ExceptionType(String exception){
            this.exception = exception;
        }


        @Override
        public String toString() {
            return exception;
        }
    }
    Context mContext;

    public ExceptionHandler(Context context){
        this.mContext = context;
    }

    public boolean showException(String exception){
        if(ExceptionType.TELEPHONE.toString() == exception){
            Toast.makeText(mContext,ExceptionType.TELEPHONE.toString(),Toast.LENGTH_LONG);
            return true;
        }
        return false;
    }



}
