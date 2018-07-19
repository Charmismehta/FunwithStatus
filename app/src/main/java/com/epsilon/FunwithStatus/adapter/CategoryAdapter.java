package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

public class CategoryAdapter extends BaseAdapter {

    private Context activity;
    private LayoutInflater inflater;
    Sessionmanager sessionmanager;

    public CategoryAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        sessionmanager = new Sessionmanager(activity);
    }
    @Override
    public int getCount() {
        return Constants.categoriesData.size();
    }

    @Override
    public Object getItem(int position) {
        return Constants.categoriesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.status_item, null);
        }
        TextView image_name =(TextView)convertView.findViewById(R.id.image_name);
        ImageView Thumbnail =(ImageView) convertView.findViewById(R.id.Thumbnail);
        image_name.setText(Constants.categoriesData.get(position).getCategoryName());
        Glide.with(activity).load(Constants.categoriesData.get(position).getCategoryStatusImage()).into(Thumbnail);

        return convertView;
    }

}
