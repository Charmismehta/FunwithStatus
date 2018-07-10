package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.epsilon.FunwithStatus.AddTextActivity;
import com.epsilon.FunwithStatus.AddVideoActivity;
import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.Demo;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.LoginPage;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.adapter.AlbumsAdapter;

import com.epsilon.FunwithStatus.adapter.RecycleVideoAdapter;
import com.epsilon.FunwithStatus.adapter.TrendingImgAdapter;
import com.epsilon.FunwithStatus.adapter.VideoAdapter;
import com.epsilon.FunwithStatus.jsonpojo.categories.Categories;
import com.epsilon.FunwithStatus.jsonpojo.tending_img.TrendingImg;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoFragment extends Fragment{

    Activity context;
    RecyclerView recycler_view;
    FloatingActionButton floatingbtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private InterstitialAd mInterstitialAd;
    SwipeRefreshLayout swipelayout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        context = getActivity();
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipelayout=(SwipeRefreshLayout)view.findViewById(R.id.swipelayout);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            Categories();
        }


        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Categories();
                swipelayout.setRefreshing(false);
            }
        });

        AdView mAdView =view.findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        return view;
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (JZVideoPlayer.backPress()) {
                        return true;
                    }
                    Intent it = new Intent(getContext(), Dashboard.class);
                    startActivity(it);
                    getActivity().finish();
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    public void Categories() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<Categories> logincall = apiInterface.categoriespojo();
        logincall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                dialog.dismiss();
                if (response.body().getStatus() == 1) {
                    if (Constants.categoriesData != null) {
                        Constants.categoriesData.clear();
                    }
                    if (!Constants.categoriesData.equals("") && Constants.statusData != null) {
                        Constants.categoriesData.addAll(response.body().getData());
                        RecycleVideoAdapter adapter = new RecycleVideoAdapter(getActivity());
                        recycler_view.setAdapter(adapter);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(context,"No Internet Connection");
            }
        });
    }
}
