package com.project.shop.lemy.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.project.shop.lemy.R;
import com.project.shop.lemy.donhang.EditDhFm;

public class TestActivity extends AppCompatActivity {
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ed= findViewById(R.id.ed);
    }

    public void click(View view) {
        String str= ed.getText().toString();
               Uri location = Uri.parse(str);
               Intent intent = new Intent(Intent.ACTION_VIEW, location);
               //intent.setPackage("com.facebook.pages.app");
               startActivity(intent);
    }
}