package com.project.shop.lemy.donhang;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.TimeUtils;

import java.util.Calendar;

public class ViewNoteTmpXuat {
    private Object oid;
    private boolean isFirstTime;
    private Context context;
    Calendar calSelect;
    public ViewNoteTmpXuat(Object oid, boolean isFirstTime, Context context){

        this.oid = oid;
        this.isFirstTime = isFirstTime;
        this.context = context;
    }

    public View getView(){
        View v = View.inflate(context, R.layout.view_dl_notetmpxuat,null);
        EditText ed = v.findViewById(R.id.edNote);
        TextView tvOid = v.findViewById(R.id.tvOid);
        TextView datePicker = v.findViewById(R.id.tvChonNgay);
        View btn = v.findViewById(R.id.btn);
        tvOid.setText("DHM "+oid);
        datePicker.setOnClickListener(v1 -> {
            DialogSupport.datePicker(context,(value, where) -> {
                calSelect= (Calendar) value;
                datePicker.setText(TimeUtils.calToString(calSelect,"dd/MM/yyyy"));
            });
        });

        btn.setOnClickListener(v1 -> {
            if (calSelect==null) {
                datePicker.setText("Chưa Chọn ngày ! Click vào đây"); return;
            }
            String note=ed.getText().toString();
            String sql=SqlTaskGeneral.updateSkipExport(oid,calSelect.getTimeInMillis()/1000,ed.getText().toString(),isFirstTime);
            TaskGeneralTh.exeTaskStatement(context,sql,"abbbbb",123,new Model(){
                @Override
                public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                    datePicker.setText("Lưu thành công");
                }

                @Override
                public void onFail(int taskType, String msg, Integer queue) {
                    datePicker.setText("Lỗi "+msg);
                }
            });
        });

        return v;


    }

}
