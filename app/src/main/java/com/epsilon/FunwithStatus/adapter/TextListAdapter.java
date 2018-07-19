package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextSliderActivity;
import com.epsilon.FunwithStatus.utills.Constants;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.vdurmont.emoji.EmojiParser;

public class TextListAdapter extends RecyclerView.Adapter<TextListAdapter.MyViewHolder> {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;

    public TextListAdapter(Activity a) {
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
        String str = Constants.statusData.get(i).text;
        str = str.replaceAll("\n\n",",");
        String result = EmojiParser.parseToUnicode(str);
        holder.tvtext_name.setText(result);

        holder.like_count.setText(String.valueOf(Constants.statusData.get(i).totalLikes));
        holder.user_name.setText(Constants.statusData.get(i).userName);
        holder.like.setColorFilter(activity.getResources().getColor(R.color.white));

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent it = new Intent(activity, TextSliderActivity.class);
                    it.putExtra("text", Constants.statusData.get(i).text);
                    it.putExtra("NAME", Constants.statusData.get(i).categoryName);
                    it.putExtra("U_NAME", Constants.statusData.get(i).userName);
                    it.putExtra("ID", Constants.statusData.get(i).categoryId);
                    it.putExtra("position", i);
                    activity.startActivity(it);
                    activity.finish();
                }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Constants.statusData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView like_count,user_name;
        public EmojiconTextView tvtext_name;
        public ImageView like;
        public RelativeLayout rlayout;


        public MyViewHolder(View item) {
            super(item);
            tvtext_name = (EmojiconTextView) item.findViewById(R.id.tvtext_name);
            like_count = (TextView) item.findViewById(R.id.like_count);
            user_name = (TextView) item.findViewById(R.id.user_name);
            like = (ImageView) item.findViewById(R.id.like);
            rlayout = (RelativeLayout) item.findViewById(R.id.recycle);

        }
    }

}
