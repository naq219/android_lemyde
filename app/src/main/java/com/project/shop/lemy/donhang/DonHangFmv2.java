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

public class DonHangFmv2 extends BaseFragment implements View.OnClickListener {
    private DonHangAdapterV2 adapter;
    private RecyclerView recycleView;
    private Spinner spnThoigian, spnTypeTonKho, spnTrangthaidh, spnNguoiban, spnSearch;
    private ProgressBar prBar;
    private TextView tvLocdh,tvShop,tvThongBaoList;
    EditText edtSearch;
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
        recycleView = view.findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DonHangAdapterV2(getActivity());
        recycleView.setAdapter(adapter);

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


    }

    private void loadDonHang_NEW(int limit) {

        if (tvShop.getVisibility()!=View.VISIBLE){
            showToast("Ch??a load ???????c danh s??ch Shop !");
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

        if(spnTypeTonKho.getSelectedItem().toString().equalsIgnoreCase("C?? th??? xu???t")){
            rqWhere+=" AND (SELECT SUM(sl) FROM khoang_products WHERE product_id =do.product_id) - (SELECT SUM(dd01.quantity)  FROM detail_orders dd01 INNER JOIN orders od01 ON dd01.order_id=od01.id WHERE  dd01.product_id =do.product_id AND  od01.STATUS=1 AND dd01.created_atm<=do.created_atm) >=0 \n" +
                    "AND o.status=1  ";
            limit =2000;
        }

        TaskOrderV2.search(rqWhere,orderBy,limit,getContext(),new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                List<BObject> dataRes = BObject.JsonArray2List((JSONArray) data);
                if(spnTypeTonKho.getSelectedItem().toString().equalsIgnoreCase("C?? th??? xu???t")){
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
                showToast("L???i k???t n???i "+msg);
                closeProgressDialog();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocdh:
                clearList();
                loadDonHang_NEW(20);
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
//            if (object.toString().equalsIgnoreCase("C?? th??? xu???t")){ // ch??? l???y ds ????n c?? th??? xu???t, ???n c??i kh??c
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
        mapTypeTonKho.put("#L???c T???n kho#", "");
        mapTypeTonKho.put("C?? th??? xu???t", "cothexuat");
//mapTypeTonKho.put("C?? sp t???n", "cosptonkho");
        String[] k2 = {"#L???c T???n kho#", "C?? th??? xu???t"};
        DialogSupport.setupSpinner(spnTypeTonKho, getActivity(), R.layout.spinner_item, k2, listenBack);

        mapThoiGian = new HashMap<>();
        mapThoiGian.put("#Th???i gian#", "");
        mapThoiGian.put("H??m nay", "today");
        mapThoiGian.put("H??m qua", "yesterday");
        mapThoiGian.put("Th??ng n??y", "thismonth");
        mapThoiGian.put("Th??ng tr?????c", "lastmonth");
        mapThoiGian.put("T??y ch???n", "Option");
        String[] k3 = {"#Th???i gian#", "H??m nay", "H??m qua", "Th??ng n??y", "Th??ng tr?????c", "T??y ch???n"};
        DialogSupport.setupSpinnerThoiGian(spnThoigian, getActivity(), R.layout.spinner_item, k3, object -> {
            String strobj = object.toJson().toString();
            if (strobj.equals("{}")) return;
            startDate = DateSuppost.converDate(object.get("start_date"));
            endDate = DateSuppost.converDate(object.get("end_date"));
        });

        mapSearch = new HashMap<>();
        mapSearch.put("#T???t c???#", "all");
       // mapSearch.put("M?? ????n h??ng", "order_id");
        mapSearch.put("T??n s???n ph???m", "product_name");
        mapSearch.put("T??n kh, fb", "customer_name");
        String[] k4 = {"#T???t c???#", "T??n s???n ph???m", "T??n kh, fb"};
        DialogSupport.setupSpinner(spnSearch, getActivity(), R.layout.spinner_item, k4, listenBack);

    }

    public void setupTrangthai(BaseObject object) {
        mapTrangThai = new HashMap<>();
        ArrayList<String> listKeys = object.getKeys();
        String[] keytrangthai = new String[listKeys.size() + 1];

        keytrangthai[0] = "#Tr???ng th??i#";
        mapTrangThai.put("#Tr???ng th??i#", "");
        for (int i = 0; i < listKeys.size(); i++) {
            String key = listKeys.get(i);
            keytrangthai[i + 1] = object.get(key);
            mapTrangThai.put(keytrangthai[i + 1], key);
        }
        DialogSupport.setupSpinner(spnTrangthaidh, getActivity(), R.layout.spinner_item, keytrangthai, null);

    }

    private void setupMenuNguoiBan(ArrayList<BaseObject> list) {
        mapNguoiBan = new HashMap<>();
        mapNguoiBan.put("#Ng?????i b??n#", "");
        String[] k5 = new String[list.size() + 1];
        k5[0] = "#Ng?????i b??n#";
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
