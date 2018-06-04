package com.epsilon.FunwithStatus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.SubCatImage;
import com.epsilon.FunwithStatus.utills.Album;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        CardView card_view1;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title1);
            count = (TextView) view.findViewById(R.id.count1);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail1);
            card_view1 = (CardView) view.findViewById(R.id.card_view1);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " Subcategory");
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SubCatImage.class);
                it.putExtra("NAME",albumList.get(position).getName());
                Log.e("NAME",albumList.get(position).getName());
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}