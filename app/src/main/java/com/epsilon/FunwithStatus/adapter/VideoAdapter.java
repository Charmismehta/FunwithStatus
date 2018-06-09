package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;

import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.DisplayVideo;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.ImageUtills;

import java.io.File;
import java.util.HashMap;

import static android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT;
import static android.media.ThumbnailUtils.createVideoThumbnail;
import static android.provider.MediaStore.Images.Thumbnails.MINI_KIND;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    String video,id;
    int intID;
    Uri uri;
    SimpleCursorAdapter adapter;

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
        video = Constants.videoListData.get(position).getImage();
        uri = Uri.parse(video);
        id = Constants.videoListData.get(position).getId();
        intID = Integer.parseInt(id);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapThumb = MediaStore.Video.Thumbnails.getThumbnail(
               activity.getContentResolver(),intID,
                MediaStore.Images.Thumbnails.MICRO_KIND,
                options);

        holder.tvvideo.setImageBitmap(bitmapThumb);
        holder.user_name.setText(Constants.videoListData.get(position).getUser());
        holder.like_count.setText(Constants.videoListData.get(position).getLiked());
        holder.video_name.setText(Constants.videoListData.get(position).getFilename());

        holder.tvvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(activity, DisplayVideo.class);
                it.putExtra("VIDEO",Constants.videoListData.get(position).getImage());
                it.putExtra("VIDEO_ID",Constants.videoListData.get(position).getId());
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
        public ImageView tvvideo;
        public LinearLayout mainlayout;
        public TextView user_name,like_count,video_name;


        public MyViewHolder(View item) {
            super(item);
            user_name = (TextView) item.findViewById(R.id.user_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
            video_name = (TextView) item.findViewById(R.id.video_name);
            tvvideo = (ImageView) item.findViewById(R.id.tvvideo);
            mainlayout = (LinearLayout) item.findViewById(R.id.mainlayout);
        }
    }
}
