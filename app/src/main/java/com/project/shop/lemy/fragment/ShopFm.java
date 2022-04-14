package com.project.shop.lemy.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.ShopAdapter;
import com.project.shop.lemy.bean.ShopObj;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class ShopFm extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ShopAdapter adapter;
    private RecyclerView rcViewql;
    private ProgressBar prBar;

    private TextView tvTongShop;
    BaseObject objectRequest = new BaseObject();
    TaskNet taskNet;
    BaseModel baseModel;

    String Quanly, Face;

    public ShopFm() {
    }

    public static ShopFm newInstance(String param1, String param2) {
        ShopFm fragment = new ShopFm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly, container, false);
        initView(view);
        return view;
    }


    private void initView(View v) {
//        tvTongShop = v.findViewById(R.id.tvTongShop);
        prBar = v.findViewById(R.id.prBar);
        rcViewql = v.findViewById(R.id.rcViewql);
        rcViewql.setHasFixedSize(true);
        rcViewql.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShopAdapter(getActivity());
        rcViewql.setAdapter(adapter);

        adapter.setClickDelete((object, position) ->
                com.project.shop.lemy.dialog.DialogSupport.dialogXoaShop(getActivity(), () ->
                        deleteShop(object.get(ShopObj.id))));
        adapter.onclickEdit((object, position) -> showDialogEdit(object));
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
//                        tvTongShop.setText(" " + String.valueOf(list.size()));
                        adapter.setData(list);
                        prBar.setVisibility(View.GONE);
                        break;
                    case TaskType.TASK_DELETE_SHOP:
                        loadShop();
                        Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskType.TASK_UPDATE_SHOP:
                        loadShop();
                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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

    public void deleteShop(String delete) {
        BaseObject object = new BaseObject();
        object.set(ShopObj.id, delete);
        taskNet = new TaskNet(baseModel, TaskType.TASK_DELETE_SHOP, getActivity());
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }

    private void showDialogEdit(BaseObject object) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Thay đổi quản lý");
        dialog.setContentView(R.layout.shop_dialog);
        final EditText edtNameQL = dialog.findViewById(R.id.edtNameQL);
        final EditText edtNameFB = dialog.findViewById(R.id.edtNameFB);
        TextView tvHuy = dialog.findViewById(R.id.tvHuy);
        TextView tvOK = dialog.findViewById(R.id.tvOK);
        edtNameQL.setText(object.get(ShopObj.name));
        edtNameFB.setText(object.get(ShopObj.facebook));
        tvOK.setOnClickListener(view -> {
            Quanly = edtNameQL.getText().toString();
            Face = edtNameFB.getText().toString();
            if (Quanly.isEmpty()) {
                Toast.makeText(getActivity(), "Chưa có quản lý", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Face.isEmpty()) {
                Toast.makeText(getActivity(), "Chưa có Facebook", Toast.LENGTH_SHORT).show();
                return;
            }
            BaseObject objectedit = new BaseObject();
            objectedit.set(ShopObj.name, Quanly);
            objectedit.set(ShopObj.facebook, Face);
            objectedit.set(ShopObj.id, object.get(ShopObj.id));
            taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATE_SHOP, getActivity());
            taskNet.setTaskParram("parram", objectedit);
            taskNet.exe();
            dialog.dismiss();
        });
        tvHuy.setOnClickListener(view -> dialog.dismiss());
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = (int) (metrics.heightPixels * 0.4);
        int width = (int) (metrics.widthPixels * 1.0);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

}
