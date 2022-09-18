//package com.project.shop.lemy.nhacviec;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.app.TaskStackBuilder;
//
//import com.lemy.telpoo2lib.utils.TimeUtils;
//import com.project.shop.lemy.R;
//import com.project.shop.lemy.helper.AllSMSLoader;
//import com.project.shop.lemy.helper.MySpr;
//import com.telpoo.frame.utils.Mlog;
//import com.telpoo.frame.utils.SPRSupport;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Calendar;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class NhacViecServicecopy extends NhacViecService01 {
//    public static boolean tagOpenView=false;
//    private WindowManager mWindowManager;
//    private View view;
//    NhacViecViewLayout view1123;
//
//    View vgUpdate;
//
//    private Runnable runableUpdate;
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
//
//        super.onCreate();
//
//        loadDataTHN();
//
//
//
//
//        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(NhacViecServicecopy.this, "Chanh Niem")
//                .setSmallIcon(R.drawable.notiyicon)
//                .setContentTitle("Đây là Title")
//                .setContentText(" nội dung của bạn là gì ")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NhacViecServicecopy.this);
//
//// notificationId is a unique int for each notification that you must define
//        notificationManager.notify(44, builder1.build());
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
//                        if (nextShowTHN<TimeUtils.getTimeMillis()){
//
//                            nextShowTHN= TimeUtils.getTimeMillis()+ minThn+ (long) (Math.random() * (maxThn - minThn));
//                            showNotifiTHN();
//                        }
//                    }
//                });
//            }
//        };
//
//        if (MySpr.isEnableCheckMessage(iContext)){
//            new Timer().schedule(taskCheckSms,1000,30*1000);
//            showToast("Bắt đầu task đọc sms");
//            checkSMSBank();
//
//
//        }
//
//        if (MySpr.isEnableShowTaskCK(iContext))  initView();
//
//
//        //runPhatNhacPhatPhap();
//
//        showNotifi("sd","ds");
//
//
//    }
//
//    private void loadDataTHN() {
//        contentTHN="";
//        linesTHN=null;
//
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
//        int hourDay=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//
//        if (hourDay<7&&hourDay>=0)return;
//        if (hourDay>21 ) return;
//
//        countTHN++;
//        //if (limitTHN>5) return;
//
//        if (linesTHN==null||linesTHN.length==0) return;
//
//
//        String a1 = linesTHN[new Random().nextInt(linesTHN.length)];
//        String[] ctShow= a1.split("#");
//        if (ctShow.length!=2){
//            //showNotifi("Niệm "+countTHN, "err="+a1);
//            showNotifi("Niệm "+countTHN,a1);
//            return;
//        }
//
//        showNotifi("Niệm "+countTHN, ctShow[1]);
//
//
//    }
//
//
//
//
//
//    private void checkSMSBank() {
//       AllSMSLoader.checkSMSBank(this);
//    }
//
//
//
//    public void initView() {
//
//         view1123 = new NhacViecViewLayout(this, NhacViecServicecopy.this);
//        view= view1123.getView();
//
//        //Add the view to the window.
//        params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                LAYOUT_FLAG,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,
//                PixelFormat.TRANSPARENT);
//
//        //Specify the view position
//        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
//        params.x = 0;
//        params.y = 100;
//        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        mWindowManager.addView(view,params);
//
//        enableKeyPadInput(false);
//
//        View.OnTouchListener touchview=new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//            private long timepress=0;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//
//
//                    case MotionEvent.ACTION_DOWN:
//                        timepress= Calendar.getInstance().getTimeInMillis();
//                        //remember the initial position.
//                        initialX = params.x;
//                        initialY = params.y;
//
//                        //get the touch location
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//
//
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        if (Calendar.getInstance().getTimeInMillis()-timepress<200){
//                            //old
//                            //view.findViewById(R.id.vgOpen).setVisibility(View.VISIBLE);
//                            enableKeyPadInput(true);
//                        }
//                        if (Calendar.getInstance().getTimeInMillis()-timepress>1000
//                                &&Math.abs(initialX-params.x)<10&&Math.abs(initialY-params.y)<10){
//                            stopSelf();
//
//                        }
//
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        //Calculate the X and Y coordinates of the view.
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//
//                        mx=initialX + (int) (event.getRawX() - initialTouchX);
//                        my=initialY + (int) (event.getRawY() - initialTouchY);
//                        //Update the layout with new X & Y coordinate
//                        mWindowManager.updateViewLayout(view, params);
//
//                        //Mlog.D("hehe "+params.x+"   "+params.y+"   initialX "+initialX+"    "+event.getRawX()+"  "+initialTouchX);
//
//                        return true;
//
//
//                }
//                return false;
//            }
//        };
//        view.findViewById(R.id.root).setOnTouchListener(touchview);
//
//
//
//        view.findViewById(R.id.close).setOnClickListener(view1 -> {
//            view1123.timeButtonCLose= Calendar.getInstance().getTimeInMillis()+2*60*1000;
//            View vgOpen=view.findViewById(R.id.vgOpen);
//            if (vgOpen.getVisibility()==View.VISIBLE){
//                vgOpen.setVisibility(View.GONE);
//                enableKeyPadInput(false);
//
//                Runnable rr= new Runnable() {
//                    @Override
//                    public void run() {
//                        Mlog.D("hehe vào");
//                        params = new WindowManager.LayoutParams(
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                WindowManager.LayoutParams.WRAP_CONTENT,
//                                LAYOUT_FLAG,
//                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                                PixelFormat.TRANSPARENT);
//                        params.x = 10000;
//                        params.y = 0;
//                        mWindowManager.updateViewLayout(view, params);
//                    }
//                };
//               // new Handler().postDelayed(rr,1500);
//                // view.findViewById(R.id.close).setVisibility(View.INVISIBLE);
//            }
//            else {
//                view1123.timeButtonCLose= 0;
//                vgOpen.setVisibility(View.VISIBLE);
//                enableKeyPadInput(true);
//            }
//
//        });
//
//
//    }
//
//    public void enableKeyPadInput(Boolean enable) {
//
//        if (enable) {
//            params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    LAYOUT_FLAG,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                    PixelFormat.TRANSPARENT);
//
//        } else {
//            params = new WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    LAYOUT_FLAG,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSPARENT);
//        }
//
//        params.x = -10000;
//        params.y = my;
//        mWindowManager.updateViewLayout(view,params);
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (view != null) mWindowManager.removeView(view);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//       //
//       //tv.setText("sadasd123123"+new Random().nextInt(1000));
//        //String input = ""+intent.getStringExtra("inputExtra");
//        //createNotificationChannel();
//
//
//
//        Notification notification= createNotifi("startcommand");
//        startForeground(1, notification);
//
//        return START_NOT_STICKY;
//    }
//
//    private Notification createNotifi(String contentText) {
//        Intent notificationIntent = new Intent(this, NhacViecReiceiver.class);
//        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,123,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this, "nhacviec_chanel")
//                .setContentTitle("NhacViec")
//                .setContentText(contentText)
//                .setSmallIcon(R.drawable.notiyicon)
//                .setContentIntent(pendingIntent)
//
//                .build();
//        return notification;
//
//    }
//
//    private void showNotifi(String title, String content) {
//
//
//        Intent notificationIntent = new Intent(this, NhacViecReiceiver.THNReceiver.class);
//       // PendingIntent pendingIntent =PendingIntent.getBroadcast(this,123,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent resultIntent = new Intent(this, ThanhHanhNiemActivity.class);
//// Create the TaskStackBuilder and add the intent, which inflates the back stack
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(resultIntent);
//// Get the PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(0,
//                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder notification111 = new NotificationCompat.Builder(this, "thanhanhniem")
//                .setContentTitle(title)
//                .setContentText(content)
//
//                .setSmallIcon(R.drawable.notiyicon);
//        notification111.setContentIntent(resultPendingIntent);
//
//
//
//        //////////
//
//
//
//        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String description = getString(R.string.ghichu);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("thanhanhniem", "thn", importance);
//            channel.setDescription(description);
//            channel.setSound(null,null);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//
//            notificationManager.createNotificationChannel(channel);
//            notificationManager.notify(44, notification111.build());
//        }
//    }
//
//
////    private void createNotificationChannel() {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel serviceChannel = new NotificationChannel(
////                    "nhacviec_chanel",
////                    "NhacViec",
////                    NotificationManager.IMPORTANCE_DEFAULT
////            );
////
////            NotificationManager manager = getSystemService(NotificationManager.class);
////            manager.createNotificationChannel(serviceChannel);
////        }
////    }
//}