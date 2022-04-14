package com.project.shop.lemy.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.project.shop.lemy.listener.ListenBack;

/**
 * Created by Ducqv on 9/5/2018.
 */

public class Keyboard {

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void addKeyboardVisibilityListener(View rootLayout, OnKeyboardVisibiltyListener onKeyboardVisibiltyListener) {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootLayout.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootLayout.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            boolean isVisible = keypadHeight > screenHeight * 0.15; // 0.15 ratio is perhaps enough to determine keypad height.
            onKeyboardVisibiltyListener.onVisibilityChange(isVisible);
        });
    }

    public interface OnKeyboardVisibiltyListener {
        void onVisibilityChange(boolean isVisible);
    }

    public static void keyEnterListener(EditText ed,boolean continuesShow, ListenBack listenBack){

        ed.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                listenBack.OnListenBack("");
                if (continuesShow){
                    ed.postDelayed(() -> {
                        ed.requestFocus();
                    }, 100);
                }


                return true;
            }
            return false;
        });
    }
}
