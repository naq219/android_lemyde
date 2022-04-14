package com.project.shop.lemy.common;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public class ScreenSupport {

    public static void BlockScreen(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
