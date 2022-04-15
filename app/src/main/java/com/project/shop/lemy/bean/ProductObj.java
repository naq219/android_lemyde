package com.project.shop.lemy.bean;

import com.lemy.telpoo2lib.model.BObject;

import org.json.JSONObject;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class ProductObj extends BObject {
    public static final String id = "id";
    public static final String image = "image";
    public static final String name = "name";
    public static final String cost_price = "cost_price";
    public static final String wholesale_price = "wholesale_price";
    public static final String retail_price = "retail_price";
    public static final String sku = "sku";
    public static final String introduction = "introduction";
    public static final String weight = "weight";
    public static final String status = "status";
    public static final String note = "note";
    public static final String page_link = "page_link";
    public static final String created_at = "created_at";
    public static final String updated_at = "updated_at";
    public static final String total_money = "total_money";
    public static final String quantity_product = "quantity_product";
    public static final String kho_id = "kho_id";
    public static final String kho_name = "kho_name";
    public static final String ngay_nhap = "ngay_nhap";
    public static final String type = "type";
    public static final String order_id = "order_id";
    public static final String ngay_xuat = "ngay_xuat";
    public static final String khoang_id = "khoang_id";
    public static final String sl = "sl";
    public  static final String gia_buonsi="gia_buonsi";
    public static final String limit0="limit";
    public static final String images="images";

    public static final String ean="ean";
    public static String post_at="post_at";

    public ProductObj(JSONObject optJSONObject) {
        super(optJSONObject);
    }

    public String getName(){
        return getAsString(name);
    }

    public String getNameId(){
        return "(SP"+getAsString(id)+") "+getAsString(name);
    }
    public int getGiaNhap(){
        return getAsInt(cost_price);
    }

    public int getGiaBanLe(){
        return getAsInt(retail_price);
    }
    public int getGiaBuon(){
        return getAsInt(wholesale_price);
    }

    public int getGiaBuonSi(){
        return getAsInt(gia_buonsi);
    }


    public String getImage() {
        return getAsString(image,"");
    }

    public String getId() {
        return getAsString(id);
    }

    public Boolean isLimit() {
        int limit= getAsInt(limit0,-999);
        if(limit==-999) return null;

        if (limit==-1) return true;
        return false;
    }

    public int getPostAt() {
        return getAsInt(post_at,1);
    }
}
