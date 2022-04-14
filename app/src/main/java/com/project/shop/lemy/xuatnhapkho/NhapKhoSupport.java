package com.project.shop.lemy.xuatnhapkho;

import com.project.shop.lemy.bean.ObjectSupport;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naq on 11/16/18.
 */

public class NhapKhoSupport {

    /**
     * khá khó hiểu, cái ni là dữ liệu khoang và sl được chọn khi nhập kho
     * @param ojProductNhap
     * @return
     */
    public static List<BaseObject> getDataKhoangFromOj(BaseObject ojProductNhap){
        List<BaseObject> ojChon= new ArrayList<>();

        JSONArray ja  = new JSONArray();
        try {
            if (ojProductNhap.containsKey("dataKhoangSL"))
                ja = new JSONArray(ojProductNhap.get("dataKhoangSL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ja.length(); i++) {
            BaseObject oj1 = new BaseObject();
            try {
                oj1.set("khoang_id",ja.getJSONObject(i).getString("khoang_id"));
                oj1.set("sl",ja.getJSONObject(i).getString("sl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ojChon.add(oj1);

        }
        return ojChon;
    }

    public static BaseObject setKhoangChon(BaseObject ojProductNhap, List<BaseObject> ojKhoangChon){
        BaseObject ojRes= ojProductNhap.clone();
        JSONArray ja  = new JSONArray();
        for (int i = 0; i < ojKhoangChon.size(); i++) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("khoang_id",ojKhoangChon.get(i).get("khoang_id"));
                jo.put("sl",ojKhoangChon.get(i).get("sl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ja.put(jo);
        }
        ojRes.set("dataKhoangSL",ja.toString());
        return ojRes;
    }

    public static ArrayList<BaseObject> getTonKhoXuatHangAsList(BaseObject ojXuat){
        ArrayList<BaseObject> ojs = new ArrayList<>();
        if (!ojXuat.containsKey("tonkho")) return ojs;

        ojs= ObjectSupport.JsonArray2List(ojXuat.get("tonkho"));
        return ojs;
    }

}
