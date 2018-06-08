package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;
import com.epsilon.FunwithStatus.adapter.TrendingImgAdapter;
import com.epsilon.FunwithStatus.adapter.VideoAdapter;
import com.epsilon.FunwithStatus.jsonpojo.tending_img.TrendingImg;
import com.epsilon.FunwithStatus.jsonpojo.videolist.VideoList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoFragment extends Fragment {

    Activity context;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        context = getActivity();
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        floatingbtn = (FloatingActionButton) view.findViewById(R.id.floatingbtn);

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
        videolist();
        }

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(),AddVideoActivity.class);
                startActivity(it);
            }
        });

        AdView mAdView =view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
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
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent it = new Intent(getContext(), Dashboard.class);
                    startActivity(it);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void videolist() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<VideoList> countrycall = apiInterface.videolistpojo();
        countrycall.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                dialog.dismiss();

                if (Constants.videoListData != null) {
                    Constants.videoListData.clear();
                }
                if (!Constants.videoListData.equals("") && Constants.videoListData != null) {
                    Constants.videoListData.addAll(response.body().getImages());
                    VideoAdapter adapter = new VideoAdapter(context);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "No Video Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
