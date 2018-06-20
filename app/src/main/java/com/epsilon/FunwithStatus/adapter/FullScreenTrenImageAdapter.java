package com.epsilon.FunwithStatus.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.utills.Constants;

public class FullScreenTrenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private LayoutInflater inflater;


    // constructor
    public FullScreenTrenImageAdapter(Activity activity) {
        this._activity = activity;
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return Constants.trendingimgData.size();
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
        String imgURL = Constants.trendingimgData.get(position).getImage();
        Glide.with(_activity).load(imgURL).thumbnail(Glide.with(_activity).load(R.drawable.load)).into(display_image);

        ((ViewPager)container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
