package com.example.micka.playgroundprojectv2.Activities;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.*;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.URLS;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;
import com.google.gson.JsonObject;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    ImageView imageview;
    String imagepath;
    File sourceFile;
    int totalSize = 0;
    String FILE_UPLOAD_URL = "http://unix.trosha.dev.lumination.com.ua/upload";
    LinearLayout uploader_area;
    LinearLayout progress_area;
    public ProgressBar donut_progress;
    Intent intent;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.upload_activity);

        uploader_area = (LinearLayout) findViewById(R.id.uploader_area);
        progress_area = (LinearLayout) findViewById(R.id.progress_area);
        Button select_button = (Button) findViewById(R.id.button_selectpic);
        Button upload_button = (Button) findViewById(R.id.button_upload);
        donut_progress = (ProgressBar) findViewById(R.id.donut_progress);
        imageview = (ImageView) findViewById(R.id.imageview);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("classFrom").equals(AddBeaconActivity.class.toString())){
            intent = new Intent(getApplicationContext(),AddBeaconActivity.class);
        }else if(bundle.getString("classFrom").equals(EditBeaconActivity.class.toString())){
            intent = new Intent(getApplicationContext(),EditBeaconActivity.class);
        }

        Boolean hasPermission = (ContextCompat.checkSelfPermission(com.example.micka.playgroundprojectv2.Activities.UploadActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(com.example.micka.playgroundprojectv2.Activities.UploadActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {

        }

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*"); // intent.setType("video/*"); to select videos to upload
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFileAsync uploadAsync = new UploadFileAsync();
                uploadAsync.execute();

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(com.example.micka.playgroundprojectv2.Activities.UploadActivity.this, "You must give access to storage.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger images
            // options.inSampleSize = 10;
            final Bitmap bitmap = BitmapFactory.decodeFile(imagepath, options);
            imageview.setImageBitmap(bitmap);

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private class UploadFileAsync extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(FILE_UPLOAD_URL);

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                File file = new File(imagepath);
                if (file != null) {
                    Log.i("FILE: ",file.toString());
                    entityBuilder.addBinaryBody("file", file);

                    Log.i("FILE: ",entityBuilder.toString());
                }

                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();

                if(entity != null){
                    String retSrc =  EntityUtils.toString(httpEntity);
                    JSONObject result = new JSONObject(retSrc);
                    Log.i("JSON RESPONCE : ",result.toString());
                    intent.putExtra("classFrom",UploadActivity.class.toString());
                    intent.putExtra("imageUrl",result.getString("file"));
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
