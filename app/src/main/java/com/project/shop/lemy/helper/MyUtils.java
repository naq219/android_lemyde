package com.project.shop.lemy.helper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.telpoo.frame.utils.Mlog;

import java.io.File;
import java.util.Random;

public class MyUtils {
    public static String getClipBoardText(Context context){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData==null) return null;

        int itemCount = clipData.getItemCount();
        String ss="";
        if(itemCount > 0) {
            ClipData.Item item = clipData.getItemAt(0);
            String text = item.getText().toString();
            ss+=text;
        }
        return ss;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void copyToClipboard(String content, Context context){
        ClipboardManager myClipboard;
        ClipboardManager clipboard = context.getApplicationContext().getSystemService(ClipboardManager.class);

        ClipData clip = ClipData.newPlainText("content", content);
        clipboard.setPrimaryClip(clip);
    }

    public static  boolean downloadTask(String url, Context context) throws Exception {
        if (!url.startsWith("http")) {
            return false;
        }

        try {
            String name = new Random().nextInt(10000)+ "_"+url.substring(url.lastIndexOf('/') + 1);
            File file = new File(Environment.getExternalStorageDirectory(), "Download");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.mkdirs();
            }
            File result = new File(file.getAbsolutePath() + File.separator + name);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationUri(Uri.fromFile(result));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
            //mToast(mContext, "Starting download...");
            MediaScannerConnection.scanFile(context, new String[]{result.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            Mlog.E(">>>>>"+e.toString());
            //mToast(this, e.toString());
            return false;
        }
        return true;
    }

}
