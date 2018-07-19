package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        CardView card_view1;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title1);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail1);
            card_view1 = (CardView) view.findViewById(R.id.card_view1);
        }
    }


    public AlbumsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(Constants.categoriesData.get(position).getCategoryName());
        Glide.with(mContext).load(Constants.categoriesData.get(position).getCategoryImage()).thumbnail(Glide.with(mContext).load(R.drawable.load)).into(holder.thumbnail);

   holder.thumbnail.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent it = new Intent(mContext, ImageListActivity.class);
           it.putExtra("ID",Constants.categoriesData.get(position).getId());
           Log.e("ID",":"+Constants.categoriesData.get(position).getId());
           it.putExtra("NAME",Constants.categoriesData.get(position).getCategoryName());
           Log.e("NAME",":"+Constants.categoriesData.get(position).getCategoryName());
           mContext.startActivity(it);
       }
   });

    }

    @Override
    public int getItemCount() {
        return Constants.categoriesData.size();
    }
}