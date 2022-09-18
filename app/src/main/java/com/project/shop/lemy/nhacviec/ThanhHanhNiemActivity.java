package com.project.shop.lemy.nhacviec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.db.MyDb;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.SPRSupport;

import java.util.ArrayList;

public class ThanhHanhNiemActivity extends MyActivity {
    EditText ed;
    Button btnSave;
//    private ArrayList<BaseObject> data;
    String data1;
    Context context = ThanhHanhNiemActivity.this;
    RecyclerView lv;
    ThanhHanhNiemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_hanh_niem2);
        lv = findViewById(R.id.lv);
        adapter= new ThanhHanhNiemAdapter(context);
        lv.setAdapter(adapter);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnStart).setOnClickListener(view -> {
            adapter.start();
        });
        findViewById(R.id.btnStop).setOnClickListener(view -> {
            adapter.stop();
        });

    }
}