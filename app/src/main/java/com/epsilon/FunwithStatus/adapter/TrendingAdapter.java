package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epsilon.FunwithStatus.DisplayText;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.rockerhieu.emojicon.EmojiconTextView;

public class TrendingAdapter  extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public TrendingAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int i) {

        String str = Constants.trendingData.get(i).getStatus();
        str = str.replaceAll("\n\n", ",");
        holder.tvtext_name.setText(str);

        holder.like_count.setText(Constants.trendingData.get(i).getLiked());
        holder.user_name.setText(Constants.trendingData.get(i).getUser());
        holder.like.setColorFilter(activity.getResources().getColor(R.color.white));

        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, DisplayText.class);
                it.putExtra("text", Constants.trendingData.get(i).getStatus());
                it.putExtra("Id", Constants.trendingData.get(i).getId());
                it.putExtra("NAME","Trending");
                it.putExtra("U_NAME", Constants.trendingData.get(i).getUser());
                activity.startActivity(it);

            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.trendingData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView like_count,user_name;
        public EmojiconTextView tvtext_name;
        public RelativeLayout rlayout;
        public ImageView like;

        public MyViewHolder(View item) {
            super(item);
            tvtext_name = (EmojiconTextView) item.findViewById(R.id.tvtext_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
            user_name = (TextView) item.findViewById(R.id.user_name);
            rlayout = (RelativeLayout) item.findViewById(R.id.recycle);
            like = (ImageView) item.findViewById(R.id.like);

        }
    }
}
