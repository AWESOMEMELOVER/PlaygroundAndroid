package com.example.micka.playgroundprojectv2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.micka.playgroundprojectv2.Activities.EditBeaconActivity;
import com.example.micka.playgroundprojectv2.Activities.MainActivity;
import com.example.micka.playgroundprojectv2.Adapters.BeaconAdapter;
import com.example.micka.playgroundprojectv2.Interfaces.BeaconOnItemClickListener;
import com.example.micka.playgroundprojectv2.Models.Beacon;
import com.example.micka.playgroundprojectv2.Models.GlobalUser;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by micka on 1/15/2018.
 */

public class  BeaconFragment extends Fragment {

    private TextView textView;
    private RecyclerView recyclerView;
    private BeaconAdapter beaconAdapter;
    public ArrayList<Beacon> beaconArrayList = new ArrayList<>();
    private Context context;
    private String userId;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beacon,container,false);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview_beacon_fragment);
        beaconAdapter = new BeaconAdapter(getContext(), beaconArrayList);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(beaconAdapter);

        beaconAdapter.setOnItemClickListener(new BeaconOnItemClickListener() {
            @Override
            public void onItemClick(Beacon item) {
                System.out.println(item.getName());
                System.out.println(item.getId());
                Intent intent = new Intent(getActivity(), EditBeaconActivity.class);
                intent.putExtra("beaconId",String.valueOf(item.getId()));
                intent.putExtra("beaconName",item.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getBeaconsArrayList();
    }

    private void getBeaconsArrayList(){
        userId = ((GlobalUser) getActivity().getApplication()).getUserId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://unix.trosha.dev.lumination.com.ua/user/1/beacon", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("Responce: ",response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i =0; i<jsonArray.length();i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Beacon beacon = new Beacon();
                        beacon.setId(object.getString("beaconId"));
                        beacon.setName(object.getString("name"));

                        beacon.setImgUrl(object.getString("imageUrl"));
                        Log.e("BEACON TAG:",beacon.getId());
                        beaconArrayList.add(beacon);
                        beaconAdapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Exeception: ",error.toString());
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}
