//package com.project.shop.lemy.nhacviec;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.media.MediaPlayer;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.app.TaskStackBuilder;
//
//import com.lemy.telpoo2lib.utils.SprUtils;
//import com.lemy.telpoo2lib.utils.TimeUtils;
//import com.project.shop.lemy.R;
//import com.project.shop.lemy.chucnangkhac.AdminSettingActivity;
//import com.project.shop.lemy.helper.AllSMSLoader;
//import com.project.shop.lemy.helper.FileUtils;
//import com.project.shop.lemy.helper.MySpr;
//import com.telpoo.frame.utils.Mlog;
//import com.telpoo.frame.utils.SPRSupport;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class NhacViecService extends NhacViecService01 {
//    public static boolean tagOpenView=false;
//
//    public static long lastCheckSms=0;
//
//    //thanhanhniem
//    long nextShowTHN=0;
//    String contentTHN="";
//    String[] linesTHN= null;
//
//    long minThn=6000000;
//    long maxThn=800000;
//    public static boolean reloadDataThn=false;
//    @Override
//    public void onCreate() {
//        iContext= NhacViecService.this;
//        super.onCreate();
//
//        loadDataTHN();
//
//
//        TimerTask taskCheckSms = new TimerTask() {
//            @Override
//            public void run() {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//
//                            checkSMSBank();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//
//
//
//
//                    }
//                });
//            }
//        };
//
//        TimerTask taskTHN = new TimerTask() {
//            @Override
//            public void run() {
//                if (reloadDataThn){
//                    loadDataTHN();reloadDataThn=false;
//                }
//                if (nextShowTHN<TimeUtils.getTimeMillis()){
//                    nextShowTHN= TimeUtils.getTimeMillis()+ minThn+ (long) (Math.random() * (maxThn - minThn));
//                    showNotifiTHN();
//                }
//            }
//        };
//
//        if (linesTHN!=null&&linesTHN.length>0) {
//            new Timer().schedule(taskTHN,1000,5*1000);
//        }
//
//
//        if (MySpr.isEnableCheckMessage(iContext)){
//            new Timer().schedule(taskCheckSms,1000,30*1000);
//            showToast("collect sm");
//            checkSMSBank();
//
//
//        }
//
//        if (MySpr.isEnableShowTaskCK(iContext))  initView();
//
//
//
//
//    }
//
//    private void loadDataTHN() {
//        contentTHN="";
//        linesTHN=null;
//        nextShowTHN=0;
//        contentTHN= SPRSupport.getString("thanhanhniem",iContext);
//        if (!contentTHN.equals(SPRSupport.GET_PREFRENCE_FALL)) {
//            if (contentTHN.length()<10) return;
//            //if (contentTHN.contains("#stop")) return;
//
//            try {
//                JSONArray ja = new JSONArray(contentTHN);
//                JSONArray jaEnable= new JSONArray();
//                for (int i = 0; i < ja.length(); i++) {
//                    JSONObject jo = ja.optJSONObject(i);
//                    if (jo.optInt("enable")==1)
//                        jaEnable.put(jo);
//                }
//                if (jaEnable.length()==0) return; //khong có cái nào enable
//                JSONObject joShow=jaEnable.optJSONObject(new Random().nextInt(jaEnable.length()));
//                linesTHN = joShow.optString("content").split("\\r?\\n");
//
//                String minStr= joShow.optString("min","600000").replace("s","000");
//                if (minStr.contains("p")) minStr= ""+(Long.parseLong(minStr.replace("p",""))*60*1000);
//                String maxStr= joShow.optString("max","800000").replace("s","000");
//                if (maxStr.contains("p")) maxStr= ""+(Long.parseLong(maxStr.replace("p",""))*60*1000);
//
//                minThn= Long.parseLong(minStr);
//                maxThn=Long.parseLong(maxStr);
//                showNotifi("p0","min "+minThn+" max "+maxThn);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                showNotifi("Lỗi THN 2123 ",contentTHN);
//                return;
//            }
//
//
//        }
//    }
//
//    private int countTHN=0;
//    private void showNotifiTHN() {
//        countTHN++;
//        if (linesTHN==null||linesTHN.length==0) return;
//        String a1 = linesTHN[new Random().nextInt(linesTHN.length)];
//        String[] ctShow= a1.split("#");
//        if (ctShow.length!=2){
//            //showNotifi("Niệm "+countTHN, "err="+a1);
//            showNotifi("Niệm "+countTHN,a1);
//            return;
//        }
//
//        showNotifi("Niệm "+countTHN, ctShow[1]);
//        if (a1.contains("@")) new Handler().postDelayed(() -> {showNotifi("Niệm "+countTHN, ctShow[1]);},1000);
//
//    }
//
//    private void checkSMSBank() {
//       AllSMSLoader.checkSMSBank(this);
//    }
//
//
//
//}