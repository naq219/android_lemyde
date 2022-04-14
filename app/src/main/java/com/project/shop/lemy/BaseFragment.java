package com.project.shop.lemy;

import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.listener.ListenBack;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Ducqv on 9/6/2018.
 */

public class BaseFragment extends Fragment {

    private ProgressDialog loadingProgress;
    private Integer mBackStateName=null;

    public  String getSimpName(){
        return this.getClass().getSimpleName();
    }

    public void showToast(String message) {
        if (message != null)
            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_LONG).show();
    }

    public void showDl(String title){
        DialogSupport.dialogThongBao(title,getContext(),null);
    }

    public void showDl(String title, ListenBack listenBack){
        DialogSupport.dialogThongBao(title,getContext(),listenBack);
    }

    @Override
    public void onResume() {
        super.onResume();
            //showToast(getSimpName());
    }

    public void showProgressDialog() {
        closeProgressDialog();

        loadingProgress = new ProgressDialog(getActivity());
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

    public int getBackStateName() {
        if (mBackStateName==null)
            mBackStateName=  (int)(Calendar.getInstance().getTimeInMillis()/1000)-new Random().nextInt(100) ;

        return mBackStateName;
    }
}
