package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.DisplayImage;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.SubAlbum;

import java.util.List;

public class TrendingImgAdapter extends RecyclerView.Adapter<TrendingImgAdapter.MyViewHolder> {

    private Context activity;
    private LayoutInflater inflater;

    public TrendingImgAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subimage_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.username.setText(Constants.trendingimgData.get(position).getUser());
        holder.tvlike_count.setText(Constants.trendingimgData.get(position).getLiked());
        holder.like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        Glide.with(activity).load(Constants.trendingimgData.get(position).getImage()).thumbnail(Glide.with(activity).load(R.drawable.load)).into(holder.tvimage);

        holder.tvimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, DisplayImage.class);
                it.putExtra("Id",Constants.trendingimgData.get(position).getId());
                it.putExtra("NAME","Featured");
                it.putExtra("U_NAME",Constants.trendingimgData.get(position).getUser());
                it.putExtra("pic",Constants.trendingimgData.get(position).getImage());
                activity.startActivity(it);
                ((Activity)activity).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constants.trendingimgData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvlike_count,username;
        public ImageView tvimage;
        CardView card_view;
        ImageView like;

        public MyViewHolder(View view) {
            super(view);
            tvlike_count = (TextView) view.findViewById(R.id.tvlike_count);
            tvimage = (ImageView) view.findViewById(R.id.tvimage);
            username = (TextView) view.findViewById(R.id.username);
            card_view = (CardView) view.findViewById(R.id.card_view);
            like = (ImageView) view.findViewById(R.id.like);
        }
    }
}

