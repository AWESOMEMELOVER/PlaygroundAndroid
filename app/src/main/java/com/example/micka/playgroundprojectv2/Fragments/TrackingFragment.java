package com.example.micka.playgroundprojectv2.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.micka.playgroundprojectv2.Adapters.TrackingAdapter;
import com.example.micka.playgroundprojectv2.Models.Tracking;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.SharedPrefUser;
import com.example.micka.playgroundprojectv2.Utils.URLS;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackingFragment extends Fragment {

    private ImageView mAddTracking;
    private String CREATE_TRACKING_URL;
    private RecyclerView recyclerView;
    private TrackingAdapter trackingAdapter;
    private ArrayList<Tracking> trackings = new ArrayList<>();
    private String userId;
    private static String GET_TRACKINGS;
    public TrackingFragment() {
        // Required empty public constructor
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.i("TRACKING URL: ",GET_TRACKINGS+" is tracking url");
        getTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userId = SharedPrefUser.getInstance(getContext()).getUserId();
        CREATE_TRACKING_URL = URLS.createTrackingByUserId(userId);
        GET_TRACKINGS = URLS.getTrackingsByUserID(userId);
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAddTracking = (ImageView) view.findViewById(R.id.btn_add_new_tracking);
        recyclerView = (RecyclerView) view.findViewById(R.id.rc_tracking);
        recyclerView = (RecyclerView) view.findViewById(R.id.rc_tracking);
        trackingAdapter = new TrackingAdapter(getContext(), trackings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(trackingAdapter);
        mAddTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendData();
            }
        });
    }

    private void sendData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_TRACKING_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");
                    Intent intent = new Intent(getContext(), PlaygroundActivity.class);
                    intent.putExtra("trackingId", id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Tracking Exception", error.toString());
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getTracking() {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, GET_TRACKINGS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Tracking tracking = new Tracking();
                        tracking.setUserBeaconName(jsonObject.getString("userBeaconName"));
                        tracking.setPlaygroundName(jsonObject.getString("playgroundName"));
                        tracking.setImageUrl(jsonObject.getString("imageUrl"));
                        tracking.setPlaygroundId(jsonObject.getString("playgroundId"));
                        tracking.setNotify(jsonObject.getString("notify"));
                        trackings.add(tracking);
                        trackingAdapter.notifyDataSetChanged();
                    }
                    for (Tracking tracking:trackings
                         ) {
                        Log.w("RESPONCE: ",tracking.getPlaygroundId());
                        Log.w("RESPONCE",tracking.getUserBeaconName());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Exeception: ",error.toString());
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
