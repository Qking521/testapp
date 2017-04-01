package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wangqiang.app.R;

/**
 * Created by wangqiang on 2015/11/2.
 */
public class AppDetailActivity extends Activity {
    TextView mPermisssion;
    TextView mUsage;


    String mPackageName;
    PackageManager mPackageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_detail_layout);

        initViews();
        mPackageName = getIntent().getStringExtra("package_name");
        mPackageManager = this.getPackageManager();

        mUsage.setText(getUsage());
        mPermisssion.setText(getPermissin());

    }

    private String getUsage() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mPackageManager.getApplicationInfo(mPackageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("backupAgentName: ").append(applicationInfo.backupAgentName).append("\n")
                .append("className: ").append(applicationInfo.className).append("\n")
                .append("dataDir: ").append(applicationInfo.dataDir).append("\n")
                .append("deviceProtectedDataDir: ").append(applicationInfo.deviceProtectedDataDir).append("\n")
                .append("manageSpaceActivityName: ").append(applicationInfo.manageSpaceActivityName).append("\n")
                .append("permission: ").append(applicationInfo.permission).append("\n")
                .append("nativeLibraryDir: ").append(applicationInfo.nativeLibraryDir).append("\n")
                .append("processName: ").append(applicationInfo.processName).append("\n")
                .append("publicSourceDir: ").append(applicationInfo.publicSourceDir).append("\n")
                .append("sourceDir: ").append(applicationInfo.sourceDir).append("\n")
                .append("taskAffinity: ").append(applicationInfo.taskAffinity).append("\n");

        return sb.toString();
    }

    private String getPermissin() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mPackageManager.getPackageInfo(mPackageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String[] permissions = packageInfo.requestedPermissions;
        StringBuilder sb = new StringBuilder();
        if (permissions != null && permissions.length > 0){
            for (int i = 0; i < permissions.length; i++) {
                try{
                    PermissionInfo permInfo = mPackageManager.getPermissionInfo(permissions[i], 0);
                    sb.append(permissions[i]).append("\n")
                      .append(permInfo.loadLabel(mPackageManager).toString()).append("\n");
                }catch (Exception e){}
            }
        }
        return  sb.toString();
    }

    private void initViews() {
        mPermisssion = (TextView)findViewById(R.id.app_detail_permission);
        mUsage = (TextView)findViewById(R.id.app_detail_usage);
    }

}
