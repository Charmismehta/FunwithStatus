package com.epsilon.FunwithStatus;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsilon.FunwithStatus.fragment.AboutusFragment;
import com.epsilon.FunwithStatus.fragment.ContactUsFragment;
import com.epsilon.FunwithStatus.fragment.HomeFragment;
import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.fragment.MainFragment;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.jsonpojo.login.Login;
import com.epsilon.FunwithStatus.jsonpojo.logout.Logout;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Helper;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import com.google.android.gms.ads.AdRequest;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Context activity;
    DrawerLayout drawer;
    LinearLayout mainbtn;
    String nav_item_intent = null;
    NavigationView navigationView;
    Fragment fragment = null;
    String passStr;
    Sessionmanager sessionmanager;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    MenuItem nav_login;
    private final String TAG = Dashboard.class.getSimpleName();
    Toolbar toolbar;
    public static int STORAGE_PERMISSION_CODE=1;
    private InterstitialAd interstitialAd;
    NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activity = this;
        sessionmanager = new Sessionmanager(activity);
        requestStoragePermission();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(!sessionmanager.getValue(Sessionmanager.Name).equalsIgnoreCase(""))
        {
        String text = sessionmanager.getValue(Sessionmanager.Name);
        String cap = text.substring(0, 1).toUpperCase() + text.substring(1);
        toolbar.setTitle(cap);}
        else
        {
            toolbar.setTitle("User");
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainbtn = (LinearLayout) findViewById(R.id.mainbtn);
        nativeAd = new NativeAd(this,"263700057716193_263738751045657");
        loadAd();

        toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = toolbar.findViewById(R.id.vc_addgift);
                if (item != null) {
                    toolbar.removeOnLayoutChangeListener(this);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectAnimator animator = ObjectAnimator
                                    .ofFloat(v, "rotation", v.getRotation() + 180);
                            animator.start();
                            interstitialAd = new InterstitialAd(activity, getString(R.string.placement_id));
                            // Set listeners for the Interstitial Ad
                            interstitialAd.setAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {
                                    // Interstitial ad displayed callback
                                    Log.e(TAG, "Interstitial ad displayed.");
                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {
                                    // Interstitial dismissed callback
                                    Log.e(TAG, "Interstitial ad dismissed.");
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                                    // Show the ad
                                    interstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                    // Ad clicked callback
                                    Log.d(TAG, "Interstitial ad clicked!");
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                    // Ad impression logged callback
                                    Log.d(TAG, "Interstitial ad impression logged!");
                                }
                            });

                            // For auto play video ads, it's recommended to load the ad
                            // at least 30 seconds before it is shown
                            interstitialAd.loadAd();
                        }
                    });
                }
            }
        });

        fragment = new MainFragment();
            if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack("Some String");
            ft.commit();
        }
        nav_item_intent = getIntent().getStringExtra(Constants.NAV_ITEM_INTENT);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        if (Sessionmanager.getPreferenceBoolean(activity, Constants.IS_LOGIN, false)) {
            Menu menu = navigationView.getMenu();
            nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Logout");

        }
        else
        {
            Menu menu = navigationView.getMenu();
            nav_login = menu.findItem(R.id.nav_logout);
            nav_login.setTitle("Login");
        }

    }


    @Override
    public void onBackPressed() {
        popup();

//        dismissAd();
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menugift, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.vc_addgift) {
            return true;
        }
            return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {


        switch (itemId) {

            case R.id.nav_main:
                fragment = new MainFragment();
                break;

            case R.id.nav_text:
                fragment = new HomeFragment();
                break;

            case R.id.nav_image:
                fragment = new ImageFragment();
                break;

            case R.id.nav_Video:
                fragment = new VideoFragment();
                break;

            case R.id.nav_profile:
                fragment = new ContactUsFragment();
                break;

            case R.id.nav_about_us:
                fragment = new AboutusFragment();
                break;

            case R.id.nav_feedback:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=com.epsilon.FunwithStatus"));
                startActivity(viewIntent);
                break;

            case R.id.nav_logout:
                if (nav_login.getTitle().equals("Logout"))
                {
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_login = menu.findItem(R.id.nav_logout);
                    nav_login.setTitle("Logout");
                    sessionmanager.logoutUser();
                    LogoutAPI();
                }
                else
                {
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_login = menu.findItem(R.id.nav_logout);
                    nav_login.setTitle("Login");
                    Intent mainIntent = new Intent(activity, LoginPage.class);
                    startActivity(mainIntent);
                    finish();
                }
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack("Some String");
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void popup()
    {

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
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
                // Native ad is loaded and ready to be displayed
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
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Button  btnNever, btnSure;
        btnNever = (Button) dialog.findViewById(R.id.btnNever);
        btnSure = (Button) dialog.findViewById(R.id.btnSure);
        nativeAdContainer = dialog.findViewById(R.id.native_ad_container);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        btnNever.setOnClickListener(new View.OnClickListener() { //yes
            @Override
            public void onClick(View view) {
              finish();
              System.exit(0);
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() { // no
            @Override
            public void onClick(View view) {
               dialog.dismiss();
            }
        });
    }

    public void loadAd()
    {
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
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(nativeAd);
                // Native ad is loaded and ready to be displayed
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
    LinearLayout nativeAdContainer;
    private void inflateAd(NativeAd nativeAd) {


        LinearLayout adView;
        nativeAd.unregisterView();

        LayoutInflater inflater = LayoutInflater.from(activity);
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
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
    public void LogoutAPI() {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        Call<Logout> logincall = apiInterface.logout();
        logincall.enqueue(new Callback<Logout>() {
            @Override
            public void onResponse(Call<Logout> call, Response<Logout> response)
            {
                dialog.dismiss();
                if (response.body().getStatus() == 1)
                {
                    Toast.makeText(activity ,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Dashboard.this, LoginPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }
                else
                {
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Logout> call, Throwable t) {
                dialog.dismiss();
                Helper.showToastMessage(activity,"No Internet Connection");
            }
        });
    }
}