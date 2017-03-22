package com.example.wangqiang.activitys;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.wangqiang.app.R;
import com.example.wangqiang.util.PermissionCheck;

import java.util.Arrays;

/**
 * Created by wangqiang on 2015/10/27.
 */
public class CameraActivity extends Activity {

    private Switch mCameraSwitch;
    private Switch mTorchSwitch;
    boolean isTorchOn;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Handler mHandler;
    //obtain preview data
    private ImageReader mImageReader;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCaptureSession;

    private static String CAMERA_DEFAULT_ID = "0";
    private static String CAMERA_FRONT_ID = "1";
    private static String CAMERA_BACK_ID = CAMERA_DEFAULT_ID;

    private String mCurrentCameraId = CAMERA_DEFAULT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        initViews();
        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mHolderCallback);
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());


    }

    private void initViews() {
        mCameraSwitch = (Switch) findViewById(R.id.camera_switch);
        mTorchSwitch = (Switch)findViewById(R.id.torch_switch);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mCameraSwitch.setOnCheckedChangeListener(mCameraCheckedChangeListener);
        mTorchSwitch.setOnCheckedChangeListener(mTorchOnCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener mCameraCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.v("wq", "mCameraCheckedChangeListener isChecked=" + isChecked);
            mCurrentCameraId = mCurrentCameraId == CAMERA_BACK_ID ? CAMERA_FRONT_ID : CAMERA_BACK_ID;
            buttonView.setText(mCurrentCameraId == CAMERA_BACK_ID ? "back" : "front");
            if (mCameraDevice != null) {
                mCameraDevice.close();
            }
            openCamera();
        }
    };

    private CompoundButton.OnCheckedChangeListener mTorchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    private SurfaceHolder.Callback mHolderCallback = new SurfaceHolder.Callback(){
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.v("wq", "surfaceCreated");
            if (!PermissionCheck.isPermissionGranted(CameraActivity.this, PermissionCheck.PERMISSION_CAMERA)) {
                PermissionCheck.requsetPermission(CameraActivity.this, new String[]{PermissionCheck.PERMISSION_CAMERA},
                        PermissionCheck.PERMISSION_REQUSTCODE_CAMERA);
            } else {
                openCamera();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private void openCamera() {
        Log.v("wq", "openCamera");
//        mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(),
//                ImageFormat.JPEG, 2);
//        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mHandler);
        try{
            mCameraManager.openCamera(mCurrentCameraId, mDeviceStateCallback, mHandler);
        }catch (SecurityException se){

        }catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraDevice != null) {
            Log.v("wq", "onStop cameraId=" + mCameraDevice.getId());
            mCameraDevice.close();
        }
        if (mCameraCaptureSession != null) {
            Log.v("wq", "mCameraCaptureSession");
            mCameraCaptureSession.close();

        }
    }

    private void openCameraTorch() {
        isTorchOn = true;
    }

    private void closeCameraTorch() {
        isTorchOn = false;
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

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

        }
    };


    private CameraDevice.StateCallback mDeviceStateCallback = new CameraDevice.StateCallback(){
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.v("wq", "onOpened cameraId="+ camera.getId());
            try {
                mCameraDevice = camera;
                mCaptureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mCaptureRequestBuilder.addTarget(mSurfaceHolder.getSurface());
                camera.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface()),
                                            mSessionStateCallback, mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraDevice = null;
            camera.close();
            Log.v("wq", "onDisconnected cameraId=" + camera.getId());
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.v("wq", "onError error="+ error);
            mCameraDevice = null;
            camera.close();

        }
    };

    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.v("wq", "onConfigured");
//            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
//                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            try {
                mCameraCaptureSession = session;
                session.setRepeatingRequest(mCaptureRequestBuilder.build(), mSessionCaptureCallback, mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.v("wq", "CameraAccessException="+ e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.v("wq", "onConfigureFailed");
        }
    };

    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.onRequestPermissionResultCallback(requestCode, permissions, grantResults);
        if (PermissionCheck.RESULT_REQUSTCODE_CAMERA) {
            openCamera();
        } else {
            PermissionCheck.shouldShowRequestPermissionRationale(this, PermissionCheck.PERMISSION_CAMERA,
                    getString(R.string.permission_rational_message));
        }
    }

}
