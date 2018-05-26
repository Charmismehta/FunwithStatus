package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.jsonpojo.category_text.CategoryDatum;
import com.epsilon.FunwithStatus.jsonpojo.category_text.SubCategory;
import com.epsilon.FunwithStatus.utills.Constants;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextPicAdapter extends BaseAdapter {
    Context activity;
    private LayoutInflater inflater;
    public Resources res;
    List<SubCategory> subCategoryList = new ArrayList<>();

    public TextPicAdapter(Context a,List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return  subCategoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return  subCategoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.text_image_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        viewHolder.tvtext_image_name.setText(subCategoryList.get(i).getName());
        Glide.with(activity).load(subCategoryList.get(i).getPicture()).placeholder(R.drawable.icon).into(viewHolder.tvtext_image);
        return view;
    }

    class MyViewHolder {
        public TextView tvtext_image_name;
        public CircleImageView tvtext_image;


        public MyViewHolder(View item) {
            tvtext_image_name = (TextView) item.findViewById(R.id.tvtext_image_name);
            tvtext_image = (CircleImageView) item.findViewById(R.id.tvtext_image);
        }
    }
}
