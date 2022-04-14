package com.project.shop.lemy.xuatnhapkho;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskOrderV2;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ObjectSupport;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.donhang.ProcessDonHangActivity;
import com.project.shop.lemy.helper.PermissionSupport;
import com.project.shop.lemy.helper.RegisterVolumeChange;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XuatHangFmv2 extends XuatHangFmView implements View.OnClickListener  {
//    ArrayList<BaseObject> listNhapHang = new ArrayList<>();

    String khoChuaHang = "";
    String kieuXuat = "";
    String ghiChu = "";
    String searchSanPham = "";


    TaskNet taskNet;
    BaseModel baseModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvChonKhoNh.setOnClickListener(this);
        tvChonKieuXuat.setOnClickListener(this);
        tvAddPhieuNhap.setOnClickListener(this);
        imgBarCode.setOnClickListener(this);
        imgCanceBarCode.setOnClickListener(this);
        imgFlash.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        searchSugestAdapter.setOnItemClickListerer((object, position) -> {
            if (position == TaskType.TASK_SEARCHPRODUCTS) {

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
                if (!isValidate()) return false ;

                // check xem co phai la may quet khong, neu co them ky tu # o dau

                searchSugestAdapter.textChangeProduct(newText);
                searchChonSanPhamNh.setSuggestionsAdapter(searchSugestAdapter);

                if (dong_mo.getVisibility()==View.VISIBLE){
                    dong_mo.setVisibility(View.GONE);
                    btnThuGon.setImageResource(R.drawable.mora);
                }


                return false;
            }
        });


        getActivity().getApplicationContext().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                new RegisterVolumeChange(new Handler(),object -> {
                    clickStartStop();
                }));

        responeData();
        loadKhoChuaHang( false);
        loadKieuXuat(true, false);

        ((SwipeBackActivity)getActivity()).setCurrentFragment(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChonKhoNh:
                loadKhoChuaHang(true);
                break;
            case R.id.tvChonKieuNhap:
                loadKieuXuat(false, true);
                break;
            case R.id.tvAddPhieuNhap:
                addXuatKho();
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


    public void loadKhoChuaHang(boolean show) {
        MenuFromApiSupport.createKhoChua(getActivity(), tvChonKhoNh, show, key -> {
            khoChuaHang = key;

            addNhapHangAdapter.resetData();

            if (orderidXuat!=null&&orderidXuat.length()>0){ // xuất kho từ đơn hàng
                //TaskSupport.callDetailOrder(orderidXuat,baseModel,getContext());
                loadOrderDetailV2();
                prBarNh.setVisibility(View.VISIBLE);
            }

        });
    }

    private void loadOrderDetailV2() {


        BaseModel baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                prBarNh.setVisibility(View.GONE);
                try {
                    BaseObject oj = JsonSupport.jsonObject2BaseOj((JSONObject) data);
                    xuLyOrderXuatV2(oj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogSupport.dialogYesNo(msg+" lỗi tải dữ liệu",getContext(),isYes -> {

                    });
                }

            }

            @Override
            public void onFail(int taskType, String msg) {
                prBarNh.setVisibility(View.GONE);
                DialogSupport.dialogYesNo(msg+" lỗi tải dữ liệu",getContext(),isYes -> {

                });

            }
        };

        TaskNetOrder.extaskDetail(orderidXuat, baseModel, getContext(), 1);


    }



    public void loadKieuXuat(boolean reload, boolean show) {
        if (orderidXuat!=null&&orderidXuat.length()>0){
            kieuXuat="1";
            tvChonKieuXuat.setText("Xuất Đơn");
        }

        MenuFromApiSupport.createKieuXuat(getActivity(), tvChonKieuXuat, reload, show, key ->{
            kieuXuat = key;
            if ("2".equals(kieuXuat))edtOrderId.setVisibility(View.VISIBLE);
            else edtOrderId.setVisibility(View.GONE);
        } );
    }



    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_ADD_XUATKHO:



                        if (cbKeepStock.isChecked()){ // lưu kho cho khách chờ gửi
                            Model model2=new Model(){
                                @Override
                                public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                                    super.onSuccess(taskType, data, msg, queue);

                                    printAfterExport();
                                }

                                @Override
                                public void onFail(int taskType, String msg, Integer queue) {
                                    super.onFail(taskType, msg, queue);
                                    DialogSupport.dialogThongBao(" (>\"<) LƯU KHO KHÔNG THÀNH CÔNG , Báo lại Q !!",getContext(),object -> {
                                        printAfterExport();
                                    });
                                }
                            };
                            BObject ojUpdateKeepStock=new BObject();
                            ojUpdateKeepStock.set(OrderObj.keep_stock,1);
                            TaskOrderV2.update(orderidXuat,ojUpdateKeepStock,getContext(),model2);
                        }
                        else {
                            printAfterExport();
                        }

                        break;


                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                prBarNh.setVisibility(View.GONE);

                switch (taskType){
                    case TaskType.TASK_DETAILORDER:
                        showToast("Không lấy được thông tin đơn hàng!");


                        break;
                    default:
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    //in đơn sau khi xuất kho thành công
    private void printAfterExport() {
        prBarNh.setVisibility(View.GONE);
        BaseObject ojOrder= new BaseObject();
        ojOrder.set("id",orderidXuat);
        ojOrder.set(OrderObj.status,2);
        ProcessDonHangActivity.inDonHang(getActivity(), ojOrder);

        getActivity().finish();
        showToast("Xuất đơn thành công");
    }

    ///////////////////// xulyorderxuat
    private void xuLyOrderXuat111(BaseObject ojDetailOrder) {

        if (!"1".equals(ojDetailOrder.get("status"))){
            showToast("chỉ đơn hàng trạng thái ORDER mới được xuất kho nha Baby!");
            getActivity().finish();
        }

        List<BaseObject> ojs = new ArrayList<>();
        boolean isDuSl=true;
        try {
            JSONArray ja = new JSONArray(""+ojDetailOrder.get("products"));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo1 = ja.getJSONObject(i);
                BaseObject ojProduct= ObjectSupport.Json2Object(jo1);



                if (!jo1.has("tonkho")){ isDuSl=false; }
                else {
                    JSONArray jaTon= jo1.getJSONArray("tonkho");
                    JSONArray jaTonTheoKho=new JSONArray();
                    int totalSl=0;
                    for (int j = 0; j < jaTon.length(); j++) {

                        JSONObject joTmp1 = jaTon.getJSONObject(j);
                        if (khoChuaHang.equals(joTmp1.getString("kho_id").trim())){
                            totalSl=totalSl+joTmp1.getInt("sl");
                            jaTonTheoKho.put(joTmp1);
                        }

                    }

                    if (totalSl<jo1.getInt("quantity")) {
                        isDuSl=false;
                    }

                    ojProduct.set("tonkho",jaTonTheoKho.toString());
                }
                ojs.add(ojProduct);


            }
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Lỗi hệ thống, liên hệ anh Quế!");
            getActivity().finish();
            return;
        }

        String ghiChuOrder=ojDetailOrder.get("note","");
        if (ghiChuOrder.toLowerCase().equals("null"))ghiChuOrder="";
        edtGhichu.setText(ghiChuOrder);
        edtOrderId.setText(ojDetailOrder.get("id"));
        edtOrderId.setFocusable(false);

        addNhapHangAdapter.setIsXuatChoDH();
        addNhapHangAdapter.setData(ojs);


        edtGhichu.postDelayed(new Runnable() {
            @Override
            public void run() {
                addNhapHangAdapter.tuDongNhatHang();
            }
        },1000);

//        if (!ghiChuOrder.isEmpty()&&!ghiChuOrder.toLowerCase().equals("null")){
//            DialogSupport.dialogThongBaoKemXacNhanBangChu("CÓ GHI CHÚ !! :",ghiChuOrder,getContext(),isYes -> {
//
//            });
//        }
    }

    private String xuLyOrderXuatV2(BaseObject oj) {
        if (!"1".equals(oj.get("status"))){
            showToast("chỉ đơn hàng trạng thái ORDER mới được xuất kho nha Baby!");
            getActivity().finish();
        }

        List<BaseObject> ojs = new ArrayList<>();
        JSONArray ja=null;
        try {
             ja = new JSONArray(""+oj.get("detail_orders"));
        } catch (JSONException e) {
            return loi00(e,9999);
        }

        HashMap<String,BaseObject> hmGom= new HashMap<>();
        try {

            for (int k = 0; k < ja.length(); k++) {
                BaseObject mojProduct = ObjectSupport.Json2Object(ja.optJSONObject(k));
                String mProductId= mojProduct.get("product_id");
                if (hmGom.containsKey(mProductId)){
                    mojProduct.set("quantity", mojProduct.getInt("quantity")+hmGom.get(mProductId).getInt("quantity"));
                }
                hmGom.put(mProductId,mojProduct);
            }
        } catch (JSONException e) {
           return loi00(e,8888);
        }
        boolean isDuSl=true;
        ArrayList<BaseObject> ojNews= new ArrayList<>(hmGom.values());

        for (int i = 0; i < ojNews.size(); i++) {
            BaseObject ojN= ojNews.get(i);
            if ((""+ojN.get("kho")).length()<6){ isDuSl=false; }
            else
            try {
                JSONArray jaTon= new JSONArray(ojN.get("kho"));
                JSONArray jaTonTheoKho=new JSONArray();
                int totalSl=0;

                for (int j = 0; j < jaTon.length(); j++) {

                    JSONObject joTmp1 = jaTon.getJSONObject(j);
                    if (khoChuaHang.equals(joTmp1.getString("kho_id").trim())){
                        totalSl=totalSl+joTmp1.getInt("sl");
                        jaTonTheoKho.put(joTmp1);
                    }

                }
                if (totalSl<ojN.getInt("quantity")) {
                    isDuSl=false;
                }

                ojN.set("tonkho",jaTonTheoKho.toString());

            } catch (JSONException e) {
                return loi00(e,6666);
            }

            ojs.add(ojN);
        }



        String ghiChuOrder=oj.get("note","null");
        if (ghiChuOrder.toLowerCase().equals("null"))ghiChuOrder="";
        edtGhichu.setText(ghiChuOrder);
        edtOrderId.setText(oj.get("id"));
        edtOrderId.setFocusable(false);

        addNhapHangAdapter.setIsXuatChoDH();
        addNhapHangAdapter.setData(ojs);


        edtGhichu.postDelayed(new Runnable() {
            @Override
            public void run() {
                addNhapHangAdapter.tuDongNhatHang();
            }
        },1000);

//        if (!ghiChuOrder.isEmpty()&&!ghiChuOrder.toLowerCase().equals("null")){
//            DialogSupport.dialogThongBaoKemXacNhanBangChu("CÓ GHI CHÚ !! :",ghiChuOrder,getContext(),isYes -> {
//
//            });
//        }
        return ghiChuOrder;
    }

    private String loi00(JSONException e,int eee) {
        e.printStackTrace();
        showToast("Có lỗi xảy ra "+eee);
        getActivity().finish();
        return "";
    }


    private void addXuatKho() {
        if (!isValidate()) return;
        if (addNhapHangAdapter == null) return;

        String ttrutGon="";

        List<BaseObject> listNhapHangLess = addNhapHangAdapter.getProduct();
        ArrayList<BaseObject> listNhapHang= new ArrayList<>();

        for (int i = 0; i < listNhapHangLess.size(); i++) {
            BaseObject oj1= listNhapHangLess.get(i);
            List<BaseObject> listcon = NhapKhoSupport.getDataKhoangFromOj(oj1);

            if (listcon==null||listcon.size()==0){
                DialogSupport.dialogThongBao("Sản phẩm: "+oj1.get("product_name")+" chưa chọn kho",getContext(),null);
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
                product.put("product_id", baseObject.get("product_id"));
                //product.put(ProductObj.name, baseObject.get("product_name"));
                String khoang = baseObject.get(ProductObj.khoang_id);
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
            //todo ??
            objNhap.put(ProductObj.type, Integer.parseInt(kieuXuat));
            objNhap.put(ProductObj.note, ghiChu);
            if (!edtOrderId.getText().toString().isEmpty()) objNhap.put(ProductObj.order_id, edtOrderId.getText());
            objNhap.put("products", products);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Có lỗi xảy ra! 85333");
            return;
        }


//        DialogSupport.dialogYesNo(addNhapHangAdapter.isDaCheckEan(),"Chưa quét hết mã vạch để kiểm tra, bạn có muốn tiếp tục không?",getContext(),isYes -> {
//            if (isYes){
//
//            }
//        });

        if (!addNhapHangAdapter.validateEanCheck().equals("ok")){
            DialogSupport.dialogThongBao(addNhapHangAdapter.validateEanCheck()+"--chưa quét mã",getContext(),null);
            return;
        }

        DialogSupport.dialogYesNo("BẠN ĐÃ CHẮC ĂN LÀ ĐÚNG HẾT CHƯA?",
                getContext(),isYes1 -> {
                    if (isYes1){
                        BaseObject ojAddNhanHang = new BaseObject();
                        ojAddNhanHang.set("param", objNhap.toString());
                        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_XUATKHO, getActivity());
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



    public boolean isValidate() {

        ghiChu = edtGhichu.getText().toString();
        searchSanPham = searchChonSanPhamNh.getQuery().toString();
        if (khoChuaHang.isEmpty() || kieuXuat.equals("null")) {
            showToast("Chưa chọn kho chứa hàng");
            return false;
        }
        if (kieuXuat.isEmpty() || kieuXuat.equals("null")) {
            showToast("Chưa chọn kiểu nhập");
            return false;
        }

        return true;
    }

}
