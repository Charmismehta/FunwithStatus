package com.epsilon.FunwithStatus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.ImageSlider;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageListDatum;
import com.epsilon.FunwithStatus.utills.Constants;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> recipeList;
    private Context context;
    private static final int RECIPE = 0;
    private static final int NATIVE_AD = 1;

    public RecipeListAdapter(Context context, List<Object> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == RECIPE) {
            View recipeItem = inflater.inflate(R.layout.subimage_item, parent, false);
            return new RecipeViewHolder(recipeItem);
        } else if (viewType == NATIVE_AD) {
            View nativeAdItem = inflater.inflate(R.layout.item_native_ad, parent, false);
            return new NativeAdViewHolder(nativeAdItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemType = getItemViewType(position);

        if (itemType == RECIPE) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            ImageListDatum recipe = (ImageListDatum) recipeList.get(position);
            Glide.with(context).load(Constants.imageListData.get(position).getImage()).thumbnail(Glide.with(context).load(R.drawable.load)).into(recipeViewHolder.tvimage);
            recipeViewHolder.tvlike_count.setText(Constants.imageListData.get(position).getLiked());
            recipeViewHolder.username.setText(Constants.imageListData.get(position).getUser());
            recipeViewHolder.like.setColorFilter(context.getResources().getColor(R.color.colorAccent));


//        holder.tvimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

            recipeViewHolder.tvimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, ImageSlider.class);
                    it.putExtra("pic", Constants.imageListData.get(position).getImage());
                    it.putExtra("position", position);
                    it.putExtra("NAME", Constants.imageListData.get(position).getSubcata());
                    it.putExtra("Id", Constants.imageListData.get(position).getId());
                    it.putExtra("U_NAME", Constants.imageListData.get(position).getUser());
//                    it.putExtra("REALNAME", maincat);
                    context.startActivity(it);

                }
            });
        } else if (itemType == NATIVE_AD) {
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
            NativeAd nativeAd = (NativeAd) recipeList.get(position);

            AdIconView adIconView = nativeAdViewHolder.adIconView;
            TextView tvAdTitle = nativeAdViewHolder.tvAdTitle;
            TextView tvAdBody = nativeAdViewHolder.tvAdBody;
            Button btnCTA = nativeAdViewHolder.btnCTA;
            LinearLayout adChoicesContainer = nativeAdViewHolder.adChoicesContainer;
            MediaView mediaView = nativeAdViewHolder.mediaView;
            TextView sponsorLabel = nativeAdViewHolder.sponsorLabel;

            tvAdTitle.setText(nativeAd.getAdvertiserName());
            tvAdBody.setText(nativeAd.getAdBodyText());
            btnCTA.setText(nativeAd.getAdCallToAction());
            sponsorLabel.setText(nativeAd.getSponsoredTranslation());

            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
            adChoicesContainer.addView(adChoicesView);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(btnCTA);
            clickableViews.add(mediaView);
            nativeAd.registerViewForInteraction(nativeAdViewHolder.container, mediaView, adIconView, clickableViews);
        }
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = recipeList.get(position);
        if (item instanceof ImageListDatum) {
            return RECIPE;
        } else if (item instanceof Ad) {
            return NATIVE_AD;
        } else {
            return -1;
        }
    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView username,tvlike_count;
        public ImageView tvimage,like;


        RecipeViewHolder(View item) {
            super(item);
            username = (TextView) item.findViewById(R.id.username);
            tvlike_count = (TextView) item.findViewById(R.id.tvlike_count);
            tvimage = (ImageView) item.findViewById(R.id.tvimage);
            like = (ImageView) item.findViewById(R.id.like);
        }
    }

    private static class NativeAdViewHolder extends RecyclerView.ViewHolder {
        AdIconView adIconView;
        TextView tvAdTitle;
        TextView tvAdBody;
        Button btnCTA;
        View container;
        TextView sponsorLabel;
        LinearLayout adChoicesContainer;
        MediaView mediaView;

        NativeAdViewHolder(View itemView) {
            super(itemView);
            this.container = itemView;
            adIconView = (AdIconView) itemView.findViewById(R.id.adIconView);
            tvAdTitle = (TextView) itemView.findViewById(R.id.tvAdTitle);
            tvAdBody = (TextView) itemView.findViewById(R.id.tvAdBody);
            btnCTA = (Button) itemView.findViewById(R.id.btnCTA);
            adChoicesContainer = (LinearLayout) itemView.findViewById(R.id.adChoicesContainer);
            mediaView = (MediaView) itemView.findViewById(R.id.mediaView);
            sponsorLabel = (TextView) itemView.findViewById(R.id.sponsored_label);
        }
    }
}
