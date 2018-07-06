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
        AdSettings.addTestDevice("57f3c833-0f05-47a7-81fc-0dfbd8c1fa0b");
//       AdSettings.clearTestDevices();
    }
}
