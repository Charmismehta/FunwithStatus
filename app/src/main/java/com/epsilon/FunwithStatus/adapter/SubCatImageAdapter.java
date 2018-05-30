package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageSubcategory;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.ImageConverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCatImageAdapter extends BaseAdapter{
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    List<ImageSubcategory> subcategories= new ArrayList<>();

    public SubCatImageAdapter(Activity a,List<ImageSubcategory> subcategories) {
        this.activity = a;
        this.subcategories = subcategories;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subcategories.size();
    }

    @Override
    public Object getItem(int i) {
        return subcategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.image_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);


        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        Glide.with(activity).load(subcategories.get(i).getPicture()).placeholder(R.drawable.icon).into(viewHolder.tvimage);
        viewHolder.tvimage_name.setText(subcategories.get(i).getName());

        return view;
    }


    class MyViewHolder {
        public TextView tvimage_name;
        public ImageView tvimage;


        public MyViewHolder(View item) {
            tvimage_name = (TextView) item.findViewById(R.id.tvimage_name);
            tvimage = (ImageView) item.findViewById(R.id.tvimage);

        }
    }

}
