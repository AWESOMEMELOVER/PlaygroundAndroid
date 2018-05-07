package com.example.micka.playgroundprojectv2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.Fragments.BeaconFragment;
import com.example.micka.playgroundprojectv2.Fragments.SettingFragment;
import com.example.micka.playgroundprojectv2.Fragments.TrackingFragment;
import com.example.micka.playgroundprojectv2.Models.GlobalUser;
import com.example.micka.playgroundprojectv2.Models.Zone;
import com.example.micka.playgroundprojectv2.MyFirebaseInstanceIdService;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;
import com.example.micka.playgroundprojectv2.Utils.URLS;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private TextView mTextMessage;
    private ImageView addNewBeacon;
    static GlobalUser globalUser = new GlobalUser();
    private String id;
    private BroadcastReceiver broadcastReceiver;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = SharedPrefUser.getInstance(getApplicationContext()).getUserId();
        addNewBeacon = (ImageView) findViewById(R.id.btn_add_new_beacon);
        token = SharedPrefUser.getInstance(getApplicationContext()).getDiviceToken();

        Log.wtf("MAINACT TOKEn", token);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sendGcm(id,token);
            }
        },2000);

        switchFragment(new BeaconFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.wtf("USER ID FROM SHAREDPREF: ",id);






        addNewBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),QRCodeActivity.class);
                startActivity(intent1);
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_beacon:
                    addNewBeacon.setVisibility(View.VISIBLE);
                    switchFragment(new BeaconFragment());
                    return true;
                case R.id.navigation_tracking:
                    addNewBeacon.setVisibility(View.INVISIBLE);
                    switchFragment(new TrackingFragment());
                    return true;
                case R.id.navigation_settings:
                    addNewBeacon.setVisibility(View.INVISIBLE);
                    switchFragment(new SettingFragment());
                    return true;
            }

            return false;
        }
    };

    private void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_in_right);
        fragmentTransaction.replace(R.id.fragment_replaceble_container,fragment);
        fragmentTransaction.commit();

    }

  private void sendGcm(String userId, String token1){
      StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://unix.trosha.dev.lumination.com.ua/user/"+userId+"/gcm", new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              Log.wtf("RESPONCE FROM GCM: ",response.toString());
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              Log.wtf("ERROR FROM GCM: ",error.toString());
          }
      }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
              Map<String,String > params = new HashMap<>();
              params.put("gcmId",token1);
              return params;
          }
      };
      VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
  }

}
