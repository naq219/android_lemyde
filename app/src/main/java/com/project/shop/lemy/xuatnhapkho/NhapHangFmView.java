package com.project.shop.lemy.xuatnhapkho;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.search.SearchSugestAdapter;

public class NhapHangFmView extends BaseFragment {

    protected TextView  tvChonKieuNhap, tvAddPhieuNhap;
    protected TextView tvChonKhoNh;
    protected EditText edtGhichu, edtOrderId;
    protected SearchView searchChonSanPhamNh;
    protected View prBarNh;
    protected ScrollView scView;
    protected ImageView imgBarCode;
    protected SearchSugestAdapter searchSugestAdapter;
    protected V2AddNhapHangAdapter addNhapHangAdapter;
    protected RecyclerView recycleView;
    protected RelativeLayout rlBarcodeNh;
    protected LinearLayout lnSrView;
    protected CompoundBarcodeView barCodeView;
    protected ImageView imgCanceBarCode;
    protected ImageView imgFlash;
    protected ImageView btnStart;
    protected ImageView btnThuGon;
    protected View dong_mo;

   // String type;
    boolean isFlash;
    boolean isStart = false;

    public NhapHangFmView() {
    }

    public static NhapHangFm newInstance() {
        NhapHangFm fragment = new NhapHangFm();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // type = getArguments().getString(NHAP_HANG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nhap_hang_fm, container, false);
        initView(v);
        return v;
    }


    public void initView(View view) {
        tvChonKhoNh = view.findViewById(R.id.tvChonKhoNh);
        tvChonKieuNhap = view.findViewById(R.id.tvChonKieuNhap);
        tvAddPhieuNhap = view.findViewById(R.id.tvAddPhieuNhap);
        edtGhichu = view.findViewById(R.id.edtGhichu);
        searchChonSanPhamNh = view.findViewById(R.id.searchChonSanPhamNh);
        recycleView = view.findViewById(R.id.recycleView);
        edtOrderId = view.findViewById(R.id.edtOrderId);
        prBarNh = view.findViewById(R.id.prBarNh);
        dong_mo = view.findViewById(R.id.dong_mo);
        scView = view.findViewById(R.id.scView);
        lnSrView = view.findViewById(R.id.lnSrView);
        btnThuGon= view.findViewById(R.id.btnThuGon);
        imgBarCode = view.findViewById(R.id.imgBarCode);
        rlBarcodeNh = view.findViewById(R.id.rlBarcodeNh);
        barCodeView = view.findViewById(R.id.barCodeView);
        imgCanceBarCode = view.findViewById(R.id.imgCanceBarCode);
        imgFlash = view.findViewById(R.id.imgFlash);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setImageResource(R.drawable.ic_pause);

        addNhapHangAdapter = new V2AddNhapHangAdapter(getActivity(), "");
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(addNhapHangAdapter);

        searchSugestAdapter = new SearchSugestAdapter(getActivity());

        EditText searchSanPhamNh = searchChonSanPhamNh.findViewById(androidx.appcompat.R.id.search_src_text);
        searchSanPhamNh.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchSanPhamNh.setTextSize(14);
        searchChonSanPhamNh.setQueryHint("Tìm tên sản phẩm");
        searchChonSanPhamNh.setIconifiedByDefault(false);



        scView.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Keyboard.hideKeyboard(getActivity());
                    break;
            }
            return false;
        });

        recycleView.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Keyboard.hideKeyboard(getActivity());
                    break;
            }
            return false;
        });

        btnThuGon.setOnClickListener(view1 -> {
            if (dong_mo.getVisibility()==View.VISIBLE){
                dong_mo.setVisibility(View.GONE);
                btnThuGon.setImageResource(R.drawable.mora);
            }

            else {
                dong_mo.setVisibility(View.VISIBLE);
                btnThuGon.setImageResource(R.drawable.thugon);
            }



        });

    }






    protected boolean validateId(String id) {
        if (addNhapHangAdapter.checkTrungId(id)) {
            return false;
        }
        return true;
    }


    protected void clickStartStop() {
        if (isStart) {
            barCodeView.resume();
            btnStart.setImageResource(R.drawable.ic_pause);
            isStart = false;
        } else {
            barCodeView.pause();
            btnStart.setImageResource(R.drawable.ic_start);
            isStart = true;
        }
    }

    protected void stopScanCode(){
        barCodeView.pause();
        btnStart.setImageResource(R.drawable.ic_start);
        isStart = true;
    }

    protected void clickFlash() {

        if (!isFlash) {
            barCodeView.setTorchOn();
            imgFlash.setImageResource(R.drawable.ic_flash_off);
            isFlash = true;
        } else {
            barCodeView.setTorchOff();
            imgFlash.setImageResource(R.drawable.ic_flash_on);
            isFlash = false;
        }
    }


    protected void resetAdd() {
        Fragment fragment = newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        barCodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barCodeView.pause();
        super.onPause();
    }
}
