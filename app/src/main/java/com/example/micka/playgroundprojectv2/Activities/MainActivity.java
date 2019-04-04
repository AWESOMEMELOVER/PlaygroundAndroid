package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.micka.playgroundprojectv2.Fragments.BeaconFragment;
import com.example.micka.playgroundprojectv2.Fragments.SettingFragment;
import com.example.micka.playgroundprojectv2.Fragments.TrackingFragment;
import com.example.micka.playgroundprojectv2.Models.GlobalUser;
import com.example.micka.playgroundprojectv2.Models.Zone;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private TextView mTextMessage;
    private ImageView addNewBeacon;
    static GlobalUser globalUser = new GlobalUser();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("userId");

        addNewBeacon = (ImageView) findViewById(R.id.btn_add_new_beacon);

        switchFragment(new BeaconFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        id = SharedPrefUser.getInstance(getApplicationContext()).getUserId();
        Log.wtf("USER ID FROM SHAREDPREF: ",id);

        new MainTask().execute("http://unix.trosha.dev.lumination.com.ua/user/"+id);

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

    class MainTask extends AsyncTask<String,Void,Integer>{


        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.print(response.toString());
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                ((GlobalUser) getApplication()).setUserId(response.getString("id"));
                ((GlobalUser) getApplication()).setTelephoneNumber("telephoneNumber");
                ((GlobalUser) getApplication()).setFirstName("firstName");
                ((GlobalUser) getApplication()).setLastName("lastName");
                ((GlobalUser) getApplication()).setGender("gender");
                ((GlobalUser) getApplication()).setBirthday("birthday");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
