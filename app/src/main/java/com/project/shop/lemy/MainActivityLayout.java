package com.project.shop.lemy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lemy.library.UpdateChecker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.project.shop.lemy.chucnangkhac.AdminSettingActivity;
import com.project.shop.lemy.common.SpinnerSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.donhang.DonHangFm;
import com.project.shop.lemy.donhang.ThayDoiTrangThaiDonHangFm;
import com.project.shop.lemy.fragment.OrderProfuctFm;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.sanpham.SanPhamFm;
import com.project.shop.lemy.smskhachhang.QlySmsFm;
import com.project.shop.lemy.xuatnhapkho.ChuyenKhoangFm;
import com.project.shop.lemy.xuatnhapkho.NhapHangFm;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLayout extends MyActivity {
    TextView tvTitle;
    Spinner sp;
    int maxItem=0;
    long versionCode=-1;
    //List<String> fmTitle= new ArrayList<>();
    List<Integer> fmIcon= new ArrayList<>();
    List<Fragment> fm= new ArrayList<>();//={DonHangFm.newInstance("",""), SanPhamFm.newInstance(),};
    BottomNavigationView navView;
    View btnGoDetail;
    public Context mContent= MainActivityLayout.this;
    EditText edDhm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        try {
            versionCode= PackageInfoCompat.getLongVersionCode(getPackageManager().getPackageInfo(getPackageName(),0));
            //versionCode= getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main_bottom);
        initView();

    }

    private void initView() {
        btnGoDetail = findViewById(R.id.btnGoDetail);
        edDhm= findViewById(R.id.edDhm);
        edDhm.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                String idGo = edDhm.getText().toString();
                if(idGo.length()==0){
                    showToast("Chưa nhập Mã DH "); return true;
                }

                edDhm.setText("");

                ChiTietDonHangActivity.startActivity(mContent,idGo);

                return true;
            }
            return false;
        });

        btnGoDetail.setOnClickListener(view -> {
            DialogSupport.dialogThongBao("ahihi",mContent,null);
        });
        navView = findViewById(R.id.nav_view);
        maxItem=navView.getMaxItemCount();

        sp= findViewById(R.id.sp);
        tvTitle = findViewById(R.id.title);

        String[] hmData={"- DANH MỤC -","ĐƠN HÀNG","SẢN PHẨM","NHẬP KHO","CHUYỂN KHOANG ","Đăng Nhập","SMS Manager","Cập Nhật","Admin Setting","CHANGE STATUS","Thoát"};
        Fragment[] fmAdd={new DonHangFm(),new SanPhamFm(),new NhapHangFm(),null, QlySmsFm.newInstance()};

        SpinnerSupport.setUpMenu(this,sp,hmData);

        tvTitle.setOnClickListener(view -> {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Fragment fmc;
                    switch (i){
                        case 2: fmc= SanPhamFm.newInstance(); break;
                        case 1: fmc=new DonHangFm(); break;
                        case 3: fmc=new NhapHangFm(); break;
                        case 4: fmc=new ChuyenKhoangFm(); break;
                        case 5:  startActivity(new Intent(MainActivityLayout.this,LoginActivity.class));
                            return;
                        case 6: fmc=new QlySmsFm(); break;
                        case 7:
                            UpdateChecker.checkForDialog(MainActivityLayout.this); return;
                        case 8:
                            startActivity(new Intent(MainActivityLayout.this, AdminSettingActivity.class));
                            return;
                        case 9:
                            fmc= ThayDoiTrangThaiDonHangFm.newInstance(); break;

                        case  10:
                            fmc= OrderProfuctFm.newInstance("1", "2");
                            finishAndRemoveTask();
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                           // Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);
                            //Process.sendSignal(Process.myPid(),Process.SIGNAL_KILL);


                            break;
                        default: return;
                    }
                    replaceFragment(fmc);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sp.setSelection(0,true);

            sp.performClick();
        });

        navView.setOnNavigationItemSelectedListener(menuItem -> {
            replaceFragment(getSupportFragmentManager().findFragmentByTag(""+menuItem.getItemId()));
            return true;
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            updateMenu();
        });

        setTitle("MENU");

    }

    private void checkUpdate() {

        if (SPRSupport.getBool("allow_update",this,false)){
            new Handler().postDelayed(() -> {
                if (PermissionSupport.hasPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    UpdateChecker.checkListener(this, b -> {
                        Mlog.D("checkupdate no update");
                        if (!b) return;

                        UpdateChecker.checkForDialog(this);
                    });
                }
            },2000);
        }
    }

    protected void replaceFragment (Fragment fragment2){
        BaseFragment fragment = (BaseFragment) fragment2;
        String backStateName =  ""+fragment.getBackStateName();//+saveDh1;

        FragmentManager manager = getSupportFragmentManager();
        Fragment fm2=manager.findFragmentByTag(backStateName);
        if (fm2==null) {
            FragmentTransaction ft = manager.beginTransaction();

            ft.add(R.id.fragment_container, fragment, backStateName);
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);


            try {
                ft.commit();
            }
            catch (Exception e){}
            tvTitle.postDelayed(() -> {
                Menu mn = navView.getMenu();
                if (mn.size()==0) return;
                mn.getItem(mn.size()-1).setChecked(true);
            },200);
        }
        else {
            FragmentTransaction ft = manager.beginTransaction();
            manager.getFragments().forEach(fragment1 -> {
                if (fragment1.isVisible()) ft.hide(fragment1);
            });
            ft.show(fm2).commit();

        }



    }




    void updateMenu(){
        FragmentManager manage = getSupportFragmentManager();

        List<Fragment> fm1 = manage.getFragments();
        Menu menu = navView.getMenu();
        menu.clear();
        int startI=0;
        if (fm1.size()>maxItem) startI=fm1.size()-maxItem;
        for (int i = startI; i < fm1.size(); i++) {
            BaseFragment fm0= (BaseFragment) fm1.get(i);
            int icon=R.drawable.ic_dashboard_black_24dp;
            String simpName = fm0.getClass().getSimpleName();
            String mTItle=simpName;
            if(simpName.equals(DonHangFm.class.getSimpleName())){
                icon= R.drawable.order99;
                mTItle="Đơn Hàng";
            }
            if(simpName.equals(SanPhamFm.class.getSimpleName())){
                icon= R.drawable.product99;
                mTItle="Sản Phẩm";
            }
            if(simpName.equals(NhapHangFm.class.getSimpleName())){
                icon= R.drawable.nhapkho99;
                mTItle="Nhập kho";
            }
            if(simpName.equals(ThayDoiTrangThaiDonHangFm.class.getSimpleName())){
                icon= R.drawable.changestatus99;
                mTItle="Change Status";
            }

            menu.add(Menu.NONE, fm0.getBackStateName(), Menu.NONE, mTItle)
                .setIcon(icon);
        }

        if (manage.getFragments().size()>maxItem){
            manage.beginTransaction().remove(manage.getFragments().get(0)).commit();
        }
    }

    void addAMenu(){
        Menu menu = navView.getMenu();
        for (int i = 0; i < maxItem; i++) {
            if (i==fm.size()) break;
            menu.removeItem(i);
        }
        for (int i = 0; i < maxItem; i++) {
            if (i==fm.size()) break;
            menu.add(Menu.NONE, i, Menu.NONE, "")
                    .setIcon(fmIcon.get(i));
        }


//        menu.add(Menu.NONE, 12, Menu.NONE, "sda")
//                .setIcon(R.drawable.ic_dashboard_black_24dp);
    }

    void setTitle(String title){

        title = title + " " + versionCode;
        if (BuildConfig.BUILD_TYPE.equals("debug"))
            title+=" >\"< DEBUG TEST";
        super.setTitle(title);
        tvTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thoát ứng dụng")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Không", null)
                .show();

    }
}
