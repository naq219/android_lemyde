package com.project.shop.lemy.donhang;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskSupport;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.PermissionSupport;
import com.telpoo.frame.model.BaseModel;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.SPRSupport;

import java.util.List;

public class SmartPushVC extends AppCompatActivity implements View.OnClickListener {
    private CompoundBarcodeView barCodeView;
    View btnPush;
    RadioButton rdDuongBo,rdDuongBay;
    TextView tvLog;
    BarcodeCallback callback;
    ImageView imgFlash;
    private boolean isFlash=false;
    EditText edmadh;
    Button btnstartStop;

    BaseModel baseModel;
    TaskNet taskNet;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= SmartPushVC.this;
        setContentView(R.layout.activity_smart_push_vc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        edmadh.postDelayed(() -> {
            Keyboard.hideKeyboard((Activity) context);
        },200);
        scandBarCode();

        taskNet = new TaskNet();
        taskNet.setModel(baseModel);

        baseModel = new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);

                switch (taskType){
                    case TaskType.TASK_DETAILORDER:
                        BaseObject oj= (BaseObject) data;

                        String dvvv = null;
                        if (rdDuongBo.isChecked())dvvv= ProcessDonHangActivity.ghtk_road;
                        if (rdDuongBay.isChecked())dvvv= ProcessDonHangActivity.ghtk_fly;
                        ProcessDonHangActivity.pushDonHangHvc(context, oj,dvvv);
                        break;
                }

            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                switch (taskType){
                    case TaskType.TASK_DETAILORDER:
                       DialogSupport.dialogThongBao("có lỗi hệ thống: "+msg,context,null);
                        break;
                }

            }
        };


        if (getIntent().getIntExtra("order_id",0)!=0){

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgFlash:
                clickFlash();
                break;
            case R.id.startStop:
                startStop(null);
                break;

            case R.id.btnPush:
                getOrderDetail();
                break;
        }
    }

    private void getOrderDetail() {
        if (!rdDuongBay.isChecked()&& !rdDuongBo.isChecked() ){
            Toast.makeText(this,"Hãy chọn đường bộ - đường bay trước!",Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId= edmadh.getText().toString();
        edmadh.setText("");
        DialogSupport.dialogYesNo("Bắn đơn: "+orderId,SmartPushVC.this, isYes -> {
            if (!isYes) return;
            TaskSupport.callDetailOrder(orderId,baseModel,context);
        });
    }

    private void startStop(Integer tag) {
        int mtag= (int) btnstartStop.getTag();
        if (tag!=null)mtag=tag;
        if (!rdDuongBay.isChecked()&& !rdDuongBo.isChecked() ){
            Toast.makeText(this,"Hãy chọn đường bộ - đường bay trước!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mtag==0){ // chua quet ma
            barCodeView.setVisibility(View.VISIBLE);
            btnstartStop.setTag(1);
            btnstartStop.setText("Dừng quét");
        }
        else {
            barCodeView.setVisibility(View.INVISIBLE);
            btnstartStop.setTag(0);
            btnstartStop.setText("Quét mã");
        }


    }

    private void initView() {
        barCodeView = findViewById(R.id.barCodeView);
        btnstartStop = findViewById(R.id.startStop);
        btnstartStop.setTag(0);
        btnstartStop.setOnClickListener(this);
        btnPush = findViewById(R.id.btnPush);
        rdDuongBo = findViewById(R.id.rdDuongBo);
        rdDuongBay = findViewById(R.id.rdDuongBay);
        tvLog = findViewById(R.id.tvLog);
        imgFlash = findViewById(R.id.imgFlash);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        imgFlash.setOnClickListener(this);
        edmadh= findViewById(R.id.edmadh);

        btnPush.setOnClickListener(this);


    }

    public void scandBarCode() {
        if (!PermissionSupport.getInstall(this).requestPermissionCamera()) return;
        barCodeView.setTorchOff();
        barCodeView.resume();
        barCodeView.setStatusText("");

         BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barCodeView.setStatusText(result.getText());

                    if ((int)btnstartStop.getTag()!=1) return; //chưa quét mã

                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.beep);
                    mediaPlayer.start();

                    startStop(1);
                    String orderId = result.getText();

                    edmadh.setText(orderId);

                    btnPush.postDelayed(() -> {
                        btnPush.performClick();
                    },200);



                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };

        barCodeView.decodeContinuous(callback);

    }

    private void clickFlash() {
        if (!isFlash) {
            barCodeView.setTorchOn();
            imgFlash.setImageResource(R.drawable.ic_flash_off);
            isFlash = true;
        } else {
            barCodeView.setTorchOff();
            imgFlash.setImageResource(R.drawable.ic_flash_on);
            isFlash = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startStop(0);

        tvLog.setText(SPRSupport.getString(SprSupport.KEY_LOGPUSHDVVC,context));
    }
}
