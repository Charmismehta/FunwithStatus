package com.epsilon.FunwithStatus;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
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
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;


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
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    private NativeAd nativeAd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activity = this;
        sessionmanager = new Sessionmanager(activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        nativeAd = new NativeAd(this,"263700057716193_263738751045657");
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
                            loadNativeAd();
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        dismissAd();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
//        getMenuInflater().inflate(R.menu.menugift, menu);
//        final ImageView locButton = (ImageView) findViewById(R.id.vc_addgift);
//        // Inflate the menu; this adds items to the action bar if it is present.
//        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
//        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);
//
//        locButton.startAnimation(animation_2);
//
//        animation_2.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                locButton.startAnimation(animation_3);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
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
                    Intent i = new Intent(Dashboard.this, LoginPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
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
    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
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
}