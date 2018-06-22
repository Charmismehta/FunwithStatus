package com.epsilon.FunwithStatus;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activity = this;
        sessionmanager = new Sessionmanager(activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
}