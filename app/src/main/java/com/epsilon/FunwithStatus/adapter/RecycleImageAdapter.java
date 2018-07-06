package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.ImageSlider;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.whatsappImageSlide;

public class RecycleImageAdapter extends RecyclerView.Adapter<RecycleImageAdapter.MyViewHolder> {

    private Context activity;
    private LayoutInflater inflater;
    Sessionmanager sessionmanager;
    String name;

    public RecycleImageAdapter(Activity a, String name) {
        this.activity = a;
        this.name = name;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return Constants.items.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }
}
