package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class ComfirmSMSActivity extends AppCompatActivity {

    EditText vFirstNum,vSecNum,vThirdNum,vFourhNum;
    ImageView mConfirm;
    String fullSmsCode,userID;
    final String URL = "http://unix.trosha.dev.lumination.com.ua/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_sms);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userId");

        vFirstNum = (EditText) findViewById(R.id.et_first_numb);
        vSecNum = (EditText) findViewById(R.id.et_second_numb);
        vThirdNum = (EditText) findViewById(R.id.et_third_numb);
        vFourhNum = (EditText) findViewById(R.id.et_fourth_numb);
        mConfirm = (ImageView)findViewById(R.id.btn_sms_button);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullSmsCode = buildSmsCode(vFirstNum.getText().toString(),vSecNum.getText().toString()
                        ,vThirdNum.getText().toString(),vFourhNum.getText().toString());
                verifySmsCode(fullSmsCode);
            }


        });
    }

    private String buildSmsCode(String f,String s,String t,String fo){
        return f+s+t+fo;
    }
    private void verifySmsCode(final String code) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Trofim responce: ",response);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("userId",userID);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Responce Error: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("smsCode",code);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
