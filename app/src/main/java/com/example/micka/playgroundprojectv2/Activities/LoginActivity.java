package com.example.micka.playgroundprojectv2.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.ExceptionHandler;
import com.example.micka.playgroundprojectv2.Utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ImageView mButtonLogin;
    EditText mTelephoneLogin;
    final String LOGIN_URL = "http://unix.trosha.dev.lumination.com.ua/login";
    String telephoneNumber;
    RequestQueue queue;
    TextView link_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        queue = Volley.newRequestQueue(this);
        mButtonLogin = (ImageView) findViewById(R.id.btn_login_button);
        mTelephoneLogin = (EditText) findViewById(R.id.et_phone_number);
        link_signup = (TextView)findViewById(R.id.link_signup);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTelephoneLogin.getText().toString()!=null){
                    telephoneNumber = mTelephoneLogin.getText().toString();
                    HashMap<String, String> data = new HashMap<>();
                    data.put("phoneNumber",telephoneNumber);
                    sendTelephoneNumber();
                }
            }
        });
    }


    private void sendTelephoneNumber(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Responce is: ",response.toString());
                if(response!=null){
                    Intent intent = new Intent(getApplicationContext(),ComfirmSMSActivity.class);
                    intent.putExtra("userId", StringUtils.getStringValue(response));
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params = new HashMap<>();
                params.put("phoneNumber",telephoneNumber);
                return params;
            }
        };
        queue.add(stringRequest);
    }

 /*  private void sendTelephoneNumber(HashMap data){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Responce is: ", response.toString());
                Log.i("TELEPHONE NUM: ", telephoneNumber);
                if (response != null) {
                  // if (!response.has("error")) {
                        try {
                            //if(response.getString("error") == new Exception())
                            Intent intent = new Intent(getApplicationContext(), ComfirmSMSActivity.class);
                            intent.putExtra("userId", response.getString("id"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    *//*else{
                        Toast.makeText(getApplicationContext(),"Empty telephone field",Toast.LENGTH_LONG);
                    }*//*
                }
           // }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley error: ",error.toString());
            }

        });
        queue.add(request);
    }
*/
}
