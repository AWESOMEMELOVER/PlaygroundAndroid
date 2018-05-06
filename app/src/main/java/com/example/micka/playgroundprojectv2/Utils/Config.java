package com.example.micka.playgroundprojectv2.Utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by micka on 4/22/2018.
 */


    public class Config {
        // File upload url (replace the ip with your server address)
        public static final String FILE_UPLOAD_URL = "http://unix.trosha.dev.lumination.com.ua/uploads/";

        // Directory name to store captured images and videos
        public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";


        public static void displayImage(Context context, String url , CircleImageView to){
            String imageForPiccassa = "http://unix.trosha.dev.lumination.com.ua/"+url;
            String actualUrl = imageForPiccassa.replace("\\", "/");
            Picasso.with(context).load(actualUrl).into(to);
        }
    }
