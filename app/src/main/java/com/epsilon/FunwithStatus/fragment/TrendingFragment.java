package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epsilon.FunwithStatus.AddOption;
import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.adapter.FirstFregmetnAdapter;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;


public class TrendingFragment extends Fragment {
    Activity activity;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipelayout;
    FloatingActionButton floatingbtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        activity = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        floatingbtn = (FloatingActionButton) view.findViewById(R.id.floatingbtn);




        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, AddOption.class);
                startActivity(it);
                activity.finish();
            }
        });



        FirstFregmetnAdapter adapter = new FirstFregmetnAdapter(activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
