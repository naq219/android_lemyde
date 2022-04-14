package com.project.shop.lemy.common;

import android.content.Context;

import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.Net.MyUrl;

/**
 * Created by Asus-GL on 10-Jul-18.
 */

public class SettingSupport {
    public static String getBaseUrl(Context context) {
        return BuildConfig.url;
    }

    public static String getUrlHvc(Context context) {
        return BuildConfig.url_vc;
    }
}
