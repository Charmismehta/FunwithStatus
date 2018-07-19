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
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.VideoListActivity;
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
                .inflate(R.layout.videocategoryitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
       holder.textview.setText(Constants.categoriesData.get(position).getCategoryName());
        Glide.with(activity).load(Constants.categoriesData.get(position).getCategoryStatusImage()).thumbnail(Glide.with(activity).load(R.drawable.load)).into(holder.imageview);

        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, VideoListActivity.class);
                it.putExtra("ID",Constants.categoriesData.get(position).getId());
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
        return Constants.categoriesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textview;
        ImageView imageview;


        public MyViewHolder(View item) {
            super(item);
            imageview = (ImageView) item.findViewById(R.id.imageview);
            textview = (TextView) item.findViewById(R.id.textview);

        }
    }
}
