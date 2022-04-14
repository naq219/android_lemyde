package com.project.shop.lemy.nhacviec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NhacViecReiceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"asd",Toast.LENGTH_LONG).show();
        NhacViecService.tagOpenView=true;
    }
}
