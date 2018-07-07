package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.ImageSlider;
import com.epsilon.FunwithStatus.LoginPage;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.epsilon.FunwithStatus.whatsappImageSlide;
import com.google.android.gms.ads.InterstitialAd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleImageAdapter extends RecyclerView.Adapter<RecycleImageAdapter.MyViewHolder> {

    private Context activity;
    private LayoutInflater inflater;
    Sessionmanager sessionmanager;

    public RecycleImageAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.image_name.setText(Constants.categoriesData.get(position).getCategoryName());
        Glide.with(activity).load(Constants.categoriesData.get(position).getCategoryStatusImage()).into(holder.Thumbnail);

        holder.Thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, TextListActivity.class);
                it.putExtra("NAME",Constants.categoriesData.get(position).getCategoryName());
                activity.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constants.categoriesData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Thumbnail;
        TextView image_name;

        public MyViewHolder(View view) {
            super(view);
            image_name =(TextView)view.findViewById(R.id.image_name);
            Thumbnail =(ImageView) view.findViewById(R.id.Thumbnail);
        }
    }



}
