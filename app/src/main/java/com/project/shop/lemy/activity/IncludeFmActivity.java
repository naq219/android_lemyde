package com.project.shop.lemy.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.donhang.DonHangFm;

import java.lang.reflect.Constructor;

public class IncludeFmActivity extends MyActivity {
    String cl="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_includefm);

        cl= getIntent().getStringExtra("class");
        if (cl.equals(DonHangFm.class.toString())){

            DonHangFm fm = new DonHangFm(getIntent().getStringExtra("keysearch"));
            pushfm(fm);
        }


    }

    private void pushfm(Fragment fm) {
        try {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fm);
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
