package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.DisplayVideo;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;

import java.io.File;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public VideoAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvvideo.setVideoPath(Constants.videoListData.get(position).getImage());

        holder.tvvideo.start();
        holder.user_name.setText(Constants.videoListData.get(position).getUser());
        holder.like_count.setText(Constants.videoListData.get(position).getLiked());
        holder.video_name.setText(Constants.videoListData.get(position).getFilename());

        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, DisplayVideo.class);
                it.putExtra("VIDEO",Constants.videoListData.get(position).getImage());
                activity.startActivity(it);
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

    class MyViewHolder extends RecyclerView.ViewHolder{
        public VideoView tvvideo;
        public LinearLayout mainlayout;
        public TextView user_name,like_count,video_name;


        public MyViewHolder(View item) {
            super(item);
            user_name = (TextView) item.findViewById(R.id.user_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
            video_name = (TextView) item.findViewById(R.id.video_name);
            tvvideo = (VideoView) item.findViewById(R.id.tvvideo);
            mainlayout = (LinearLayout) item.findViewById(R.id.mainlayout);
        }
    }

 }
