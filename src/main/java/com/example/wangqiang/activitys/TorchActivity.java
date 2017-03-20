package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.wangqiang.app.R;

/**
 * Created by wangqiang on 2015/10/27.
 */
public class TorchActivity extends Activity {

    Button mBtnOn;
    Button mBtnOff;
    Camera mCamera;
    boolean isTorchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.torch);
        initCamera();
        initViews();


    }

    private void initCamera() {
        if (mCamera == null){
            mCamera = Camera.open();
        }

    }

    private void initViews() {
        mBtnOff = (Button)findViewById(R.id.torch_off);
        mBtnOn = (Button)findViewById(R.id.torch_on);
        mBtnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCameraTorch();
            }
        });
        mBtnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraTorch();
            }
        });
    }

    private void openCameraTorch() {
        isTorchOn = true;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameters);
    }

    private void closeCameraTorch() {
        isTorchOn = false;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
        mCamera.release();
    }

    //simulate home key
    @Override
    public void onBackPressed() {
        if (isTorchOn){
            moveTaskToBack(true);
        }else {
            super.onBackPressed();
        }

    }

}
