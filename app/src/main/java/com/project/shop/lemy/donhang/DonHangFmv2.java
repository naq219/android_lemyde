package com.project.shop.lemy.donhang;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskOrderV2;
import com.project.shop.lemy.common.DateSuppost;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.MyOnkeyEnterListener;
import com.project.shop.lemy.helper.MyTextWatcher;
import com.project.shop.lemy.helper.OrderHelper;
import com.project.shop.lemy.listener.ListenBack;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * adapter @{@link DonHangAdapterV2}
 * view xml {@link R.layout#don_hang_fmv2}
 */
public class DonHangFmv2 extends BaseFragment implements View.OnClickListener {
    private DonHangAdapterV2 adapter;
    private RecyclerView recycleView;
    private Spinner spnThoigian, spnTypeTonKho, spnTrangthaidh, spnNguoiban, spnSearch;
    private ProgressBar prBar;
    private TextView tvLocdh,tvShop,tvThongBaoList;
    EditText edtSearch;
    View btnXemThem;
    private ImageView imgForcus;
    int size = 50;
    private String KEYRQ_SORT = "sort";
    private String KEYRQ_STATUS = "status";
    private String KEYRQ_USER = "user";
    private String KEYRQ_START_DATE = "start_date";
    private String KEYRQ_END_DATE = "end_date";
    private String KEYRQ_Q = "q";
    private String KEYRQ_TYPESEARCH = "type";
    public static final String KEYRQ_IDLOADING = "KEYRQ_IDLOADING";
    private String KEYRQ_STATUS_TONKHO = "type_tonkho"; // "cosptonkho", "cothexuat"
    private boolean isLoading = false;
    String startDate = "";
    String endDate = "";



    public static DonHangFmv2 newInstance(String param1, String param2) {
        return new DonHangFmv2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.don_hang_fmv2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View view) {
        tvThongBaoList= view.findViewById(R.id.tvThongBaoList);
        tvShop = view.findViewById(R.id.tvShop);
        spnThoigian = view.findViewById(R.id.spnThoigian);
        spnTypeTonKho = view.findViewById(R.id.spnSapxep);
        spnTrangthaidh = view.findViewById(R.id.spnTrangthaidh);
        spnNguoiban = view.findViewById(R.id.spnNguoiban);
        spnSearch = view.findViewById(R.id.spnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        prBar = view.findViewById(R.id.prBar);
        tvLocdh = view.findViewById(R.id.tvLocdh);
        imgForcus = view.findViewById(R.id.imgForcus);
        btnXemThem = view.findViewById(R.id.btnXemThem);
        recycleView = view.findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DonHangAdapterV2(getActivity());
        recycleView.setAdapter(adapter);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    showToast("cuoi cung");
                    if (btnXemThem.getVisibility()==View.GONE)
                    btnXemThem.setVisibility(View.VISIBLE);

                }
            }
        });
        setupMenu();
        OrderHelper.loadShop2Menu(false,tvShop,getContext());
        tvShop.setOnClickListener(v -> {
            OrderHelper.loadShop2Menu(true,tvShop,getContext());
        });
        getView().findViewById(R.id.lltvShop).setOnClickListener(v -> {OrderHelper.loadShop2Menu(true,tvShop,getContext());});
        tvLocdh.setOnClickListener(this);
        imgForcus.setOnClickListener(this);
        edtSearch.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                clearList();
                String q = s.toString();
                if (!q.isEmpty()) {
                    imgForcus.setVisibility(View.VISIBLE);

                } else {
                    imgForcus.setVisibility(View.GONE);
                }
            }
        });



        edtSearch.setOnKeyListener(new MyOnkeyEnterListener(object -> {
            clearList();
            loadDonHang_NEW(20);
        }));



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

        btnXemThem.setOnClickListener(view1 -> {
            loadDonHang_NEW(1000);
            btnXemThem.setVisibility(View.INVISIBLE);
        });


    }

    private void loadDonHang_NEW(int limit) {

        if (tvShop.getVisibility()!=View.VISIBLE){
            showToast("Chưa load được danh sách Shop !");
            return;
        }

        showProgressDialog();

        String orderBy=" do_created_atm_max desc";
        String q= edtSearch.getText().toString();
        String typeSeach=mapSearch.get(spnSearch.getSelectedItem().toString());

        String rqWhere="1";
        if (typeSeach.equals("all")){
            rqWhere= " (p.name like '%#,#%' OR c.name like  '%#,#%' OR c.phone like  '%#,#%' )";
            rqWhere= rqWhere.replace("#,#",q);
        }
        if (typeSeach.equals("product_name")){
            rqWhere= " p.name like '%"+q+"%' ";
        }
        if (typeSeach.equals("customer_name")){
            rqWhere= " c.name like  '%"+q+"%' ";
        }
        if(q.length()==0) rqWhere=" 1 ";


        String whereShopId="";
        if(tvShop.getText().toString().contains("-")){
            String shopId=tvShop.getText().toString().substring(0,tvShop.getText().toString().indexOf("-"));
            whereShopId=" AND o.shop_id = "+shopId+" ";
        }
        else {
            whereShopId= String.format(" AND o.shop_id in ( %s ) ", OrderHelper.listShopIdByCodenv());
            if(SprSupport.isAdmin(getContext())) whereShopId ="";
        }

        rqWhere+=whereShopId;

        if(spnTypeTonKho.getSelectedItem().toString().equalsIgnoreCase("Có thể xuất")){
            rqWhere+=" AND (SELECT SUM(sl) FROM khoang_products WHERE product_id =do.product_id) - (SELECT SUM(dd01.quantity)  FROM detail_orders dd01 INNER JOIN orders od01 ON dd01.order_id=od01.id WHERE  dd01.product_id =do.product_id AND  od01.STATUS=1 AND dd01.created_atm<=do.created_atm) >=0 \n" +
                    "AND o.status=1  ";
            limit =2000;
        }

        TaskOrderV2.search(rqWhere,orderBy,limit,getContext(),new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                List<BObject> dataRes = BObject.JsonArray2List((JSONArray) data);
                if(spnTypeTonKho.getSelectedItem().toString().equalsIgnoreCase("Có thể xuất")){
                    tvThongBaoList.setVisibility(View.VISIBLE);
                    dataRes.sort((o1, o2) -> {
                        if(o1.getAsInt("do_created_atm_min")>o2.getAsInt("do_created_atm_min")) return 1;
                        else return -1;
                    });
                }
                else tvThongBaoList.setVisibility(View.GONE);

                adapter.setData(dataRes);
                closeProgressDialog();

            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                showToast("Lỗi kết nối "+msg);
                closeProgressDialog();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocdh:
                clearList();
                loadDonHang_NEW(50);
                btnXemThem.setVisibility(View.GONE);
                break;
            case R.id.imgForcus:
                edtSearch.setText("");
                clearList();
                break;
        }
    }

    private void clearList() {
        prBar.setVisibility(View.GONE);
        isLoading = false;
        adapter.setData(new ArrayList<>());
        btnXemThem.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        Keyboard.hideKeyboard(getActivity());
        super.onPause();
    }



    HashMap<String, String> mapThoiGian = new HashMap<>();
    HashMap<String, String> mapTypeTonKho = new HashMap<>();
    HashMap<String, String> mapTrangThai = new HashMap<>();
    HashMap<String, String> mapNguoiBan = new HashMap<>();
    HashMap<String, String> mapSearch = new HashMap<>();


    private void setupMenu() {
        ListenBack listenBack = object -> {
            clearList();
            tvThongBaoList.setVisibility(View.GONE);
//            if (object.toString().equalsIgnoreCase("Có thể xuất")){ // chỉ lấy ds đơn có thể xuất, ẩn cái khác
//                spnThoigian.setVisibility(View.INVISIBLE);
//
//                spnNguoiban.setVisibility(View.INVISIBLE);
//                spnSearch.setVisibility(View.INVISIBLE);
//            }
//            else {
//                spnThoigian.setVisibility(View.VISIBLE);
//
//                spnNguoiban.setVisibility(View.VISIBLE);
//                spnSearch.setVisibility(View.VISIBLE);
//            }
        };

        mapTypeTonKho = new HashMap<>();
        mapTypeTonKho.put("#Lọc Tồn kho#", "");
        mapTypeTonKho.put("Có thể xuất", "cothexuat");
//mapTypeTonKho.put("Có sp tồn", "cosptonkho");
        String[] k2 = {"#Lọc Tồn kho#", "Có thể xuất"};
        DialogSupport.setupSpinner(spnTypeTonKho, getActivity(), R.layout.spinner_item, k2, listenBack);

        mapThoiGian = new HashMap<>();
        mapThoiGian.put("#Thời gian#", "");
        mapThoiGian.put("Hôm nay", "today");
        mapThoiGian.put("Hôm qua", "yesterday");
        mapThoiGian.put("Tháng này", "thismonth");
        mapThoiGian.put("Tháng trước", "lastmonth");
        mapThoiGian.put("Tùy chọn", "Option");
        String[] k3 = {"#Thời gian#", "Hôm nay", "Hôm qua", "Tháng này", "Tháng trước", "Tùy chọn"};
        DialogSupport.setupSpinnerThoiGian(spnThoigian, getActivity(), R.layout.spinner_item, k3, object -> {
            String strobj = object.toJson().toString();
            if (strobj.equals("{}")) return;
            startDate = DateSuppost.converDate(object.get("start_date"));
            endDate = DateSuppost.converDate(object.get("end_date"));
        });

        mapSearch = new HashMap<>();
        mapSearch.put("#Tất cả#", "all");
       // mapSearch.put("Mã đơn hàng", "order_id");
        mapSearch.put("Tên sản phẩm", "product_name");
        mapSearch.put("Tên kh, fb", "customer_name");
        String[] k4 = {"#Tất cả#", "Tên sản phẩm", "Tên kh, fb"};
        DialogSupport.setupSpinner(spnSearch, getActivity(), R.layout.spinner_item, k4, listenBack);

    }

    public void setupTrangthai(BaseObject object) {
        mapTrangThai = new HashMap<>();
        ArrayList<String> listKeys = object.getKeys();
        String[] keytrangthai = new String[listKeys.size() + 1];

        keytrangthai[0] = "#Trạng thái#";
        mapTrangThai.put("#Trạng thái#", "");
        for (int i = 0; i < listKeys.size(); i++) {
            String key = listKeys.get(i);
            keytrangthai[i + 1] = object.get(key);
            mapTrangThai.put(keytrangthai[i + 1], key);
        }
        DialogSupport.setupSpinner(spnTrangthaidh, getActivity(), R.layout.spinner_item, keytrangthai, null);

    }

    private void setupMenuNguoiBan(ArrayList<BaseObject> list) {
        mapNguoiBan = new HashMap<>();
        mapNguoiBan.put("#Người bán#", "");
        String[] k5 = new String[list.size() + 1];
        k5[0] = "#Người bán#";
        for (int i = 0; i < list.size(); i++) {
            BaseObject item = list.get(i);
            String key = item.get("username");
            String value = item.get("id");
            mapNguoiBan.put(key, value);
            k5[i + 1] = key;
        }
        DialogSupport.setupSpinner(spnNguoiban, getActivity(), R.layout.spinner_item, k5, null);
    }
}
