package com.project.shop.lemy.Net;

import com.project.shop.lemy.BuildConfig;
import com.telpoo.frame.object.BaseObject;

public class NetProduct extends NetSupport2{
    public static NetData seachProduct(String text, String field,String orderby,String limit,String page) {
        if ((""+limit).equals("null")) limit="";
        if ((""+page).equals("null")) page="";
        String url= getBaseUrlv2()+MyUrl2.productSearch+String.format("?field=%s&text=%s&orderby=%s&limit=%s&page=%s",field,text,orderby,limit,page);
        NetData data = simpleGet(url);
        return data;

    }


    public static NetData  update(String product_id, BaseObject oj) {
        String url = getBaseUrlv2()+MyUrl2.product+product_id;

        return NetSupport2.simpleRequest("PUT", url, oj.convert2NetParramsNotEncode());
    }
}
