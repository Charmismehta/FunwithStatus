package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
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
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.SubCatImage;
import com.epsilon.FunwithStatus.utills.Album;
import com.epsilon.FunwithStatus.utills.SubAlbum;

import java.util.List;

public class SubalbumAdapter extends RecyclerView.Adapter<SubalbumAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubAlbum> albumList;
    private String name;


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


    public SubalbumAdapter(Context mContext, List<SubAlbum> albumList,String name) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.name = name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subalbum_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SubAlbum album = albumList.get(position);
        holder.title.setText(album.getName());
        Glide.with(mContext).load(album.getThumbnail()).thumbnail(Glide.with(mContext).load(R.drawable.load)).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ImageListActivity.class);
                it.putExtra("NAME",albumList.get(position).getName());
                it.putExtra("REALNAME",name);
                mContext.startActivity(it);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}