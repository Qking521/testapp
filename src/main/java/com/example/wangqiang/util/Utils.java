package com.example.wangqiang.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by wangqiang on 2015/8/19.
 */
public class Utils {

    public static final String SHARE_NAVIGATION = "share_navigation";

    public static void setNavigation(Context context, boolean pass) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(SHARE_NAVIGATION, pass).commit();
    }

    public static boolean getNavigation(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(SHARE_NAVIGATION, true);
    }


}
