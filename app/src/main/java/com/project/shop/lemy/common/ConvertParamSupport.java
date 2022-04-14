package com.project.shop.lemy.common;

/**
 * Created by Asus-GL on 10-Jul-18.
 */

public class ConvertParamSupport {
    static ConvertParamSupport convertParamSupport;

    public static ConvertParamSupport paramSupport() {
        if (convertParamSupport == null) convertParamSupport = new ConvertParamSupport();
        return convertParamSupport;
    }

    public static String conVertStatusOrder(String value) {
        if (value == null) return "";
        int index = Integer.parseInt(value);
        String status[] = {"status", "Order", "Gửi ship", "Đã nhận", "Bị hủy", "Lấy hàng"};
        if (index < status.length)
            return status[Integer.parseInt(value)];
        return "";
    }

    public String conVertPttt(String value) {
        if (value == null) return "";
        int index = Integer.parseInt(value);
        String pttt[] = {"pttt", "Tiền mặt", "Qua thẻ",};
        if (index < pttt.length)
            return pttt[Integer.parseInt(value)];
        return "";
    }

    public static int conVertSttProduct(String value) {
        if (value == null || value.isEmpty()) return 0;
        if (value.contains("Còn hàng")) return 1;
        else if (value.contains("Ngừng bán")) return 2;
        else if (value.contains("Order")) return 3;
        else if (value.contains("chua_ro")) return 4;
        return 4;
    }
}
