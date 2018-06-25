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
        AdSettings.addTestDevice("da4523bc-d2c7-4717-893e-ed6e2274ad60");
    }
}
