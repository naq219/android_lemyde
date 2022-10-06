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

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.utils.TimeUtils;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.nhacviec.NhacViecService;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * nếu keyPhone = abc thì không đọc sms mà chỉ show
 */
public class AllSMSLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    public List<SMS> sms_All = new ArrayList<SMS>();
    private String keyPhone="";
    private static long lastUpdateServiceIsLive=0; //updateLastCheckMoneySv();

    public AllSMSLoader(Context context) {
        this.context = context;
        keyPhone=MySpr.getPhoneBank(context);
    }

    public static void log(String s){
        Mlog.D("task_sms= "+s);
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
        NhacViecService.lastCheckSms= TimeUtils.getTimeMillis();
        boolean forceCheck=false;
        if (TimeUtils.getTimeMillis()- lastUpdateServiceIsLive > 5*60*1000 ){
            lastUpdateServiceIsLive=TimeUtils.getTimeMillis();
            updateLastCheckMoneySv();
            forceCheck=true;

        }

        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            if (phoneNumber==null)
                continue;
            phoneNumber=phoneNumber.toLowerCase();
            int typeSmsLoc = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if (typeSmsLoc!=1) continue;

            //if (!keyPhone.contains(phoneNumber)) continue;

            String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));
            //Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            String date=cursor.getString(cursor.getColumnIndexOrThrow("date"));
            SMS sms = new SMS( phoneNumber, smsContent, 4, date);
            String ck=smsContent.toLowerCase();
            if (ck.contains("123888111 tai vpb tang")||ck.contains("0338783 +")||ck.contains("123888111 tai vpb +")){
                sms.setType(3);
            }
            countLimit++;
            if (countLimit>50) break;
            sms_All.add(sms);
        }



        uploadSms(forceCheck);


    }

    /**
     * telpooupdate list to jsonarray
     * forceCheck
     */
    private void uploadSms(boolean forceCheck) {
        if (sms_All.size()==0) return;

        String tmpCheckDump="1";
        String lastSprSmsCheck= SPRSupport.getString("lastSprSmsCheck",context,"novalue");
        for (int i = 0; i < sms_All.size(); i++) {
            tmpCheckDump+="#="+sms_All.get(i).getSmsContent();
        }


        if (!forceCheck&&tmpCheckDump.equals(lastSprSmsCheck)) {
            log("đã load sms nhưng không có sms mới (check local)");
            return;
        }
        SPRSupport.save("lastSprSmsCheck",tmpCheckDump,context);

        log("request sms server để đối chiếu dữ liệu mới ");

        String sqlCheck="SELECT group_concat(param1) as dates FROM "+BuildConfig.db_nhacviec+".task WHERE param1 IN (%s)";
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

                String sqlInsert="INSERT INTO "+BuildConfig.db_nhacviec+".`task` (`type`, `content`, `param1`, `param2`) VALUES ";
                String dateSv= data.toString();
                StringBuilder childs= new StringBuilder();
                for (SMS sms: sms_All){
                    if (dateSv.contains(sms.getDate())) continue;
                    String child="('%s', '%s', '%s', '%s'),";
                    child= String.format(child,sms.getType(),sms.getSmsContent(),sms.getDate(),sms.getPhoneNumber());
                    childs.append(child);
                }
                if (childs.length()==0) {
                    log("request sms server để đối chiếu dữ liệu mới => không có ");
                    return;
                }
                childs.deleteCharAt(childs.length()-1);

                sqlInsert=sqlInsert+childs.toString();

                log("insert sms "+sqlInsert);
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

        log("updateLastCheckMoneySv "+sqlLastCheck);
        Model model=new Model();
        TaskGeneralTh.exeTaskStatement(context,sqlLastCheck,"sadasd",123123,model);
    }
}