package com.project.shop.lemy.xuatnhapkho;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;


public class XuatKhoActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String orderId=null;
        if (getIntent().hasExtra("orderid")){

            orderId=""+ getIntent().getExtras().get("orderid");
            setTitle("Xuất kho đơn hàng DHM"+orderId);
        }

//        if (getIntent().hasExtra("ghichu")){
//
//            String ghichu = "" + getIntent().getExtras().get("ghichu");
//            if (ghichu.toLowerCase().contains("block")){
//                Long count= SPRSupport.getCountPlus("block_"+orderId,this);
//                if(count==null) count=0L;
//
//                if (count==0){
//                    DialogSupport.dialogThongBao("Đơn bị chặn xuất kho, Báo cho Admin",this,object -> {
//
//                    });
//                }
//            }
//
//        }


        setTitle("Xuất kho đơn hàng");
        setContentView(R.layout.activity_xuat_kho);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        XuatHangFmv2 fm = XuatHangFmv2.newInstance(orderId);
        fragmentTransaction.replace(R.id.fragment_container, fm);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
