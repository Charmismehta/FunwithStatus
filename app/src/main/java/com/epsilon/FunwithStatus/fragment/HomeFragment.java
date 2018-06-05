package com.epsilon.FunwithStatus.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epsilon.FunwithStatus.Dashboard;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.TextListActivity;

import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HomeFragment extends Fragment {

    Context context;
    LinearLayout ic_trending,ic_politics,ic_groupadmin,ic_teacherstudent,ic_encourage,ic_friendship,ic_goodmorning,ic_goodnight,ic_birthday,ic_thankyou,ic_Anniversary,
            ic_congratulation,ic_insult,ic_flirt,ic_love,ic_shayri,ic_sad,ic_sorry,ic_smile,ic_doublemeaning,ic_gujrati,ic_marathi,ic_religion,ic_music,
            ic_lyrics,ic_movie,ic_status;
    TextView tv_trending,tv_politics,tv_groupadmin,tv_teacherstudent,tv_encourage,tv_friendship,tv_goodmorning,tv_goodnight,tv_birthday,tv_thankyou,tv_Anniversary,
            tv_congratulation, tv_insult,tv_flirt,tv_love,tv_shayri,tv_sad,tv_sorry,tv_smile,tv_doublemeaning,tv_gujrati,tv_marathi,tv_religion,tv_music,
            tv_lyrics,tv_movie,tv_status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'> Texts </font>"));
        context = getContext();
        IpMappings(view);
        Lisnter();
        AdView mAdView = view.findViewById(R.id.adView);
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

    private void Lisnter() {
        ic_trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_trending.getText().toString());
                Log.e("NAME",tv_trending.getText().toString());
                startActivity(it);
            }
        });
        ic_politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_politics.getText().toString());
                Log.e("NAME",tv_politics.getText().toString());
                startActivity(it);
            }
        });
        ic_groupadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_groupadmin.getText().toString());
                startActivity(it);
            }
        });
        ic_teacherstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_teacherstudent.getText().toString());
                startActivity(it);
            }
        });
        ic_encourage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_encourage.getText().toString());
                startActivity(it);
            }
        });
        ic_friendship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_friendship.getText().toString());
                startActivity(it);
            }
        });
        ic_goodmorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_goodmorning.getText().toString());
                startActivity(it);
            }
        });
        ic_goodnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_goodnight.getText().toString());
                startActivity(it);
            }
        });
        ic_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_birthday.getText().toString());
                startActivity(it);
            }
        });
        ic_thankyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_thankyou.getText().toString());
                startActivity(it);
            }
        });
        ic_Anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_Anniversary.getText().toString());
                startActivity(it);
            }
        });
        ic_congratulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_congratulation.getText().toString());
                startActivity(it);
            }
        });
        ic_insult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_insult.getText().toString());
                startActivity(it);
            }
        });
        ic_flirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_flirt.getText().toString());
                startActivity(it);
            }
        });
        ic_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_love.getText().toString());
                startActivity(it);
            }
        });
        ic_shayri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_shayri.getText().toString());
                startActivity(it);
            }
        });
        ic_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_sad.getText().toString());
                startActivity(it);
            }
        });
        ic_sorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_sorry.getText().toString());
                startActivity(it);
            }
        });
        ic_smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_smile.getText().toString());
                startActivity(it);
            }
        });
        ic_doublemeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_doublemeaning.getText().toString());
                startActivity(it);
            }
        });
        ic_gujrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_gujrati.getText().toString());
                startActivity(it);
            }
        });
        ic_marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_marathi.getText().toString());
                startActivity(it);
            }
        });
        ic_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_religion.getText().toString());
                startActivity(it);
            }
        });
        ic_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_music.getText().toString());
                startActivity(it);
            }
        });
        ic_lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_lyrics.getText().toString());
                startActivity(it);

            }
        });
        ic_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_movie.getText().toString());
                startActivity(it);
            }
        });
        ic_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_status.getText().toString());
                startActivity(it);
            }
        });
        ic_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), TextListActivity.class);
                it.putExtra("NAME",tv_status.getText().toString());
                startActivity(it);

            }
        });
    }

    private void IpMappings(View view) {
        ic_trending = (LinearLayout)view.findViewById(R.id.ic_trending);
        ic_politics = (LinearLayout)view.findViewById(R.id.ic_politics);
        ic_groupadmin = (LinearLayout)view.findViewById(R.id.ic_groupadmin);
        ic_teacherstudent = (LinearLayout)view.findViewById(R.id.ic_teacherstudent);
        ic_encourage = (LinearLayout)view.findViewById(R.id.ic_encourage);
        ic_friendship = (LinearLayout)view.findViewById(R.id.ic_friendship);
        ic_goodmorning = (LinearLayout)view.findViewById(R.id.ic_goodmorning);
        ic_goodnight = (LinearLayout)view.findViewById(R.id.ic_goodnight);
        ic_birthday = (LinearLayout)view.findViewById(R.id.ic_birthday);
        ic_thankyou = (LinearLayout)view.findViewById(R.id.ic_thankyou);
        ic_Anniversary = (LinearLayout)view.findViewById(R.id.ic_Anniversary);
        ic_congratulation = (LinearLayout)view.findViewById(R.id.ic_congratulation);
        ic_insult = (LinearLayout)view.findViewById(R.id.ic_insult);
        ic_flirt = (LinearLayout)view.findViewById(R.id.ic_flirt);
        ic_love = (LinearLayout)view.findViewById(R.id.ic_love);
        ic_shayri = (LinearLayout)view.findViewById(R.id.ic_shayri);
        ic_sad = (LinearLayout)view.findViewById(R.id.ic_sad);
        ic_sorry = (LinearLayout)view.findViewById(R.id.ic_sorry);
        ic_smile = (LinearLayout)view.findViewById(R.id.ic_smile);
        ic_doublemeaning = (LinearLayout)view.findViewById(R.id.ic_doublemeaning);
        ic_gujrati = (LinearLayout)view.findViewById(R.id.ic_gujrati);
        ic_marathi = (LinearLayout)view.findViewById(R.id.ic_marathi);
        ic_religion = (LinearLayout)view.findViewById(R.id.ic_religion);
        ic_music = (LinearLayout)view.findViewById(R.id.ic_music);
        ic_lyrics = (LinearLayout)view.findViewById(R.id.ic_lyrics);
        ic_movie = (LinearLayout)view.findViewById(R.id.ic_movie);
        ic_status = (LinearLayout)view.findViewById(R.id.ic_status);
        tv_politics = (TextView) view.findViewById(R.id.tv_politics);
        tv_trending = (TextView) view.findViewById(R.id.tv_trending);
        tv_groupadmin = (TextView) view.findViewById(R.id.tv_groupadmin);
        tv_teacherstudent = (TextView) view.findViewById(R.id.tv_teacherstudent);
        tv_encourage = (TextView) view.findViewById(R.id.tv_encourage);
        tv_friendship = (TextView) view.findViewById(R.id.tv_friendship);
        tv_friendship = (TextView) view.findViewById(R.id.tv_friendship);
        tv_goodmorning = (TextView) view.findViewById(R.id.tv_goodmorning);
        tv_goodnight = (TextView) view.findViewById(R.id.tv_goodnight);
        tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);
        tv_thankyou = (TextView) view.findViewById(R.id.tv_thankyou);
        tv_Anniversary = (TextView) view.findViewById(R.id.tv_Anniversary);
        tv_congratulation = (TextView) view.findViewById(R.id.tv_congratulation);
        tv_insult = (TextView) view.findViewById(R.id.tv_insult);
        tv_flirt = (TextView) view.findViewById(R.id.tv_flirt);
        tv_love = (TextView) view.findViewById(R.id.tv_love);
        tv_shayri = (TextView) view.findViewById(R.id.tv_shayri);
        tv_sad = (TextView) view.findViewById(R.id.tv_sad);
        tv_sorry = (TextView) view.findViewById(R.id.tv_sorry);
        tv_smile = (TextView) view.findViewById(R.id.tv_smile);
        tv_doublemeaning = (TextView) view.findViewById(R.id.tv_doublemeaning);
        tv_gujrati = (TextView) view.findViewById(R.id.tv_gujrati);
        tv_marathi = (TextView) view.findViewById(R.id.tv_marathi);
        tv_religion = (TextView) view.findViewById(R.id.tv_religion);
        tv_music = (TextView) view.findViewById(R.id.tv_music);
        tv_lyrics = (TextView) view.findViewById(R.id.tv_lyrics);
        tv_movie = (TextView) view.findViewById(R.id.tv_movie);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
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
}


