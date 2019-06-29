package com.sample.androidgithubrepositories.Receiver;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adi on 21-01-2017.
 */
public class PrefManager {
    public static final int Notification_VARIABLE = 0;
    public static final int ISNOTIFICATIONSHOWN = 0;
    public static final int LOADALARMFIRST_TIME = 0;
    public static final int ShowADD_VARIABLE = 0;
    private static final String TAG = PrefManager.class.getSimpleName();
    private static final String PREF_NAME = "AwesomeWallpapers";
    private static String notificationvariable = "notificationvariable";
    private static String setAlarmOnce = "setAlarmOnce";
    private static String showNotification = "showNotification";
    private static String showaddvar = "showaddvar";
    private static String swapSwitchOnce= "swapSwitchOnce";
    private static String oneMoreAlarm= "oneMoreAlarm";



    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }


    public int getshow_Notification() {
        return pref.getInt(showNotification, ISNOTIFICATIONSHOWN);
    }

    public void setshow_Notification(int fileNames) {
        editor = pref.edit();
        editor.putInt(showNotification, fileNames);
        editor.commit();
    }

    public int getSetAlarmOnce() {
        return pref.getInt(setAlarmOnce, LOADALARMFIRST_TIME);
    }

    public void setSetAlarmOnce(int setAlarmOnces) {
        editor = pref.edit();
        editor.putInt(setAlarmOnce, setAlarmOnces);
        editor.commit();
    }

    public int  getSwapSwitchOnce() {
        return pref.getInt(swapSwitchOnce, LOADALARMFIRST_TIME);
    }

    public void setSwapSwitchOnce(int swapSwitchOnces) {
        editor = pref.edit();
        editor.putInt(swapSwitchOnce, swapSwitchOnces);
        editor.commit();
    }

    public int getOneMoreAlarm() {
        return pref.getInt(oneMoreAlarm, LOADALARMFIRST_TIME);
    }

    public void setOneMoreAlarm(int oneMoreAlarms) {
        editor = pref.edit();
        editor.putInt(oneMoreAlarm, oneMoreAlarms);
        editor.commit();
    }

    public int getNotificationvariable() {
        return pref.getInt(notificationvariable, Notification_VARIABLE);
    }

    public void setNotificationvariable(int columns2) {
        editor = pref.edit();

        editor.putInt(notificationvariable, columns2);

        // commit changes
        editor.commit();
    }

    public int getShowaddvar() {
        return pref.getInt(showaddvar, ShowADD_VARIABLE);
    }

    public void setShowaddvar(int showaddvars) {
        editor = pref.edit();
        editor.putInt(showaddvar, showaddvars);
        editor.commit();
    }


}
