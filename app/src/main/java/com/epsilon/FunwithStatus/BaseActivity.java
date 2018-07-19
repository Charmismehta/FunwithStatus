package com.epsilon.FunwithStatus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BaseActivity extends AppCompatActivity {

//    public static final String DATE_FORMAT_SERVER = Constant.DATE_FORMAT_SERVER;
    public boolean isRetry = false;
    public boolean isCallAgain = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.FINISH_ACTIVITY);
//        intentFilter.addAction(Constant.GET_NEW_NOTIFICATION);


    }


    @Override
    protected void onResume() {
//        if(isCallAgain) {
//            checkVersion();
//        }
        super.onResume();
    }


//

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

//    Drawer result;
//
//    public void initDrawer(boolean b) {
//        if (b) {
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//
//
////create the drawer and remember the `Drawer` result object
//            result = new DrawerBuilder()
//                    .withActivity(this).withCloseOnClick(true).withSelectedItemByPosition(-1)
//                    .withHeader(R.layout.nav_header_main)
//                    .addDrawerItems(
//                            new PrimaryDrawerItem().withName("Log out").withSelectable(false).withIcon(R.drawable.ic_place),
//                            new PrimaryDrawerItem().withName("Trip").withSelectable(false).withIcon(R.drawable.ic_call_24dp),
//                            new PrimaryDrawerItem().withName("Violations").withSelectable(false).withIcon(R.drawable.ic_chat),
//                            new PrimaryDrawerItem().withName("Hos").withSelectable(false).withIcon(R.drawable.ic_call_24dp),
//                            new PrimaryDrawerItem().withName("VIR").withSelectable(false).withIcon(R.drawable.ic_fax_24dp),
//                            new PrimaryDrawerItem().withName("Change Password").withSelectable(false).withIcon(R.drawable.ic_chat),
//                            new PrimaryDrawerItem().withName("Logout").withSelectable(false).withIcon(R.drawable.ic_delete_24dp)
//                    )
//                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                        @Override
//                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                            if (position == 0) {
//
//                            } else if (position == 1) {
//
//
//                            } else if (position == 2) {
//
//
//                            } else if (position == 6) {
////                            confirmLogout();
//                            }
//                            return true;
//                        }
//                    })
//                    .build();
//
//            ImageView imgMenu = (ImageView) findViewById(R.id.imgFilter);
//            if (imgMenu != null) {
//                imgMenu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (result.isDrawerOpen()) {
//                            result.closeDrawer();
//                        } else {
//                            result.openDrawer();
//                        }
//                    }
//                });
//            }
//
//        initMenuItems();
//        fillProfileData();
//        }else {
//            ImageView imgMenu = (ImageView) findViewById(R.id.imgFilter);
//            imgMenu.setVisibility(View.GONE);
//        }
//    }
//    public void showLoginDialog() {
//
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
//                .title(R.string.login_title)
//                .content(R.string.login_msg)
//                .buttonsGravity(GravityEnum.CENTER)
//                .positiveText(R.string.btn_ok)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//
//                    }
//                });
//
//        MaterialDialog dialog = builder.build();
//        dialog.show();
//    }
//
//    private void hideMenu(boolean b) {
//        try {
////            if (b)
//            result.closeDrawer();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void finishActivity() {
//        if ((getActivity() instanceof MainActivity)) {
//
//        } else {
//            getActivity().finish();
//        }
//
//    }





    public BaseActivity getActivity() {
        return this;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = super.onKeyDown(keyCode, event);

        // Eat the long press event so the keyboard doesn't come up.
        if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
            return true;
        }

        return handled;
    }

    Toast toast;

    public void showToast(final String text, final int duration) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
//            locationService.stopLocationUpdates();
//            locationService.stop();
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationReceiver);
//            LocalBroadcastManager.getInstance(getApplicationContext())
//                    .unregisterReceiver(commonReciever);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }



}
