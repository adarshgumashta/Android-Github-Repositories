package com.sample.androidgithubrepositories;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



/**
 * Created by Adi on 26-03-2018.
 */

public class MyApplication extends Application {
    private static Map<Integer, String> defaultLocaleString;
    private static Map<String, Integer> defaultLocaleInteger;
    String[] categories;

    public static String getStringInDefaultLocale(int resId) {
        return defaultLocaleString.get(resId);
    }

    public static Map<String, Integer> stringIntegerMap() {
        return defaultLocaleInteger;
    }

    public static Map<Integer, String> integerStringMap() {
        return defaultLocaleString;
    }

    public void onCreate() {
        super.onCreate();
        Resources currentResources = getResources();
        AssetManager assets = currentResources.getAssets();
        DisplayMetrics metrics = currentResources.getDisplayMetrics();
        Configuration config = new Configuration(currentResources.getConfiguration());
        config.locale = Locale.ENGLISH;
        new Resources(assets, metrics, config);

        defaultLocaleString = new HashMap<Integer, String>();
        defaultLocaleInteger=new HashMap<String, Integer>();
        Class<?> stringResources = R.string.class;
        for (Field field : stringResources.getFields()) {
            String packageName = getPackageName();
            int resId = getResources().getIdentifier(field.getName(), "string", packageName);
            try {
                defaultLocaleString.put(resId, getString(resId));
                defaultLocaleInteger.put(getString(resId),resId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Restore device-specific locale
        new Resources(assets, metrics, currentResources.getConfiguration());
    }

}
