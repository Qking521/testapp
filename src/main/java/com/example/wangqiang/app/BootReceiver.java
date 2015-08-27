package com.example.wangqiang.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wangqiang on 2015/8/26.
 */
public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v("wq", "BootReceiver action="+ action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)){

        }
    }
}
