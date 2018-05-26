package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;

public class TextListAdapter extends BaseAdapter {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public TextListAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return Constants.statusData.size();
    }

    @Override
    public Object getItem(int i) {
        return Constants.statusData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final MyViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.text_list_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }

        viewHolder.tvtext_name.setText(Constants.statusData.get(i).getStatus());
        viewHolder.like_count.setText(Constants.statusData.get(i).getLike());

        return view;
    }

    class MyViewHolder {
        public TextView tvtext_name,like_count;


        public MyViewHolder(View item) {
            tvtext_name = (TextView) item.findViewById(R.id.tvtext_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
        }
    }

}
