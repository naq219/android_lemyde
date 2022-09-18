package com.project.shop.lemy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.project.shop.lemy.common.ScreenSupport;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.db.DbSupport;
import com.project.shop.lemy.donhang.DonHangFmv2;
import com.project.shop.lemy.helper.MySpr;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.nhacviec.NhacViecService;
import com.project.shop.lemy.nhacviec.ThanhHanhNiemActivity;
import com.project.shop.lemy.sanpham.SanPhamFm;

import java.util.ArrayList;

import io.sentry.Sentry;

/**
 * danh sach don hang {@link DonHangFmv2}
 */
public class MainActivity extends MainActivityLayout {


    DrawerLayout drawer;
    private TextView tvVersion;
    private TextView textView;
    MainActivity mContext = MainActivity.this;
    boolean isPermissionDOne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenSupport.BlockScreen(this);
        drawer = findViewById(R.id.drawer_layout);

        Sentry.captureMessage("testing SDK setup"+Build.MODEL);

        String pass = "" + SprSupport.getPass(getBaseContext());
        if (!pass.contains("10801")) return;

        String[] per = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Permissions.check(this, per, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                isPermissionDOne = true;
                innitDb();
                readSms();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(mContext)&& MySpr.isEnableShowTaskCK(mContext))
                        startService(new Intent(MainActivity.this, NhacViecService.class));
                }


            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                showDl("Bạn chưa cấp đủ quyền để tiếp tục", object -> {
                    finish();
                });
            }
        });




        tvTitle.postDelayed(() -> {
            replaceFragment(new DonHangFmv2());

        }, 600);
        tvTitle.postDelayed(() -> {
            replaceFragment(new SanPhamFm());

        }, 300);


        //ChiTietDonHangActivity.startActivity(MainActivity.this,"22203");
        //startActivity(new Intent(this, ThanhHanhNiemActivity.class));

        startService(new Intent(this, NhacViecService.class));
    }



    private void readSms() {

        if (MySpr.getPhoneBank(mContext)!=null && MySpr.getPhoneBank(mContext).length()>0&&!MySpr.getPhoneBank(mContext).equals("abc")) {
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_SMS

                    ).withListener(new BaseMultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    super.onPermissionsChecked(multiplePermissionsReport);

                }
            }).check();
        } 

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 22);
        } else {
            //initializeView();
        }
    }

    public void innitDb() {
        if (PermissionSupport.hasPermissionGranted(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            DbSupport.init(getApplicationContext());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionSupport.READ_PERMISSIONS_STORAGE) innitDb();
    }


//    public void pushFm(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
//
//    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();




// notificationId is a unique int for each notification that you must define



        new Handler().postDelayed(() -> {
            //KeyboardSupport.hideSoftKeyboard(MainActivity.this);

        }, 500);

        new Handler().postDelayed(() -> {
            //if (!PermissionSupport.hasPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) return;
            String pass = "" + SprSupport.getPass(getBaseContext());
            if (!pass.contains("10801")) startActivity(new Intent(this, LoginActivity.class));
        }, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
