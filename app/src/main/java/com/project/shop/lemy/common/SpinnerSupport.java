package com.project.shop.lemy.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.shop.lemy.R;

/**
 * Created by Asus-GL on 16-Jul-18.
 */

public class SpinnerSupport {
    public static final String[] trangthai = {
            "--Trạng Thái--",
            "Order",
            "Lấy Hàng",
            "Gửi Ship",
            "Đã Nhận",
            "Bị Hủy"
    };

//    public static final String[] trangthaiupdatesanpham = {
//            "--Trạng Thái--",
//            "Còn hàng",
//            "Ngừng bán",
//            "Order",
//            "Chưa rõ"
//    };

    public static final String[] nhomKhachhang = {
            "---Chọn---",
            "Khách mua buôn",
            "Khách mua lẻ"
    };

    public static void setUp(Context context, Spinner spinner, String[] listData) {
        ArrayAdapter<String> adapterCTV = new ArrayAdapter<String>(context, R.layout.spinner_item, listData);
        spinner.setAdapter(adapterCTV);
    }

    public static void setUpMenu(Context context, Spinner spinner, String[] listData) {
        ArrayAdapter<String> adapterCTV = new ArrayAdapter<String>(context, R.layout.spinner_item_menu, listData);
        spinner.setAdapter(adapterCTV);
    }

    public static void setNhomShop(Context context, Spinner spinner, String[] listData) {
        ArrayAdapter<String> adapterCTV = new ArrayAdapter<String>(context, R.layout.spinner_item_15sp, listData);
        spinner.setAdapter(adapterCTV);
    }

    public static void customSp(Context context, Spinner spinner, String[] listData) {
        ArrayAdapter<String> adapterCTV = new ArrayAdapter<String>(context, R.layout.spinner_item_dh, listData);
        spinner.setAdapter(adapterCTV);
    }


}

// . sản phẩm , mã đh  kh là khách hàng