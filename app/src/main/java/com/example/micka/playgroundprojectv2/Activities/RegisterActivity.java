package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micka.playgroundprojectv2.Models.GlobalUser;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.StringUtils;
import com.example.micka.playgroundprojectv2.Utils.URLS;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    final String REGISTRY_URL = URLS.REGISTRY_URL;
    EditText mUserName, mUserLastname, mUserBirthDay, mUserBirthMonth, mUserBirthYear;
    String username, userlastname, userDay, userMonth, userYear,userFullBirth;
    ImageView mRegistry;
    int gender = 1;
    String telNum = "380984895779";
    GlobalUser globalUser;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        queue = Volley.newRequestQueue(this);


        mUserName = (EditText) findViewById(R.id.et_user_name);
        mUserLastname = (EditText) findViewById(R.id.et_user_lastname);
        mUserBirthDay = (EditText) findViewById(R.id.et_birthday_day_holder);
        mUserBirthMonth = (EditText) findViewById(R.id.et_birthday_month_holder);
        mUserBirthYear = (EditText) findViewById(R.id.et_birthday_year_holder);
        mRegistry = (ImageView) findViewById(R.id.btn_registration);


        mRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mUserName.getText().toString();
                userlastname = mUserLastname.getText().toString();
                userYear = mUserBirthYear.getText().toString();
                userMonth = mUserBirthMonth.getText().toString();
                userDay = mUserBirthDay.getText().toString();

                userFullBirth = buildBirthDay();
                sendData();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String buildBirthDay(){
        return userYear+"-"+userMonth+"-"+userDay;
    }

    private void sendData(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, REGISTRY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    Log.i("Trofim responce: ",response.toString());
                    MainActivity.globalUser.setUserId(StringUtils.getStringValue(response));
                    MainActivity.globalUser.setFirstName(username);
                    MainActivity.globalUser.setLastName(userlastname);
                    MainActivity.globalUser.setGender(String.valueOf(gender));
                    MainActivity.globalUser.setBirthday(userFullBirth);
                    Log.i("Users ID: ",MainActivity.globalUser.getUserId());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error responce: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("phoneNumber",telNum);
                params.put("firstName",username);
                params.put("lastName",userlastname);
                params.put("gender", String.valueOf(gender));
                params.put("birthday",userFullBirth);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
