package com.project.shop.lemy.bean;

import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by naq on 11/16/18.
 */

public class ObjectSupport {
    public static ArrayList<BaseObject> JsonArray2List(String jsonArr) {
        if (jsonArr==null) return null;
        ArrayList<BaseObject> ojs= new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(jsonArr);
            for (int i = 0; i < ja.length(); i++) {

                ojs.add(Json2Object(ja.getJSONObject(i)));
            }

            return ojs;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BaseObject Json2Object(JSONObject jo) throws JSONException {
        Iterator<String> keys = jo.keys();
      BaseObject oj = new BaseObject();
        while (keys.hasNext()){
            String key= keys.next();
            oj.set(key,jo.getString(key));
        }

        return oj;
    }



}
