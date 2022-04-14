package com.project.shop.lemy.xuatnhapkho;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.helper.RegisterVolumeChange;
import com.project.shop.lemy.search.SearchSugestAdapter;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChuyenKhoangFm extends BaseFragment implements View.OnClickListener {
    protected TextView  tvAddPhieuNhap;
    protected TextView tvChonKhoNh;
    protected EditText  edtOrderId;
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

    String khoChuaHang = "";
    String lastsearchChonSanPhamNh="";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvChonKhoNh.setOnClickListener(this);
        tvAddPhieuNhap.setOnClickListener(this);
        imgBarCode.setOnClickListener(this);
        imgCanceBarCode.setOnClickListener(this);
        imgFlash.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        //ss1 đã chọn được sản phẩm từ tìm kiếm
        searchSugestAdapter.setOnItemClickListerer((object, position) -> {
            if (position == TaskType.SEARCH_PRODUCT_V2) {

                searchChonSanPhamNh.clearFocus();
                searchChonSanPhamNh.setQuery("", false);
                String id=object.get(ProductObj.id);
                if ("999999".equals(id)){// search theo ean mà ko có
                    Intent it=new Intent(getActivity(), TimSpEan.class);
                    it.putExtra("ean",object.get("ean"));
                    startActivityForResult(it,123);
                    return;
                }

                if (!validateId(id)) {
                    showToast("Sản phẩm đã có trong danh sách");
                    return;
                }



                addNhapHangAdapter.addData(object);
            }
        });

        searchChonSanPhamNh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=""+newText;
                searchSugestAdapter.setCurrentTextSearch(newText);
                if (!isValidate()) return false ;
                //if (newText.length()==0) return false;
                if (dong_mo.getVisibility()==View.VISIBLE){
                    dong_mo.setVisibility(View.GONE);
                    btnThuGon.setImageResource(R.drawable.mora);
                }

                String mNewText = newText;
                if (newText.length()-lastsearchChonSanPhamNh.length()>5){ //quet barcode
                    mNewText="#"+newText;
                }

                searchSugestAdapter.textChangeProduct(mNewText);
                searchChonSanPhamNh.setSuggestionsAdapter(searchSugestAdapter);

                lastsearchChonSanPhamNh= ""+mNewText;

                return false;
            }
        });


        edtOrderId.setOnKeyListener((v, keyCode, event) -> { // bat su kien nhap madh de lay productid cho nhap kho nhanh

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                loadProductFromOrder(edtOrderId.getText().toString());
                return true;
            }
            return false;
        });

        getActivity().getApplicationContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                new RegisterVolumeChange(new Handler(), object -> {
                    clickStartStop();
                }));


        loadKhoChuaHang( false);


    }

    public void loadKhoChuaHang(boolean show) {
        MenuFromApiSupport.createKhoChua(getActivity(), tvChonKhoNh, show, key -> {
            khoChuaHang = key;
            List<BaseObject> listKhoang;
            listKhoang= MenuFromApiSupport.getKhoangByKho(key);
            addNhapHangAdapter.setKhoang(listKhoang);
            addNhapHangAdapter.resetData();
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChonKhoNh:
                loadKhoChuaHang(true);
                break;

            case R.id.tvAddPhieuNhap:

                break;
            case R.id.imgBarCode:

                scandBarCode();
                break;
            case R.id.imgCanceBarCode:

                closeScanView();
                break;
            case R.id.btnStart:
                clickStartStop();
                break;
            case R.id.imgFlash:
                clickFlash();
                break;
        }
    }

    private void loadProductFromOrder(String oid) {
        BaseModel model1=new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);

                try {
                    JSONObject jo = (JSONObject) data;
                    int status= jo.getInt("status");


                    JSONArray detail_ordersJa= jo.getJSONArray("detail_orders");
                    if (detail_ordersJa.length()==0){
                        showToast("đơn này không có sp ");
                        return;
                    }
                    List<BaseObject> listP=new ArrayList<>();

                    for (int i = 0; i < detail_ordersJa.length(); i++) {
                        BaseObject oj =new BaseObject();
                        oj.set(ProductObj.id,detail_ordersJa.getJSONObject(i).getString("product_id"));
                        oj.set(ProductObj.name,detail_ordersJa.getJSONObject(i).getString("product_name"));
                        listP.add(oj);
                    }

                    addNhapHangAdapter.setData(listP);


                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Lỗi tìm đơn hàng ");
                }

            }
        };
        TaskNetOrder.extaskDetail(oid,model1,getContext(),123);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chuyenkhoangfm, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvChonKhoNh = view.findViewById(R.id.tvChonKhoNh);
        tvAddPhieuNhap = view.findViewById(R.id.tvAddPhieuNhap);
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

    public boolean isValidate() {
        if (khoChuaHang.isEmpty() ) {
            showToast("Chưa chọn kho chứa hàng");
            return false;
        }


        return true;
    }


    boolean isFlash;
    boolean isStart = false;
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

    public void scandBarCode() {
        if (!PermissionSupport.getInstall(getActivity()).requestPermissionCamera()) return;
        if (!isValidate()) return;

        barCodeView.setTorchOff();
        barCodeView.resume();
        barCodeView.setStatusText("");
        barCodeView.decodeContinuous(callback);
        rlBarcodeNh.setVisibility(View.VISIBLE);
        lnSrView.setVisibility(View.GONE);
        rutgon();
    }

    private void rutgon() {
        if (dong_mo.getVisibility()==View.VISIBLE){
            dong_mo.setVisibility(View.GONE);
            btnThuGon.setImageResource(R.drawable.mora);
        }
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barCodeView.setStatusText(result.getText());
                String macode = "#"+result.getText();


                closeScanView();
                searchChonSanPhamNh.setQuery(macode,false);
                searchChonSanPhamNh.postDelayed(() -> {
                    getActivity().findViewById(R.id.searchChonSanPhamNh).requestFocus();
                },400);


                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
                mediaPlayer.start();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private void closeScanView() {
        isFlash = false;
        rlBarcodeNh.setVisibility(View.GONE);
        lnSrView.setVisibility(View.VISIBLE);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        barCodeView.setTorchOff();
        barCodeView.pause();

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



}
