package com.project.shop.lemy.xuatnhapkho;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.helper.RegisterVolumeChange;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NhapHangFm extends NhapHangFmView implements View.OnClickListener  {
//    ArrayList<BaseObject> listNhapHang = new ArrayList<>();

    String khoChuaHang = "";
    String kieuNhap = "";
    String ghiChu = "";
    String searchSanPham = "";
    String orderId = "";
    String khoang = "";
    TaskNet taskNet;
    BaseModel baseModel;
    List<String> listKhoangTheoKho;
    List<BaseObject> listKhoang;
    String lastsearchChonSanPhamNh="";
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvChonKhoNh.setOnClickListener(this);
        tvChonKieuNhap.setOnClickListener(this);
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

                if (object.getInt("tonkho",-999)>0){
                    DialogSupport.dialogThongBao("Sản phẩm này đang tồn kho, báo lại Quế",getContext(),null);
                    TaskNetGeneral.exTaskNotifyOnlyMe(object.get("name"),getContext());
                }

                String ean=object.get(ProductObj.ean,"");
                if ("".equals(ean)||"null".equalsIgnoreCase(ean)){
                    //View viewDl= LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_updateean,null);
                    ViewUpdateEan vv = new ViewUpdateEan(id,getContext());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setView(vv.getView());
                    AlertDialog dl = builder.create();
                    dl.show();

                    vv.setDialog(dl);


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
                new RegisterVolumeChange(new Handler(),object -> {
                    clickStartStop();
                }));

        responeData();
        loadKhoChuaHang( false);
        loadKieuNhap(true, false);


    }


    private void loadProductFromOrder(String oid) {
        BaseModel model1=new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);

                try {
                    JSONObject jo = (JSONObject) data;
                    int status= jo.getInt("status");
                    if (status==1){
                        DialogSupport.dialogThongBao("Đơn đang order mà ?",getActivity(),null);
                        return;
                    }

                    DialogSupport.manyButton("Bạn có muốn đổi trạng thái đơn hàng không ?","Đóng lại",null,"Đổi Sang ORDER","Đổi sang HỦY",getActivity(),state -> {
                        int status123 =99;
                        int state2= (int) state;
                        if (state2==3) status123=1;
                        else if(state2==4) status123=4;
                        else return;

                        Model model23=new Model(){
                            @Override
                            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                                ChiTietDonHangActivity.startActivity(getActivity(),oid);
                            }

                            @Override
                            public void onFail(int taskType, String msg, Integer queue) {
                                ChiTietDonHangActivity.startActivity(getActivity(),oid);
                                showToast("Có lỗi cập nhật trạng thái đơn hàng");
                            }
                        };
                        TaskGeneralTh.exeTaskStatement(getContext(), SqlTaskGeneral.updateStatusOid(oid,status123),"123sd",123,model23);

                    });




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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChonKhoNh:
                loadKhoChuaHang(true);
                break;
            case R.id.tvChonKieuNhap:
                loadKieuNhap(false, true);
                break;
            case R.id.tvAddPhieuNhap:
                addPhieuNhap();
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

    private void closeScanView() {
        isFlash = false;
        rlBarcodeNh.setVisibility(View.GONE);
        lnSrView.setVisibility(View.VISIBLE);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        barCodeView.setTorchOff();
        barCodeView.pause();

    }

    public void loadKhoChuaHang(boolean show) {
        MenuFromApiSupport.createKhoChua(getActivity(), tvChonKhoNh, show, key -> {
            khoChuaHang = key;
            listKhoang= MenuFromApiSupport.getKhoangByKho(key);
            addNhapHangAdapter.setKhoang(listKhoang);
            addNhapHangAdapter.resetData();
        });
    }

    public void loadKieuNhap(boolean reload, boolean show) {
        MenuFromApiSupport.createKieuNhap(getActivity(), tvChonKieuNhap, reload, show, key ->{
            kieuNhap = key;
            if ("2".equals(kieuNhap)||"3".equals(kieuNhap))edtOrderId.setVisibility(View.VISIBLE);
            else edtOrderId.setVisibility(View.GONE);
        } );
    }



    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_ADD_NHAPHANG:
                        prBarNh.setVisibility(View.GONE);
                        BaseObject object = (BaseObject) data;
                        if (object.get("message").equals("success")) {
                            resetAdd();
                            showToast("Nhập đơn thành công");
                        } else showToast(object.get("message"));
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                prBarNh.setVisibility(View.GONE);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        };
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

    public boolean isValidate() {
        orderId = edtOrderId.getText().toString();
        ghiChu = edtGhichu.getText().toString();
        searchSanPham = searchChonSanPhamNh.getQuery().toString();
        if (khoChuaHang.isEmpty() || kieuNhap.equals("null")) {
            showToast("Chưa chọn kho chứa hàng");
            return false;
        }
        if (kieuNhap.isEmpty() || kieuNhap.equals("null")) {
            showToast("Chưa chọn kiểu nhập");
            return false;
        }

        if (edtOrderId.getVisibility()==View.VISIBLE&&edtOrderId.getText().toString().length()==0){
            showToast("Chưa nhập Mã Đơn hàng");
            return false;
        }

        if (edtOrderId.getVisibility()==View.GONE&edtGhichu.getText().toString().length()==0){
            showToast("Chưa nhập ghi chú");
            return false;
        }


        return true;
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


    private void addPhieuNhap() {
        if (!isValidate()) return;
        if (addNhapHangAdapter == null) return;

        String ttrutGon="";

        List<BaseObject> listNhapHangLess = addNhapHangAdapter.getProduct();
        ArrayList<BaseObject> listNhapHang= new ArrayList<>();



        for (int i1 = 0; i1 < listNhapHangLess.size(); i1++) {
            BaseObject oj1= listNhapHangLess.get(i1);
            List<BaseObject> listcon = NhapKhoSupport.getDataKhoangFromOj(oj1);

            if (listcon==null||listcon.size()==0){
                DialogSupport.dialogThongBao("Sản phẩm: "+oj1.get(ProductObj.name)+" chưa chọn kho",getContext(),null);
               return;
            }

            int sltong=0;

            for (int j = 0; j < listcon.size(); j++) {
                BaseObject oj2= oj1.clone();
                oj2.set(ProductObj.khoang_id,listcon.get(j).get("khoang_id"));
                oj2.set(ProductObj.sl,listcon.get(j).get("sl"));
                listNhapHang.add(oj2);

                try {
                    sltong=sltong+ Integer.parseInt(listcon.get(j).get("sl"));
                }catch (Exception e){};
            }

            ttrutGon=ttrutGon+oj1.get(ProductObj.name)+" -- SL: "+sltong+"\n";
        }
        if (listNhapHang.size()==0) {
            showToast("Đã nhập sp mô mồ?");
            return;

        }

        ttrutGon=ttrutGon+"Hãy kiểm tra lại phiếu giao hàng 1 lần nữa!";

        JSONObject objNhap = new JSONObject();
        JSONArray products = new JSONArray();
        for (BaseObject baseObject : listNhapHang) {
            try {
                JSONObject product = new JSONObject();
                product.put(ProductObj.id, baseObject.get(ProductObj.id));
                product.put(ProductObj.name, baseObject.get(ProductObj.name));
                khoang = baseObject.get(ProductObj.khoang_id);
                if (khoang == null) {
                    showToast("Chưa chon khoang chứa");
                    return;
                }
                product.put(ProductObj.khoang_id, khoang);
                product.put(ProductObj.sl, Integer.parseInt(baseObject.get(ProductObj.sl)));
                products.put(product);
            } catch (JSONException e) {
                e.printStackTrace();
                showToast("Có lỗi xảy ra 41231");
                return;
            }
        }
        try {
            objNhap.put(ProductObj.kho_id, khoChuaHang);
            objNhap.put(ProductObj.type, Integer.parseInt(kieuNhap));
            objNhap.put(ProductObj.note, ghiChu);
            if (!orderId.isEmpty()) objNhap.put(ProductObj.order_id, orderId);
            if (kieuNhap.equals("3") && orderId.isEmpty()) {
                showToast("Type nhập kho là Trả hàng bắt buộc phải có Order Id");
                return;
            }
            objNhap.put("products", products);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Có lỗi xảy ra! 85333");
            return;
        }

        DialogSupport.dialogYesNo("Hãy kiểm tra lại phiếu giao hàng nếu chưa chắc chắn, bấm YES để tiếp tục lưu",
                getContext(),isYes -> {
                    if (isYes){
                        BaseObject ojAddNhanHang = new BaseObject();
                        ojAddNhanHang.set("param", objNhap.toString());
                        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_NHAPHANG, getActivity());
                        taskNet.setTaskParram("parram", ojAddNhanHang);
                        taskNet.exe();
                        prBarNh.setVisibility(View.VISIBLE);
                    }
                });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionSupport.READ_PERMISSIONS_CAMERA) scandBarCode();
    }

}
