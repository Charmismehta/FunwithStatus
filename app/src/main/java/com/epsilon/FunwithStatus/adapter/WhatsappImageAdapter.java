package com.epsilon.FunwithStatus.adapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;


public class WhatsappImageAdapter extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;
    APIInterface apiInterface;

    // constructor
    public WhatsappImageAdapter(Activity activity) {
        this._activity = activity;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public int getCount() {
        return Constants.items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        ImageView display_image = (ImageView) viewLayout.findViewById(R.id.display_image);
        final ImageView download = (ImageView) viewLayout.findViewById(R.id.download);
        final ImageView like = (ImageView) viewLayout.findViewById(R.id.like);
        final ImageView dislike = (ImageView) viewLayout.findViewById(R.id.dislike);
        final ImageView share = (ImageView) viewLayout.findViewById(R.id.share);
        final ImageView delete = (ImageView) viewLayout.findViewById(R.id.delete);
        final ImageView whatsapp = (ImageView) viewLayout.findViewById(R.id.whatsapp);
        final ImageView facebook = (ImageView) viewLayout.findViewById(R.id.facebook);
        share.setVisibility(View.GONE);
        like.setVisibility(View.GONE);
        dislike.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        download.setVisibility(View.GONE);
        whatsapp.setVisibility(View.GONE);
        facebook.setVisibility(View.GONE);
        Glide.with(_activity).load(Constants.items.get(position).getImage()).thumbnail(Glide.with(_activity).load(R.drawable.load)).into(display_image);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}