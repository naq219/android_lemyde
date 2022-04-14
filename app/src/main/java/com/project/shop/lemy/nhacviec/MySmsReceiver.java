package com.project.shop.lemy.nhacviec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lemy.telpoo2lib.utils.Mlog;
import com.project.shop.lemy.helper.AllSMSLoader;

public class MySmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AllSMSLoader.checkSMSBank(context);
        Mlog.D("intent sms ->");
    }
}
