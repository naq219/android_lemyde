package com.project.shop.lemy.sanpham;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;

/**
 * vẫn đang dùng nhưng k nên dùng tiếp
 * @deprecated
 */
public class DetailProductActivity extends SwipeBackActivity {

    public static void startActivity(Activity activity,String productID) {
        Intent intent= new Intent(activity,DetailProductActivity.class);
        if (productID!=null)
        intent.putExtra("id",productID);
        activity.startActivity(intent);
    }

    public static void startActivityAddNew(Activity activity, String newSpString) {
        Intent intent= new Intent(activity,DetailProductActivity.class);
        if (newSpString!=null)
            intent.putExtra(SPFm.OBJ_PARAM_ADDNEW,newSpString);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_product_activity);


        String productId= null;
        if (getIntent().hasExtra("id")) {
            productId= getIntent().getStringExtra("id");
            setTitle("Chi tiết sản phẩm");
        }
        else setTitle("Thêm Sản phẩm");

        String newSpName=null;
        if (getIntent().hasExtra(SPFm.OBJ_PARAM_ADDNEW)) {
            newSpName= getIntent().getStringExtra(SPFm.OBJ_PARAM_ADDNEW);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SPFm.newInstance(productId,newSpName))
                    .commitNow();
        }
    }
}
