package com.example.wangqiang.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TableRow;

import com.example.wangqiang.activitys.DeviceInfoActivity;
import com.example.wangqiang.app.R;

/**
 * Created by wangqiang on 2017/3/20.
 */

public class PermissionCheck {

    public static final int PERMISSION_REQUSTCODE_CALENDAR = 0;
    public static final int PERMISSION_REQUSTCODE_CAMERA = 1;
    public static final int PERMISSION_REQUSTCODE_CONTACTS = 2;
    public static final int PERMISSION_REQUSTCODE_LOCATION = 3;
    public static final int PERMISSION_REQUSTCODE_MICROPHONE = 4;
    public static final int PERMISSION_REQUSTCODE_PHONE = 5;
    public static final int PERMISSION_REQUSTCODE_SENSORS = 6;
    public static final int PERMISSION_REQUSTCODE_SMS = 7;
    public static final int PERMISSION_REQUSTCODE_STORAGE = 8;
    public static final int PERMISSION_REQUSTCODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    public static final String PERMISSION_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    public static final String PERMISSION_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    public static final String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public static final String PERMISSION_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static boolean RESULT_REQUSTCODE_CALENDAR = false;
    public static boolean RESULT_REQUSTCODE_CAMERA = false;
    public static boolean RESULT_REQUSTCODE_CONTACTS = false;
    public static boolean RESULT_REQUSTCODE_LOCATION = false;
    public static boolean RESULT_REQUSTCODE_MICROPHONE = false;
    public static boolean RESULT_REQUSTCODE_PHONE = false;
    public static boolean RESULT_REQUSTCODE_SENSORS = false;
    public static boolean RESULT_REQUSTCODE_SMS = false;
    public static boolean RESULT_REQUSTCODE_STORAGE = false;
    public static boolean RESULT_REQUSTCODE_MULTI_PERMISSION = false;

    public static boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void shouldShowRequestPermissionRationale(final Activity activity, String permission, String rationale){
        if (!activity.shouldShowRequestPermissionRationale(permission)){
            ShowRequestPermissionRationaleDialog(activity, rationale, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + activity.getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    activity.startActivity(i);
                }
            });
        }
    }

    public static void requsetPermission(final Activity activity, final String[] permissions, final int requestCode) {
        activity.requestPermissions(permissions, requestCode);
    }

    private static void ShowRequestPermissionRationaleDialog(Context context, String msg,
                                                             DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.permission_rational_title))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, onClickListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    public static void onRequestPermissionResultCallback(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length > 0) {
            switch (requestCode) {
                case PERMISSION_REQUSTCODE_CALENDAR:
                case PERMISSION_REQUSTCODE_CAMERA:
                case PERMISSION_REQUSTCODE_CONTACTS:
                case PERMISSION_REQUSTCODE_LOCATION:
                case PERMISSION_REQUSTCODE_MICROPHONE:
                case PERMISSION_REQUSTCODE_PHONE:
                case PERMISSION_REQUSTCODE_SENSORS:
                case PERMISSION_REQUSTCODE_SMS:
                case PERMISSION_REQUSTCODE_STORAGE:
                    setResult(permissions[0], grantResults[0]);
                    break;

                case PERMISSION_REQUSTCODE_MULTI_PERMISSION:
                    for (int i = 0; i < permissions.length; i++) {
                        setResult(permissions[i], grantResults[i]);
                    }
                default:
                    break;
            }
        }

    }

    private static void setResult(String permission, int grantResult) {
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            switch (permission) {
                case Manifest.permission.READ_CALENDAR:
                case Manifest.permission.WRITE_CALENDAR:
                    RESULT_REQUSTCODE_CALENDAR = true;
                    break;
                case Manifest.permission.CAMERA:
                    RESULT_REQUSTCODE_CAMERA = true;
                    break;
                case Manifest.permission.READ_CONTACTS:
                case Manifest.permission.WRITE_CONTACTS:
                case Manifest.permission.GET_ACCOUNTS:
                    RESULT_REQUSTCODE_CONTACTS = true;
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                    RESULT_REQUSTCODE_LOCATION = true;
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    RESULT_REQUSTCODE_MICROPHONE = true;
                    break;
                case Manifest.permission.READ_PHONE_STATE:
                case Manifest.permission.CALL_PHONE:
                case Manifest.permission.READ_CALL_LOG:
                case Manifest.permission.WRITE_CALL_LOG:
                case Manifest.permission.ADD_VOICEMAIL:
                case Manifest.permission.USE_SIP:
                case Manifest.permission.PROCESS_OUTGOING_CALLS:
                    RESULT_REQUSTCODE_PHONE = true;
                    break;
                case Manifest.permission.BODY_SENSORS:
                    RESULT_REQUSTCODE_SENSORS = true;
                    break;
                case Manifest.permission.SEND_SMS:
                case Manifest.permission.RECEIVE_SMS:
                case Manifest.permission.READ_SMS:
                case Manifest.permission.RECEIVE_WAP_PUSH:
                case Manifest.permission.RECEIVE_MMS:
                    RESULT_REQUSTCODE_SMS = true;
                    break;
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    RESULT_REQUSTCODE_STORAGE = true;
                    break;
                default:
                    break;

            }

        }
    }
}
