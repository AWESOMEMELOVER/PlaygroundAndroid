package com.example.micka.playgroundprojectv2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.micka.playgroundprojectv2.Interfaces.OnItemClickListener;
import com.example.micka.playgroundprojectv2.Models.Playground;
import com.example.micka.playgroundprojectv2.R;

import java.util.List;

/**
 * Created by micka on 3/20/2018.
 */

public class PlaygroundAdapter extends RecyclerView.Adapter<PlaygroundAdapter.PlaygroundViewHolder> {

    private Context mContext;
    private List<Playground> playgrounds;
    private OnItemClickListener playgroundOnItemClickListener;

    public PlaygroundAdapter(Context context ,List<Playground> playgrounds){
        mContext = context;
        this.playgrounds =  playgrounds;
    }


    @Override
    public PlaygroundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.playground_item_list,null);
       PlaygroundViewHolder playgroundViewHolder = new PlaygroundViewHolder(view);
       return playgroundViewHolder;
    }

    @Override
    public void onBindViewHolder(PlaygroundViewHolder holder, int position) {
        final Playground playground = playgrounds.get(position);
        Log.e("PLAYGROUND: ",playground.getDescription());
        holder.mDescription.setText(playground.getDescription());
        holder.mStreet.setText(playground.getHouseNumber());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playgroundOnItemClickListener.onItemClick(playground);
            }
        };


        holder.mDescription.setOnClickListener(listener);
        holder.mStreet.setOnClickListener(listener);
    }


    @Override
    public int getItemCount() {
        return playgrounds.size();
    }

    class PlaygroundViewHolder extends RecyclerView.ViewHolder{

        private TextView mDescription;
        private TextView mStreet;

        public PlaygroundViewHolder(View itemView) {
            super(itemView);
            this.mDescription =(TextView) itemView.findViewById(R.id.tv_playground_description);
            this.mStreet = (TextView) itemView.findViewById(R.id.tv_playground_street);
        }
    }

    public OnItemClickListener getPlaygroundOnItemClickListener() {
        return playgroundOnItemClickListener;
    }

    public void setPlaygroundOnItemClickListener(OnItemClickListener playgroundOnItemClickListener) {
        this.playgroundOnItemClickListener = playgroundOnItemClickListener;
    }
}
