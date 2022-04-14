package com.project.shop.lemy;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Toast;

import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.listener.ListenBack;


public class MyActivity extends AppCompatActivity {
    protected ProgressDialog loadingProgress;
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


    public void showProgressDialog( String message) {

        closeProgressDialog();

        loadingProgress = new ProgressDialog(this);
        loadingProgress.setMessage(message);
        loadingProgress.setCanceledOnTouchOutside(false);
        loadingProgress.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String pass=""+SprSupport.getPass(getBaseContext());
//        if (!pass.contains("1080")) startActivity(new Intent(this,LoginActivity.class));

        


    }

    public void showDl(String title){
        DialogSupport.dialogThongBao(title,this,null);
    }

    public void showDl(String title, ListenBack listenBack){
        DialogSupport.dialogThongBao(title,this,listenBack);
    }

    public void showProgressDialog() {
        closeProgressDialog();
        loadingProgress = new ProgressDialog(this);
        loadingProgress.setMessage("Loading");
        loadingProgress.setCanceledOnTouchOutside(false);
        loadingProgress.show();
    }

    public void closeProgressDialog() {
        if (loadingProgress != null) {
            if (loadingProgress.isShowing())
                loadingProgress.dismiss();
            loadingProgress = null;
        }
    }

    public void openActivity(Class<?> classto){
        startActivity(new Intent(this,classto));
    }
}
