package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.R;

import java.io.File;

public class VideoAdapter extends BaseAdapter {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public VideoAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.video_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.tvvideo.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        final String path = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
//        viewHolder.videoview.start();

        return view;
    }

    class MyViewHolder {
        public VideoView tvvideo;
        public TextView username,tvlike_count;


        public MyViewHolder(View item) {
            username = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            tvvideo = (VideoView) item.findViewById(R.id.tvvideo);
        }
    }

 }
