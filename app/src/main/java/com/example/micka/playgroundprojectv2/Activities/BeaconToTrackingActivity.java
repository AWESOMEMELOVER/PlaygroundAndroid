package com.example.micka.playgroundprojectv2.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.Adapters.BeaconAdapter;
import com.example.micka.playgroundprojectv2.Interfaces.BeaconOnItemClickListener;
import com.example.micka.playgroundprojectv2.Models.Beacon;
import com.example.micka.playgroundprojectv2.Models.Zone;
import com.example.micka.playgroundprojectv2.R;
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

public class BeaconToTrackingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Beacon> beacons;
    private ProgressBar progressBar;
    private BeaconAdapter beaconAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_to_tracking);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rv_beacons_to_tracking);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar_tracking_beacon);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        new BeaconTask().execute("http://unix.trosha.dev.lumination.com.ua/user/1/beacon/");

    }


    private void sendData(final Beacon beacon){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://unix.trosha.dev.lumination.com.ua/tracking/1/to/beacon", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponceTag: ",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorTag: ",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("beaconId",beacon.getId());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    class BeaconTask extends AsyncTask<String , Void , Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                progressBar.setVisibility(View.GONE);
                beaconAdapter = new BeaconAdapter(getApplicationContext(),beacons);
                beaconAdapter.setOnItemClickListener(new BeaconOnItemClickListener() {
                    @Override
                    public void onItemClick(Beacon beacon) {
                        sendData(beacon);
                    }
                });
                recyclerView.setAdapter(beaconAdapter);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
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

        private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("data");
                beacons = new ArrayList<>();

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    Beacon beacon = new Beacon();
                    beacon.setId(post.getString("id"));
                    beacon.setName(post.getString("name"));
                    beacon.setImgUrl(post.getString("imageUrl"));

                    beacons.add(beacon);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
