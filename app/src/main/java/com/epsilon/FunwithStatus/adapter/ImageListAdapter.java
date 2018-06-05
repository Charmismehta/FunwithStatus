package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.DisplayImage;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.ImageConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageListAdapter extends BaseAdapter {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public ImageListAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Constants.imageListData.size();
    }

    @Override
    public Object getItem(int i) {
        return Constants.imageListData;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.subimage_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);


        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
            Glide.with(activity).load(Constants.imageListData.get(i).getImage()).placeholder(R.drawable.icon).into(viewHolder.tvimage);
            viewHolder.tvlike_count.setText(Constants.imageListData.get(i).getLiked());
//            viewHolder.tvimage_name.setText(Constants.imageListData.get(i).getSubcata());

        viewHolder.tvimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, DisplayImage.class);
                it.putExtra("pic",Constants.imageListData.get(i).getImage());
                it.putExtra("NAME",Constants.imageListData.get(i).getSubcata());
                it.putExtra("Id",Constants.imageListData.get(i).getId());
                it.putExtra("U_NAME",Constants.imageListData.get(i).getUser());
                activity.startActivity(it);
                activity.finish();
            }
        });

        return view;
    }


    class MyViewHolder {
        public TextView username,tvlike_count;
        public ImageView tvimage;


        public MyViewHolder(View item) {
            username = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            tvimage = (ImageView) item.findViewById(R.id.tvimage);
        }
    }
}
