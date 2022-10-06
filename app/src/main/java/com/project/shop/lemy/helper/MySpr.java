package com.project.shop.lemy.helper;

import android.Manifest;
import android.content.Context;

import com.lemy.telpoo2lib.utils.SprUtils;
import com.telpoo.frame.utils.SPRSupport;

public class MySpr extends SprUtils {
//hhh

    public static void savePhoneBank(String value, Context context){
        SprUtils.saveString("keyphonebank",value,context);
    }

    public static String getPhoneBank(Context context){
        return  getString("keyphonebank",context);
    }
    public static boolean isEnableCheckMessage(Context context){
        String smsfilter= SprUtils.getString("keyphonebank",context);
        if("abc".equals(smsfilter)) return false;
        if (smsfilter!=null&&smsfilter.length()>0&&PermissionSupport.hasPermissionGranted(context, Manifest.permission.READ_SMS))
            return true;
        return false;
    }


    public static boolean isVietCombank(Context context){
        String smsfilter= SprUtils.getString("keyphonebank",context);

        if (smsfilter!=null&&smsfilter.length()>0&&smsfilter.toLowerCase().contains("vietcombank")) return true;
        return false;
    }

    public static boolean isEnableShowTaskCK(Context context){
        return SPRSupport.getBool("show_tienck",context,false);
//        String smsfilter= SprUtils.getString("keyphonebank",context);
//
//        if (smsfilter!=null&&smsfilter.length()>0&&smsfilter.toLowerCase().contains("vietcom1")) return false;
//
//        if (smsfilter!=null&&smsfilter.length()>0)
//            return true;
//
//        return false;
    }
}
