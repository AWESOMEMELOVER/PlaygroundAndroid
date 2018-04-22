package com.example.micka.playgroundprojectv2.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.micka.playgroundprojectv2.Activities.PlaygroundActivity;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackingFragment extends Fragment {

    private ImageView mAddTracking;
    final String URL = "http://unix.trosha.dev.lumination.com.ua/user/1/tracking";

    public TrackingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_tracking,container,false);

      mAddTracking = (ImageView) view.findViewById(R.id.btn_add_new_tracking);

      mAddTracking.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            sendData();
          }
      });

      return view;
    }



    private void sendData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");
                    Intent intent = new Intent(getContext(),PlaygroundActivity.class);
                    intent.putExtra("trackingId",id);
                    startActivity(intent);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tracking Exception",error.toString());
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}
