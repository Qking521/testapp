package com.example.wangqiang.app;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by wangqiang on 2015/8/20.
 */
public class FloatView extends ImageView{

    WindowManager mWindowManager;
    WindowManager.LayoutParams layoutParams;
    float mStartX;
    float mStartY;
    float mTouchX;
    float mTouchY;
    int mParamsX = 0;
    int mParamsY = 0;

    private Context mContext;
    private OnClickListener mClickListener;

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        initWindowManager();
        registerClickListener();

    }

    private void registerClickListener() {
        mClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        };
        setOnClickListener(mClickListener);
    }

    private void initWindowManager() {
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.x = 0;
        layoutParams.y = 0;
        mWindowManager.addView(this, layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mTouchX = event.getRawX();
        mTouchY = event.getRawY();

        int motion = event.getAction();
        switch (motion){
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mParamsX = layoutParams.x;
                mParamsY = layoutParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                updateWindow();
//                Log.v("wq", "onTouchEvent mRowX="+ mStartX + ", mRowY="+ mStartY + ", mTouchX="+ mTouchX + " ,mTouchY="+ mTouchY);
                break;
            case MotionEvent.ACTION_UP:
                if (isUpdateWindow()){
                    updateWindow();
                }else {
                    performClick();
                }
                break;
        }
        return true;
    }

    private boolean isUpdateWindow() {
        int dx = (int)(mTouchX - mStartX);
        int dy = (int)(mTouchY - mStartY);
        return  (Math.abs(dx) > 5 && Math.abs(dy) > 5);
    }

    private void updateWindow() {
        layoutParams.x = mParamsX + (int)(mTouchX - mStartX);
        layoutParams.y = mParamsY + (int)(mTouchY - mStartY);
        mWindowManager.updateViewLayout(this, layoutParams);
    }

    public void finish() {
        mWindowManager.removeView(this);
    }

}
