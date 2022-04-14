package com.project.shop.lemy.smskhachhang;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.SmskhachHangAdapter;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class SmsKhachHangFragment extends BaseFragment {

    private SmskhachHangAdapter adapter;

    private RecyclerView rcViewql;
    private ProgressBar prBar;

    BaseObject objectRequest = new BaseObject();
    TaskNet taskNet;
    BaseModel baseModel;

    public SmsKhachHangFragment() {
    }

    public static SmsKhachHangFragment newInstance(String param1, String param2) {
        SmsKhachHangFragment fragment = new SmsKhachHangFragment();
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
        View view = inflater.inflate(R.layout.fragment_sms_khachhang, container, false);
        initView(view);
        return view;

    }

    private void initView(View v) {
        prBar = v.findViewById(R.id.prBar);
        rcViewql = v.findViewById(R.id.rcViewql);
        rcViewql.setHasFixedSize(true);
        rcViewql.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SmskhachHangAdapter(getActivity());
        rcViewql.setAdapter(adapter);
        responeData();
        loadShop();
    }

    private void loadShop() {
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_SHOP, getActivity());
        taskNet.setTaskParram("parram", objectRequest);
        taskNet.exe();
        prBar.setVisibility(View.VISIBLE);
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_LIST_SHOP:
                        ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                        adapter.setData(list);
                        prBar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                prBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
