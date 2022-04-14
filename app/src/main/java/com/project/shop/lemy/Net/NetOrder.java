package com.project.shop.lemy.Net;

import com.telpoo.frame.object.BaseObject;

import org.json.JSONException;
import org.json.JSONObject;

public class NetOrder extends NetSupport2{



    public static NetData NhapDon(String data) {
        String url= getBaseUrlv2()+MyUrl2.nhapdon;
        NetData das = NetSupport2.parseResponse(CustomBaseNetSupport.getInstance().simplePostJson(url, data));

        return das;
    }

    public static NetData  update(String order_id, BaseObject oj) {
        String url = getBaseUrlv2()+MyUrl2.ordersResource+order_id;

        return NetSupport2.simpleRequest("PUT", url, oj.convert2NetParramsNotEncode());
    }



    public static NetData order_ghtk(String orderId){
        String url="http://api.lemyde.com/api/order-ghtk?id="+orderId;

        String da = CustomBaseNetSupport.getInstance().methodHttp("get", url, null);

        NetData netData= new NetData();
        if (da==null){
            netData.setConnectError();
            return netData;
        }
        try {
            JSONObject jo = new JSONObject(da);
            JSONObject joO = jo.getJSONObject("DHM" + orderId);
            joO.put("order_id01",orderId);
            netData.setSuccess(joO.toString());
            return  netData;

        } catch (JSONException e) {
            e.printStackTrace();
            netData.setConnectError();
            return netData;
        }
    }


    public static NetData getDetail(String order_id1) {
        String url= getBaseUrlv2()+"v2/order/"+order_id1;
        return simpleGet(url);

    }


}
