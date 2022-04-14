package com.project.shop.lemy;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.lemy.library.UpdateChecker;

import com.project.shop.lemy.common.StaticData;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.data.StringFormat;

import io.sentry.Sentry;





public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!SPRSupport.contain("allow_update",getApplicationContext())){
            SPRSupport.save("allow_update",true,getApplicationContext());
        }


//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
//        config.defaultDisplayImageOptions(getImageOption());
//        config.writeDebugLogs(); // Remove for release app
//
//        ImageLoader.getInstance().init(config.build());
        UpdateChecker.setUrl(StaticData.urlUpdate+"?app_id="+getPackageName(),this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);


    }

    //    public DisplayImageOptions getImageOption() {
//        return new DisplayImageOptions.Builder()
//                .delayBeforeLoading(0)
//                .cacheOnDisk(true)
//                .cacheInMemory(true) // default
//                .cacheOnDisk(true) // default
//                .considerExifParams(false) // default
//                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//                .showImageForEmptyUri(R.drawable.ic_hinhanhsp)
//                .showImageOnFail(R.drawable.ic_hinhanhsp)
//                .showImageOnLoading(R.drawable.ic_hinhanhsp)
//                .displayer(new FadeInBitmapDisplayer(500, true, false, false))
//                .build();
//    }
}
