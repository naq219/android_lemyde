package com.project.shop.lemy.sanpham;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.MainActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskProductv2;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.SanPhamAdapter;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.Mlog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SanPhamFm extends BaseFragment implements View.OnClickListener {
//    public static final String LIST_SP = "LIST_SP";
//    public static final String LIST_UPDATE = "LIST_UPDATE";
//    public static final String UPDATE_SP = "UPDATE_SP";
//    private static final String ARG_PARAM1 = "param1";
    public static final int SAN_PHAM = 2;
    int limit=30;
    //private String type;
    private SanPhamAdapter adapter;
    private RecyclerView rcViewsp;

    int page = 1;
    int size = 20;
    private EditText edtDssp;
    private ImageView imgHuy;
    //BaseObject objectRequest = new BaseObject();
    String q = "";
    Boolean cancelRequest = false;
    private ProgressBar prBar;
    ArrayList<BaseObject> listStatus;
    HashMap<String, String> mapSx = new HashMap<>();
    Spinner spinner;
    TaskProductv2 taskSeach;
    View loadNhieu,btnAddSp;
    long lastCallSearch=1;
    long lastCheckSearch=0;
    public SanPhamFm() {
    }

    public static SanPhamFm newInstance() {
        SanPhamFm fragment = new SanPhamFm();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.san_pham_fm, container, false);
        rcViewsp = view.findViewById(R.id.rcViewsp);
        edtDssp = view.findViewById(R.id.edtDssp);
        imgHuy = view.findViewById(R.id.imgHuy);
        prBar = view.findViewById(R.id.prBar);
        imgHuy.setOnClickListener(this);
        rcViewsp.setHasFixedSize(true);
        rcViewsp.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SanPhamAdapter(getActivity());
        rcViewsp.setAdapter(adapter);
        spinner= view.findViewById(R.id.spinner);
        String orderBy="ssa";
        loadNhieu = view.findViewById(R.id.loadNhieu);
        btnAddSp= view.findViewById(R.id.btnAddSp);

        btnAddSp.setOnClickListener(view1 -> {
            DetailProductActivity.startActivityAddNew(getActivity(),edtDssp.getText().toString());
        });


        adapter.setItemClick((object) -> {
            Intent intent = new Intent(getActivity(), DetailProductActivity.class);
            BObject oj1= (BObject) object;
            intent.putExtra("id", oj1.getAsString(ProductObj.id));
            startActivityForResult(intent, 1);
        });
        adapter.setOnLoadEnd(object -> {
            loadNhieu.setVisibility(View.VISIBLE);
        });
        loadNhieu.setOnClickListener(view1 -> {
            limit=500;
            loadSearchSp();
        });

        edtDssp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                page = 1;
                cancelRequest = false;
                q = editable.toString();
                if (!q.isEmpty()) {

                    imgHuy.setVisibility(View.VISIBLE);
                    loadSearchSp();
                    btnAddSp.setVisibility(View.VISIBLE);

                } else {
                    imgHuy.setVisibility(View.GONE);
                    adapter.setData(new ArrayList<>());
                    btnAddSp.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
    public void loadSearchSp(){

        lastCallSearch= Calendar.getInstance().getTimeInMillis();
    };
    public void loadSearchSp1() {

        loadNhieu.setVisibility(View.GONE);

        prBar.setVisibility(View.VISIBLE);
        if (taskSeach!=null) taskSeach.cancel(true);
        String morderby= mapSx.get(spinner.getSelectedItem().toString());
         taskSeach= TaskProductv2.searchProduct(edtDssp.getText().toString(),morderby,limit,null,getContext(),new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                List<BObject> jdata = BObject.JsonArray2List((JSONArray) data);
                adapter.setData(jdata);
                prBar.setVisibility(View.GONE);
                limit=30;
            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                adapter.setData(new ArrayList<>());
                showToast("có lỗi "+msg);
                prBar.setVisibility(View.GONE);
                limit=30;
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapSx.put("Mới đăng", "post_at");
        mapSx.put("Order Mới", "lasttimeorder");
        //mapSx.put("Order yesterday", "order_yesterday");
        mapSx.put("Tổng order", "order_tong");
        mapSx.put("Tồn kho", "tonkho");
        mapSx.put("Sl kho", "slkho");
        String[] k3 = {"Mới đăng","Order Mới", "Tổng order",  "Tồn kho", "Sl kho"};
        DialogSupport.setupSpinner(spinner, getActivity(), R.layout.spinner_item, k3, object -> {
            edtDssp.setText("");
            loadSearchSp();
        });

        TimerTask timertask=new TimerTask() {
            @Override
            public void run() {
                if (lastCheckSearch<lastCallSearch) {
                    getActivity().runOnUiThread(() -> {
                        loadSearchSp1();
                    });
                }
                lastCheckSearch=lastCallSearch;
            }
        };
        new Timer().schedule(timertask,0,1500L);

    }

    public void loadMode(RecyclerView rcViewsp) {
        if (rcViewsp != null) {
            rcViewsp.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int pastVisibleItems, visibleItemCount, totalItemCount;
                    visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                    }
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHuy:

                edtDssp.setText("");
                q = "";
                imgHuy.setVisibility(View.GONE);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SAN_PHAM) {
            page = 1;
            size = 20;
            cancelRequest = false;

        }
    }
}
