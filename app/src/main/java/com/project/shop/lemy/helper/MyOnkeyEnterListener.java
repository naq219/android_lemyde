package com.project.shop.lemy.helper;

import android.view.KeyEvent;
import android.view.View;

import com.project.shop.lemy.listener.ListenBack;

public class MyOnkeyEnterListener implements View.OnKeyListener{

    private ListenBack listenBack;

    public MyOnkeyEnterListener(ListenBack listenBack){
        this.listenBack = listenBack;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

           listenBack.OnListenBack(true);
            return true;
        }
        return false;
    }
}
