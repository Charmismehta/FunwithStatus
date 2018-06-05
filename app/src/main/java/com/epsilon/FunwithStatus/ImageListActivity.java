package com.epsilon.FunwithStatus;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.epsilon.FunwithStatus.utills.Sessionmanager.Id;

public class ImageListActivity extends AppCompatActivity {

    Activity context;
    TextView text, title;
    ImageView ileft, iright;
    GridView imagelistgrid_view;
    String subcat;
    String post_files, user;
    File fileGallery;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    SwipeRefreshLayout swipelayout;
    Sessionmanager sessionmanager;
    EditText edit_caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        context = this;
        sessionmanager = new Sessionmanager(this);
        idMappings();

        subcat = getIntent().getStringExtra("NAME");
        user = sessionmanager.getValue(Sessionmanager.Name);

        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            Imagecategory(subcat);
        }

        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Imagecategory(subcat);
                swipelayout.setRefreshing(false);
            }
        });

        title.setText(subcat);

        AdView mAdView = findViewById(R.id.adView);
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

        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,Demo.class);
                it.putExtra("subcat",subcat);
                startActivity(it);
                finish();
            }
        });
    }
//    public void onBackPressed() {
//        Intent it = new Intent(ImageListActivity.this, SubCatImage.class);
//        it.putExtra("NAME",subcat);
//        startActivity(it);
//        finish();
//    }

    private void idMappings() {

        imagelistgrid_view = (GridView) findViewById(R.id.imagelistgrid_view);
        swipelayout=(SwipeRefreshLayout) findViewById(R.id.swipelayout);
        text=(TextView) findViewById(R.id.text);
        title=(TextView) findViewById(R.id.title);
        ileft=(ImageView) findViewById(R.id.ileft);
        iright=(ImageView) findViewById(R.id.iright);
    }

    public void Imagecategory(String subcat) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageList> countrycall = apiInterface.imagelistpojo(subcat);
        countrycall.enqueue(new Callback<ImageList>() {
            @Override
            public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                dialog.dismiss();
                if (response.body().getStatus().equals("Success")) {
                    if (Constants.imageListData != null) {
                        Constants.imageListData.clear();
                    }
                    Constants.imageListData.addAll(response.body().getImages());
                    ImageListAdapter adapter = new ImageListAdapter(context);
                    imagelistgrid_view.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(context,"Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}



