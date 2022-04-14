package com.project.shop.lemy.dvvc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.DvvcObj;
import com.project.shop.lemy.common.Keyboard;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;


public class ThemDvvcFm extends BaseFragment {
    private EditText edtAddNameDvvc;
    private EditText edtAddIdDvvc;
    private TextView btnAddDvvc;
    private LinearLayout lnAddDvvc;
    String id;
    String name;
    ArrayList<BaseObject> listDvvc = new ArrayList<>();

    public ThemDvvcFm() {
    }

    private BaseModel baseModel = new BaseModel() {
        @Override
        public void onSuccess(int taskType, Object data, String msg) {
            super.onSuccess(taskType, data, msg);
            switch (taskType) {
                case TaskType.TASK_LIST_DVVC:
                    listDvvc = (ArrayList<BaseObject>) data;
                    break;
                case TaskType.TASK_ADD_DVVC:
                    showToast("Thêm đơn vị vận chuyển thành công");
                    edtAddNameDvvc.setText("");
                    edtAddIdDvvc.setText("");
                    break;

            }

        }

        @Override
        public void onFail(int taskType, String msg) {
            super.onFail(taskType, msg);
            showToast(msg);

        }
    };

    public static ThemDvvcFm newInstance() {
        return new ThemDvvcFm();
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
        View view = inflater.inflate(R.layout.fragment_them_dvvc, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        loadDvvc();
        edtAddIdDvvc = v.findViewById(R.id.edtAddIdDvvc);
        edtAddNameDvvc = v.findViewById(R.id.edtAddNameDvvc);
        btnAddDvvc = v.findViewById(R.id.btnAddDvvc);
        lnAddDvvc = v.findViewById(R.id.lnAddDvvc);

        lnAddDvvc.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Keyboard.hideKeyboard(getActivity());
                    break;
            }
            return false;
        });
        btnAddDvvc.setOnClickListener(view -> addShop());
    }

    private void addShop() {
        String idDvvc = edtAddIdDvvc.getText().toString();
        String nameDvvc = edtAddNameDvvc.getText().toString();
        if (idDvvc.isEmpty()) {
            showToast("Chưa nhập id đơn vị vận chuyển");
            return;
        }
        if (nameDvvc.isEmpty()) {
            showToast("Chưa nhập tên đơn vị vận chuyển");
            return;
        }
        for (int i = 0; i < listDvvc.size(); i++) {
            id = listDvvc.get(i).get(DvvcObj.id);
            name = listDvvc.get(i).get(DvvcObj.name);
            if (idDvvc.equals(id)) {
                showToast("Id đã tồn tại");
                return;
            } else if (nameDvvc.equals(name)) {
                showToast("Tên vận chuyển đã tồn tại");
                return;
            }
        }
        BaseObject object = new BaseObject();
        object.set(DvvcObj.name, nameDvvc);
        object.set(DvvcObj.id, idDvvc);
        TaskNet taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_DVVC, getActivity());
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }

    void loadDvvc() {
        BaseObject objDvvcRequest = new BaseObject();
        TaskNet taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_DVVC, getActivity());
        taskNet.setTaskParram("parram", objDvvcRequest);
        taskNet.exe();
    }
}
