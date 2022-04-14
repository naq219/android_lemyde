package com.project.shop.lemy.Task;

import android.content.Context;

/**
 * Created by naq on 05/04/2015.
 */
public interface TaskType {
    public static final int TASK_SESRCH = 0;
    public static final int TASK_LIST_DONHANG = 1;
    public static final int TASK_LIST_NGUOIBAN = 2;
    public static final int TASK_LIST_SANPHAM = 3;
    public static final int TASK_LIST_ORDERPROFUCT = 4;
    public static final int TASK_DETAILORDER = 5;
    public static final int TASK_UPDATEORDER = 6;
    public static final int TASK_SEARCHPRODUCTS = 7;
    public static final int TASK_CHITIET_SANPHAM = 8;
    public static final int TASK_SEARCH_CUSTOMERS = 9;
    public static final int TASK_UPDATE_SANPHAM = 10;
    public static final int TASK_ADD_SANPHAM = 11;
    public static final int TASK_ADD_ORDER = 12;
    public static final int TASK_DELETE_SANPHAM = 13;
    public static final int TASK_SHOP = 14;
    public static final int TASK_ADD_KHACHHANG = 15;
    public static final int TASK_LIST_SHOP = 16;
    public static final int TASK_ADD_SHOP = 17;
    public static final int TASK_DELETE_SHOP = 18;
    public static final int TASK_UPDATE_SHOP = 19;
    public static final int TASK_SEARCH_QUANHUYEN = 20;
    public static final int TASK_DELETE_CUSTOMERS = 21;
    public static final int TASK_UPDATE_CUSTOMERS = 22;
    public static final int TASK_PUSH_HVC = 23;
    public static final int TASK_LIST_STATUS = 24;
    public static final int TASK_LIST_DVVC = 25;
    public static final int TASK_UPDATE_DVVC = 26;
    public static final int TASK_ADD_DVVC = 27;
    public static final int TASK_DELETE_DVVC = 28;
    public static final int TASK_LIST_TYPES_CUSTOMER = 29;
    public static final int TASK_LIST_STATUS_PRODUCT = 30;
    public static final int TASK_LIST_PAYMENT = 31;
    public static final int TASK_LIST_KHO = 32;
    public static final int TASK_LIST_KIEUNHAP = 33;
    public static final int TASK_LIST_KHOANG = 34;
    public static final int TASK_ADD_NHAPHANG = 35;
    public static final int TASK_LIST_NHAPHANG = 36;
    public static final int TASK_LIST_DONXUAT = 37;
    public static final int TASK_LIST_KIEUXUAT = 38;
    public static final int TASK_ADD_XUATKHO = 39;
    public static final int TASK_LIST_KHOANGSLSP = 40;


    public static final int SEARCH_CUSTOMER_V2 = 50;
    public static final int SEARCH_PRODUCT_V2 = 51;
    //public static final int OTHER_DATAV2 = 52; //ds shop...
    //public static final int NHAP_DONV2 = 52;
    public static final int UPDATE_PRODUCT_V2 = 53;
    //public static final int SUA_COD_GHTK = 54;
    //public static final int ORDER_GHTK = 55;

    public static final int start_tasknetorder = 100;
    public static final int TASKTYPE_GENERAL = 200;
    int start_taskproductv2 = 300;
    int start_taskNhapDonV2 = 400;
    int start_tasknetorderv2 = 500;
    int start_taskStock = 600;
    int TASKTYPE_GENERALTH = 600;

}
