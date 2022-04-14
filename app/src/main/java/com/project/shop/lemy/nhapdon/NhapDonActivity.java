package com.project.shop.lemy.nhapdon;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;

public class NhapDonActivity extends MyActivity {
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nhapdon_activity);
        context= NhapDonActivity.this;
        ViewNhapDonAc view= new ViewNhapDonAc(context);

        RelativeLayout root=findViewById(R.id.root);
        root.addView(view.getView());

        root.findViewById(R.id.vgOpen).setVisibility(View.VISIBLE);


    }
}
