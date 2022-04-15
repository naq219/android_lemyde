package com.project.shop.lemy.Task;

import com.project.shop.lemy.BuildConfig;
import com.telpoo.frame.utils.TimeUtils;

public class SqlTaskGeneral {
    public static final String INSERT_NHACVIEC_SIMPLE = "INSERT INTO "+ BuildConfig.db_nhacviec+".`task` (`type`, `content`, `param1`, `param2`) VALUES ('%s','%s','%s','%s') ";
    public static String clearGiaNhapGiaBan="UPDATE ;::;.products p SET p.cost_price =  1, p.wholesale_price = 1 WHERE p.id = ";

    public static String updateStatusOid(Object oid, Object status) {
        String sql="update ;::;.orders set status = %s where id = %s";
        sql= String.format(sql, status,oid);
        return sql;
    }

    public static String updateKeepStoreDo(Object orderId, Object productId,int sl) {
        String sql="update ;::;.detail_orders set doval = json_set(doval, '$.keepstock_time',unix_timestamp(),'$.keepstock_sl', %s ) where order_id= %s and product_id = %s";
        return String.format(sql, sl,orderId,productId);
    }

    //bỏ qua xuất kho khi đã có hàng, và lý do
    public static String updateSkipExport(Object orderId, Object toDate, String note,boolean isFirstTime) {
       if (isFirstTime){
           String sql1="update ;::;.orders set oval = json_set(oval,'$.skipexport_from',%s, '$.skipexport_to',%s,'$.skipexport_note', '%s' ) where id= %s";
           return String.format(sql1, TimeUtils.getTimeMillis()/1000, toDate,note,orderId);
       }
       else {
           String sql1="update ;::;.orders set oval = json_set(oval, '$.skipexport_to',%s,'$.skipexport_note', '%s' ) where id= '%s'";
           return String.format(sql1, toDate,note,orderId);
       }
    }

    public static String nhacviecSetActive0(Object id) {
        String sql="UPDATE "+BuildConfig.db_nhacviec+".`task` SET `active`='0' WHERE  `id`= "+id;
        return sql;
    }


}
