package com.example.wangqiang.app;

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
import android.widget.TextView;

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
        mPermisssion.setOnClickListener(new MyOnClickListener("permission", getPermissin()));

    }

    private String getUsage() {
        ApplicationInfo applicationInfo = new ApplicationInfo();

        return null;
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
                    sb.append(permissions[i]).append("\n").
                            append(permInfo.loadLabel(mPackageManager).toString()).append("\n");
                }catch (Exception e){}
            }
        }
        return  sb.toString();
    }

    private void initViews() {
        mPermisssion = (TextView)findViewById(R.id.app_detail_permission);
        mUsage = (TextView)findViewById(R.id.app_detail_usage);
    }

    class  MyOnClickListener implements View.OnClickListener{

        String title;
        String summary;
        int count = 0;
        public MyOnClickListener(String title, String summary){
            this.title = title;
            this.summary = summary;
        }

        @Override
        public void onClick(View v) {
            count += 1;
            if (v instanceof TextView){
                if (count % 2 == 0){
                    ((TextView) v).setText(summary);
                }else {
                    ((TextView) v).setText(title);
                }
            }

        }
    }

}
