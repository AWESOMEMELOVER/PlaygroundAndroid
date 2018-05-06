package com.example.micka.playgroundprojectv2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micka.playgroundprojectv2.Models.Tracking;
import com.example.micka.playgroundprojectv2.R;
import com.example.micka.playgroundprojectv2.Utils.Config;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by micka on 4/23/2018.
 */

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder> {

    private Context mContext;
    private ArrayList<Tracking> trackingArrayList;

    public TrackingAdapter(Context context, ArrayList<Tracking> trackings){
        this.mContext = context;
        this.trackingArrayList = trackings;
    }

    @Override
    public void onBindViewHolder(TrackingViewHolder holder, int position) {
        final Tracking tracking = trackingArrayList.get(position);
       // Log.e("ERROR STRING TAG: ",tracking.getName().toString());
        holder.mName.setText(tracking.getUserBeaconName());
        holder.mPlaygroundName.setText(tracking.getPlaygroundName());
        Config.displayImage(mContext,tracking.getImageUrl(),holder.circleImageView);

    }

    @Override
    public TrackingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tracking_item_list,null);
        TrackingViewHolder trackingViewHolder = new TrackingViewHolder(view);
        return trackingViewHolder;
    }





    @Override
    public int getItemCount() {
        return (null != trackingArrayList ? trackingArrayList.size() : 0);
    }

    class TrackingViewHolder extends RecyclerView.ViewHolder{
        public  TextView mName;
        public TextView mPlaygroundName;
        public CircleImageView circleImageView;

        public TrackingViewHolder(View itemView){
            super(itemView);
            this.mName = (TextView) itemView.findViewById(R.id.tv_tracking_beacon_name);
            this.mPlaygroundName = (TextView) itemView.findViewById(R.id.tv_playground_name_test);
            this.circleImageView = (CircleImageView)itemView.findViewById(R.id.civ_tracking_avatar);
        }
    }
}
