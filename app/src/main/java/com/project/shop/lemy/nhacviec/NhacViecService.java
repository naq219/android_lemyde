package com.project.shop.lemy.nhacviec;

import android.Manifest;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lemy.telpoo2lib.utils.SprUtils;
import com.lemy.telpoo2lib.utils.TimeUtils;
import com.project.shop.lemy.R;
import com.project.shop.lemy.chucnangkhac.AdminSettingActivity;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.helper.AllSMSLoader;
import com.project.shop.lemy.helper.FileUtils;
import com.project.shop.lemy.helper.MySpr;
import com.project.shop.lemy.helper.PermissionSupport;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class NhacViecService extends Service {
    public static boolean tagOpenView=false;
    private WindowManager mWindowManager;
    private View view;
    NhacViecViewLayout view1123;
    private int mx,my;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;
    View vgUpdate;
    NhacViecService iContext;
    private Runnable runableUpdate;
    public static long lastCheckSms=0;

    ThanHanhNiem thanHanhNiem;

    @Override
    public void onCreate() {
        iContext= NhacViecService.this;
        thanHanhNiem =new ThanHanhNiem(iContext);
        thanHanhNiem.start();
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }




        TimerTask taskCheckSms = new TimerTask() {
            @Override
            public void run() {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//
//                        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(10);
//                        String s= runningTasks.get(0).topActivity.getPackageName();
//                        Mlog.D("task1 "+s);
//
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                            UsageStatsManager usm = (UsageStatsManager)iContext.getSystemService(Context.USAGE_STATS_SERVICE);
//                            long time = System.currentTimeMillis();
//                            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 10000*10000, time);
//                            if (appList != null && appList.size() == 0) {
//                                Log.d("Executed app ok4 ", "######### NO APP FOUND ##########" );
//                            }
//                            if (appList != null && appList.size() > 0) {
//                                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
//                                for (UsageStats usageStats : appList) {
//                                    Log.d("Executed app", "usage stats executed : " +usageStats.getPackageName() + "\t\t ID: ");
//                                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
//                                }
//                                if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                                    String currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                                    Mlog.D("ok4 "+currentApp);
//
//                                }
//                            }
//                        }
//
//
//
//                        try {
//                            checkSMSBank();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        };

        if (MySpr.isEnableCheckMessage(iContext)){
            new Timer().schedule(taskCheckSms,1000,30*1000);
            showToast("Bắt đầu task đọc sms");
            checkSMSBank();



        }

        if (MySpr.isEnableShowTaskCK(iContext))  initView();


    }




    private void showToast(String msg) {
        Toast.makeText(iContext,msg,Toast.LENGTH_LONG).show();
    }



    private void checkSMSBank() {
        AllSMSLoader.checkSMSBank(this);
    }



    public void initView() {

        view1123 = new NhacViecViewLayout(this,NhacViecService.this);
        view= view1123.getView();

        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,
                PixelFormat.TRANSPARENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindowManager.addView(view,params);

        enableKeyPadInput(false);

        View.OnTouchListener touchview=new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long timepress=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                        timepress= Calendar.getInstance().getTimeInMillis();
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                        return true;
                    case MotionEvent.ACTION_UP:
                        if (Calendar.getInstance().getTimeInMillis()-timepress<200){
                            //old
                            //view.findViewById(R.id.vgOpen).setVisibility(View.VISIBLE);
                            enableKeyPadInput(true);
                        }
                        if (Calendar.getInstance().getTimeInMillis()-timepress>1000
                                &&Math.abs(initialX-params.x)<10&&Math.abs(initialY-params.y)<10){
                            stopSelf();

                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        mx=initialX + (int) (event.getRawX() - initialTouchX);
                        my=initialY + (int) (event.getRawY() - initialTouchY);
                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(view, params);

                        //Mlog.D("hehe "+params.x+"   "+params.y+"   initialX "+initialX+"    "+event.getRawX()+"  "+initialTouchX);

                        return true;


                }
                return false;
            }
        };
        view.findViewById(R.id.root).setOnTouchListener(touchview);



        view.findViewById(R.id.close).setOnClickListener(view1 -> {
            view1123.timeButtonCLose= Calendar.getInstance().getTimeInMillis()+2*60*1000;
            View vgOpen=view.findViewById(R.id.vgOpen);
            if (vgOpen.getVisibility()==View.VISIBLE){
                vgOpen.setVisibility(View.GONE);
                enableKeyPadInput(false);

                Runnable rr= new Runnable() {
                    @Override
                    public void run() {
                        Mlog.D("hehe vào");
                        params = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                LAYOUT_FLAG,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSPARENT);
                        params.x = 10000;
                        params.y = 0;
                        mWindowManager.updateViewLayout(view, params);
                    }
                };
                // new Handler().postDelayed(rr,1500);
                // view.findViewById(R.id.close).setVisibility(View.INVISIBLE);
            }
            else {
                view1123.timeButtonCLose= 0;
                vgOpen.setVisibility(View.VISIBLE);
                enableKeyPadInput(true);
            }

        });


    }

    public void enableKeyPadInput(Boolean enable) {

        if (enable) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSPARENT);

        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }

        params.x = -10000;
        params.y = my;
        mWindowManager.updateViewLayout(view,params);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) mWindowManager.removeView(view);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        //tv.setText("sadasd123123"+new Random().nextInt(1000));
        //String input = ""+intent.getStringExtra("inputExtra");

        createNotificationChannel();

        Notification notification= createNotifi("startcommand");
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    private Notification createNotifi(String contentText) {
        Intent notificationIntent = new Intent(this, NhacViecReiceiver.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,123,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, "nhacviec_chanel")
                .setContentTitle("NhacViec")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.notiyicon)
                .setContentIntent(pendingIntent)

                .build();
        return notification;

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "nhacviec_chanel",
                    "NhacViec",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}