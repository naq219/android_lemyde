package com.project.shop.lemy.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.bean.SmsObj;
import com.telpoo.frame.object.BaseObject;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by sonlv on 8/18/18.
 */

public class StringHelper {
    public static String formatTien(float tien) {
        if (tien < 1000) return tien + "d";
        tien = tien / 1000;
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(tien) + "K";
    }

    public static String formatStr(String str) {
        if (str.contains(".") || (str.contains(","))) {
            str = str.replace(".", "");
            str = str.replace(",", "");
        }
        return str;
    }

    public static String formatDoubleMoney(String money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        if (money.isEmpty()) return "";
        double myMoney = Double.parseDouble(money);
        return formatter.format(myMoney);
    }

    public static String inhoaSupport(String str) {
        if (str.isEmpty() || str.equals("null")) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String formatID(String id) {
        String dhm = "DHM";
        if (id.isEmpty() || id.equals("null")) return "";
        return dhm + id;
    }

    public static String replaceBody(BaseObject object) {
        String data = object.get(SmsObj.content, "");
        String thongTinSP = object.get("thongtinsp", "");
        data = data.replaceAll("#ttsp#", thongTinSP);
        data = data.replaceAll("#mdh#", "dhm" + object.get(OrderObj.id));
        return data;
    }

    public static void covenrtGia(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editText.removeTextChangedListener(this);
                String editTT = editable.toString();
                editTT = StringHelper.formatStr(editTT);
                editText.setText(StringHelper.formatDoubleMoney(editTT));
                editText.setSelection(editText.getText().length());
                editText.addTextChangedListener(this);
            }
        });
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace("Đ","D").replace("đ","d");
    }

    public static String getTimeAgo(long timeInMillis) {

        long t = Calendar.getInstance().getTimeInMillis()-timeInMillis;
        long second= t/1000;
        if (second<60) return second+" Giây";

        long minute= second/60;
        if (minute<60) return minute+" Phút";

        long hour = minute/60;
        if (hour<24) return hour+" Phút";

        long day= hour/24;
        return day+" Ngày";

    }


    public static String joinIdProductName(String pid, String mname) {
        return "(SP"+pid+") "+mname;
    }
}
