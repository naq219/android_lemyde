package com.project.shop.lemy.nhacviec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.project.shop.lemy.R;

public class NhacViecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhac_viec);

        //SprUtils.saveString();
        NhacViecServiceLayout.tagOpenView=true;

    }
}
