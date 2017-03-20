package com.example.wangqiang.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

    public static boolean isSDcardMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static void showToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void test(){
        //nothing to do
    }
}
