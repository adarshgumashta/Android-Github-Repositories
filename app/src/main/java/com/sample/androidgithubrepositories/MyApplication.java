package com.sample.androidgithubrepositories;

import android.app.Application;




/**
 * Created by Adi on 26-03-2018.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
       // Stetho.initializeWithDefaults(this);
       // Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        //Fabric.with(this, crashlytics);
    }
}
