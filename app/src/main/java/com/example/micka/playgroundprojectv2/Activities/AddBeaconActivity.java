package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;
import com.example.micka.playgroundprojectv2.Utils.URLS;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AddBeaconActivity extends AppCompatActivity {

    private EditText mNewBeaconName;
    private ImageView mBtnConfirm;
    private TextView addAva;
    private String beaconId,beaconName;
    private String SAVE_BEACON_URL;
    private String userId;
    private String imgurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beacon);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        if (intent.getStringExtra("qrCodeValue") != null) {
            beaconId = intent.getStringExtra("qrCodeValue");
        }else {
            beaconId = SharedPrefUser.getInstance(getApplicationContext()).getBeaconId();
        }

        if(intent.getStringExtra("classFrom")!= null && intent.getStringExtra("classFrom").equals(UploadActivity.class.toString())) {
            imgurl = intent.getStringExtra("imageUrl");
        }

        userId = SharedPrefUser.getInstance(getApplicationContext()).getUserId();
        SAVE_BEACON_URL = URLS.getBeaconsByIdURL(userId);

        mNewBeaconName = (EditText) findViewById(R.id.et_new_beacon_name);
        mBtnConfirm = (ImageView) findViewById(R.id.btn_add_new_beacon_inf);
        addAva = (TextView) findViewById(R.id.tv_add_new_ava);

        addAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transferToUploadImageFromAddBeacon = new Intent(getApplicationContext(),UploadActivity.class);
                transferToUploadImageFromAddBeacon.putExtra("classFrom",AddBeaconActivity.class.toString());
                startActivity(transferToUploadImageFromAddBeacon);
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beaconName = mNewBeaconName.getText().toString();
                saveNewBeacon(beaconName);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i("BEACON QR CODE : ",beaconId+" is beaconID");
    }

    private void saveNewBeacon(String beaconName2){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SAVE_BEACON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("onResponse: ",beaconId);
                Log.i("Responce: ",response);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("name",beaconName2);
                params.put("beaconId",beaconId);
                params.put("imageUrl",imgurl);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
