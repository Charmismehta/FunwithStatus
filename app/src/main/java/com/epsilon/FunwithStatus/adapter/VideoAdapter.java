package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.DisplayVideoActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.bumptech.glide.request.RequestOptions;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    Sessionmanager sessionmanager;


    public VideoAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final String video = Constants.videoListData.get(position).file;
        final String name = Constants.videoListData.get(position).userName;
        final String likecount = String.valueOf(Constants.videoListData.get(position).totalLikes);
        holder.like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        RequestOptions options = new RequestOptions().frame(10000);
        Glide.with(activity).asBitmap()
                .load(video)
                .apply(options)
                .into(holder.JZVideoPlayerStandard);

        holder.user_name.setText(name);
        holder.tvlike_count.setText(likecount);

        holder.JZVideoPlayerStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity,DisplayVideoActivity.class);
                it.putExtra("position",position);
                it.putExtra("ID",Constants.videoListData.get(position).categoryId);
                activity.startActivity(it);
                activity.finish();
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.videoListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name,tvlike_count;
        ImageView JZVideoPlayerStandard,like;


        public MyViewHolder(View item) {
            super(item);
            JZVideoPlayerStandard = (ImageView) item.findViewById(R.id.Thumbnail);
            user_name = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            like = (ImageView) item.findViewById(R.id.like);

        }
    }
}


