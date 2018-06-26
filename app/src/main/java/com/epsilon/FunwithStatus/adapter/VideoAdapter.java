package com.epsilon.FunwithStatus.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.epsilon.FunwithStatus.DisplayImage;
import com.epsilon.FunwithStatus.DisplayVideo;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.jsonpojo.deletetext.DeleteText;
import com.epsilon.FunwithStatus.jsonpojo.deletevideo.DeleteVideo;
import com.epsilon.FunwithStatus.jsonpojo.videodislike.VideoDisLike;
import com.epsilon.FunwithStatus.jsonpojo.videolike.VideoLike;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static cn.jzvd.JZVideoPlayer.TAG;


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
        final String video = Constants.videoListData.get(position).getImage();
        final String name = Constants.videoListData.get(position).getUser();
        final String likecount = Constants.videoListData.get(position).getLiked();
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
                Intent it = new Intent(activity,DisplayVideo.class);
                it.putExtra("position",position);
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


