package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.adapter.RecipeListAdapter;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Advertise extends AppCompatActivity {

    Activity activity;
    private List<Object> recipeList;
    RecipeListAdapter recipeListAdapter;
    RecyclerView rvRecipes;
    RecyclerView.LayoutManager mLayoutManager;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    NativeAd nativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad2);
        activity = this;

        Imagecategory("Funny");
        rvRecipes = (RecyclerView)findViewById(R.id.idRecyclerViewHorizontalList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity,
                layoutManager.getOrientation());
        rvRecipes.addItemDecoration(dividerItemDecoration);
        rvRecipes.setLayoutManager(layoutManager);

        nativeAd = new NativeAd(activity, "263700057716193_263738751045657");
    }

    public void Imagecategory(String subcat) {
        final ProgressDialog dialog = new ProgressDialog(activity);
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
                    recipeList = new ArrayList<>();
                    recipeList.add(response.body());
                    Constants.imageListData.addAll(response.body().getImages());
                    rvRecipes.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                    rvRecipes.setLayoutManager(mLayoutManager);
                    nativeAd.setAdListener(new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {
                            recipeList.add(4, ad);
                            recipeListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    });

                    nativeAd.loadAd();
                    recipeListAdapter = new RecipeListAdapter(activity, recipeList);
                    rvRecipes.setAdapter(recipeListAdapter);
                }
                else
                {
                    Toast.makeText(activity,"Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageList> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
