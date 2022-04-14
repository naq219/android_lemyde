package com.project.shop.lemy.Net;

import com.project.shop.lemy.BuildConfig;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONArray;
import org.json.JSONException;

public class NetCustomer extends NetSupport2{
    public static NetData seachCustomer(String text, String field) {
        String url= getBaseUrlv2()+MyUrl2.customerSearch+String.format("?field=%s&text=%s",field,text);
        NetData data = simpleGet(url);


        return data;

    }


}
