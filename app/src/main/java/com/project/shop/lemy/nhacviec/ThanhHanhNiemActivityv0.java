package com.project.shop.lemy.nhacviec;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.SPRSupport;

public class ThanhHanhNiemActivityv0 extends MyActivity {
    EditText ed;
    Button btnSave;
//    private ArrayList<BaseObject> data;
    String data1;
    Context context = ThanhHanhNiemActivityv0.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_hanh_niem);
        ed= findViewById(R.id.ed);
        btnSave= findViewById(R.id.btnSave);

        data1 = SPRSupport.getString("thanhanhniem",context);


       if (data1!=null && data1.length()>10){
           ed.setText(data1);
       }

       btnSave.setOnClickListener(view -> {
           DialogSupport.simpleYesNo(this,"Yes","No","OK SAVE?","",(value, where) -> {
               if (where==1){
                   SPRSupport.save("thanhanhniem",ed.getText().toString(),context);
               }
           });
       });


    }
}