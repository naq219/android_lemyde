package com.project.shop.lemy.nhapdon;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.project.shop.lemy.R;
import com.telpoo.frame.utils.Mlog;

import java.util.Calendar;

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View view;
    private int mx,my;
     WindowManager.LayoutParams params;
    int LAYOUT_FLAG;

    public FloatingViewService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
       initView();

    }

    public void resetx(){
        mWindowManager.removeViewImmediate(view);
        //mWindowManager.removeView(view);

        new Handler().postDelayed(() -> {
            initView();
        },500);
    }

    public void initView() {
        view = new ViewNhapDon(FloatingViewService.this).getView();

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



        //Add the view to the window
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
                        //int Xdiff = (int) (event.getRawX() - initialTouchX);
                        //int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Calendar.getInstance().getTimeInMillis()-timepress<200){
                            view.findViewById(R.id.vgOpen).setVisibility(View.VISIBLE);
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
            View vgOpen=view.findViewById(R.id.vgOpen);
            if (vgOpen.getVisibility()==View.VISIBLE){
                vgOpen.setVisibility(View.GONE);

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
                new Handler().postDelayed(rr,1500);
                // view.findViewById(R.id.close).setVisibility(View.INVISIBLE);
            }
            else {
                vgOpen.setVisibility(View.VISIBLE);
                enableKeyPadInput(true);
            }

        });

        view.findViewById(R.id.btnReset).setOnClickListener(view1 -> {
            resetx();
        });
    }



    private void enableKeyPadInput(Boolean enable) {

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

        params.x = mx;
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
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, NhapDonHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "nhapdoncrm_channel")
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher1)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread


        //stopSelf();

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "nhapdoncrm_channel",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}