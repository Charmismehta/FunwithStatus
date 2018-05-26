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

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.like.setImageResource(R.drawable.like);
            }
        });
        viewHolder.videoview.setVideoPath("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        final String path = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
//        viewHolder.videoview.start();

        viewHolder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(path);

                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
          }
        });
        return view;
    }

    class MyViewHolder {
        public TextView tvvideo_name;
        public ImageView tvimage,dislike ,like,iv_share,iv_delete,iv_save;
        public VideoView videoview;


        public MyViewHolder(View item) {
            tvvideo_name = (TextView) item.findViewById(R.id.tvvideo_name);
            tvimage = (ImageView) item.findViewById(R.id.tvimage);
            like = (ImageView) item.findViewById(R.id.like);
            dislike = (ImageView) item.findViewById(R.id.dislike);
            iv_share = (ImageView) item.findViewById(R.id.iv_share);
            iv_delete = (ImageView) item.findViewById(R.id.iv_delete);
            iv_save = (ImageView) item.findViewById(R.id.iv_save);
            videoview = (VideoView) item.findViewById(R.id.videoview);
        }
    }

 }
