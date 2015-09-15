package com.example.wangqiang.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wangqiang.nav.NavigationActivity;
import com.example.wangqiang.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    static  final String ENTER = "\n";

    private RelativeLayout deviceInfoLayout;
    private TextView deviceInfo;
    private ImageView mInfoShowImage;
    private ListView mInfoShowListView;


    private int mStatusBarHeight = 0;
    private List<AppInfo> mAppInfos = new ArrayList<AppInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getNavigation(this)){
            startActivity(new Intent(this, NavigationActivity.class));
        }
        setContentView(R.layout.activity_main);

        if (Utils.isSDcardMounted())

        initUI();

    }

    private void initUI() {
        deviceInfoLayout = (RelativeLayout)findViewById(R.id.device_info_layout);
        deviceInfo = (TextView)findViewById(R.id.device_info);
        mInfoShowImage = (ImageView)findViewById(R.id.info_show_image);
        mInfoShowListView = (ListView)findViewById(R.id.info_show_listview);
    }

    public void startFloatService(View view){
        this.startService(new Intent(MainActivity.this, FloatViewService.class));
    }

    public void stopFloatService(View view){
        this.stopService(new Intent(MainActivity.this, FloatViewService.class));
    }

    public void startNavigation(View view){
        startActivity(new Intent(this, NavigationActivity.class));
    }

    public void hideDeviceInfo(View view){

        for(int i=0; i < deviceInfoLayout.getChildCount(); i++){
            View childView = deviceInfoLayout.getChildAt(i);
            if (childView instanceof ScrollView){
                ((TextView)((ScrollView) childView).getChildAt(0)).setText(null);
            }else if (childView instanceof ImageView){
                childView.setBackground(null);
            }else if (childView instanceof  ListView){
                ((ListView) childView).setAdapter(null);
            }
        }
        deviceInfoLayout.setVisibility(View.GONE);
    }

    public void deviceInfo(View view){

        deviceInfoLayout.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        displayInfo(sb);
        buildInfo(sb);
        telephonyInfo(sb);
        SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());
        highLightString(ssb, sb.toString());
        deviceInfo.setText(ssb);
    }

    //highlight given string
    private void highLightString(SpannableStringBuilder ssb, String text) {
        String[] highLightedStrings = new String[]{"--------------telephone info-----------------------",
                                                "----------------display info----------------",
                                                "----------------build info---------------"};
        for (int i=0; i<highLightedStrings.length; i++){
            int start = text.indexOf(highLightedStrings[i]);
            int end = start + highLightedStrings[i].length();
            ssb.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }


    public void appsInfo(View view){
        deviceInfoLayout.setVisibility(View.VISIBLE);
        List<ApplicationInfo> packageInfos = this.getPackageManager().getInstalledApplications(0);
        for (int i=0; i<packageInfos.size(); i++){
            AppInfo appInfo = new AppInfo();
            ApplicationInfo packageInfo = packageInfos.get(i);
            appInfo.setPackageName(packageInfo.packageName);
            appInfo.setAppName(packageInfo.className);
            appInfo.setIcon(packageInfo.loadIcon(getPackageManager()));
            mAppInfos.add(appInfo);
        }
        mInfoShowListView.setAdapter(new AppInfosAdapter(this));

    }

    private class AppInfosAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        public AppInfosAdapter(Context context){
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
            AppInfo appInfo = (AppInfo)getItem(position);
            if (convertView == null){
                convertView = inflater.inflate(R.layout.info_show_listview_item, null);
                holderView = new HolderView();
                holderView.icon = (ImageView)convertView.findViewById(R.id.icon);
                holderView.packageName = (TextView)convertView.findViewById(R.id.package_name);
                convertView.setTag(holderView);
            }else {
                holderView = (HolderView)convertView.getTag();
            }
            holderView.icon.setImageDrawable(appInfo.getIcon());
            holderView.packageName.setText(appInfo.getPackageName());
            return convertView;
        }

        class HolderView{
            public TextView packageName;
            public TextView className;
            public ImageView icon;
        }
    }

    private void telephonyInfo(StringBuilder sb) {
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        sb.append("--------------telephone info-----------------------").append(ENTER)
                .append("device id: ").append(tm.getDeviceId()).append(ENTER)
                .append("device soft version: ").append(tm.getDeviceSoftwareVersion()).append(ENTER)
                .append("groupId level: ").append(tm.getGroupIdLevel1()).append(ENTER)
                .append("line number: ").append(tm.getLine1Number()).append(ENTER)
                .append("mmsUA prof url: ").append(tm.getMmsUAProfUrl()).append(ENTER)
                .append("mmsUserAgent: ").append(tm.getMmsUserAgent()).append(ENTER)
                .append("network country ISO: ").append(tm.getNetworkCountryIso()).append(ENTER)
                .append("network operator: ").append(tm.getNetworkOperator()).append(ENTER)
                .append("network operator name: ").append(tm.getNetworkOperatorName()).append(ENTER)
                .append("sim country iso: ").append(tm.getSimCountryIso()).append(ENTER)
                .append("sim operator: ").append(tm.getSimOperator()).append(ENTER)
                .append("sim operator name: ").append(tm.getSimOperatorName()).append(ENTER)
                .append("sim serial number: ").append(tm.getSimSerialNumber()).append(ENTER)
                .append("subscriber id: ").append(tm.getSubscriberId()).append(ENTER);


    }

    private void buildInfo(StringBuilder sb) {
        sb.append("----------------build info---------------").append(ENTER)
                .append("board: ").append(Build.BOARD).append(ENTER)
                .append("boot load: ").append(Build.BOOTLOADER).append(ENTER)
                .append("brand: ").append(Build.BRAND).append(ENTER)
                .append("device: ").append(Build.DEVICE).append(ENTER)
                .append("display: ").append(Build.DISPLAY).append(ENTER)
                .append("fingerPrint: ").append(Build.FINGERPRINT).append(ENTER)
                .append("hard: ").append(Build.HARDWARE).append(ENTER)
                .append("host: ").append(Build.HOST).append(ENTER)
                .append("id: ").append(Build.ID).append(ENTER)
                .append("manufacturer: ").append(Build.MANUFACTURER).append(ENTER)
                .append("model: ").append(Build.MODEL).append(ENTER)
                .append("product: ").append(Build.PRODUCT).append(ENTER)
                .append("serial: ").append(Build.SERIAL).append(ENTER)
                .append("time: ").append(Build.TIME).append(ENTER)
                .append("type: ").append(Build.TYPE).append(ENTER)
                .append("user: ").append(Build.USER).append(ENTER)
                .append("version: ").append(Build.VERSION.RELEASE).append(ENTER)
                .append("tag: ").append(Build.TAGS).append(ENTER)
                .append("radio version: ").append(Build.getRadioVersion()).append(ENTER);


    }

    private void displayInfo(StringBuilder sb) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        sb.append("----------------display info----------------").append(ENTER)
                .append("width: ").append(dm.widthPixels).append(ENTER)
                .append("height: ").append(dm.heightPixels).append(ENTER)
                .append("scale: ").append(dm.scaledDensity).append(ENTER)
                .append("density: ").append(dm.density).append(ENTER)
                .append("statusBarHeight: ").append(getStatusBarHeight()).append(ENTER);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public int getStatusBarHeight() {
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0)
            mStatusBarHeight = getResources().getDimensionPixelSize(resID);
        return mStatusBarHeight;
    }
}
