package com.project.shop.lemy.Net;

import android.util.Log;

import com.project.shop.lemy.BuildConfig;
import com.telpoo.frame.database.BaseDBSupport3;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.net.NetConfig;
import com.telpoo.frame.object.CacheUrlOj;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetSupport2 {



    public static final NetData simpleGet(String url) {
        NetData data = new NetData();


        String res = BaseNetSupport.getInstance().simpleGetHttp(url);
        Log.d("telpoo", "url: " + url);
        Log.d("telpoo", "Respone: " + res);

        try {
            JSONObject dataResponse = new JSONObject(""+res);
            if (dataResponse.optInt("status", 2)!=1) {
                data.setCode(NetData.CODE_FALSE);
                data.setMessage(dataResponse.optString("msg", "Có lỗi api"));
                return data;
            }
            data.setCode(NetData.CODE_SUCCESS);

            if (!dataResponse.has("data")   ) {
                data.setSuccess(dataResponse);
                return data;
            }

            data.setSuccess(dataResponse.get("data"));
            return data;


        } catch (JSONException e) {
            data.setCode(NetData.CODE_FALSE);
            data.setMessage("Không đúng định dạng json");
        }
        return data;
    }

    public static final NetData simpleRequest(String method,String url,String param) {
        NetData data = new NetData();


        String res = CustomBaseNetSupport.getInstance().methodHttp(method,url,param);
        Log.d("telpoo", "url: " + url);
        Log.d("telpoo", "Respone: " + res);

        try {
            JSONObject dataResponse = new JSONObject(""+res);
            if (dataResponse.optInt("status", 2)!=1) {
                data.setCode(NetData.CODE_FALSE);
                data.setMessage(dataResponse.optString("msg", "Có lỗi api"));
                return data;
            }
            data.setCode(NetData.CODE_SUCCESS);

            if (!dataResponse.has("data")   ) {
                data.setSuccess(dataResponse);
                return data;
            }

            data.setSuccess(dataResponse.get("data"));
            return data;


        } catch (JSONException e) {
            data.setCode(NetData.CODE_FALSE);
            data.setMessage("Không đúng định dạng json");
        }
        return data;
    }



    public static String getBaseUrlv2(){
        return BuildConfig.url2;
    }



    public static NetData getOtherData() {
        String url= getBaseUrlv2()+MyUrl2.otherData;
        NetData data = simpleGet(url);
        return data;

    }

    public  static  NetData parseResponse(NetData res1){
        if (res1.isFailed()) return res1;
        NetData data= new NetData();
        try {
            JSONObject dataResponse = new JSONObject(""+res1.getDataAsString());
            if (dataResponse.optInt("status", 2)!=1) {
                data.setCode(NetData.CODE_FALSE);
                data.setMessage(dataResponse.optString("msg", "Có lỗi api"));
                return data;
            }
            data.setCode(NetData.CODE_SUCCESS);

            if (!dataResponse.has("data")   ) {
                data.setSuccess(dataResponse);
                return data;
            }

            data.setSuccess(dataResponse.get("data"));
            return data;


        } catch (JSONException e) {
            data.setCode(NetData.CODE_FALSE);
            data.setMessage("Không đúng định dạng json");
        }
        return data;
    }
}
