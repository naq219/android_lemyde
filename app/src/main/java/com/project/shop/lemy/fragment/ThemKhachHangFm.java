package com.project.shop.lemy.fragment;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.ShopObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.search.SearchSugestQuanHuyen;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.shop.lemy.common.SpinnerSupport.nhomKhachhang;

public class ThemKhachHangFm extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText edtTenkh, edtDienthoai, edtDiachi;
    private TextView tvThemkh;
    private Spinner spNhomkh, spShop;
    String id = "";
    String[] shops;
    ArrayList<BaseObject> listShop;
    BaseObject objLoaiKhachHang;
    String tenKh, nhomKh, dienThoai, diaChi, idQuan, idHuyen, searchQh, idShop, idloaiKhachHang;
    TaskNet taskNet;
    BaseModel baseModel;
    private SearchView searchQuanHuyen;
    private SearchSugestQuanHuyen searchSugestQuanHuyen;
    private ProgressBar prBarkh;
    HashMap<String, String> mapKhachHang = new HashMap<>();

    public ThemKhachHangFm() {
    }

    public static ThemKhachHangFm newInstance(String param1, String param2) {
        ThemKhachHangFm fragment = new ThemKhachHangFm();
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
            responeData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.them_khach_hang_fm, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        edtTenkh = view.findViewById(R.id.edtTenkh);
        edtDienthoai = view.findViewById(R.id.edtDienthoai);
        edtDiachi = view.findViewById(R.id.edtDiachi);
        tvThemkh = view.findViewById(R.id.tvThemkh);
        spNhomkh = view.findViewById(R.id.spNhomkh);
        spShop = view.findViewById(R.id.spShop);
        searchQuanHuyen = view.findViewById(R.id.searchQuanHuyen);
        prBarkh = view.findViewById(R.id.prBarkh);
        tvThemkh.setOnClickListener(this);
        responeData();
        listShop();
        loadListKhachHang();
        searchQuanHuyen();

        spShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listShop != null) {
                    idShop = listShop.get(i).get(ShopObj.id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addKhachHang() {
        if (!isValidate()) return;
        BaseObject object = new BaseObject();
        object.set(CustomerObj.name, tenKh);
        object.set(CustomerObj.phone, dienThoai);
        object.set(CustomerObj.district_id, idHuyen);
        object.set(CustomerObj.province_id, idQuan);
        object.set(CustomerObj.address, diaChi);
        object.set(CustomerObj.manager_id, idShop);
        object.set(CustomerObj.customer_group, mapKhachHang.get(spNhomkh.getSelectedItem().toString()));
        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_KHACHHANG, getActivity());
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
        prBarkh.setVisibility(View.VISIBLE);
    }

    public void listShop() {
        BaseObject baseObject = new BaseObject();
        baseObject.set(ShopObj.id, id);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SHOP, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    public void loadListKhachHang() {
        BaseObject baseObject = new BaseObject();
        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_TYPES_CUSTOMER, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    private void searchQuanHuyen() {
        searchSugestQuanHuyen = new SearchSugestQuanHuyen(getActivity());
        searchSugestQuanHuyen.setOnItemClickListerer((object, position) -> {
            if (position == TaskType.TASK_SEARCH_QUANHUYEN) {
                searchQuanHuyen.setQuery(object.get(CustomerObj.districts_name) + "-" + (object.get(CustomerObj.provinces_name)), true);
                idHuyen = object.get(CustomerObj.districts_id);
                idQuan = object.get(CustomerObj.province_id);
            }
        });
        clickChonQuanHuyen();
    }

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_ADD_KHACHHANG:
                        prBarkh.setVisibility(View.GONE);
                        spShop.clearFocus();
                        searchQuanHuyen.clearFocus();
                        Toast.makeText(getActivity(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                        break;

                    case TaskType.TASK_SHOP:
                        listShop = ((ArrayList<BaseObject>) data);
                        if (listShop.size() < 1) return;
                        shops = new String[listShop.size()];
                        for (int i = 0; i < listShop.size(); i++) {
                            shops[i] = listShop.get(i).get(ShopObj.name);
                        }
                        ArrayAdapter<String> adapterChonShop = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_15sp, shops);
                        spShop.setAdapter(adapterChonShop);
                        break;

                    case TaskType.TASK_LIST_TYPES_CUSTOMER:
                        setupKhachHang((BaseObject) data);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvThemkh:
                addKhachHang();
                break;
        }
    }

    public void setupKhachHang(BaseObject object) {
        mapKhachHang = new HashMap<>();
        ArrayList<String> listKeys = object.getKeys();
        String[] keykhachhang = new String[listKeys.size()];

        for (int i = 0; i < listKeys.size(); i++) {
            String key = listKeys.get(i);
            keykhachhang[i] = object.get(key);
            mapKhachHang.put(keykhachhang[i], key);
        }
        DialogSupport.setupSpinner(spNhomkh, getActivity(), R.layout.spinner_item_15sp, keykhachhang, null);
    }

    private void clickChonQuanHuyen() {
        setSizeSearchView();
        searchQuanHuyen.setQueryHint("Nhập khu vực");
        searchQuanHuyen.setIconifiedByDefault(false);
        searchQuanHuyen.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSugestQuanHuyen.textChangeQuanHuyen(newText);
                searchQuanHuyen.setSuggestionsAdapter(searchSugestQuanHuyen);
                return false;
            }
        });
    }

    void setSizeSearchView() {
        EditText searchEditText = searchQuanHuyen.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchEditText.setTextSize(14);
    }

    public boolean isValidate() {
        tenKh = edtTenkh.getText().toString();
        dienThoai = edtDienthoai.getText().toString();
        diaChi = edtDiachi.getText().toString();
        nhomKh = spNhomkh.getSelectedItem().toString();
        searchQh = searchQuanHuyen.getQuery().toString();
        if (tenKh.isEmpty()) {
            Toast.makeText(getActivity(), "Tên khách hàng không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (dienThoai.isEmpty()) {
            Toast.makeText(getActivity(), "Số điện thoại không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (diaChi.isEmpty()) {
            Toast.makeText(getActivity(), "Địa chỉ không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (searchQh.isEmpty()) {
            Toast.makeText(getActivity(), "Khu vực không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        if (spNhomkh.getSelectedItem().toString().equals(nhomKhachhang[0])) {
            Toast.makeText(getActivity(), "Nhóm khách hàng không được để trống", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
