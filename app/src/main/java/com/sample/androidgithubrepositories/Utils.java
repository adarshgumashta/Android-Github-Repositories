package com.sample.androidgithubrepositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.sample.androidgithubrepositories.Receiver.PrefManager;

/**
 * Created by Adi on 21-01-2017.
 */
public class Utils {
    private static final String PREFERENCES_FILE = "materialsample_settings";
    private Context _context;
    private PrefManager pref;

    // constructor
    public Utils(Context context) {
        this._context = context;
        pref = new PrefManager(_context);
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.commit();
    }
}
