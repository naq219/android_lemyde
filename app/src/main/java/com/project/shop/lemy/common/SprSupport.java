package com.project.shop.lemy.common;

import android.content.Context;

import com.telpoo.frame.utils.SPRSupport;

/**
 * Created by Asus-GL on 10-Jul-18.
 */

public class SprSupport {
    public static final String KEY_LOGPUSHDVVC = "logpushdvvc";
    private static final String IS_DEBUG = "IS_DEBUG";
    private static final String PASS = "PASS";
    private static final String ISCHECK = "ISCHECK";

    public static void saveDebug(Context context, boolean isDebug) {
        SPRSupport.save(IS_DEBUG, isDebug, context);
    }

    public static boolean isDebug(Context context) {
        return SPRSupport.getBool(IS_DEBUG, context, true);
    }

    public static void savePass(Context context, String pass) {
        SPRSupport.save(PASS, pass, context);
    }

    public static String getPass(Context context) {
        return SPRSupport.getString(PASS, context, "");
    }

    public static void saveIsCheck(Context context, Boolean ischeck) {
        SPRSupport.save(ISCHECK, ischeck, context);
    }

    public static Boolean getIscheck(Context context) {
        return SPRSupport.getBool(ISCHECK, context);
    }

    public static boolean isAdmin(Context context){
        if (SprSupport.getPass(context).equals("10801@@@")) return true;
        return false;
    }

    public static String getCodeNv(Context context) {
        return SPRSupport.getString("codenv",context,"nn");
    }
}
