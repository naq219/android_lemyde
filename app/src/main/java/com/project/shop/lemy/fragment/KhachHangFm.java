package com.project.shop.lemy.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.KhachHangAdapter;
import com.project.shop.lemy.bean.CustomerObj;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class KhachHangFm extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private KhachHangAdapter adapter;
    private RecyclerView rcViewkh;
    private ImageView imgXoakh;
    private ProgressBar prBarkh;
    private EditText edtDskh;
    String KhachHang, SoDienThoai, DiaChi;
    BaseObject objectRequest = new BaseObject();
    Boolean cancelRequest = false;
    int page = 1;
    int size = 20;
    String q = "";
    BaseModel baseModel;
    TaskNet taskNet;

    public KhachHangFm() {
    }

    public static KhachHangFm newInstance(String param1, String param2) {
        KhachHangFm fragment = new KhachHangFm();
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
        View view = inflater.inflate(R.layout.khach_hang_fm, container, false);

        rcViewkh = view.findViewById(R.id.rcViewkh);
        edtDskh = view.findViewById(R.id.edtDskh);
        imgXoakh = view.findViewById(R.id.imgXoakh);
        prBarkh = view.findViewById(R.id.prBarkh);

        imgXoakh.setOnClickListener(this);
        rcViewkh.setHasFixedSize(true);
        rcViewkh.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KhachHangAdapter(getActivity());
        rcViewkh.setAdapter(adapter);
        responeData();
        loadKhachHang();

        rcViewkh.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisibleItems, visibleItemCount, totalItemCount;
                visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loadKhachHang();
                }
            }
        });

        adapter.setClickDelete((object, position) ->
                com.project.shop.lemy.dialog.DialogSupport.dialogXoaKhachHang(getActivity(), () ->
                        deleteKhachHang(object.get(CustomerObj.id))));
        adapter.onclickEdit((object, position) -> showDialogEdit(object));

        edtDskh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                imgXoakh.setVisibility(View.VISIBLE);
                page = 1;
                cancelRequest = false;
                q = editable.toString();
                if (!q.isEmpty()) {
                    objectRequest = new BaseObject();
                    objectRequest.set("q", q);
                    prBarkh.setVisibility(View.VISIBLE);
                    loadKhachHang();
                } else imgXoakh.setVisibility(View.GONE);
            }
        });
        return view;
    }

    public void loadKhachHang() {
        if (cancelRequest) return;
        cancelRequest = true;
        objectRequest.set("page", page);
        objectRequest.set("size", size);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SEARCH_CUSTOMERS, getActivity());
        taskNet.setTaskParram("parram", objectRequest);
        taskNet.exe();
        prBarkh.setVisibility(View.VISIBLE);
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_SEARCH_CUSTOMERS:
                        prBarkh.setVisibility(View.GONE);
                        ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                        if (page == 1) adapter.setData(list);
                        else adapter.addData(list);
                        if (list.size() > 0) {
                            page++;
                            cancelRequest = false;
                        }
                        break;
                    case TaskType.TASK_UPDATE_CUSTOMERS:
                        objectRequest = new BaseObject();
                        edtDskh.setText("");
                        loadKhachHang();
                        Toast.makeText(getActivity(), "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskType.TASK_DELETE_CUSTOMERS:
                        objectRequest = new BaseObject();
                        edtDskh.setText("");
                        loadKhachHang();
                        Toast.makeText(getActivity(), "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                switch (taskType) {
                    case TaskType.TASK_SEARCH_CUSTOMERS:
                        prBarkh.setVisibility(View.GONE);
                        page--;
                        cancelRequest = false;
                        Toast.makeText(getActivity(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskType.TASK_UPDATE_CUSTOMERS:
                        Toast.makeText(getActivity(), "Cập nhật khách hàng thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    case TaskType.TASK_DELETE_CUSTOMERS:
                        Toast.makeText(getActivity(), "Xóa khách hàng thất bại", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public void deleteKhachHang(String delete) {
        BaseObject object = new BaseObject();
        object.set(CustomerObj.id, delete);
        taskNet = new TaskNet(baseModel, TaskType.TASK_DELETE_CUSTOMERS, getActivity());
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }


    private void showDialogEdit(BaseObject object) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Cập nhật thông tin");
        dialog.setContentView(R.layout.shop_dialog_kh);
        final EditText edtNameKh = dialog.findViewById(R.id.edtNameKh);
        final EditText edtSdt = dialog.findViewById(R.id.edtSdt);
        final EditText edtDiachi = dialog.findViewById(R.id.edtDiachi);
        TextView tvHuykh = dialog.findViewById(R.id.tvHuykh);
        TextView tvOKkh = dialog.findViewById(R.id.tvOKkh);
        edtNameKh.setText(object.get(CustomerObj.name));
        edtSdt.setText(object.get(CustomerObj.phone));
        edtDiachi.setText(object.get(CustomerObj.address));
        tvOKkh.setOnClickListener(view -> {
            KhachHang = edtNameKh.getText().toString();
            SoDienThoai = edtSdt.getText().toString();
            DiaChi = edtDiachi.getText().toString();
            if (KhachHang.isEmpty()) {
                Toast.makeText(getActivity(), "Khách hàng không được để chống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (SoDienThoai.isEmpty()) {
                Toast.makeText(getActivity(), "Số điện thoại không được để chống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (DiaChi.isEmpty()) {
                Toast.makeText(getActivity(), "Địa chỉ thoại không được để chống", Toast.LENGTH_SHORT).show();
                return;
            }
            BaseObject objectedit = new BaseObject();
            objectedit.set(CustomerObj.name, KhachHang);
            objectedit.set(CustomerObj.phone, SoDienThoai);
            objectedit.set(CustomerObj.address, DiaChi);
            objectedit.set(CustomerObj.id, object.get(CustomerObj.id));
            taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATE_CUSTOMERS, getActivity());
            taskNet.setTaskParram("parram", objectedit);
            taskNet.exe();
            dialog.dismiss();
        });

        tvHuykh.setOnClickListener(view -> dialog.dismiss());
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = (int) (metrics.heightPixels * 0.6);
        int width = (int) (metrics.widthPixels * 1.0);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgXoakh:
                objectRequest = new BaseObject();
                edtDskh.setText("");
                q = "";
                imgXoakh.setVisibility(View.GONE);
                loadKhachHang();
                break;
        }
    }
}
