package com.project.shop.lemy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemy.telpoo2lib.utils.SprUtils;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.MySpr;
import com.project.shop.lemy.helper.MyTextWatcher;
import com.project.shop.lemy.nhacviec.ThanhHanhNiemActivity;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.widget.TextWatcherAdapter;

public class LoginActivity extends SwipeBackActivity {
    private EditText edtPass,edCodeNv,edCommand,  edFilterPhone;
    private TextView btnLogin;
    private LinearLayout lnChung;
    private ImageView btnShowText;
    Boolean isCheck = false;
    String pass;
    boolean isNewCodeNV=false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext= LoginActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        String phienBan=""+BuildConfig.VERSION_CODE;

        ((TextView)findViewById(R.id.phienban)).setText("Phiên bản: "+phienBan);

        //DangNhap();
    }

    void initView() {
        edFilterPhone= findViewById(R.id.edFilterPhone);
        if(MySpr.getPhoneBank(mContext)!=null)
        edFilterPhone.setText(MySpr.getPhoneBank(mContext));
        edtPass = findViewById(R.id.edtPass);
        edtPass.setText(SprSupport.getPass(LoginActivity.this));

        edCodeNv= findViewById(R.id.edCodeNv);
        edCodeNv.setText(SPRSupport.getString("codenv",mContext,""));
        edCommand=findViewById(R.id.edCommand);

        btnLogin = findViewById(R.id.btnLogin);
        lnChung = findViewById(R.id.lnChung);
        btnShowText = findViewById(R.id.btnShowText);
        pass = edtPass.getText().toString();

        edCodeNv.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                isNewCodeNV=true;
            }
        });
        edCommand.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String vs= edCommand.getText().toString();
                if (vs.equals("...")) stopSelf();

                if (vs.equals("thn")) startActivity(new Intent(mContext, ThanhHanhNiemActivity.class));

            }
        });



        edtPass.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                pass = s.toString();
                if (!pass.isEmpty()) {
                    btnShowText.setVisibility(View.VISIBLE);
                    btnShowText.setImageResource(R.drawable.ic_hidepass);
                } else btnShowText.setVisibility(View.GONE);

                if ("*#123".equals(pass)){
                    SPRSupport.save("allow_update",true,LoginActivity.this);
                }

                if ("*#321".equals(pass)){
                    SPRSupport.save("allow_update",false,LoginActivity.this);
                }
            }
        });

        btnLogin.setOnClickListener(view -> DangNhap());

        lnChung.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Keyboard.hideKeyboard(LoginActivity.this);
                    break;
            }
            return false;
        });

        btnShowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheck) {
                    edtPass.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowText.setImageResource(R.drawable.ic_hidepass);
                    isCheck = false;
                } else {
                    edtPass.setTransformationMethod(new HideReturnsTransformationMethod());
                    btnShowText.setImageResource(R.drawable.ic_hidepass);
                    isCheck = true;
                }
            }
        });

    }

    private void stopSelf() {
        int a=1/0;
    }

    private void DangNhap() {

        if (edtPass.getText().toString().length()>0){
            pass = edtPass.getText().toString();
            SprSupport.savePass(this, pass);
        }





        MySpr.savePhoneBank(edFilterPhone.getText().toString(),this);

        if (isNewCodeNV){
            if (edCodeNv.getText().toString().length()>0){
                SPRSupport.save("codenv",edCodeNv.getText().toString(),LoginActivity.this);
                showToast("Hãy mở lại ứng dụng");

            }
        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pass", pass);
        //startActivity(intent);
        finish();
    }
}
