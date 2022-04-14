package com.project.shop.lemy.chucnangkhac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.lemy.telpoo2lib.utils.SprUtils;

import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.helper.FileUtils;
import com.project.shop.lemy.helper.MyTextWatcher;
import com.telpoo.frame.utils.SPRSupport;

import lib.folderpicker.FolderPicker;

public class AdminSettingActivity extends MyActivity {

    public static final String SPR_ENABLE_RUN=" AdminSettingActivity SPR_ENABLE_RUN";
    public static final String SPR_DELAY_START=" AdminSettingActivity SPR_DELAY_START";
    public static final String SPR_DELAY_END= " AdminSettingActivity SPR_DELAY_END";
    public static final String SPR_LOCATION_PHAP = " AdminSettingActivity SPR_LOCATION_PHAP";
    private static final int MY_REQUEST_CODE_PERMISSION = 342;
    private static final int MY_RESULT_CODE_FILECHOOSER = 927;
    EditText edChuong,edPhapChoose, edPhapStart, edPhapEnd;
    Context mcontext;
    RadioButton rdChay,rdDung;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext= AdminSettingActivity.this;
        setContentView(R.layout.ac_admin_setting);
        edChuong= findViewById(R.id.edChuong);
        edPhapEnd = findViewById(R.id.edPhapEnd);
        edPhapStart = findViewById(R.id.edPhapStart);
        rdChay= findViewById(R.id.rdChay);
        rdDung = findViewById(R.id.rdDung);
        edPhapChoose = findViewById(R.id.edPhapChoose);
        edChuong.setText(""+SprUtils.getLong("edChuong",this,-1l));
        edChuong.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s11) {
                long s= -1;
                try {
                    s= Long.parseLong(edChuong.getText().toString());
                }catch ( Exception e){};
                SprUtils.saveLong("edChuong",s,AdminSettingActivity.this);
            }
        });

        edPhapStart.setText(""+ SPRSupport.getAsInt(SPR_DELAY_START,this));
        edPhapEnd.setText(""+SPRSupport.getAsInt(SPR_DELAY_END,this));
        edPhapChoose.setText(""+SPRSupport.getString(SPR_LOCATION_PHAP,this));

        edPhapStart.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                SPRSupport.save(SPR_DELAY_START,edPhapStart.getText().toString(),mcontext);
            }
        });

        edPhapEnd.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                SPRSupport.save(SPR_DELAY_END,edPhapEnd.getText().toString(),mcontext);
            }
        });

        edPhapChoose.addTextChangedListener(new MyTextWatcher(){

            @Override
            public void afterTextChanged(Editable s) {
                SPRSupport.save(SPR_LOCATION_PHAP,edPhapChoose.getText().toString(),mcontext);
            }
        });


        boolean enableRun= SPRSupport.getBool(SPR_ENABLE_RUN,mcontext,false);
        if (enableRun)
            rdChay.setChecked(true);
        else rdDung.setChecked(true);

       edPhapChoose.postDelayed(() -> {
           rdChay.setOnCheckedChangeListener( (buttonView, isChecked) -> {
               SPRSupport.save(SPR_ENABLE_RUN,isChecked,mcontext);
           });

           rdDung.setOnCheckedChangeListener((buttonView, isChecked) -> {
               SPRSupport.save(SPR_ENABLE_RUN,!isChecked,mcontext);
           });

       },1000);


    }

    public void clickChoosePhap(View view) {
        askPermissionAndBrowseFile();
    }

    private void askPermissionAndBrowseFile()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            int permisson = ActivityCompat.checkSelfPermission(mcontext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile()  {

        Intent intent = new Intent(this, FolderPicker.class);


        intent.putExtra("title", "Chọn File Âm chuông");

        //To begin from a selected folder instead of sd card's root folder. Example : Pictures directory
        //intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.).getAbsolutePath());
        //To pick files
       // intent.putExtra("pickFiles", true);
        startActivityForResult(intent, MY_RESULT_CODE_FILECHOOSER);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    this.doBrowseFile();
                }
                // Cancelled or denied.
                else {
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();

                        String folderLocation = ""+data.getExtras().getString("data");
                        edPhapChoose.setText(folderLocation);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
