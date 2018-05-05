package com.example.micka.playgroundprojectv2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.Adapters.PlaygroundAdapter;
import com.example.micka.playgroundprojectv2.Interfaces.OnItemClickListener;
import com.example.micka.playgroundprojectv2.Models.Playground;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaygroundActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaygroundAdapter playgroundAdapter;
    String URL12 = "http://unix.trosha.dev.lumination.com.ua/playground";
    String trackingId;
    String secUrl = "/to/playground";
    String trackUrl = "http://unix.trosha.dev.lumination.com.ua/tracking/";
    private ArrayList<Playground> playgrounds;
    private Context context ;
    LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Intent intent = getIntent();
        trackingId = intent.getStringExtra("trackingId");

        recyclerView = (RecyclerView) findViewById(R.id.rv_playgrounds);
        context = getApplicationContext();


        //Log.e("ARRAY SIZE : ",String.valueOf(playgrounds.size()));

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());





        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        new PlaygroundTask().execute(URL12);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }



    class PlaygroundTask extends AsyncTask<String,Void,Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                playgroundAdapter = new PlaygroundAdapter(PlaygroundActivity.this, playgrounds);
                recyclerView.setAdapter(playgroundAdapter);

                playgroundAdapter.setPlaygroundOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Playground playground) {
                        Toast.makeText(PlaygroundActivity.this, playground.getId(), Toast.LENGTH_LONG).show();
                        sendData(playground);
                    }
                });
            } else {
                Toast.makeText(PlaygroundActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
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
                playgrounds = new ArrayList<>();

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    Playground item = new Playground();
                    item.setId(post.getString("id"));
                    item.setDescription(post.getString("description"));
                    item.setName(post.getString("street"));
                    item.setLatitude(post.getString("longitude"));
                    item.setLongtitude(post.getString("longitude"));
                    item.setCity(post.getString("city"));
                    item.setImageUrl(post.getString("imageUrl"));
                    item.setHouseNumber(post.getString("houseNumber"));
                    Log.e("ARRAY SIZE : ", post.getString("street"));
                    playgrounds.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    /*TODO
    * http://unix.trosha.dev.lumination.com.ua/tracking/1/to/playground
    * trackingid in url
    * playgroundid in post body
    *
    * */

    private void sendData(Playground playground){
        final String id = playground.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, trackUrl + trackingId + secUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Responce is: ",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.has("error")) {
                        String id = jsonObject.getString("id");
                        Intent intent = new Intent(getApplicationContext(), ZoneActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Already tracking",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
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
                HashMap<String,String> params = new HashMap<>();
                params.put("playgroundId",id);

                return params;

            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
