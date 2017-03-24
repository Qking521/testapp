package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wangqiang.app.R;
import com.example.wangqiang.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    Map<String, Class> mMainListData = new HashMap<String, Class>();
    ListView mMainListView;
    private static final int PRESS_INTERVAL = 3000;
    private static long BackPressedTime = 0;
    public static String TAG = "wq";

    private String[] mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG", "onCreate");
        setContentView(R.layout.activity_main);

        init();
        initViews();
    }

    private void initViews() {
        mMainListView = (ListView)findViewById(R.id.main_listview);
        mMainListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getKey()));
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = (String) parent.getAdapter().getItem(position);
                Class clazz = mMainListData.get(key);
                Intent intent = new Intent(MainActivity.this, clazz);
                intent.putExtra("current_key", key);
                startActivity(intent);

            }
        });
    }

    private void init() {
        mMainListData.put(getString(R.string.main_data_torch), CameraActivity.class);
        mMainListData.put(getString(R.string.main_data_file_manager), FileManager.class);
        mMainListData.put(getString(R.string.main_data_app_manager), AppsManager.class);
        mMainListData.put(getString(R.string.main_data_device_info), DeviceInfoActivity.class);


    }

    public List<String> getKey() {

        List<String> keyList = new ArrayList<>();
        Iterator<String> it = mMainListData.keySet().iterator();
        while (it.hasNext()){
            keyList.add(it.next());
        }

        return keyList;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - BackPressedTime < PRESS_INTERVAL){
            super.onBackPressed();
        }else {
            Utils.showToast(this, "Press once again to exit");
            BackPressedTime = System.currentTimeMillis();
        }

    }
}
