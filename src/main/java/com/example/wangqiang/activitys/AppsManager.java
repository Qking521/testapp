package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wangqiang.app.AppInfo;
import com.example.wangqiang.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2015/10/27.
 */
public class AppsManager extends Activity {

    ListView mListView;
    private List<AppInfo> mAppInfos = new ArrayList<AppInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager);
        initData();
        initViews();

    }

    private void initViews() {

        mListView = (ListView)findViewById(R.id.app_listview);
        mListView.setAdapter(new AppInfosAdapter(this));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = mAppInfos.get(position).getIntent();
                if (intent != null) {
                    AppsManager.this.startActivity(intent);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startAPPDetailActivity(position);
                return true;
            }
        });
    }

    private void initData() {

        List<ApplicationInfo> packageInfos = this.getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo packageInfo = packageInfos.get(i);
            appInfo.setPackageName(packageInfo.packageName);
            appInfo.setAppName(packageInfo.loadLabel(getPackageManager()).toString());
            appInfo.setIcon(packageInfo.loadIcon(getPackageManager()));
            appInfo.setIntent(getPackageManager().getLaunchIntentForPackage(packageInfo.packageName));
            appInfo.setIsSystemApp((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
            appInfo.setIsUninstallable((packageInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0
                                       || (packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) < 0);
            mAppInfos.add(appInfo);
        }
    }

    private void startAPPDetailActivity(int position) {
        AppInfo appInfo = mAppInfos.get(position);
        Intent intent = new Intent(this, AppDetailActivity.class);
        intent.putExtra("package_name", appInfo.getPackageName());
        startActivity(intent);

    }

    private class AppInfosAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public AppInfosAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mAppInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holderView;
            AppInfo appInfo = (AppInfo) getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.info_show_listview_item, null);
                holderView = new HolderView();
                holderView.frontLayout = (RelativeLayout)convertView.findViewById(R.id.info_show_front_layout);
                holderView.icon = (ImageView) convertView.findViewById(R.id.icon);
                holderView.className = (TextView)convertView.findViewById(R.id.app_name);
                holderView.frontLayout.setOnTouchListener(new MyOnTouchListener());
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }
            holderView.icon.setImageDrawable(appInfo.getIcon());
            holderView.className.setText(appInfo.getAppName());
            return convertView;
        }

        class HolderView {
            public RelativeLayout frontLayout;
            public TextView className;
            public ImageView icon;
        }
    }

    class MyOnTouchListener implements View.OnTouchListener{

        int currentX = 0;
        boolean scrollToLeftComplete = false;
        boolean scrollToRightComplete = false;
        final static int MAX_SCROLL = 100;
        int delX = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            Log.v("wq", "getX="+ event.getX() + " ,getRawX="+ event.getRawX());
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    currentX = (int)event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    delX = currentX - (int)event.getX();
                    if (delX > 0){
                        v.scrollBy(delX, 0);
                    }else if (delX < 0 && scrollToLeftComplete){
                        v.scrollBy(-MAX_SCROLL, 0);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (delX < 0 && Math.abs(delX) < MAX_SCROLL / 2 && !scrollToRightComplete){
                        v.scrollBy(-delX, 0);
                        scrollToRightComplete = true;
                    }
                    if (delX > 0){
                        v.scrollBy(MAX_SCROLL, 0);
                        scrollToLeftComplete = true;
                    }

                    break;

            }
            return true;
        }
    }

}
