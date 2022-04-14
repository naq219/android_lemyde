package com.project.shop.lemy.smskhachhang;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.bean.SmsObj;
import com.project.shop.lemy.db.DbConfig;
import com.project.shop.lemy.db.DbSupport;
import com.telpoo.frame.object.BaseObject;

import java.util.Calendar;

public class AddTypeSmsActivity extends SwipeBackActivity {
    private EditText edtChuDe;
    private EditText edtNoiDung;
    private TextView btnHuy;
    private TextView btnOKSms;
    String type;
    String noidung;
    private BaseObject object = new BaseObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_sms);
        initView();
    }

    private void initView() {
        edtChuDe = findViewById(R.id.edtChuDe);
        edtNoiDung = findViewById(R.id.edtNoiDung);
        btnHuy = findViewById(R.id.btnHuy);
        btnOKSms = findViewById(R.id.btnOKSms);
        object = getIntent().getParcelableExtra("data");
        loadData();
        btnHuy.setOnClickListener(view -> finish());
    }

    private void loadData() {
        if (object == null) {
            setTitle("Tạo Chủ Đề Sms");
            btnOKSms.setOnClickListener(view -> addSMS());
            return;
        }
        setTitle("Sửa chủ đề Sms");
        edtChuDe.setText(object.get(SmsObj.title));
        edtNoiDung.setText(object.get(SmsObj.content));
        btnOKSms.setOnClickListener(view -> editSMS());
    }

    private void addSMS() {
        if (!isValidete()) return;
        BaseObject object = new BaseObject();
        object.set(SmsObj.title, type);
        object.set(SmsObj.content, noidung);
        object.set(SmsObj.desc, Calendar.getInstance().getTimeInMillis());
        DbSupport.saveChuDe(object);
        finish();
        showToast("Đã thêm tin nhắn mẫu");
    }

    void editSMS() {
        if (!isValidete()) return;
        BaseObject object = new BaseObject();
        object.set(SmsObj.title, type);
        object.set(SmsObj.content, noidung);
        object.set(SmsObj.desc, Calendar.getInstance().getTimeInMillis());
        DbSupport.saveChuDe(object);
        finish();
        showToast("Đã sửa tin nhắn mẫu");
    }

    boolean isValidete() {
        type = edtChuDe.getText().toString();
        noidung = edtNoiDung.getText().toString();
        if (type.isEmpty()) {
            Toast.makeText(this, "Không có chủ đề tin nhắn", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (noidung.isEmpty()) {
            Toast.makeText(this, "Không có nội dung tin nhắn", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (DbSupport.isHaveRow(DbConfig.SMS, SmsObj.title, type)) {
            Toast.makeText(this, "Chủ đề tin nhắn đã có trong danh sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
