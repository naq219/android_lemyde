package com.project.shop.lemy.bean;

import com.telpoo.frame.object.BaseObject;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class OrderObj extends BaseObject {

    public static final String id = "id";
    public static final String total_amount = "total_amount";
    public static final String trang_thai_dh = "trang_thai_dh";
    public static final String product_name = "product_name";
    public static final String quantity = "quantity";
    public static final String status = "status";
    public static final String count = "count";
    public static final String payment = "payment";
    public static final String order_shipper_lb_id = "order_shipper_lb_id";
    public static final String free_ship = "free_ship";
    public static final String phi_ship = "phi_ship";
    public static final String note = "note";
    public static final String date_created = "date_created";
    public static final String username = "username";
    public static final String gia_ban = "gia_ban";
    public static final String additional_cost = "additional_cost";
    public static final String money_received = "money_received";
    public static final String shop_name = "shop_name";
    public static final String customers_name = "customers_name";
    public static final String customers_phone = "customers_phone";
    public static final String customers_address = "customers_address";
    public static final String customers_district_id = "customers_district_id";
    public static final String customers_province_id = "customers_province_id";
    public static final String customers_district_name = "customers_district_name";
    public static final String customers_province_name = "customers_province_name";
    public static final String product_weight = "product_weight";
    public static final String customer_id = "customer_id";
    public static final String shop_id = "shop_id";
    public static final String products = "products";
    public static final String dvvc = "dvvc";
    public static final String cod = "cod";
    public static final String customers_pttt="customers_pttt";
    public static final String customers_group="customers_group";
    public static String keep_stock="keep_stock";


    public OrderObj(BaseObject oj) {

    }

    public int getStatus(){
        return Integer.parseInt(get(status,"9999"));
    }

    public String getId() {
        return get(id);
    }
}
