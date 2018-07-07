package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.formats.NativeAdViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class FirstFregmetnAdapter extends RecyclerView.Adapter<FirstFregmetnAdapter.MyViewHolder> {
    Activity activity;
    private LayoutInflater inflater;
    public Resources res;
    public FirstFregmetnAdapter(Activity a) {
        this.activity = a;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firstfragment, parent, false);

        return new MyViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.iv_download.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.iv_share.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
        holder.iv_like.setColorFilter(activity.getResources().getColor(R.color.colorAccent));
            String video="http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
            RequestOptions options = new RequestOptions().frame(10000);
            Glide.with(activity).asBitmap()
                    .load(video)
                    .apply(options)
                    .into(holder.iv_imageview);

        holder.dot.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v) {
                    final PopupMenu popup = new PopupMenu(activity, v);
                    popup.getMenuInflater().inflate(R.menu.menuitem, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.delete_post:
                                        Toast.makeText(activity, "delete Post", Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.edit_post:
                                        Toast.makeText(activity, "edit Post", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show(); //showing popup menu
                    }
            });
    }




    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
       return 5;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_download, iv_facebook, iv_whatsapp, iv_share, iv_like, iv_imageview, dot;
        TextView tv_username, tv_view, tv_caption, tv_textview, tv_likecount;
        VideoView vv_videoview;

        public MyViewHolder(View item) {
            super(item);
            iv_download = (ImageView) item.findViewById(R.id.iv_download);
            iv_facebook = (ImageView) item.findViewById(R.id.iv_facebook);
            iv_whatsapp = (ImageView) item.findViewById(R.id.iv_whatsapp);
            iv_share = (ImageView) item.findViewById(R.id.iv_share);
            iv_like = (ImageView) item.findViewById(R.id.iv_like);
            iv_imageview = (ImageView) item.findViewById(R.id.iv_imageview);
            dot = (ImageView) item.findViewById(R.id.dot);
            vv_videoview = (VideoView) item.findViewById(R.id.vv_videoview);
            tv_username = (TextView) item.findViewById(R.id.tv_username);
            tv_view = (TextView) item.findViewById(R.id.tv_view);
            tv_caption = (TextView) item.findViewById(R.id.tv_caption);
            tv_textview = (TextView) item.findViewById(R.id.tv_textview);
            tv_likecount = (TextView) item.findViewById(R.id.tv_likecount);
        }
    }

}
