package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.epsilon.FunwithStatus.adapter.ImageAdapter;
import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.adapter.SubCatImageAdapter;
import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.fragment.MainFragment;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageCategory;
import com.epsilon.FunwithStatus.jsonpojo.image_category.ImageSubcategory;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatImage extends AppCompatActivity {
    Activity context;
    GridView imagelistgrid_view;
    ImageView ileft, iright;
    TextView title;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    List<ImageSubcategory> imageSubcategories = new ArrayList<>();
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_image);
        context = this;
        idMappings();

        Intent mIntent = getIntent();
        int i = mIntent.getIntExtra("position", 0);
        Imagecategory(i);

        imagelistgrid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SubCatImage.this, ImageListActivity.class);
                it.putExtra("position", position);
                startActivity(it);
            }
        });

        ileft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getSupportFragmentManager().popBackStack();
                } else
                {
                    finish();
                }
            }
        });

        ileft.setImageResource(R.drawable.back);
        iright.setVisibility(View.GONE);
        title.setText("");

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
    }

    private void idMappings() {

        imagelistgrid_view = (GridView) findViewById(R.id.imagelistgrid_view);
        ileft = (ImageView) findViewById(R.id.ileft);
        iright = (ImageView) findViewById(R.id.iright);
        title = (TextView) findViewById(R.id.title);
    }

    public void Imagecategory(final int i) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
        final Call<ImageCategory> countrycall = apiInterface.imagepojo();
        countrycall.enqueue(new Callback<ImageCategory>() {
            @Override
            public void onResponse(Call<ImageCategory> call, Response<ImageCategory> response) {
                dialog.dismiss();

                if (Constants.imageSubcategories != null) {
                    Constants.imageSubcategories.clear();
                }
                Constants.imageSubcategories.addAll(response.body().getCatagory().get(i).get0());
                imageSubcategories = response.body().getCatagory().get(i).get0();
                SubCatImageAdapter adapter = new SubCatImageAdapter(context, imageSubcategories);
                imagelistgrid_view.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ImageCategory> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        } else
        {
            this.finish();
        }
    }
}
