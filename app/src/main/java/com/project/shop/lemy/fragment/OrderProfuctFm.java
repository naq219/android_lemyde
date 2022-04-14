package com.project.shop.lemy.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.OrderProfuctAdapter;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class OrderProfuctFm extends BaseFragment implements View.OnClickListener {
    private RecyclerView recycleView;
    private TextView tvTrangthaiOrder;
    private LinearLayout lnTrangThai;
    private OrderProfuctAdapter adapter;
    TaskNet taskNet;
    BaseModel baseModel;
    int page = 1;
    int size = 20;
    String q = "";
    Boolean cancelRequest = false;
    private ProgressBar prBarOrder;
    private EditText edtOrder;
    private ImageView imgReset;
    BaseObject objectRequest = new BaseObject();
    private String status;

    public OrderProfuctFm() {
    }

    public static OrderProfuctFm newInstance(String param1, String param2) {
        OrderProfuctFm fragment = new OrderProfuctFm();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_profuct_fm, container, false);
        recycleView = view.findViewById(R.id.recycleView);
        prBarOrder = view.findViewById(R.id.prBarOrder);
        edtOrder = view.findViewById(R.id.edtOrder);
        imgReset = view.findViewById(R.id.imgReset);
        tvTrangthaiOrder = view.findViewById(R.id.tvTrangthaiOrder);
        lnTrangThai = view.findViewById(R.id.lnTrangThai);
        imgReset.setOnClickListener(this);
        tvTrangthaiOrder.setOnClickListener(this);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderProfuctAdapter(getActivity());
        recycleView.setAdapter(adapter);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisibleItems, visibleItemCount, totalItemCount;
                visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loadOrderprofuct();
                }
            }
        });
        responeData();
        loadOrderprofuct();
        loadTrangThaiDonHang(true, false);
        edtOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                imgReset.setVisibility(View.VISIBLE);
                page = 1;
                cancelRequest = false;
                q = editable.toString();
                if (!q.isEmpty()) {
                    objectRequest = new BaseObject();
                    objectRequest.set("q", q);
                    loadOrderprofuct();
                } else imgReset.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public void loadOrderprofuct() {
        if (cancelRequest) return;
        cancelRequest = true;
        objectRequest.set("page", page);
        objectRequest.set("size", size);
        objectRequest.set("status", status);
        objectRequest.removeEmpty();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_ORDERPROFUCT, getActivity());
        taskNet.setTaskParram("parram", objectRequest);
        taskNet.exe();
        prBarOrder.setVisibility(View.VISIBLE);
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_ORDERPROFUCT:
                        ArrayList<BaseObject> list = ((ArrayList<BaseObject>) data);
                        prBarOrder.setVisibility(View.GONE);
                        if (page == 1) adapter.setData(list);
                        else adapter.addData(list);
                        if (list.size() > 0) {
                            page++;
                            cancelRequest = false;
                        }
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                page--;
                cancelRequest = false;
                prBarOrder.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgReset:
                objectRequest = new BaseObject();
                edtOrder.setText("");
                q = "";
                imgReset.setVisibility(View.GONE);
                loadOrderprofuct();
                break;

            case R.id.tvTrangthaiOrder:
                loadTrangThaiDonHang(false, true);
                break;
        }
    }

    public void loadTrangThaiDonHang(boolean reload, boolean show) {
        MenuFromApiSupport.createTrangThaiDonHang(getActivity(), tvTrangthaiOrder, reload, show, key -> {
            status = key;
            cancelRequest = false;
            prBarOrder.setVisibility(View.GONE);
            page = 1;
            loadOrderprofuct();
        });
    }

}