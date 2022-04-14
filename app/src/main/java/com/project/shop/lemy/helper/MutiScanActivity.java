package com.project.shop.lemy.helper;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.shop.lemy.SwipeBackActivity;


public class MutiScanActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator integrator = new IntentIntegrator(MutiScanActivity.this);
        integrator.initiateScan();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String reultContent=null;
        if (scanResult!=null) reultContent= scanResult.getContents();
        Intent itBack=new Intent();
        itBack.putExtra("vl",reultContent);
        setResult(requestCode,itBack);

        finish();



    }
}
