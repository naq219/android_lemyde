package com.project.shop.lemy.nhacviec;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.lemy.telpoo2lib.utils.SprUtils;
import com.project.shop.lemy.R;
import com.project.shop.lemy.chucnangkhac.AdminSettingActivity;
import com.project.shop.lemy.helper.FileUtils;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NhacViecService01 extends Service {
    NhacViecService iContext;
    long lastRunPhap=0;

    private int mx,my;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;

    private WindowManager mWindowManager;
    protected View view;
    protected NhacViecViewLayout view1123;

    protected View vgUpdate;

    private Runnable runableUpdate;

    @Override
    public void onCreate() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        super.onCreate();
    }

    protected void runPhatNhacPhatPhap() {
        long chuong= SprUtils.getLong("edChuong",iContext,-1l);
        if (chuong>0){
            TimerTask taskChuong= new TimerTask() {
                @Override
                public void run() {
                    baoChuong();
                }
            };

            new Timer().schedule(taskChuong,1000,chuong*60*1000);
            baoChuong();

        }
        if(SPRSupport.getBool(AdminSettingActivity.SPR_ENABLE_RUN,iContext,false)){
            // chạy phát âm thanh đoạn Pháp ngắn.
            TimerTask taskPhap = new TimerTask() {
                @Override
                public void run() {
                    if (lastRunPhap> Calendar.getInstance().getTimeInMillis()) return;

                    int ppStart= SPRSupport.getAsInt(AdminSettingActivity.SPR_DELAY_START,iContext);
                    int ppEnd= SPRSupport.getAsInt(AdminSettingActivity.SPR_DELAY_END,iContext);
                    String ppLocation = SPRSupport.getString(AdminSettingActivity.SPR_LOCATION_PHAP,iContext,null);
                    if (ppStart<0 ||ppEnd<0 || ppStart> ppEnd||ppLocation==null|| ppLocation.length()<3) return;

                    List<String> listFile = new FileUtils().listFile(ppLocation);
                    int nextRun = 60*1000*(ppStart + new Random().nextInt(ppEnd - ppStart));

                    lastRunPhap= Calendar.getInstance().getTimeInMillis()+nextRun;
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        MediaPlayer mp = new MediaPlayer();

                        try {
                            mp.setDataSource(ppLocation + File.separator + listFile.get(new Random().nextInt(listFile.size())));
                            mp.prepare();
                            mp.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    },nextRun);

                }
            };

            new Timer().schedule(taskPhap,1000,30*1000);

        }
    }

    protected void baoChuong() {
        MediaPlayer mediaPlayer = MediaPlayer.create(iContext, R.raw.dekeu);
        mediaPlayer.start();
    }

    protected void showToast(String msg) {
        Toast.makeText(iContext,msg,Toast.LENGTH_LONG).show();
    }

    public void initView() {
        Mlog.D("initView");
        view1123 = new NhacViecViewLayout(this, iContext);
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
        //createNotificationChannel();



        Notification notification= tmpcreateNotifi("startcommand");
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    protected Notification tmpcreateNotifi(String contentText) {
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

    protected void showNotifi(String title, String content) {
        Intent notificationIntent = new Intent(this, NhacViecReiceiver.THNReceiver.class);
        // PendingIntent pendingIntent =PendingIntent.getBroadcast(this,123,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(this, ThanhHanhNiemActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification111 = new NotificationCompat.Builder(this, "thanhanhniem")
                .setContentTitle(title)
                .setContentText(content)

                .setSmallIcon(R.drawable.notiyicon);
        notification111.setContentIntent(resultPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = getString(R.string.ghichu);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("thanhanhniem", "thn", importance);
            channel.setDescription(description);
            channel.setSound(null,null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(44, notification111.build());
        }
    }
}
