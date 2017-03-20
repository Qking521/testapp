package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.wangqiang.services.FloatViewService;
import com.example.wangqiang.app.R;
import com.example.wangqiang.nav.NavigationActivity;
import com.example.wangqiang.util.Utils;
import com.mediatek.telephony.TelephonyManagerEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeviceInfoActivity extends Activity {

    private static final String ENTER = "\n";
    private static final int SIM_BUMBER_ONE = 1;
    private static final int SIM_BUMBER_TWO = 2;
    private static final String[] highLightedStrings = new String[]{
            "--------------telephone info-----------------------",
            "----------------display info----------------",
            "----------------build info---------------"};

    private RelativeLayout deviceInfoLayout;
    private TextView deviceInfo;
    private ImageView mInfoShowImage;
    private ListView mInfoShowListView;
    private TelephonyManagerEx mTelephonyManagerEx;




    private int mStatusBarHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getNavigation(this)) {
            startActivity(new Intent(this, NavigationActivity.class));
        }
        setContentView(R.layout.device_info);
        initUI();

    }

    private void initUI() {
        deviceInfoLayout = (RelativeLayout) findViewById(R.id.device_info_layout);
        deviceInfo = (TextView) findViewById(R.id.device_info);
        mInfoShowImage = (ImageView) findViewById(R.id.info_show_image);
        mInfoShowListView = (ListView) findViewById(R.id.info_show_listview);
    }

    public void startFloatService(View view) {
        this.startService(new Intent(DeviceInfoActivity.this, FloatViewService.class));
    }

    public void stopFloatService(View view) {
        this.stopService(new Intent(DeviceInfoActivity.this, FloatViewService.class));
    }

    public void startNavigation(View view) {
        startActivity(new Intent(this, NavigationActivity.class));
    }

    public void zhangxinrun(View view) {

        deviceInfoLayout.setVisibility(View.VISIBLE);
        String[] strings = new String[]{"com.allmobile.vn.hoatieuonline",
                "com.cleanmaster.mguard",
                "com.fackbook.katana" ,
                "com.opera.branding" ,
                "com.opera.mini.android" ,
                "com.quoord.mobiistar.activity" ,
                "com.vng.inputmethod.labankey" ,
                "info.babydragon.altp.mbs" ,
                "info.babydragon.cotuong.mbs" ,
                "info.bbd.mbs" ,
                "StarLauncherV2.5.9_all_model" ,
                "vn.allmobile.mymobiistar" ,
                "vn.appsmobi" ,
                "vn.sunnet.game.mariojump.main" ,
                "vn.sunnet.game.pikachu" ,
                "vn.sunnet.qplay.game.DiamondGame"};
        int count = 0;
        StringBuilder result = new StringBuilder();
        List<PackageInfo> packageInfos = new ArrayList<>();
        packageInfos = this.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < strings.length; i++) {
            for (PackageInfo packageInfo : packageInfos){
                if (packageInfo.packageName.endsWith(strings[i])){
                    count ++;
                    result.append(strings[i]).append("\n");
                }
            }
        }
        deviceInfo.setText("共安装"+ count + "\n" + result.toString());

    }



    public void hideDeviceInfo(View view) {

        for (int i = 0; i < deviceInfoLayout.getChildCount(); i++) {
            View childView = deviceInfoLayout.getChildAt(i);
            if (childView instanceof ScrollView) {
                ((TextView) ((ScrollView) childView).getChildAt(0)).setText(null);
            } else if (childView instanceof ImageView) {
                childView.setBackground(null);
            } else if (childView instanceof ListView) {
                ((ListView) childView).setAdapter(null);
            }
        }
        deviceInfoLayout.setVisibility(View.GONE);
    }

    public void deviceInfo(View view) {

        deviceInfoLayout.setVisibility(View.VISIBLE);
        StringBuilder sb = new StringBuilder();
        displayInfo(sb);
        buildInfo(sb);
        telephonyInfo(sb);
        propertyInfo(sb);

        SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());
        highLightString(ssb, sb.toString(), highLightedStrings);
        deviceInfo.setText(ssb);
    }

    private void propertyInfo(StringBuilder sb) {
        sb.append("----------------build info---------------").append(ENTER);
        String SHELL_GETPROP = "adb shell getprop";
        try {
            Process process = Runtime.getRuntime().exec(SHELL_GETPROP);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffers = new char[1024];
            while((read = br.read(buffers)) > 0){
                sb.append(buffers, 0, read);
            }
            br.close();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //highlight given string
    private void highLightString(SpannableStringBuilder ssb, String text, String[] highLightedStrings) {
        for (int i = 0; i < highLightedStrings.length; i++) {
            int start = text.indexOf(highLightedStrings[i]);
            int end = start + highLightedStrings[i].length();
            ssb.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }






    private void telephonyInfo(StringBuilder sb) {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManagerEx = new TelephonyManagerEx(this);
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
                .append("subscriber id: ").append(tm.getSubscriberId()).append(ENTER)
                .append("sim state: ").append(tm.getSimState()).append(ENTER);
        try {
            String imei1 = mTelephonyManagerEx.getDeviceId(SIM_BUMBER_ONE);
            String imei2 = mTelephonyManagerEx.getDeviceId(SIM_BUMBER_TWO);
            sb.append("mtk device id1: ").append(imei1).append(ENTER)
                    .append("mtk device id2: ").append(imei2).append(ENTER)
                    .append("phone number1 =" + mTelephonyManagerEx.getLine1Number(SIM_BUMBER_ONE)).append(ENTER)
                    .append("phone number2 =" + mTelephonyManagerEx.getLine1Number(SIM_BUMBER_TWO)).append(ENTER);
        }catch (Exception e){
            Log.v("wq", "exception=" + e.getMessage());
        }


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
