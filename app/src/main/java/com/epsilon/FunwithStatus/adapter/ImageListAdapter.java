package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.ImageSliderActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder>  {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    public String maincat;

    public ImageListAdapter(Activity a) {
        this.activity = a;
        this.maincat = maincat;
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

        Glide.with(activity).load(Constants.imageListData.get(position).file).thumbnail(Glide.with(activity).load(R.drawable.load)).into(holder.tvimage);
        holder.tvlike_count.setText(String.valueOf(Constants.imageListData.get(position).totalLikes));
        holder.username.setText(Constants.imageListData.get(position).userName);
        holder.like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));


//        holder.tvimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        holder.tvimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, ImageSliderActivity.class);
                it.putExtra("pic", Constants.imageListData.get(position).file);
                it.putExtra("position", position);
                it.putExtra("NAME",Constants.imageListData.get(position).categoryName);
                it.putExtra("ID", Constants.imageListData.get(position).categoryId);
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
        return Constants.imageListData.size();

    }


   public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView username,tvlike_count;
        public ImageView tvimage,like;


        public MyViewHolder(View item) {
            super(item);
            username = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            tvimage = (ImageView) item.findViewById(R.id.tvimage);
            like = (ImageView) item.findViewById(R.id.like);
        }
    }
}
