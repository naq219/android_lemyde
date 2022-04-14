package com.project.shop.lemy.common;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.listener.ListenBack;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ducqv on 8/30/2018.
 */

public class PoupMenuSupport {

    public static void showMenu(Context context, TextView textView, BaseObject objectMenu, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        showMenu(context, textView, objectMenu, objectMenu.getKeys(), onPoupMenuItemClick);
    }

    public static void showMenu(Context context, TextView textView, BaseObject objectMenu,boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!BaseNetSupport.isNetworkAvailable(context)) return;
        if (objectMenu==null||context==null||textView==null) return;
        showMenu(context, textView, objectMenu, objectMenu.getKeys(),show, onPoupMenuItemClick);
    }

    public static void showMenu(Context context, TextView textView, BaseObject objectMenu, ArrayList<String> keys,boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        PopupMenu popupMenu = new PopupMenu(context, textView);
        for (int i = 0; i < objectMenu.getKeys().size(); i++) {
            popupMenu.getMenu().add(1, i, i, keys.get(i));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String key = keys.get(menuItem.getOrder());
            onPoupMenuItemClick.onItemClick(objectMenu.get(key));
            textView.setText(key);
            return false;
        });
        if (show)popupMenu.show();
    }

    public static void showMenuFromOj(Context context, TextView textView, List<BaseObject> ojs, String field_oj, Listerner.callBack listerner) {
        PopupMenu popupMenu = new PopupMenu(context, textView);
        for (int i = 0; i < ojs.size(); i++) {
            popupMenu.getMenu().add(1, i, i, ojs.get(i).get(field_oj,"Không có dữ liệu"));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String key = ojs.get(menuItem.getOrder()).get(field_oj);
            if (listerner!=null)
            listerner.onCallBackListerner(ojs.get(menuItem.getOrder()),key,null);
            textView.setText(key);
            return false;
        });

        popupMenu.show();

    }

    public static void showMenu(Context context, TextView textView, BaseObject objectMenu, ArrayList<String> keys, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        PopupMenu popupMenu = new PopupMenu(context, textView);
        for (int i = 0; i < objectMenu.getKeys().size(); i++) {
            popupMenu.getMenu().add(1, i, i, keys.get(i));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String key = keys.get(menuItem.getOrder());
            onPoupMenuItemClick.onItemClick(objectMenu.get(key));
            textView.setText(key);
            return false;
        });
        popupMenu.show();
    }

    public static void showMenuValue(Context context, TextView textView, BaseObject objectMenu, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        PopupMenu popupMenu = new PopupMenu(context, textView);
        ArrayList<String> keys=objectMenu.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            popupMenu.getMenu().add(1, i, i, objectMenu.get(keys.get(i)));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String key = keys.get(menuItem.getOrder());
            onPoupMenuItemClick.onItemClick(key);
            textView.setText(objectMenu.get(key));
            return false;
        });
        popupMenu.show();
    }

    public static void showSearchMenu(SearchableSpinner sp ,String title, Context context, String[] listData, ListenBack listenBack){
        sp.setTitle(title);
        ArrayAdapter<String> adapterCTV = new ArrayAdapter<String>(context, R.layout.spinner_item_15sp, listData);
        sp.setAdapter(adapterCTV);
        sp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Keyboard.hideKeyboard((Activity) context);
                    }
                },300);
                return false;
            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listenBack.OnListenBack(listData[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
