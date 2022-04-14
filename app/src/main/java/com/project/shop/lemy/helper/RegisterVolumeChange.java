package com.project.shop.lemy.helper;

import android.database.ContentObserver;
import android.os.Handler;

import com.project.shop.lemy.listener.ListenBack;


/**
 * Created by naq on 11/15/18.
 */

public class RegisterVolumeChange extends ContentObserver {

    private ListenBack listenBack;

    public RegisterVolumeChange(Handler handler, ListenBack listenBack) {
        super(handler);
        this.listenBack = listenBack;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        listenBack.OnListenBack("");

    }
}