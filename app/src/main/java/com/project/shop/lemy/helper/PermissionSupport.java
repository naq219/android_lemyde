package com.project.shop.lemy.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.Log;

/**
 * Created by SonLv on 11/27/2016.
 */
public class PermissionSupport {

    public static final int READ_PERMISSIONS_CAMERA = 0;
    public static final int READ_PERMISSIONS_STORAGE = 1;
    private static Activity activity;
    private static PermissionSupport support;


    public PermissionSupport(Activity activity) {
        this.activity = activity;
    }

    public static PermissionSupport getInstall(Activity activity) {
        if (support == null || activity != support.activity)
            support = new PermissionSupport(activity);
        return support;
    }

    private static Boolean hasPermissionGranted(String permission) {
        Boolean status = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        Log.d("PermissionSupport", "hasPermissionGranted1: " + permission + ": " + status);
        return status;
    }

    public static Boolean hasPermissionGranted(Context context,String permission) {
        Boolean status = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        Log.d("PermissionSupport", "hasPermissionGranted2: " + permission + ": " + status);
        return status;
    }


    public static Boolean requestPermissionCamera() {
        if (hasPermissionGranted(Manifest.permission.CAMERA)) return true;
        if (Build.VERSION.SDK_INT < 23) return true;

        activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                READ_PERMISSIONS_CAMERA);
        return false;
    }

    public static Boolean requestPermissionStore() {
        if (hasPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) return true;
        if (Build.VERSION.SDK_INT < 23) return true;

        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                READ_PERMISSIONS_STORAGE);
        return false;
    }




}
