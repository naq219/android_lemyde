package com.project.shop.lemy.donhang;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.telpoo.frame.object.BaseObject;

public class EditDonHangActivity extends SwipeBackActivity {
    private BaseObject object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_donhang_activity);
    }

}
