package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.epsilon.FunwithStatus.fragment.HomeFragment;
import com.epsilon.FunwithStatus.fragment.ImageFragment;
import com.epsilon.FunwithStatus.fragment.MainFragment;
import com.epsilon.FunwithStatus.fragment.VideoFragment;
import com.epsilon.FunwithStatus.retrofit.APIClient;
import com.epsilon.FunwithStatus.retrofit.APIInterface;
import com.epsilon.FunwithStatus.utills.Constants;
import com.epsilon.FunwithStatus.utills.Sessionmanager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Dashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    Context activity;
    DrawerLayout drawer;
    LinearLayout mainbtn;
    String nav_item_intent=null;
    NavigationView navigationView;
    Fragment fragment = null;
    String passStr;
    Sessionmanager sessionmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activity = this;
        sessionmanager = new Sessionmanager(activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.vc_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        String text = sessionmanager.getValue(Sessionmanager.Name);
        String cap = text.substring(0, 1).toUpperCase() + text.substring(1);
        toolbar.setTitle(cap);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainbtn = (LinearLayout) findViewById(R.id.mainbtn);


        fragment = new MainFragment();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        nav_item_intent=getIntent().getStringExtra(Constants.NAV_ITEM_INTENT);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);


//
//
//
//            if(nav_item_intent!=null)
//            {
//                if(nav_item_intent.equals("nav_profile"))
//                {
//                    displaySelectedScreen(R.id.nav_home);
//                    navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
//
//                }
//                else
//                {
//                    displaySelectedScreen(R.id.nav_chat);
//                    navigationView.getMenu().findItem(R.id.nav_chat).setChecked(true);
//
//                }
//            }
//            else
//            {
//                displaySelectedScreen(R.id.nav_chat);
//                navigationView.getMenu().findItem(R.id.nav_chat).setChecked(true);
//
//            }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
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

            case R.id.nav_Contact:

            case R.id.nav_about_us:

            case R.id.nav_feedback:

            case R.id.nav_logout:
                sessionmanager.logoutUser();
                Intent i = new Intent(Dashboard.this, LoginPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack("Some String").commit();
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
