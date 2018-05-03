package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;
import com.example.micka.playgroundprojectv2.Utils.URLS;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class ComfirmSMSActivity extends AppCompatActivity {

    EditText vFirstNum,vSecNum,vThirdNum,vFourhNum;
    ImageView mConfirm;
    String fullSmsCode,userID;
     String URL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_sms);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userId");

        URL = URLS.confirmSmsByUserId(userID);

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
                Log.i("SMS CODE: ",fullSmsCode);
                verifySmsCode(fullSmsCode);
            }


        });

       vFirstNum.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(vFirstNum.getText() != null){
                    vSecNum.requestFocus();
               }
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
       vSecNum.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(vSecNum.getText() != null){
                   vThirdNum.requestFocus();
               }
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

        vThirdNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(vThirdNum.getText() != null){
                    vFourhNum.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private String buildSmsCode(String f,String s,String t,String fo){
        return f+s+t+fo;
    }

    private void verifySmsCode(final String code) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = (new JsonParser()).parse(response).getAsJsonObject();
                if (jsonObject.has("error")) {
                    Toast.makeText(getApplicationContext(),jsonObject.get("error").toString(),Toast.LENGTH_LONG).show();
                } else {
                    Log.i("Trofim responce: ", response);
                    SharedPrefUser.getInstance(getApplicationContext()).saveNewId(jsonObject.get("userId").getAsString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userId", userID);
                    startActivity(intent);
                    overridePendingTransition(R.animator.slide_in_up, R.animator.slide_out_up);
                }
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
