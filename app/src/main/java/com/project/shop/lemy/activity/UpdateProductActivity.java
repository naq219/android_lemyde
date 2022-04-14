package com.project.shop.lemy.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

public class UpdateProductActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTensp, tvGiavonedt, tvGiabuonedt, tvGialeedt, tvTrangthaisp, tvHuy, tvLuu;
    BaseObject objChange = new BaseObject();
    TaskNet taskNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        intView();
    }

    private void intView() {
        objChange = getIntent().getParcelableExtra("obj");
        tvTensp = findViewById(R.id.tvTensp);
        tvGiavonedt = findViewById(R.id.tvGiavonedt);
        tvGiabuonedt = findViewById(R.id.tvGiabuonedt);
        tvGialeedt = findViewById(R.id.tvGialeedt);
        tvTrangthaisp = findViewById(R.id.tvTrangthaisp);
        tvHuy = findViewById(R.id.tvHuy);
        tvLuu = findViewById(R.id.tvLuu);
        tvHuy.setOnClickListener(this);
        tvLuu.setOnClickListener(this);
        tvTensp.setText(objChange.get(ProductObj.name));
        tvGiavonedt.setText(objChange.get(ProductObj.cost_price));
        tvGiabuonedt.setText(objChange.get(ProductObj.wholesale_price));
        tvGialeedt.setText(objChange.get(ProductObj.retail_price));
        MenuFromApiSupport.setTextStatus(this, tvTrangthaisp, objChange.get(ProductObj.status), TaskType.TASK_LIST_STATUS_PRODUCT);
    }

    private void updateProduct() {
        taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject object = (BaseObject) data;
                finish();
                Toast.makeText(UpdateProductActivity.this, object.get("message"), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                Toast.makeText(UpdateProductActivity.this, msg, Toast.LENGTH_LONG).show();

            }
        }, TaskType.TASK_UPDATE_SANPHAM, this);
        taskNet.setTaskParram("parram", objChange);
        taskNet.exe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvHuy:
                finish();
                break;
            case R.id.tvLuu:
                updateProduct();
                break;
        }
    }
}