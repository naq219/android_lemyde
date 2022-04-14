package com.project.shop.lemy.xuatnhapkho;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.search.SearchSugestAdapter;
import com.telpoo.frame.widget.TextWatcherAdapter;

import java.util.Calendar;
import java.util.List;

public class XuatHangFmView extends BaseFragment {

    protected TextView  tvChonKieuXuat, tvAddPhieuNhap;
    protected TextView tvChonKhoNh;
    protected EditText edtGhichu, edtOrderId;
    protected SearchView searchChonSanPhamNh;
    protected View prBarNh;
    protected ScrollView scView;
    protected ImageView imgBarCode;
    protected SearchSugestAdapter searchSugestAdapter;
    protected XuatHangAdapter addNhapHangAdapter;
    protected RecyclerView recycleView;
    protected RelativeLayout rlBarcodeNh;
    protected LinearLayout lnSrView;
    protected CompoundBarcodeView barCodeView;
    protected ImageView imgCanceBarCode;
    protected ImageView imgFlash;
    protected ImageView btnStart;
    protected ImageView btnThuGon;
    protected View dong_mo,scanXacMinh;
    protected EditText edAddEan;
    protected View btnAddEan;
    protected CheckBox cbKeepStock;
    boolean isFlash;
    boolean isStart = false;
    protected String orderidXuat=null;
    String lastEdAddEan="";
    public XuatHangFmView() {
    }

    public static XuatHangFmv2 newInstance(String orderid) {
        XuatHangFmv2 fragment = new XuatHangFmv2();
        Bundle args = new Bundle();
        args.putString("ojorder", orderid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderidXuat = getArguments().getString("ojorder");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.xuat_hang_fm, container, false);
        initView(v);
        return v;
    }


    public void initView(View view) {
        tvChonKhoNh = view.findViewById(R.id.tvChonKhoNh);
        tvChonKieuXuat = view.findViewById(R.id.tvChonKieuNhap);
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
        scanXacMinh = view.findViewById(R.id.scanXacMinh);
        cbKeepStock = view.findViewById(R.id.cbKeepStock);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setImageResource(R.drawable.ic_pause);

        btnAddEan = view.findViewById(R.id.btnAddEan);
        edAddEan= view.findViewById(R.id.edAddEan);

        addNhapHangAdapter = new XuatHangAdapter(getActivity());
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


        scanXacMinh.setOnClickListener(view1 -> {
            scandBarCode();

        });


        if (!orderidXuat.isEmpty()){
            searchChonSanPhamNh.setVisibility(View.INVISIBLE);
            imgBarCode.setVisibility(View.INVISIBLE);
        }

        btnAddEan.setOnClickListener(view1 -> {
            String eanText= ""+edAddEan.getText().toString();
            edAddEan.setText("");
            if (eanText.length()!=5){
                showToast("Chỉ nhập 5 số cuối của EAN"); return;
            }
            addNhapHangAdapter.addEanCheckShort(eanText);


        });

        edAddEan.addTextChangedListener(new TextWatcherAdapter(edAddEan,(view1, text) -> {
            if(lastEdAddEan.length()==0&&text.length()>4){ //scan bang may quet
                addNhapHangAdapter.addEanCheck(text);
                edAddEan.setText("");
            }

            lastEdAddEan=text;
        }));

    }

    public void scandBarCode() {
        if (!PermissionSupport.getInstall(getActivity()).requestPermissionCamera()) return;


        barCodeView.setTorchOff();
        barCodeView.resume();
        barCodeView.setStatusText("");
        barCodeView.decodeContinuous(callback);
        rlBarcodeNh.setVisibility(View.VISIBLE);
        lnSrView.setVisibility(View.GONE);

        dong_mo.setVisibility(View.GONE);
        btnThuGon.setImageResource(R.drawable.mora);

        addNhapHangAdapter.clearEanCheck();


    }

    long lastTimeScan=0;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().length()<6) return;
            long mlastTimeScan=lastTimeScan; lastTimeScan= Calendar.getInstance().getTimeInMillis();
            if (lastTimeScan-mlastTimeScan<1000) return;

            barCodeView.setStatusText("đã quét: "+result.getText());
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
            mediaPlayer.start();

            addNhapHangAdapter.addEanCheck(result.getText());



        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    protected void closeScanView() {

        rlBarcodeNh.setVisibility(View.GONE);
        lnSrView.setVisibility(View.VISIBLE);
        barCodeView.pause();
        clickFlash(false);



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
        clickFlash(false);
    }

    protected void clickFlash(){
        clickFlash(null);
    }

    protected void clickFlash(Boolean curIsFlash) {
        boolean mIsFlash=isFlash; if (curIsFlash!=null) mIsFlash=curIsFlash;
        if (!mIsFlash) {
            barCodeView.setTorchOn();
            imgFlash.setImageResource(R.drawable.ic_flash_off);
            isFlash = true;
        } else {
            barCodeView.setTorchOff();
            imgFlash.setImageResource(R.drawable.ic_flash_on);
            isFlash = false;
        }
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
