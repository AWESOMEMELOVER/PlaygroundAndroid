package com.example.micka.playgroundprojectv2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micka.playgroundprojectv2.Interfaces.BeaconOnItemClickListener;
import com.example.micka.playgroundprojectv2.Interfaces.OnItemClickListener;
import com.example.micka.playgroundprojectv2.Models.Beacon;
import com.example.micka.playgroundprojectv2.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by micka on 1/16/2018.
 */

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.BeaconViewHolder> {

    private Context mContext;
    private List<Beacon> beaconList;
    private BeaconOnItemClickListener onItemClickListener;

    public BeaconOnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(BeaconOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BeaconAdapter(Context context, List<Beacon> beaconList){
        this.mContext = context;
        this.beaconList = beaconList;
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder holder, int position) {
        final Beacon beacon = beaconList.get(position);
        holder.nameHolder.setText(beacon.getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(beacon);
            }
        };
        holder.nameHolder.setOnClickListener(listener);
        holder.circleImageView.setOnClickListener(listener);

    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_item_list,null);
        BeaconViewHolder beaconViewHolder = new BeaconViewHolder(view);
        return beaconViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null != beaconList ? beaconList.size() : 0);
    }

    class BeaconViewHolder extends RecyclerView.ViewHolder{

        protected CircleImageView circleImageView;
        protected TextView nameHolder;

        public BeaconViewHolder(View view){
            super(view);
            this.circleImageView = (CircleImageView) view.findViewById(R.id.civ_beacon_avatar);
            this.nameHolder = (TextView) view.findViewById(R.id.tv_beacon_name);
        }
    }
}
