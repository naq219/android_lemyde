package com.project.shop.lemy.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.utils.SprUtils;
import com.lemy.telpoo2lib.utils.TimeUtils;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.nhacviec.NhacViecServiceLayout;
import com.telpoo.frame.utils.Mlog;

import java.util.ArrayList;
import java.util.List;

public class AllSMSLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    public List<SMS> sms_All = new ArrayList<SMS>();
    private String keyPhone="";

    public AllSMSLoader(Context context) {
        this.context = context;
        keyPhone=MySpr.getPhoneBank(context);
    }

    public static void checkSMSBank(Context context){
        AllSMSLoader allSMSLoader= new AllSMSLoader(context);
        Loader<Cursor> cr = allSMSLoader.onCreateLoader(123, null);
        Loader.OnLoadCompleteListener<Cursor> listener=new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
                allSMSLoader.onLoadFinished(loader,data);
                Mlog.D(allSMSLoader.sms_All.size());

            }

        };
        cr.registerListener(12,listener);
        cr.startLoading();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i11, Bundle bundle) {
        final String SMS_ALL = "content://sms/";
        Uri uri = Uri.parse(SMS_ALL);
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "body", "date", "type"};
        return new CursorLoader(context, uri, projection, null, null, "date desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (keyPhone==null) return;
        List<String> phoneNumbers = new ArrayList<String>();
        int countLimit=0;
        NhacViecServiceLayout.lastCheckSms= TimeUtils.getTimeMillis();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            if (phoneNumber==null)
                continue;
            phoneNumber=phoneNumber.toLowerCase();
            int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if (type!=1) continue;
            if (!keyPhone.contains(phoneNumber)) continue;

            String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));
            //Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            String date=cursor.getString(cursor.getColumnIndexOrThrow("date"));
            SMS sms = new SMS( phoneNumber, smsContent, type, date);
            String ck=smsContent.toLowerCase();
            if (ck.contains("123888111 tai vpb tang")||ck.contains("0338783 +")||ck.contains("123888111 tai vpb +")){
                countLimit++;
                if (countLimit>50) break;
                sms_All.add(sms);
            }

        }
        Mlog.D("asda");

        uploadSms();


    }

    private void uploadSms() {
        if (sms_All.size()==0) return;
        String sqlCheck="SELECT group_concat(param1) as dates FROM "+BuildConfig.db_nhacviec+".task WHERE TYPE=3 AND param1 IN (%s)";
        StringBuilder dates=new StringBuilder();
        for (SMS sms:sms_All){
            dates.append(sms.getDate()+",");
        }

        dates.deleteCharAt(dates.length()-1);

        sqlCheck=String.format(sqlCheck,dates.toString());

        Model modelCheck=new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                super.onSuccess(taskType, data, msg, queue);

                updateLastCheckMoneySv();

                String sqlInsert="INSERT INTO "+BuildConfig.db_nhacviec+".`task` (`type`, `content`, `param1`, `param2`) VALUES ";
                String dateSv= data.toString();
                StringBuilder childs= new StringBuilder();
                for (SMS sms: sms_All){
                    if (dateSv.contains(sms.getDate())) continue;
                    String child="('3', '%s', '%s', '%s'),";
                    child= String.format(child,sms.getSmsContent(),sms.getDate(),sms.getPhoneNumber());
                    childs.append(child);
                }
                if (childs.length()==0) return;
                childs.deleteCharAt(childs.length()-1);

                sqlInsert=sqlInsert+childs.toString();

                TaskGeneralTh.exeTaskStatement(context,sqlInsert,"sda2",12312,new Model());


            }

        };
        TaskGeneralTh.exeTaskGetApi(modelCheck,context,sqlCheck);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //simpleCursorAdapter.swapCursor(null);
    }


    private void updateLastCheckMoneySv() {
        //long timeCheck= MySqr.getLong(MySqr.KEY_LASTCHECKMONEY,context,-1l);
        String sqlLastCheck="UPDATE "+BuildConfig.db_nhacviec+".`checkservice` SET `lastrun_collect_money`= "+ TimeUtils.getTimeMillis() /1000;
        Model model=new Model();
        TaskGeneralTh.exeTaskStatement(context,sqlLastCheck,"sadasd",123123,model);
    }
}