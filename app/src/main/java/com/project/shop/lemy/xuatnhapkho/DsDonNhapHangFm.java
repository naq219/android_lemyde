package com.project.shop.lemy.xuatnhapkho;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.NhapHangAdapter;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.DateSuppost;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.listener.ListenBack;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DsDonNhapHangFm extends BaseFragment implements View.OnClickListener {
    private TextView tvLocNhapHang;
    private Spinner spnThoigian, spnSapxep, spnKhoHang, spnLoaiNhap;
    private ProgressBar prBar;
    private RecyclerView recycleView;
    private NhapHangAdapter adapter;
    TaskNet taskNet;
    BaseModel baseModel;
    BaseObject ojRequest = new BaseObject();
    private boolean isLoading = false;
    int page = 1;
    int size = 20;
    private String loadingId = UUID.randomUUID().toString();
    private String KEYRQ_START_DATE = "start_date";
    private String KEYRQ_END_DATE = "end_date";
    private String KEYRQ_SORT = "sort";
    private String KEYRQ_KHO_ID = "kho_id";
    private String KEYRQ_TYPE = "type";
    public static final String KEYRQ_IDLOADING = "KEYRQ_IDLOADING";
    String startDate = "";
    String endDate = "";

    public DsDonNhapHangFm() {
    }

    HashMap<String, String> mapThoiGian = new HashMap<>();
    HashMap<String, String> mapSapXep = new HashMap<>();
    HashMap<String, String> mapLoaiNhap = new HashMap<>();
    HashMap<String, String> mapKhoHang = new HashMap<>();

    private void setupMenu() {
        ListenBack listenBack = object -> {
            clearList();
        };

        mapSapXep = new HashMap<>();
        mapSapXep.put("#Sắp xếp#", "");
        mapSapXep.put("Mới nhất", "new");
        mapSapXep.put("Cũ nhất", "old");
        String[] keysapxep = {"#Sắp xếp#", "Mới nhất", "Cũ nhất"};
        DialogSupport.setupSpinner(spnSapxep, getActivity(), R.layout.spinner_item, keysapxep, listenBack);

        mapThoiGian = new HashMap<>();
        mapThoiGian.put("#Thời gian#", "");
        mapThoiGian.put("Hôm nay", "today");
        mapThoiGian.put("Hôm qua", "yesterday");
        mapThoiGian.put("Tháng này", "thismonth");
        mapThoiGian.put("Tháng trước", "lastmonth");
        mapThoiGian.put("Tùy chọn", "Option");
        String[] keythoigian = {"#Thời gian#", "Hôm nay", "Hôm qua", "Tháng này", "Tháng trước", "Tùy chọn"};
        DialogSupport.setupSpinnerThoiGian(spnThoigian, getActivity(), R.layout.spinner_item, keythoigian, object -> {
            String strobj = object.toJson().toString();
            if (strobj.equals("{}")) return;
            startDate = DateSuppost.converDate(object.get("start_date"));
            endDate = DateSuppost.converDate(object.get("end_date"));
        });
    }

    public void setupKieuNhap(BaseObject object) {
        mapLoaiNhap = new HashMap<>();
        ArrayList<String> listKeys = object.getKeys();
        String[] keyloainhap = new String[listKeys.size() + 1];
        keyloainhap[0] = "#Loại nhập#";
        mapLoaiNhap.put("#Loại nhập#", "");
        for (int i = 0; i < listKeys.size(); i++) {
            String key = listKeys.get(i);
            keyloainhap[i + 1] = object.get(key);
            mapLoaiNhap.put(keyloainhap[i + 1], key);
        }
        DialogSupport.setupSpinner(spnLoaiNhap, getActivity(), R.layout.spinner_item, keyloainhap, null);
    }

    public void setupKhoHang(ArrayList<BaseObject> list) {
        mapKhoHang = new HashMap<>();
        mapKhoHang.put("#Kho hàng#", "");
        String[] keykhohang = new String[list.size() + 1];
        keykhohang[0] = "#Kho hàng#";
        for (int i = 0; i < list.size(); i++) {
            BaseObject item = list.get(i);
            String key = item.get("name");
            String value = item.get("id");
            mapKhoHang.put(key, value);
            keykhohang[i + 1] = key;
        }
        DialogSupport.setupSpinner(spnKhoHang, getActivity(), R.layout.spinner_item, keykhohang, null);
    }

    public static DsDonNhapHangFm newInstance() {
        return  new DsDonNhapHangFm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ds_don_nhap_hang_fm, container, false);
        initView(view);
        return view;
    }

    public void initView(View v) {
        spnThoigian = v.findViewById(R.id.spnThoigian);
        spnSapxep = v.findViewById(R.id.spnSapxep);
        spnLoaiNhap = v.findViewById(R.id.spnLoaiNhap);
        spnKhoHang = v.findViewById(R.id.spnKhoHang);
        tvLocNhapHang = v.findViewById(R.id.tvLocNhapHang);
        prBar = v.findViewById(R.id.prBar);
        recycleView = v.findViewById(R.id.recycleView);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NhapHangAdapter(getActivity());
        recycleView.setAdapter(adapter);
        responeData();
        loadKhoHang();
        loadLoaiNhap();
        loadMore();
        setupMenu();
        tvLocNhapHang.setOnClickListener(this);
    }

    private void loadNhapHang() {
        ojRequest.set("page", page);
        ojRequest.set("size", size);
        ojRequest.set(KEYRQ_SORT, mapSapXep.get(spnSapxep.getSelectedItem().toString()));
        ojRequest.set(KEYRQ_KHO_ID, mapKhoHang.get(spnKhoHang.getSelectedItem().toString()));
        ojRequest.set(KEYRQ_TYPE, mapLoaiNhap.get(spnLoaiNhap.getSelectedItem().toString()));
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
        ojRequest.removeEmpty();
        String uuid = UUID.randomUUID().toString();
        loadingId = uuid;
        isLoading = true;
        ojRequest.set(KEYRQ_IDLOADING, uuid);
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_NHAPHANG, getActivity());
        taskNet.setTaskParram("parram", ojRequest);
        taskNet.exe();
        prBar.setVisibility(View.VISIBLE);
    }

    public void loadLoaiNhap() {
        BaseObject baseObject = new BaseObject();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_KIEUNHAP, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    public void loadKhoHang() {
        BaseObject baseObject = new BaseObject();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_KHO, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_NHAPHANG:
                        prBar.setVisibility(View.GONE);
                        ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                        if (page == 1) adapter.setData(list);
                        else adapter.addData(list);
                        if (list.size() > 0) {
                            page++;
                            isLoading = false;
                            processNhapHang(list);
                        }
                        break;

                    case TaskType.TASK_LIST_KIEUNHAP:
                        setupKieuNhap((BaseObject) data);
                        break;

                    case TaskType.TASK_LIST_KHO:
                        setupKhoHang((ArrayList<BaseObject>) data);
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_NHAPHANG:
                        prBar.setVisibility(View.GONE);
                        page--;
                        isLoading = false;
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public void processNhapHang(ArrayList<BaseObject> datas) {
        try {
            for (int i = 0; i < datas.size(); i++) {
                BaseObject order = datas.get(i);
                ArrayList<BaseObject> products = JsonSupport.jsonArray2BaseOj(order.get("products"));
                String sanPham = "";
                for (int j = 0; j < products.size(); j++) {
                    BaseObject product = products.get(j);
                    if (j > 0) sanPham += "\n";
                    sanPham += product.get(OrderObj.count) + "-" + product.get(ProductObj.name);
                }
                order.set(ProductObj.name, sanPham);
            }
        } catch (JSONException e) {
        }
    }

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
                        if (!isLoading) loadNhapHang();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocNhapHang:
                clearList();
                loadNhapHang();
                break;
        }
    }

    private void clearList() {
        prBar.setVisibility(View.GONE);
        page = 0;
        isLoading = false;
        loadingId = UUID.randomUUID().toString();
        adapter.setData(new ArrayList<>());
    }
}
