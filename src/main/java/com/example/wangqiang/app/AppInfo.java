package com.example.wangqiang.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by wangqiang on 2015/9/11.
 */
public class AppInfo {
    private String packageName;
    private String appName;
    private Drawable icon;
    private Intent intent;

    boolean isSystemApp;
    boolean isUninstallable;

    public boolean isUninstallable() {
        return isUninstallable;
    }

    public void setIsUninstallable(boolean isUninstallable) {
        this.isUninstallable = isUninstallable;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


}
