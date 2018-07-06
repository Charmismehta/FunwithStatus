package com.epsilon.FunwithStatus;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.adapter.FullScreenImageAdapter;
import com.epsilon.FunwithStatus.adapter.FullScreenTrenImageAdapter;
import com.epsilon.FunwithStatus.adapter.ImageListAdapter;
import com.epsilon.FunwithStatus.adapter.WhatsappImageAdapter;
import com.epsilon.FunwithStatus.jsonpojo.deleteimage.DeleteImage;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageList;
import com.epsilon.FunwithStatus.jsonpojo.image_list.ImageListDatum;
import com.epsilon.FunwithStatus.jsonpojo.imagedislike.ImageDislike;
import com.epsilon.FunwithStatus.jsonpojo.imagelike.ImageLike;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageSlider extends AppCompatActivity {

    ViewPager pager;
    Activity activity;
    String name, maincat, pic;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    LinearLayout layout_content, ll;
    RelativeLayout mainlayout;
    ImageView display_image, download, like, dislike, share, delete, whatsapp, facebook;
    InputStream is = null;
    Sessionmanager sessionmanager;
    ProgressDialog mProgressDialog;
    File myDir;
    Toolbar toolbar;
    InputStream stream = null;
    RecyclerView rv_image;
    RecyclerView.LayoutManager mLayoutManager;
    private final String TAG = Dashboard.class.getSimpleName();
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        activity = this;
        sessionmanager = new Sessionmanager(activity);
        pager = (ViewPager) findViewById(R.id.pager);
        rv_image = (RecyclerView) findViewById(R.id.rv_image);
        Intent mIntent = getIntent();
        final int position = mIntent.getIntExtra("position", 0);
        final String Id = getIntent().getStringExtra("Id");
        name = getIntent().getStringExtra("NAME");
        String u_name = getIntent().getStringExtra("U_NAME");
        maincat = getIntent().getStringExtra("REALNAME");
        final String email = sessionmanager.getValue(Sessionmanager.Email);
        final String loginuser = sessionmanager.getValue(Sessionmanager.Name);
        pic = getIntent().getStringExtra("pic");
        String bitmap = getIntent().getStringExtra("picture");
        loadNativeAd();

        if (name.equalsIgnoreCase("WHATSAPP")) {
            WhatsappImageAdapter adapter = new WhatsappImageAdapter(activity);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        } else if (name.equalsIgnoreCase("Featured")) {
            FullScreenTrenImageAdapter adapter = new FullScreenTrenImageAdapter(activity,name,maincat,position);
            Imagecategory(name);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        } else {
            FullScreenImageAdapter adapter = new FullScreenImageAdapter(activity,name,maincat,position);
            Imagecategory(name);
            pager.setAdapter(adapter);
            pager.post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(position, true);
                }
            });
        }
    }



    // TODO : END SHARE IMAGE CODE ////////////////

    public void onBackPressed() {
        if(nativeAdContainer.getVisibility() == View.VISIBLE) {
            dismissAd();
        }
        else {
            if (name.equalsIgnoreCase("featured")) {
                Intent it = new Intent(activity, SubCatImage.class);
                it.putExtra("NAME", name);
                it.putExtra("REALNAME", maincat);
                Log.e("NAME", name);
                Log.e("REALNAME", maincat);
                startActivity(it);
                finish();// close this activity and return to preview activity (if there is any)
            } else {
                Intent it = new Intent(activity, ImageListActivity.class);
                it.putExtra("NAME", name);
                it.putExtra("REALNAME", maincat);
                Log.e("NAME", name);
                Log.e("REALNAME", maincat);
                startActivity(it);
                finish();
            }
        }
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
                    Constants.imageListData.addAll(response.body().getImages());
                    rv_image.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                    rv_image.setLayoutManager(mLayoutManager);
                    ImageListAdapter adapter = new ImageListAdapter(activity,maincat);
                    rv_image.setAdapter(adapter);
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
    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeAd = new NativeAd(this,"263700057716193_263738751045657");
        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                showNativeAdWithDelay();

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();

    }
    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();
        nativeAd.destroy();
        // Add the Ad view into the ad container.
        nativeAdContainer = findViewById(R.id.native_ad_container);
        nativeAdContainer.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(activity, nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }
    public void dismissAd() {
        nativeAdContainer.setVisibility(View.GONE);
        nativeAd.destroy();
    }


    private void showNativeAdWithDelay() {

         Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Check if nativeAd has been loaded successfully
                if(nativeAd == null || !nativeAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(nativeAd.isAdInvalidated()) {
                    return;
                }
                inflateAd(nativeAd); // Inflate Native Ad into Container same as previous code example
            }
        }, 1000 * 60 * 1/4); // Show the ad after 15 minutes
    }
}
