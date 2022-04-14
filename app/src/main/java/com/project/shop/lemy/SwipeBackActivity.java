package com.project.shop.lemy;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.common.SwipeBackLayout;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.listener.ListenBack;


import static androidx.core.view.ViewCompat.animate;


public class SwipeBackActivity extends AppCompatActivity implements TaskType {

    private SwipeBackLayout swipeBackLayout;
    public Toolbar toolbar;
    public View progress;

    ProgressDialog progressDialog;

    BaseFragment curFragment;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_swipe_back);
        swipeBackLayout = findViewById(R.id.swipe_layout);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        swipeBackLayout.addView(view);
        initSwipeBack();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //showToast("activit "+this.getClass().getSimpleName());
    }

    public void setCurrentFragment(BaseFragment curFragment){

        this.curFragment = curFragment;
    }

    public void showProcessDialog(String sms) {
        progressDialog = ProgressDialog.show(this, "",
                sms, false);
    }

    public void showProcessDialog() {

        if (progressDialog!=null){

            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(this, "",
                "Loading...", false);
    }

    public void closeProcessDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }


    public void hindeProgress() {
        if (progress == null) return;
        progress.setVisibility(View.GONE);
    }

    public void showProgress() {
        if (progress == null) return;
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    void initSwipeBack() {
        swipeBackLayout = findViewById(R.id.swipe_layout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        swipeBackLayout.setEnablePullToBack(false);

        findViewById(R.id.touch_swipe_enable).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    swipeBackLayout.setLocked(true);
                    swipeBackLayout.setEnablePullToBack(true);
                    if (mOnSwipe != null) mOnSwipe.onSwipe(true);
                }
                return false;
            }
        });
        findViewById(R.id.touch_swipe_disable).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    swipeBackLayout.setLocked(false);
                    swipeBackLayout.setEnablePullToBack(false);
                    if (mOnSwipe != null) mOnSwipe.onSwipe(false);
                }
                return false;
            }
        });

        findViewById(R.id.swipe_bg).setBackgroundColor(Color.parseColor("#bb000000"));
        swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                animate(findViewById(R.id.swipe_bg)).alpha(1 - fractionScreen).setDuration(0).start();
                if (fractionScreen <= 0)
                    if (mOnSwipe != null) mOnSwipe.onSwipe(false);
            }
        });
    }

    public void setEnablePullToBack(boolean b) {
        swipeBackLayout.setLocked(b);
        swipeBackLayout.setEnablePullToBack(b);
        findViewById(R.id.touch_swipe_enable).setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    OnSwipe mOnSwipe;

    public void setOnSwipe(OnSwipe onSwipe) {
        mOnSwipe = onSwipe;
    }

    public interface OnSwipe {
        void onSwipe(boolean swipe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("onOptionsItemSelected", "call finish activity from home button");
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showToast(String message) {
        if (message != null)
            Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show();
    }

    public void showDl(String msg){
        DialogSupport.dialogThongBao(msg,this,null);
    }

    public void showThongBao(String message, ListenBack listenBack) {
        if (message == null) message = "??";
        new AlertDialog.Builder(this)
                // other setter methods
                .setNeutralButton(message, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if (listenBack != null)
                        listenBack.OnListenBack(0);
                })
                .setOnDismissListener(dialogInterface -> {
                    if (listenBack != null)
                        listenBack.OnListenBack(0);
                })
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (curFragment!=null)
        curFragment.onActivityResult(requestCode,resultCode,data);
    }
}
