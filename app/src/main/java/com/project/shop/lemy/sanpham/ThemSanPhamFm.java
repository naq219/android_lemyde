package com.project.shop.lemy.sanpham;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

/**
 * @deprecated chuyen sang SPFmView
 */
public class ThemSanPhamFm extends BaseFragment implements View.OnClickListener {
    private static final String TYPE_PARAM = "param1";
    private static final String OBJ_PARAM = "param2";
    public static final String TYPE_ADD = "TYPE_ADD";
    public static final String TYPE_UPD = "TYPE_UPDATE";
    private String type;
    private BaseObject baseObject;
    private TextView tvAddSp;
    private EditText edtTensp, edtGiaBuon, edtGiaBanSi, edtGiabanle, edtKhoiluong, edtLinkanh, edtGioithieu,
            edtPagelink, edtGhiChu;
    String tenSp, giaBuon, giaBanSi, giaBanLe, khoiLuong, linkAnh, gioiThieu, pageLink, note;
    private ProgressBar prBars;
    TaskNet taskNet;
    BaseModel baseModel;

    public ThemSanPhamFm() {
    }

    public static ThemSanPhamFm newInstance(String type, BaseObject baseObject) {
        ThemSanPhamFm fragment = new ThemSanPhamFm();
        Bundle args = new Bundle();
        args.putString(TYPE_PARAM, type);
        args.putParcelable(OBJ_PARAM, baseObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE_PARAM);
            baseObject = getArguments().getParcelable(OBJ_PARAM);
            responeData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.them_san_pham_fm, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvAddSp = view.findViewById(R.id.tvAddSp);
        edtTensp = view.findViewById(R.id.edtTensp);

//        edtGiaBuon = view.findViewById(R.id.edtGiaBuon);
//        edtGiaBanSi = view.findViewById(R.id.edtGiaBanSi);
//        edtGiabanle = view.findViewById(R.id.edtGiabanle);
//        edtKhoiluong = view.findViewById(R.id.edtKhoiluong);
        edtLinkanh = view.findViewById(R.id.edtLinkanh);
        edtGioithieu = view.findViewById(R.id.edtGioithieu);
        edtPagelink = view.findViewById(R.id.edtPagelink);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        prBars = view.findViewById(R.id.prBars);
        edtGiaBuon.addTextChangedListener(onTextChangedGiaBuon());
        edtGiaBanSi.addTextChangedListener(onTextChangedGiaSi());
        edtGiabanle.addTextChangedListener(onTextChangedGiaLe());
        edtKhoiluong.addTextChangedListener(onTextChangedKL());

        if (type.equals(TYPE_UPD)) {
            tvAddSp.setText("Cập nhật");
            infoProduct();

        } else
            tvAddSp.setText("Thêm");
        tvAddSp.setOnClickListener(this);

    }

    private TextWatcher onTextChangedGiaBuon() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGiaBuon.removeTextChangedListener(this);
                String editGiaBuon = s.toString();
                editGiaBuon = StringHelper.formatStr(editGiaBuon);
                edtGiaBuon.setText(StringHelper.formatDoubleMoney(editGiaBuon));
                edtGiaBuon.setSelection(edtGiaBuon.getText().length());
                edtGiaBuon.addTextChangedListener(this);

            }
        };
    }

    private TextWatcher onTextChangedGiaSi() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGiaBanSi.removeTextChangedListener(this);
                String editGiaSi = s.toString();
                editGiaSi = StringHelper.formatStr(editGiaSi);
                edtGiaBanSi.setText(StringHelper.formatDoubleMoney(editGiaSi));
                edtGiaBanSi.setSelection(edtGiaBanSi.getText().length());
                edtGiaBanSi.addTextChangedListener(this);

            }
        };
    }

    private TextWatcher onTextChangedGiaLe() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGiabanle.removeTextChangedListener(this);
                String editGiaLe = s.toString();
                editGiaLe = StringHelper.formatStr(editGiaLe);
                edtGiabanle.setText(StringHelper.formatDoubleMoney(editGiaLe));
                edtGiabanle.setSelection(edtGiabanle.getText().length());
                edtGiabanle.addTextChangedListener(this);

            }
        };
    }

    private TextWatcher onTextChangedKL() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtKhoiluong.removeTextChangedListener(this);
                String editKL = s.toString();
                editKL = StringHelper.formatStr(editKL);
                edtKhoiluong.setText(StringHelper.formatDoubleMoney(editKL));
                edtKhoiluong.setSelection(edtKhoiluong.getText().length());
                edtKhoiluong.addTextChangedListener(this);

            }
        };
    }

    private void addProduct() {
        if (!isValidate()) return;
        BaseObject object = new BaseObject();

        giaBuon = StringHelper.formatStr(giaBuon);
        giaBanLe = StringHelper.formatStr(giaBanLe);
        giaBanSi = StringHelper.formatStr(giaBanSi);
        khoiLuong = StringHelper.formatStr(khoiLuong);

        object.set(ProductObj.name, tenSp);
        object.set(ProductObj.cost_price, giaBuon);
        object.set(ProductObj.wholesale_price, giaBanSi);
        object.set(ProductObj.retail_price, giaBanLe);
        object.set(ProductObj.weight, khoiLuong);
        object.set(ProductObj.image, linkAnh);
        object.set(ProductObj.introduction, gioiThieu);
        object.set(ProductObj.page_link, pageLink);
        object.set(ProductObj.note, note);
        object.set(ProductObj.sku, "sku");
        object.set(ProductObj.status, "1");
        if (type.equals(TYPE_UPD)) {
            prBars.setVisibility(View.VISIBLE);
            object.set(ProductObj.id, baseObject.get(ProductObj.id));
            taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATE_SANPHAM, getActivity());
        } else {
            prBars.setVisibility(View.VISIBLE);
            taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_SANPHAM, getActivity());
        }
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_UPDATE_SANPHAM:
                        prBars.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Cập nhật sản phẩm thành công", Toast.LENGTH_LONG).show();
                        break;
                    case TaskType.TASK_ADD_SANPHAM:
                        prBars.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                       // pushFm(ThemSanPhamFm.newInstance("1", baseObject));
                        pushFm(SPFm.newInstance("223",null));
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void infoProduct() {
        if (baseObject != null) {
            edtTensp.setText(baseObject.get(ProductObj.name));

            edtGiaBuon.setText(baseObject.get(ProductObj.cost_price));
            edtGiaBanSi.setText(baseObject.get(ProductObj.wholesale_price));
            edtGiabanle.setText(baseObject.get(ProductObj.retail_price));
            edtKhoiluong.setText(baseObject.get(ProductObj.weight));
            edtLinkanh.setText(baseObject.get(ProductObj.image));
            edtGioithieu.setText(baseObject.get(ProductObj.introduction));
            edtPagelink.setText(baseObject.get(ProductObj.page_link));
            edtGhiChu.setText(baseObject.get(ProductObj.note));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddSp:
                addProduct();
                break;
            case R.id.tvTrangthai:
                break;

        }
    }



    public boolean isValidate() {
        tenSp = edtTensp.getText().toString();

        giaBuon = edtGiaBuon.getText().toString();
        giaBanSi = edtGiaBanSi.getText().toString();
        giaBanLe = edtGiabanle.getText().toString();
        khoiLuong = edtKhoiluong.getText().toString();
        linkAnh = edtLinkanh.getText().toString();
        gioiThieu = edtGioithieu.getText().toString();
        pageLink = edtPagelink.getText().toString();
        note = edtGhiChu.getText().toString();
         if (tenSp.isEmpty()) {
            Toast.makeText(getActivity(), "Tên sản phẩm không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }

        if (giaBuon.isEmpty()) {
            Toast.makeText(getActivity(), "Giá bán buôn không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (giaBanSi.isEmpty()) {
            Toast.makeText(getActivity(), "Giá bán sỉ không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (giaBanLe.isEmpty()) {
            Toast.makeText(getActivity(), "Giá bán lẻ không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (khoiLuong.isEmpty()) {
            Toast.makeText(getActivity(), "Khối lượng không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (linkAnh.isEmpty()) {
            Toast.makeText(getActivity(), "Link ảnh không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (pageLink.isEmpty()) {
            Toast.makeText(getActivity(), "Page link không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (gioiThieu.isEmpty()) {
            Toast.makeText(getActivity(), "Giới thiệu sản phẩm không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (note.isEmpty()) {
            Toast.makeText(getActivity(), "Ghi chú không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void pushFm(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlLayout1, fragment);
        fragmentTransaction.commit();
    }
}