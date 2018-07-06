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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.DisplayVideo;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

public class RecycleVideoAdapter extends RecyclerView.Adapter<RecycleVideoAdapter.MyViewHolder> {


    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    Sessionmanager sessionmanager;


    public RecycleVideoAdapter(Activity a) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final String video = Constants.videoitems.get(position).getImage();
        holder.user_name.setVisibility(View.GONE);
        holder.tvlike_count.setVisibility(View.GONE);
        holder.like.setVisibility(View.GONE);
        String filenameArray[] = video.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];

        if (extension.equalsIgnoreCase("mp4")) {
            RequestOptions options = new RequestOptions().frame(10000);
            Glide.with(activity).asBitmap()
                    .load(video)
                    .apply(options)
                    .into(holder.JZVideoPlayerStandard);
        }else {
            Toast.makeText(activity, "no image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.videoitems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, tvlike_count;
        ImageView JZVideoPlayerStandard, like;


        public MyViewHolder(View item) {
            super(item);
            JZVideoPlayerStandard = (ImageView) item.findViewById(R.id.Thumbnail);
            user_name = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            like = (ImageView) item.findViewById(R.id.like);

        }
    }
}
