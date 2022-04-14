package com.project.shop.lemy.donhang;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.DonHangAdapter;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.common.DateSuppost;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.listener.ListenBack;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @deprecated
 */
public class DonHangFm extends BaseFragment implements View.OnClickListener {
    private DonHangAdapter adapter;
    private RecyclerView recycleView;
    TaskNet taskNet;
    BaseModel baseModel;
    private Spinner spnThoigian, spnTypeTonKho, spnTrangthaidh, spnNguoiban, spnSearch;
    private ProgressBar prBar;
    private TextView tvLocdh;
    EditText edtSearch;
    private ImageView imgForcus;
    int lpage = 0;
    int size = 50;
    View view;
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
    private String loadingId = UUID.randomUUID().toString();
    BaseObject ojRequest = new BaseObject();
    String startDate = "";
    String endDate = "";
    private String keySearchIntent;

    public DonHangFm() {

    }

    public DonHangFm(String keySearchIntent) {

        this.keySearchIntent = keySearchIntent;
    }

    HashMap<String, String> mapThoiGian = new HashMap<>();
    HashMap<String, String> mapTypeTonKho = new HashMap<>();
    HashMap<String, String> mapTrangThai = new HashMap<>();
    HashMap<String, String> mapNguoiBan = new HashMap<>();
    HashMap<String, String> mapSearch = new HashMap<>();


    private void setupMenu() {
        ListenBack listenBack = object -> {
            clearList();

            if (object.toString().equalsIgnoreCase("Có thể xuất")){ // chỉ lấy ds đơn có thể xuất, ẩn cái khác
                spnThoigian.setVisibility(View.INVISIBLE);
                spnTrangthaidh.setVisibility(View.INVISIBLE);
                spnNguoiban.setVisibility(View.INVISIBLE);
                spnSearch.setVisibility(View.INVISIBLE);
            }
            else {
                spnThoigian.setVisibility(View.VISIBLE);
                spnTrangthaidh.setVisibility(View.VISIBLE);
                spnNguoiban.setVisibility(View.VISIBLE);
                spnSearch.setVisibility(View.VISIBLE);
            }
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
        //mapSearch.put("Địa chỉ", "address");
        mapSearch.put("Tên sản phẩm", "product_name");
        mapSearch.put("Tên kh, fb", "customer_name");
        String[] k4 = {"#Tất cả#", "Id đơn hàng", "Tên sản phẩm", "Tên kh, fb"};
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

    public static DonHangFm newInstance(String param1, String param2) {
        return new DonHangFm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.don_hang_fm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View view) {
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
        adapter = new DonHangAdapter(getActivity());
        recycleView.setAdapter(adapter);
        responeData();
        loadNguoiBan();
        loadTrangThai();
        loadMore();
        setupMenu();

        tvLocdh.setOnClickListener(this);
        imgForcus.setOnClickListener(this);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                clearList();
                String vs=edtSearch.getText().toString();
                String q = editable.toString();
                if (!q.isEmpty()) {
                    imgForcus.setVisibility(View.VISIBLE);

                } else {
                    imgForcus.setVisibility(View.GONE);
                }





            }
        });

        edtSearch.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                clearList();
                loadDonHang_NEW();

                return true;
            }
            return false;
        });

        adapter.setOnclickUpdate((status, position) -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (status == 0) {
                Toast.makeText(getActivity(), "Bạn chưa nhập Trạng thái đơn hàng", Toast.LENGTH_LONG).show();
                return;
            }
            builder
                    .setTitle("Bạn muốn thay đổi trạng thái: ")
                    .setPositiveButton("Có", (dialogInterface, i) -> {
                        BaseObject baseObject = new BaseObject();
                        baseObject.set(OrderObj.id, position);
                        baseObject.set(OrderObj.status, status);
                        taskNet = new TaskNet(baseModel, TaskNet.TASK_UPDATEORDER, getActivity());
                        taskNet.setTaskParram("parram", baseObject);
                        taskNet.exe();
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton("không", (dialogInterface, i) -> dialogInterface.dismiss());
                builder.show();
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

        if (keySearchIntent!=null){
            edtSearch.setText(keySearchIntent);
            tvLocdh.performLongClick();
        }
    }

    // NEW load dong hang tu dau, chưa co loadmore
    private void loadDonHang_NEW() {
        try {
            if (lpage>0&&"Có thể xuất".equalsIgnoreCase(spnTypeTonKho.getSelectedItem().toString()))
                return; // lấy ds order có thể xuất chỉ lấy 1 page

            ojRequest.set("page", lpage + 1);
            ojRequest.set("size", size);
            ojRequest.set(KEYRQ_Q, edtSearch.getText().toString());
            ojRequest.set(KEYRQ_STATUS_TONKHO,mapTypeTonKho.get(spnTypeTonKho.getSelectedItem().toString()));
            ojRequest.set(KEYRQ_SORT, "new"); //khong su dung nua
            ojRequest.set(KEYRQ_STATUS, mapTrangThai.get(spnTrangthaidh.getSelectedItem().toString()));
            String check = spnThoigian.getSelectedItem().toString();
            if (check.equals("#Thời gian#")) {
                ojRequest.set(KEYRQ_START_DATE, "");
                ojRequest.set(KEYRQ_END_DATE, "");
            } else if (check.equals("Hôm nay")) {
                ojRequest.set(KEYRQ_START_DATE, DateSuppost.getDateToday());
                ojRequest.set(KEYRQ_END_DATE, DateSuppost.getDateToday());
            } else if (check.equals("Hôm qua")) {
                ojRequest.set(KEYRQ_START_DATE, DateSuppost.getDateYesterday());
                ojRequest.set(KEYRQ_END_DATE, DateSuppost.getDateYesterday());
            } else if (check.equals("Tháng này")) {
                ojRequest.set(KEYRQ_START_DATE, DateSuppost.getDateStart());
                ojRequest.set(KEYRQ_END_DATE, DateSuppost.getDateToday());
            } else if (check.equals("Tháng trước")) {
                ojRequest.set(KEYRQ_START_DATE, DateSuppost.getFirstDayLastMonth());
                ojRequest.set(KEYRQ_END_DATE, DateSuppost.getLastDayLastMonth());
            } else if (check.equals("Tùy chọn")) {
                ojRequest.set(KEYRQ_START_DATE, startDate);
                ojRequest.set(KEYRQ_END_DATE, endDate);
            }
            ojRequest.set(KEYRQ_TYPESEARCH, mapSearch.get(spnSearch.getSelectedItem().toString()));
            ojRequest.set(KEYRQ_USER, mapNguoiBan.get(spnNguoiban.getSelectedItem().toString()));
            ojRequest.removeEmpty();
            String uuid = UUID.randomUUID().toString();
            loadingId = uuid;
            isLoading = true;
            ojRequest.set(KEYRQ_IDLOADING, uuid);

            taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_DONHANG, getActivity());
            taskNet.setTaskParram("parram", ojRequest);
            taskNet.exe();
            prBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("naq", "Exception: " + e.getMessage());
        }

    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_DONHANG:
                        Object[] mdata = (Object[]) data;
                        String resUUID = (String) mdata[0];
                        if (resUUID.equals(loadingId)) {
                            prBar.setVisibility(View.GONE);
                            processDonHang((BaseObject) mdata[1]);
                            isLoading = false;
                        } else Log.d("naq", "khong khop");
                        break;
                    case TaskType.TASK_LIST_NGUOIBAN:
                        setupMenuNguoiBan((ArrayList<BaseObject>) data);
                        break;
                    case TaskType.TASK_UPDATEORDER:
                        BaseObject obj = (BaseObject) data;
                        if (obj.get("message").equals("success"))
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskType.TASK_LIST_STATUS:
                        setupTrangthai((BaseObject) data);
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_DONHANG:
                        if (loadingId.equals("" + msg)) {
                            prBar.setVisibility(View.GONE);
                            isLoading = false;
                        }
                        break;
                }
            }
        };
    }

    public void processDonHang(BaseObject datas) {
        isLoading = false;
        ArrayList<BaseObject> list;
        try {
            list = JsonSupport.jsonArray2BaseOj(datas.get("datas"));
            for (int i = 0; i < list.size(); i++) {
                BaseObject order = list.get(i);
                ArrayList<BaseObject> products = JsonSupport.jsonArray2BaseOj(order.get("products"));
                String sanPham = "";
                for (int j = 0; j < products.size(); j++) {
                    BaseObject product = products.get(j);
                    if (j > 0) sanPham += "\n";
                    sanPham += product.get(OrderObj.quantity) + "-" + product.get(OrderObj.product_name);
                }
                order.set(OrderObj.product_name, sanPham);
            }

            if(edtSearch.getText().toString().contains("#")){
                String loc= edtSearch.getText().toString().replace("#","");
                ArrayList<BaseObject> list1=new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).convert2NetParramsNotEncode().toLowerCase().contains(loc))
                        list1.add(list.get(i));
                }
                list= (ArrayList<BaseObject>) list1.clone();
            }

            if (lpage == 0) adapter.setData(list);
            else adapter.addData(list);
            lpage = lpage + 1;

        } catch (JSONException e) {
            if ("[]".contains(datas.get("datas"))) return;

            Toast.makeText(getActivity(), "có lỗi lấy đơn hàng", Toast.LENGTH_LONG).show();
        }
    }

    // note
    public void loadMore() {
        if (recycleView != null) {
            recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int pastVisibleItems, visibleItemCount, totalItemCount;
                    visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (totalItemCount >= size && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (!isLoading) loadDonHang_NEW();
                    }
                }
            });
        }
    }

    public void loadNguoiBan() {
        BaseObject baseObject = new BaseObject();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_NGUOIBAN, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    public void loadTrangThai() {
        BaseObject baseObject = new BaseObject();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_STATUS, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocdh:
                clearList();
                loadDonHang_NEW();
                break;
            case R.id.imgForcus:
                edtSearch.setText("");
                clearList();
                break;
        }
    }

    private void clearList() {
        prBar.setVisibility(View.GONE);
        lpage = 0;
        isLoading = false;
        loadingId = UUID.randomUUID().toString();
        adapter.setData(new ArrayList<>());
    }

    @Override
    public void onPause() {
        Keyboard.hideKeyboard(getActivity());
        super.onPause();
    }
}
