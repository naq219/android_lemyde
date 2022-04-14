package com.project.shop.lemy.dvvc;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.project.shop.lemy.adapter.DonViVanChuyenAdapter;
import com.project.shop.lemy.bean.DvvcObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;


public class DonViVanChuyenFm extends BaseFragment {
    private RecyclerView rcListDvvc;
    private ProgressBar prBar;
    private DonViVanChuyenAdapter adapter;
    private BaseObject objDvvcRequest = new BaseObject();
    private TaskNet taskNet;

    public DonViVanChuyenFm() {
    }

    private BaseModel baseModel = new BaseModel() {
        @Override
        public void onSuccess(int taskType, Object data, String msg) {
            super.onSuccess(taskType, data, msg);
            switch (taskType) {
                case TaskType.TASK_LIST_DVVC:
                    ArrayList<BaseObject> listDvvc = (ArrayList<BaseObject>) data;
                    adapter.setData(listDvvc);
                    prBar.setVisibility(View.GONE);
                    break;
                case TaskType.TASK_UPDATE_DVVC:
                    loadDvvc();
                    showToast("Cập nhật thành công");
                    break;
                case TaskType.TASK_DELETE_DVVC:
                    loadDvvc();
                    showToast("Xóa thành công");
                    break;
            }

        }

        @Override
        public void onFail(int taskType, String msg) {
            super.onFail(taskType, msg);
            switch (taskType) {
                case TaskNet.TASK_LIST_DVVC:
                    prBar.setVisibility(View.GONE);
                    showToast(msg);
                    break;
                case TaskNet.TASK_UPDATE_DVVC:
                    showToast(msg);
                    break;
                case TaskNet.TASK_DELETE_DVVC:
                    showToast(msg);
                    break;
            }

        }
    };

    public static DonViVanChuyenFm newInstance() {
        return new DonViVanChuyenFm();
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
        return inflater.inflate(R.layout.fragment_dvvc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    void initView(View v) {
        prBar = v.findViewById(R.id.prBar);
        rcListDvvc = v.findViewById(R.id.rcListDvvc);
        rcListDvvc.setHasFixedSize(true);
        rcListDvvc.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DonViVanChuyenAdapter(getActivity());
        rcListDvvc.setAdapter(adapter);

        adapter.setOnclickEdit((object, position) -> showDialogEdit(object));

        adapter.setOnDeleteItemClick((object, position) -> DialogSupport.dialogXoaDvvc(getActivity(), () -> deleteDvvc(object.get(DvvcObj.id))));
        loadDvvc();
    }

    void loadDvvc() {
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_DVVC, getActivity());
        taskNet.setTaskParram("parram", objDvvcRequest);
        taskNet.exe();
        prBar.setVisibility(View.VISIBLE);
    }

    private void showDialogEdit(BaseObject object) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Thay đổi đơn vị vận chuyển");
        dialog.setContentView(R.layout.dialog_dvvc);
        final EditText edtNameVC = dialog.findViewById(R.id.edtNameVC);
        TextView tvHuy = dialog.findViewById(R.id.tvHuy);
        TextView tvOK = dialog.findViewById(R.id.tvOK);
        edtNameVC.setText(object.get(DvvcObj.name));
        tvOK.setOnClickListener(view -> {
            String nameDvvc = edtNameVC.getText().toString();
            if (nameDvvc.isEmpty()) {
                Toast.makeText(getActivity(), "Chưa có tên đơn vị vận chuyển", Toast.LENGTH_SHORT).show();
                return;
            }
            BaseObject objectedit = new BaseObject();
            objectedit.set(DvvcObj.name, nameDvvc);
            objectedit.set(DvvcObj.id, object.get(DvvcObj.id));
            taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATE_DVVC, getActivity());
            taskNet.setTaskParram("parram", objectedit);
            taskNet.exe();
            dialog.dismiss();
        });
        tvHuy.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void deleteDvvc(String delete) {
        BaseObject object = new BaseObject();
        object.set(DvvcObj.id, delete);
        taskNet = new TaskNet(baseModel, TaskType.TASK_DELETE_DVVC, getActivity());
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }
}
