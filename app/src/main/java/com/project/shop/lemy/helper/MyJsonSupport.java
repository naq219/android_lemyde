package com.project.shop.lemy.helper;

import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONArray;

public class MyJsonSupport extends JsonSupport{
    public static JSONArray jaAddAll(JSONArray source,JSONArray more){
        for (int i = 0; i < more.length(); i++) {
            source.put(more.opt(i));
        }
        return source;
    }
}
