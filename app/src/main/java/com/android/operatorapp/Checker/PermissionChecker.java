package com.android.operatorapp.Checker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionChecker {
    public static final int ASK_PERMISSION_CODE=111;
    public static final String[] NEED_PERMISSIONS=new String[]{
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.PROCESS_OUTGOING_CALLS,
            android.Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PRECISE_PHONE_STATE
    };

    public static void askPermission(Activity activity, String[] permission, int code) {
        ActivityCompat.requestPermissions(activity, permission, code);
    }

    public static boolean checkPermission(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
