package com.project.shop.lemy.helper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.common.PoupMenuSupport;
import com.project.shop.lemy.listener.ListenBack;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHelper {


    private static final ArrayList<BaseObject> ojsShop=new ArrayList<>();

    public static void loadShop2Menu(boolean isShow,TextView tvShop, Context context){
        Listerner.callBack listenSelectShop= new Listerner.callBack() {
            @Override
            public void onCallBackListerner(Object oj1, Object oj2, Object oj3) {

                tvShop.postDelayed(() -> {
                    String key = (String) oj2;

                    if (key.length() > 13) key = key.substring(0, 13) + "..";
                    tvShop.setText(key);
                }, 200);

                tvShop.setTextColor(Color.GREEN);

            }
        };

        loadShop(isShow,tvShop,context,listenSelectShop);
    }


//    public static void loadListShopByCodenv(Context context, ListenBack listenBack){
//        TaskNetGeneral.extaskOtherData(context,new BaseModel(){
//            @Override
//            public void onSuccess(int taskType, Object data, String msg) {
//                super.onSuccess(taskType, data, msg);
//                if (data instanceof JSONObject){
//
//                    try {
//                        JSONObject jo = (JSONObject) data;
//                        JSONArray ja = jo.getJSONArray("shop");
//                        JSONObject joShop_nv=jo.getJSONObject("shop_nv");
//                        String listShopnv= joShop_nv.getString(SPRSupport.getString("codenv",context));
//                        ojsShop= new ArrayList<>();
//                        for (int i = 0; i < ja.length(); i++) {
//                            JSONObject oo = ja.getJSONObject(i);
//                            BaseObject ojitem = JsonSupport.jsonObject2BaseOj(oo);
//                            if (!listShopnv.contains("-"+oo.get("id")+"-"))continue;
//                            ojitem.set("id_name",oo.get("id")+"- "+oo.getString("name").replace("Shop ",""));
//                            ojsShop.add(ojitem);
//                        }
//
//                        listenBack.OnListenBack(ojsShop);
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }


    //lấy  danh sách shop theo mã của nhân viên quản lý
    public static String listShopIdByCodenv(){
        StringBuilder ids= new StringBuilder();
        for (int i = 0; i < ojsShop.size(); i++) {
            ids.append(ojsShop.get(i).get("id"));
            if(i<ojsShop.size()-1)
                ids.append(",");
        }
        return ids.toString();
    }
    private static void loadShop(boolean isShow, TextView tvShop, Context context, Listerner.callBack listenSelectShop){
        if  ( ojsShop!=null&&ojsShop.size()>0&&isShow){
            PoupMenuSupport.showMenuFromOj(context,tvShop,ojsShop,"id_name",listenSelectShop);
            return;
        }
        TaskNetGeneral.extaskOtherData(context,new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                if (data instanceof JSONObject){

                    try {
                        JSONObject jo = (JSONObject) data;
                        JSONArray ja = jo.getJSONArray("shop");
                        JSONObject joShop_nv=jo.getJSONObject("shop_nv");
                        String listShopnv= joShop_nv.getString(SPRSupport.getString("codenv",context));
                        //ojsShop= new ArrayList<>();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject oo = ja.getJSONObject(i);
                            BaseObject ojitem = JsonSupport.jsonObject2BaseOj(oo);
                            if (!listShopnv.contains("-"+oo.get("id")+"-"))continue;
                            ojitem.set("id_name",oo.get("id")+"- "+oo.getString("name").replace("Shop ",""));
                            ojsShop.add(ojitem);
                        }
                        tvShop.setVisibility(View.VISIBLE);
                        if (isShow)
                            PoupMenuSupport.showMenuFromOj(context,tvShop,ojsShop,"id_name",listenSelectShop);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static int getStatusColor(Object status){
        int status1= Integer.parseInt(""+status);
        int statusColor=Color.GREEN;
        switch (status1){
            case 1: statusColor= Color.GREEN;break;
            case 2: statusColor= Color.parseColor("#D78E24");break;
            case 3: statusColor= Color.CYAN;break;
            case 4: statusColor= Color.RED;break;
            case 5: statusColor= Color.parseColor("#D78E24");break;
        }
        return statusColor;
    }
    public static String getStatusName(String status1){
        String name="Trạng thái ";
        int status= Integer.parseInt(status1);
        switch (status){
            case 1: name="Đang order";
                break;
            case 2: name="Gửi ship";
                break;
            case 3: name="Đã nhận";
                break;
            case 4: name="Đã hủy";
                break;
            case 5: name="Lấy hàng";
                break;
            default:
                name=name+status;
        }
        return name;
    }

    public static String formatOid(Object id) {
        StringBuilder stringBuilder = new StringBuilder(""+id);
        if(stringBuilder.length()<4) return ""+id;

        stringBuilder.insert(3,".");
        return stringBuilder.toString();

    }


}
