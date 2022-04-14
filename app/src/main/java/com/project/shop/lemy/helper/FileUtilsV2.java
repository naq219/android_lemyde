package com.project.shop.lemy.helper;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class FileUtilsV2 {
    public static List<Uri> getUriListFromIntent(Intent data, Context context){
        List<Uri> mArrayUri = new ArrayList<>();
        if (data==null) return null;
        if (data.getData() != null) {
            Uri mImageUri = data.getData();
            mArrayUri.add(mImageUri);


        } else {
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);


                }

            }
        }
        return mArrayUri;
    }

    public static Bitmap getBitmapFromUri(Uri uri,Context context){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //Bitmap bm1 = new Compressor(this).compressToBitmap(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


}
