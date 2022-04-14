package com.project.shop.lemy.smskhachhang;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.ItemKhachHangShopAdapter;
import com.project.shop.lemy.adapter.ListTieuDeAdapter;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.db.DbSupport;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class ItemKhachHangShopActivity extends SwipeBackActivity implements View.OnClickListener {
    private String name;
    private String id;
    private RecyclerView rcItemShop;
    private TextView tvTileCb;
    private TextView tvTiepTuc;
    private CheckBox cbAll;
    private ProgressBar prBarkh;
    private LinearLayout rlCheckBor;
    private ItemKhachHangShopAdapter adapter;
    ListTieuDeAdapter listTieuDeAdapter;
    BaseObject objectRequest = new BaseObject();
    ArrayList<BaseObject> listSDT = new ArrayList<>();
    private ArrayList<BaseObject> listnumberSend = new ArrayList<>();
    private BaseModel baseModel;
    private TaskNet taskNet;
    Boolean cancelRequest = false;
    boolean cbAllClick = true;
    int page = 1;
    int size = 20;
    private String SDT = "";
    String type = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_khach_hang_shop);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        tvTileCb = findViewById(R.id.tvTileCb);
        tvTiepTuc = findViewById(R.id.tvTiepTuc);
        cbAll = findViewById(R.id.cbAll);
        prBarkh = findViewById(R.id.prBarkh);
        rlCheckBor = findViewById(R.id.rlCheckBor);
        rcItemShop = findViewById(R.id.rcItemShop);
        rcItemShop.setHasFixedSize(true);
        rcItemShop.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemKhachHangShopAdapter(this);
        rcItemShop.setAdapter(adapter);
        setTitle(name);

        cbAll.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (cbAllClick) adapter.setAllChecked(isChecked);
            if (isChecked) tvTileCb.setText("Bỏ chọn tất cả");
            else tvTileCb.setText("Chọn tất cả");

        });

        adapter.setOnAllItemCheckedListerner(new Listerner.OnAllItemCheckedListerner() {
            @Override
            public void onAllChecked(boolean checked) {
                cbAllClick = false;
                cbAll.setChecked(checked);
                cbAllClick = true;
            }
        });

        tvTiepTuc.setOnClickListener(this);
        responeData();
        loadDSKhachHang();
    }

    private void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                prBarkh.setVisibility(View.GONE);
                listSDT = ((ArrayList<BaseObject>) data);
                adapter.setData(listSDT);
                rlCheckBor.setVisibility(View.VISIBLE);
                tvTiepTuc.setVisibility(View.VISIBLE);
                if (listSDT.size() == 0) {
                    tvTiepTuc.setVisibility(View.GONE);
                    rlCheckBor.setVisibility(View.GONE);
                    Toast.makeText(ItemKhachHangShopActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                prBarkh.setVisibility(View.GONE);
                page--;
                cancelRequest = false;
                Toast.makeText(ItemKhachHangShopActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void loadDSKhachHang() {
        if (cancelRequest) return;
        cancelRequest = true;
        objectRequest.set("page", page);
        objectRequest.set("size", size);
        objectRequest.set(CustomerObj.shop_id, id);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SEARCH_CUSTOMERS, this);
        taskNet.setTaskParram("parram", objectRequest);
        taskNet.exe();
        prBarkh.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTiepTuc:
                listnumberSend.clear();
                listnumberSend.addAll(listNumberSelect());
                if (listnumberSend.size() < 1) {
                    showToast(getString(R.string.checkbox_null));
                    return;
                }
                sendSmsKH();
                break;
        }
    }

    private ArrayList<BaseObject> listNumberSelect() {
        if (listSDT == null) return new ArrayList<>();
        ArrayList<BaseObject> listSdtSelec = new ArrayList<>();
        for (int i = 0; i < listSDT.size(); i++) {
            BaseObject object = listSDT.get(i);
            if (listSDT.get(i).getBool("checked", false)) {
                listSdtSelec.add(object);
            }
        }
        return dataMember(listSdtSelec);
    }

    private ArrayList<BaseObject> dataMember(ArrayList<BaseObject> listSdt) {
        ArrayList listSdtKH = new ArrayList<>();
        if (listSdt.size() < 1) return listSdtKH;
        boolean checkList = false;
        for (int i = 0; i < listSdt.size(); i++) {
            BaseObject object = listSdt.get(i);
            SDT = SDT + object.get(CustomerObj.phone) + ";";
            if (i != 0 && i % 200 == 0 || (i + 1) == listSdt.size()) {
                BaseObject objPhone = new BaseObject();
                objPhone.set(CustomerObj.phone, SDT);
                listSdtKH.add(objPhone);
                SDT = "";
                checkList = true;
            }
        }
        if (!checkList) {
            BaseObject objPhone = new BaseObject();
            objPhone.set(CustomerObj.phone, SDT);
            listSdtKH.add(objPhone);
        }
        return listSdtKH;
    }

    public void sendSmsKH() {
        ArrayList<BaseObject> listSms = new ArrayList<>();
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.setTitle("Tin nhắn mẫu");
        dialog.setContentView(R.layout.list_tieude_dialog);
        int width = (int) (this.getResources().getDisplayMetrics().widthPixels * 1);
        int height = (int) (this.getResources().getDisplayMetrics().heightPixels * 0.70);
        dialog.getWindow().setLayout(width, height);
        RecyclerView rcListTieuDe = dialog.findViewById(R.id.rcListTieuDe);
        rcListTieuDe.setHasFixedSize(true);
        rcListTieuDe.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BaseObject> smsDb = DbSupport.getChude();
        listTieuDeAdapter = new ListTieuDeAdapter(smsDb, this, listnumberSend, "type");
        if (smsDb.size() == 0) {
            Toast.makeText(this, "Chưa có SMS mẫu", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < smsDb.size(); i++) {
            BaseObject object = smsDb.get(i);
            listSms.add(object);
            rcListTieuDe.setAdapter(listTieuDeAdapter);
            dialog.show();
        }
        listTieuDeAdapter.setOnClick(new Listerner.OnClick() {
            @Override
            public void clickView() {
                finish();
            }
        });
    }
}
