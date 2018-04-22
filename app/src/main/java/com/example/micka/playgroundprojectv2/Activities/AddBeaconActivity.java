package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AddBeaconActivity extends AppCompatActivity {

    private EditText mNewBeaconName;
    private ImageView mBtnConfirm;
    private String beaconId,beaconName;
    String URL = "http://unix.trosha.dev.lumination.com.ua/user/";
    String URL2 = "/beacon/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beacon);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        beaconId = intent.getStringExtra("qrCodeValue");

        mNewBeaconName = (EditText) findViewById(R.id.et_new_beacon_name);
        mBtnConfirm = (ImageView) findViewById(R.id.btn_add_new_beacon_inf);


        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beaconName = mNewBeaconName.getText().toString();
                saveNewBeacon();
            }
        });
    }


    private void saveNewBeacon(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + 1 + URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                params.put("name",beaconName);
                params.put("beaconId",beaconId);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
