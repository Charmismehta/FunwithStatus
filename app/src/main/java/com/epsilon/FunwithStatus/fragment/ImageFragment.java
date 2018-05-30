package com.epsilon.FunwithStatus.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.GallaryUtils;
import com.epsilon.FunwithStatus.ImageListActivity;
import com.epsilon.FunwithStatus.R;
import com.epsilon.FunwithStatus.SubCatImage;
import com.epsilon.FunwithStatus.adapter.ImageAdapter;
import com.epsilon.FunwithStatus.adapter.TextAdapter;
import com.epsilon.FunwithStatus.jsonpojo.category_text.Category;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageCategory;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageFragment extends Fragment {

    Activity context;
    GridView imagegrid_view;
    ImageView plus;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_image, container, false);

        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'>Image </font>"));


        context = getActivity();
        plus = (ImageView)view.findViewById(R.id.plus);
        imagegrid_view = (GridView)view.findViewById(R.id.imagegrid_view);


        if (!Helper.isConnectingToInternet(context)) {
            Helper.showToastMessage(context, "Please Connect Internet");
        } else {
            Imagecategory();
        }

        imagegrid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(), SubCatImage.class);
                it.putExtra("position",position);
                startActivity(it);
            }
        });

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
    public void Imagecategory() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageCategory> countrycall = apiInterface.imagepojo();
        countrycall.enqueue(new Callback<ImageCategory>() {
            @Override
            public void onResponse(Call<ImageCategory> call, Response<ImageCategory> response) {
                dialog.dismiss();

                if (Constants.imageCategoryData != null) {
                    Constants.imageCategoryData.clear();
                }
                Constants.imageCategoryData.addAll(response.body().getCatagory());
                ImageAdapter adapter = new ImageAdapter(context);
                imagegrid_view.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ImageCategory> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
