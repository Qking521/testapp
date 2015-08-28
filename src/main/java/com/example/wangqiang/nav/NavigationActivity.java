package com.example.wangqiang.nav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wangqiang.app.MainActivity;
import com.example.wangqiang.app.R;
import com.example.wangqiang.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2015/8/26.
 */
public class NavigationActivity extends Activity {

    static  final int PAGER_COUNT = 4;
    private int mCurrentPosition = 0;
    private int mLastPosition = 0;

    private List<View> mViewList = new ArrayList<View>();

    ViewPager mViewPager;
    private ViewPagerAdapder mPagerAdapter;
    LinearLayout mIndicatorGroup;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_main);
        mViewPager = (ViewPager)findViewById(R.id.nav_viewpager);
        mIndicatorGroup = (LinearLayout)findViewById(R.id.nav_indicator_group);
        mStartButton = (Button)findViewById(R.id.nav_start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                Utils.setNavigation(NavigationActivity.this, false);
                finish();
            }
        });
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i=0; i<PAGER_COUNT; i++){
            mViewList.add(inflater.inflate(R.layout.nav_viewpager_item, null));
            mIndicatorGroup.addView(createIndicator(i));
        }

        mPagerAdapter = new ViewPagerAdapder();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
    }

    private View createIndicator(int i) {
        ImageView indicator = new ImageView(this);
        if (i == 0){
            indicator.setBackground(getDrawable(R.drawable.nav_indicator_focus));
        }else {
            indicator.setBackground(getDrawable(R.drawable.nav_indicator_nomal));
        }
        return  indicator;
    }


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mLastPosition = mCurrentPosition;
            mCurrentPosition = position;
            mIndicatorGroup.getChildAt(mCurrentPosition).setBackground(getDrawable(R.drawable.nav_indicator_focus));
            mIndicatorGroup.getChildAt(mLastPosition).setBackground(getDrawable(R.drawable.nav_indicator_nomal));
            if (position == (PAGER_COUNT - 1)){
                mStartButton.setVisibility(View.VISIBLE);
            }else {
                mStartButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    class ViewPagerAdapder extends PagerAdapter{
        @Override
        public int getCount() {
            return PAGER_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }
}
