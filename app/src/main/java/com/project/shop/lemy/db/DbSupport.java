package com.project.shop.lemy.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.project.shop.lemy.bean.SmsObj;
import com.telpoo.frame.database.DbCacheUrl;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.TimeUtils;

import java.util.ArrayList;

public class DbSupport {

    public static final void init(Context context) {
        MyDb.init(DbConfig.tables, DbConfig.keys, context, context.getExternalFilesDir("lemy") + "/" + DbConfig.dbName, DbConfig.dbVersion);
        DbCacheUrl.initDb(context);
    }

    public static final BaseObject getCacheUrl(String url) {
        Long time = TimeUtils.minute2Milisecond(60);
        BaseObject objectCache = DbCacheUrl.getCacheUrl(url, time);
        if (objectCache != null)
            Log.d("telpoo", "objectCache: " + objectCache.toJson());
        return objectCache;
    }


    public static ArrayList<BaseObject> getChude() {
        String querry = "select * from " + DbConfig.SMS + " ORDER BY" + " rowid" + " DESC";
        ArrayList<BaseObject> data = MyDb.rawQuery(querry);
        Log.d("ducqv", "getChude: " + data);
        return data;
    }

    public static boolean isHaveRow(String table, String key, String value) {
        boolean isSaveRecord = false;
        ArrayList arrayList = MyDb.selectRow(table, key, value);
        if (arrayList != null && arrayList.size() > 0) {
            isSaveRecord = true;
        }
        return isSaveRecord;
    }

    public static Boolean saveChuDe(BaseObject object) {
        ArrayList<BaseObject> list = new ArrayList<>();
        list.add(object);
        boolean save = MyDb.addToTable(list, DbConfig.SMS);
        Log.d("ducqv", "saveChuDe: " + save);
        return save;
    }

    public static Boolean deleteChude(String chude) {
        boolean delete = MyDb.deleteRowInTable(DbConfig.SMS, SmsObj.desc, chude);
        return delete;
    }

    public static void saveCacheUrl(String url, String res) {
        DbCacheUrl.saveCacheUrl(url, res);
    }

    public static void clearCache() {
        DbCacheUrl.clearCache();
    }


    public static void deleteAll(String tableName) {
        int save = MyDb.removeAllInTable(tableName);
        Log.d("DbSupport", "deleteAll: " + save);

    }


}