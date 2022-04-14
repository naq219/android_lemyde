package com.project.shop.lemy.xuatnhapkho;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNetProduct;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

public class ViewUpdateEan {

    private String productId;
    private Context context;
    private View view;

    View btnUpdateEan,vCancel,btnKhongCo;
    EditText ed;
    BaseModel baseModel;
    private AlertDialog dl;

    public  ViewUpdateEan(String productId,Context context){
        this.productId = productId;
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_dialog_updateean,null);
        btnUpdateEan = view.findViewById(R.id.btnUpdateEan);
        vCancel = view.findViewById(R.id.vCancel);
        btnKhongCo = view.findViewById(R.id.btnKhongCo);
        ed = view.findViewById(R.id.ed);

        vCancel.setOnClickListener(view1 -> {
            closeDialog();
        });

    }

    public View getView(){
        btnKhongCo.setOnClickListener(view1 -> {
            if (ed.getText().toString().isEmpty()){
                ed.setText("no");
                return;
            }

            callUpdateEan();
        });

        btnUpdateEan.setOnClickListener(view1 -> {
            callUpdateEan();
        });


        baseModel = new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                Toast.makeText(context,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                closeDialog();
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                Toast.makeText(context,"LỖI CẬP NHẬT: "+msg,Toast.LENGTH_LONG).show();
            }
        };



        return view;
    }

    private void closeDialog() {
        if (dl!=null) dl.cancel();
    }

    private void callUpdateEan() {
        BaseObject oj = new BaseObject();
        oj.set(ProductObj.ean,ed.getText().toString());
        TaskNetProduct.extaskUpdate(baseModel,productId,oj,context);


    }

    public void setDialog(AlertDialog dl) {
        this.dl = dl;
    }
}
