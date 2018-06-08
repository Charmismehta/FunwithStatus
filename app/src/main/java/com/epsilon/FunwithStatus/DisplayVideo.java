package com.epsilon.FunwithStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Sessionmanager;

import java.io.File;
import java.io.InputStream;

public class DisplayVideo extends AppCompatActivity {

    Context activity;
    LinearLayout layout_content;
    RelativeLayout mainlayout;
    ImageView download, like, dislike, share, delete, whatsapp;
    VideoView display_video;
    String pic, name, root, Id, email, u_name, loginuser;
    InputStream is = null;
    Sessionmanager sessionmanager;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    Toolbar toolbar;
    InputStream stream = null;
    String video;
    DisplayMetrics dm;
    SurfaceView sur_View;
    MediaController media_Controller;
    ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        activity = this;
        idMappings();
        Listners();
        sessionmanager = new Sessionmanager(this);
        video = getIntent().getStringExtra("VIDEO");
        share.setColorFilter(getResources().getColor(R.color.colorAccent));
        like.setColorFilter(getResources().getColor(R.color.colorAccent));
        dislike.setColorFilter(getResources().getColor(R.color.colorAccent));
        delete.setColorFilter(getResources().getColor(R.color.colorAccent));
        download.setColorFilter(getResources().getColor(R.color.colorAccent));

        getInit();

    }

    public void getInit() {
        progressBar.setVisibility(View.VISIBLE);
        display_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        // TODO Auto-generated method stub
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });
        media_Controller = new MediaController(this);
        dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        display_video.setMinimumWidth(width);
        display_video.setMinimumHeight(height);
        display_video.setMediaController(media_Controller);
        display_video.setVideoPath(video);
        display_video.start();
    }


    private void Listners() {

    }

    private void idMappings() {
        display_video = (VideoView) findViewById(R.id.display_video);
        download = (ImageView) findViewById(R.id.download);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        share = (ImageView) findViewById(R.id.share);
        delete = (ImageView) findViewById(R.id.delete);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }
}
