package com.epsilon.FunwithStatus;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.ads.AdSettings;

public class MyApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AdSettings.addTestDevice("bfbdb58b-e3db-4765-b72b-3ef06ae80597");
//       AdSettings.clearTestDevices();
    }
}
