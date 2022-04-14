package com.project.shop.lemy.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.telpoo.frame.object.BaseObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ducqv on 9/13/2018.
 */

public class DateSuppost {

    //lấy ngày hiện tại
    public static String getDateToday() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = dft.format(cal.getTime());
        return date;
    }

    //ngày hôm qua
    public static String getDateYesterday() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());
        return date;
    }

    //bắt đầu ngày của tháng
    public static String getDateStart() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());
        return date;
    }

    //ngày đầu tiên của tháng trước
    public static String getFirstDayLastMonth() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());
        return date;
    }

    //ngày cuối cùng của tháng trước
    public static String getLastDayLastMonth() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());
        return date;
    }

    public static String setDate() {
        String date = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date = dft.format(cal.getTime());
        return date;
    }

    public static void chonDate(Context context, TextView textView) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener callback = (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            textView.setText(sdf.format(cal.getTime()));
        };
        String s = textView.getText() + "";
        String strArrtmp[] = s.split("-");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(context, callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày");
        pic.show();
    }

    public static String converDate(String strdate) {
        String outDate = "";
        DateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = input.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outDate = output.format(date);
        return outDate;
    }

}
