package com.project.shop.lemy.nhacviec;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.lemy.telpoo2lib.utils.SprUtils;
import com.lemy.telpoo2lib.utils.TimeUtils;
import com.project.shop.lemy.R;
import com.project.shop.lemy.chucnangkhac.AdminSettingActivity;
import com.project.shop.lemy.helper.FileUtils;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ThanHanhNiem {
    long nextShowTHN=0;
    String contentTHN="";
    String[] linesTHN= null;

    long minThn=6000000;
    long maxThn=800000;
    public static boolean reloadDataThn=false;
    

    private Context context;

    public ThanHanhNiem(Context context){

        this.context = context;
    }

    public void start(){
        loadDataTHN();
        TimerTask taskTHN = new TimerTask() {
            @Override
            public void run() {
                if (reloadDataThn){
                    loadDataTHN();reloadDataThn=false;
                }
                if (nextShowTHN< TimeUtils.getTimeMillis()){
                    nextShowTHN= TimeUtils.getTimeMillis()+ minThn+ (long) (Math.random() * (maxThn - minThn));
                    showNotifiTHN();
                }
            }
        };

        if (linesTHN!=null&&linesTHN.length>0) {
            new Timer().schedule(taskTHN,1000,5*1000);
        }
    }


    public void loadDataTHN() {
        contentTHN="";
        linesTHN=null;
        nextShowTHN=0;
        contentTHN= SPRSupport.getString("thanhanhniem",context);
        if (!contentTHN.equals(SPRSupport.GET_PREFRENCE_FALL)) {
            if (contentTHN.length()<10) return;
            //if (contentTHN.contains("#stop")) return;

            try {
                JSONArray ja = new JSONArray(contentTHN);
                JSONArray jaEnable= new JSONArray();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.optJSONObject(i);
                    if (jo.optInt("enable")==1)
                        jaEnable.put(jo);
                }
                if (jaEnable.length()==0) return; //khong có cái nào enable
                JSONObject joShow=jaEnable.optJSONObject(new Random().nextInt(jaEnable.length()));
                linesTHN = joShow.optString("content").split("\\r?\\n");

                String minStr= joShow.optString("min","600000").replace("s","000");
                if (minStr.contains("p")) minStr= ""+(Long.parseLong(minStr.replace("p",""))*60*1000);
                String maxStr= joShow.optString("max","800000").replace("s","000");
                if (maxStr.contains("p")) maxStr= ""+(Long.parseLong(maxStr.replace("p",""))*60*1000);

                minThn= Long.parseLong(minStr);
                maxThn=Long.parseLong(maxStr);
                showNotifi("p0","min "+minThn+" max "+maxThn);

            } catch (JSONException e) {
                e.printStackTrace();
                showNotifi("Lỗi THN 2123 ",contentTHN);
                return;
            }


        }
    }

    private int countTHN=0;
    public void showNotifiTHN() {
        countTHN++;
        if (linesTHN==null||linesTHN.length==0) return;
        String a1 = linesTHN[new Random().nextInt(linesTHN.length)];
        String[] ctShow= a1.split("#");
        if (ctShow.length!=2){
            //showNotifi("Niệm "+countTHN, "err="+a1);
            showNotifi("Niệm "+countTHN,a1);
            return;
        }

        showNotifi("Niệm "+countTHN, ctShow[1]);
        if (a1.contains("@")) new Handler().postDelayed(() -> {showNotifi("Niệm "+countTHN, ctShow[1]);},1000);

    }

    protected void showNotifi(String title, String content) {
        Intent notificationIntent = new Intent(context, NhacViecReiceiver.THNReceiver.class);
        // PendingIntent pendingIntent =PendingIntent.getBroadcast(this,123,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(context, ThanhHanhNiemActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification111 = new NotificationCompat.Builder(context, "thanhanhniem")
                .setContentTitle(title)
                .setContentText(content)

                .setSmallIcon(R.drawable.notiyicon);
        notification111.setContentIntent(resultPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Niệm";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("thanhanhniem", "thn", importance);
            channel.setDescription(description);
            channel.setSound(null,null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(44, notification111.build());
        }
    }
    
    
    
    
    
    
    
    
    
    /////// ther 
    long lastRunPhap=0;
    private void baoChuong() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.dekeu);
        mediaPlayer.start();
    }

    
    private void runPhatNhacPhatPhap() {
        long chuong= SprUtils.getLong("edChuong",context,-1l);
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
        if(SPRSupport.getBool(AdminSettingActivity.SPR_ENABLE_RUN,context,false)){
            // chạy phát âm thanh đoạn Pháp ngắn.
            TimerTask taskPhap = new TimerTask() {
                @Override
                public void run() {
                    if (lastRunPhap> Calendar.getInstance().getTimeInMillis()) return;

                    int ppStart= SPRSupport.getAsInt(AdminSettingActivity.SPR_DELAY_START,context);
                    int ppEnd= SPRSupport.getAsInt(AdminSettingActivity.SPR_DELAY_END,context);
                    String ppLocation = SPRSupport.getString(AdminSettingActivity.SPR_LOCATION_PHAP,context,null);
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
}
