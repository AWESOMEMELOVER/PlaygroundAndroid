package com.example.micka.playgroundprojectv2.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.Models.Zone;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ZoneActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar progressBar;
    private ArrayList<Zone> zones;
    private String playgroundId,zoneId;
    private Zone choosedZone;
    private LinearLayout a_holder,b_holder,c_holder;

    private ImageView zoneA,zoneB,zoneC,confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_zones);

        new ZoneTask().execute("http://unix.trosha.dev.lumination.com.ua/zone");

        Intent intent = getIntent();
        playgroundId = intent.getStringExtra("id");

        a_holder = (LinearLayout)findViewById(R.id.ll_a_holder);
        b_holder = (LinearLayout) findViewById(R.id.ll_b_holder);
        c_holder = (LinearLayout) findViewById(R.id.ll_c_holder);

        zoneA = (ImageView) findViewById(R.id.iv_btn_a);
        zoneB = (ImageView) findViewById(R.id.iv_btn_b);
        zoneC = (ImageView) findViewById(R.id.iv_btn_c);

        confirm = (ImageView) findViewById(R.id.btn_add_new_zone);


        zoneA.setOnClickListener(this);
        zoneB.setOnClickListener(this);
        zoneC.setOnClickListener(this);

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_btn_a:
                dropColors();
                choosedZone = zones.get(0);
                a_holder.setBackgroundColor(Color.parseColor("#e7e7e7"));
                break;
            case R.id.iv_btn_b:
                dropColors();
                choosedZone = zones.get(1);
                b_holder.setBackgroundColor(Color.parseColor("#e7e7e7"));
                break;
            case R.id.iv_btn_c:
                dropColors();
               choosedZone = zones.get(2);
                c_holder.setBackgroundColor(Color.parseColor("#e7e7e7"));
                break;

            case R.id.btn_add_new_zone:
                if (choosedZone== null)
                    Toast.makeText(this,"CHOOSE ZONE ",Toast.LENGTH_SHORT).show();
                else
                sendData(choosedZone);

        }

    }

    private void sendData(final Zone zone){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://unix.trosha.dev.lumination.com.ua/playground/" + playgroundId + "/to/zone", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ZONE RESPONCE: ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    zoneId = jsonObject.getString("id");
                    newIntent(zoneId);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR RESPONCE: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("zoneId",zone.getId());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


   private void newIntent(String extra){
        Intent intent = new Intent(getApplicationContext(),BeaconToTrackingActivity.class);
        intent.putExtra("zoneId",extra);
        startActivity(intent);
   }

    private void dropColors(){
        a_holder.setBackgroundColor(Color.parseColor("#fcfcfc"));
        b_holder.setBackgroundColor(Color.parseColor("#fcfcfc"));
        c_holder.setBackgroundColor(Color.parseColor("#fcfcfc"));
    }


    class ZoneTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

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

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                progressBar.setVisibility(View.GONE);
            }
        }

        private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("data");
                zones = new ArrayList<>();

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    Zone zone = new Zone();
                    zone.setId(post.getString("id"));
                    zone.setName(post.getString("name"));
                    zone.setDescription(post.getString("decription"));
                    zone.setAgeFrom(post.getString("ageFrom"));
                    zone.setAgeTo(post.getString("ageTo"));

                    System.out.println(zone.getName());
                    zones.add(zone);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
