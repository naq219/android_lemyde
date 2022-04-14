//package com.project.shop.lemy.xuatnhapkho;
//
//import android.os.Bundle;
//import android.support.v4.content.res.ResourcesCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.project.shop.lemy.BaseFragment;
//import com.project.shop.lemy.R;
//import com.project.shop.lemy.Task.TaskNet;
//import com.project.shop.lemy.Task.TaskType;
//import com.project.shop.lemy.bean.OrderObj;
//import com.project.shop.lemy.bean.ProductObj;
//import com.project.shop.lemy.common.Keyboard;
//import com.project.shop.lemy.common.MenuFromApiSupport;
//import com.project.shop.lemy.helper.StringHelper;
//import com.project.shop.lemy.search.SearchSugestAdapter;
//import com.telpoo.frame.model.BaseModel;
//import com.telpoo.frame.object.BaseObject;
//import com.telpoo.frame.utils.JsonSupport;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class XuatKhoFm extends BaseFragment implements View.OnClickListener {
//    private static final String OBJ_PARAM = "OBJ_PARAM";
//    public static final String XUAT_KHO = "XUAT_KHO";
//    public static final String XUAT_HANG = "XUAT_HANG";
//    private TextView tvChonKhoXk, tvChonKieuXuat, tvAddPhieuXuat, tvTrangThai, tvMaDh, tvChonKhoXh;
//    private EditText edtGhichu;
//    private LinearLayout lnXuatHang, lnSanPham, lnKhoXh, lnChonXuatkho;
//    private SearchView searchChonSanPhamXh;
//    private ScrollView scView;
//    private SearchSugestAdapter searchSugestAdapter;
//    //private AddNhapHangAdapter addNhapHangAdapter;
//    private RecyclerView recycleView;
//    private ProgressBar prBarXh;
//    ArrayList<BaseObject> listProduct = new ArrayList<>();
//    String khoChuaHang = "";
//    String kieuXuat = "";
//    String ghiChu = "";
//    String searchSanPham = "";
//    String khoang = "";
//    TaskNet taskNet;
//    BaseModel baseModel;
//    private String type;
//    private BaseObject baseObject;
//    Boolean cancelRequest = false;
//    String khoId;
//
//    public XuatKhoFm() {
//    }
//
//    public static XuatKhoFm newInstance(String type, BaseObject baseObject) {
//        XuatKhoFm fragment = new XuatKhoFm();
//        Bundle args = new Bundle();
//        args.putString(XUAT_HANG, type);
//        args.putParcelable(OBJ_PARAM, baseObject);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            type = getArguments().getString(XUAT_HANG);
//            baseObject = getArguments().getParcelable(OBJ_PARAM);
//            responeData();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.xuat_kho_fm, container, false);
//        initView(view);
//        return view;
//    }
//
//    public void initView(View v) {
//        tvChonKhoXk = v.findViewById(R.id.tvChonKhoXk);
//        tvChonKieuXuat = v.findViewById(R.id.tvChonKieuXuat);
//        tvAddPhieuXuat = v.findViewById(R.id.tvAddPhieuXuat);
//        edtGhichu = v.findViewById(R.id.edtGhichu);
//        searchChonSanPhamXh = v.findViewById(R.id.searchChonSanPhamXh);
//        recycleView = v.findViewById(R.id.recycleView);
//        prBarXh = v.findViewById(R.id.prBarXh);
//        scView = v.findViewById(R.id.scView);
//        tvTrangThai = v.findViewById(R.id.tvTrangThai);
//        tvMaDh = v.findViewById(R.id.tvMaDh);
//        lnXuatHang = v.findViewById(R.id.lnXuatHang);
//        lnSanPham = v.findViewById(R.id.lnSanPham);
//        tvChonKhoXh = v.findViewById(R.id.tvChonKhoXh);
//        lnChonXuatkho = v.findViewById(R.id.lnChonXuatkho);
//        lnKhoXh = v.findViewById(R.id.lnKhoXh);
//        tvChonKhoXk.setOnClickListener(this);
//        tvChonKieuXuat.setOnClickListener(this);
//        tvAddPhieuXuat.setOnClickListener(this);
//        tvChonKhoXh.setOnClickListener(this);
//        responeData();
//        loadKhoChuaHang(true, false);
//        loadKieuXuat(true, false);
//        loadKhoXuatHang(true, false);
//
//        //search sản phẩm xuất kho
//        addNhapHangAdapter = new AddNhapHangAdapter(getActivity(), type);
//        recycleView.setHasFixedSize(true);
//        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recycleView.setAdapter(addNhapHangAdapter);
//
//        searchSugestAdapter = new SearchSugestAdapter(getActivity());
//        searchSugestAdapter.setOnItemClickListerer((object, position) -> {
//            if (position == TaskType.TASK_SEARCHPRODUCTS) {
//                searchChonSanPhamXh.clearFocus();
//                searchChonSanPhamXh.setQuery("", false);
//                if (!validateId(object.get(ProductObj.id))) {
//                    showToast("Sản phẩm đã có trong danh sách");
//                    return;
//                }
//                object.set(ProductObj.sl, "1");
//                addNhapHangAdapter.addData(object);
//            }
//        });
//
//        EditText searchSanPhamNh = searchChonSanPhamXh.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchSanPhamNh.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
//        searchSanPhamNh.setTextSize(14);
//        searchChonSanPhamXh.setQueryHint("Tìm tên sản phẩm");
//        searchChonSanPhamXh.setIconifiedByDefault(false);
//        searchChonSanPhamXh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchSugestAdapter.textChangeProduct(newText);
//                searchChonSanPhamXh.setSuggestionsAdapter(searchSugestAdapter);
//                return false;
//            }
//        });
//
//        scView.setOnTouchListener((view1, motionEvent) -> {
//            switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                case MotionEvent.ACTION_POINTER_UP:
//                case MotionEvent.ACTION_POINTER_DOWN:
//                case MotionEvent.ACTION_MOVE:
//                    Keyboard.hideKeyboard(getActivity());
//                    break;
//            }
//            return false;
//        });
//
//        //type Xuất Hàng
//        if (type.equals(XUAT_HANG)) {
//            infoXuatHang();
//            lnXuatHang.setVisibility(View.VISIBLE);
//            lnKhoXh.setVisibility(View.VISIBLE);
//            lnSanPham.setVisibility(View.GONE);
//            lnChonXuatkho.setVisibility(View.GONE);
//            getActivity().setTitle("Xuất  hàng");
//        }
//    }
//
//    private void infoXuatHang() {
//        if (baseObject == null) return;
//        MenuFromApiSupport.setTextStatus(getActivity(), tvTrangThai, baseObject.get(OrderObj.status), TaskType.TASK_LIST_STATUS);
//        tvMaDh.setText("DHM" + baseObject.get(OrderObj.id));
//        edtGhichu.setText(StringHelper.inhoaSupport(baseObject.get(OrderObj.note)));
//        MenuFromApiSupport.setTextStatus(getActivity(), tvChonKieuXuat, baseObject.get(ProductObj.type), TaskType.TASK_LIST_KIEUXUAT);
//
//        //hiển thị sản phẩm xuất hàng
//        recycleView.setHasFixedSize(true);
//        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        try {
//            listProduct = JsonSupport.jsonArray2BaseOj(baseObject.get(OrderObj.products));
//            addNhapHangAdapter.setData(listProduct);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadKhoHang() {
//        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_KHOANGSLSP, getActivity());
//        taskNet.setTaskParram("kho_id", khoId);
//        taskNet.setTaskParram("products", addNhapHangAdapter.getProduct());
//        taskNet.exe();
//        prBarXh.setVisibility(View.VISIBLE);
//    }
//
//    private void addXuatKho() {
//        if (addNhapHangAdapter == null) return;
//        listProduct = addNhapHangAdapter.getProduct();
//        if (!isValidate()) return;
//        JSONObject objXuat = new JSONObject();
//        JSONArray products = new JSONArray();
//        for (BaseObject baseObject : listProduct) {
//            try {
//                JSONObject product = new JSONObject();
//                product.put(ProductObj.id, baseObject.get(ProductObj.id));
//                product.put(ProductObj.name, baseObject.get(ProductObj.name));
//                khoang = baseObject.get(ProductObj.khoang_id);
//                if (khoang == null) {
//                    showToast("Chưa chọn khoang chứa");
//                    return;
//                }
//                product.put(ProductObj.khoang_id, khoang);
//                product.put(ProductObj.sl, Integer.parseInt(baseObject.get(ProductObj.sl)));
//                products.put(product);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            objXuat.put(ProductObj.kho_id, khoChuaHang);
//            objXuat.put(ProductObj.type, Integer.parseInt(kieuXuat));
//            objXuat.put(ProductObj.note, ghiChu);
//            objXuat.put("products", products);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        BaseObject ojAddXuatKho = new BaseObject();
//        ojAddXuatKho.set("param", objXuat.toString());
//        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_XUATKHO, getActivity());
//        taskNet.setTaskParram("parram", ojAddXuatKho);
//        taskNet.exe();
//        prBarXh.setVisibility(View.VISIBLE);
//    }
//
//    private void responeData() {
//        baseModel = new BaseModel() {
//            @Override
//            public void onSuccess(int taskType, Object data, String msg) {
//                super.onSuccess(taskType, data, msg);
//                switch (taskType) {
//                    case TaskType.TASK_ADD_XUATKHO:
//                        prBarXh.setVisibility(View.GONE);
//                        BaseObject object = (BaseObject) data;
//                        if (object.get("message").equals("success")) {
//                            showToast("Thêm đơn xuất thành công");
//                        } else showToast(object.get("message"));
//                        break;
//                    case TaskType.TASK_LIST_KHOANGSLSP:
//                        //Chỗ này sẽ setup chọn khoang
//                        //nếu mà khoang rỗng thì cảnh báo sản phẩm không có trong kho
//                        prBarXh.setVisibility(View.GONE);
//                        JSONArray datas = (JSONArray) data;
//                }
//            }
//
//            @Override
//            public void onFail(int taskType, String msg) {
//                super.onFail(taskType, msg);
//                prBarXh.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//            }
//        }
//
//        ;
//    }
//
//    HashMap<String, Integer> mapsKho = new HashMap<>();
//    HashMap<String, Boolean> mapsSanPhamKhongTonTaiTrongKho = new HashMap<>();
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tvChonKhoXk:
//                loadKhoChuaHang(false, true);
//                break;
//            case R.id.tvChonKieuXuat:
//                loadKieuXuat(false, true);
//                break;
//            case R.id.tvAddPhieuXuat:
//                addXuatKho();
//                break;
//            case R.id.tvChonKhoXh:
//                loadKhoXuatHang(false, true);
//                break;
//        }
//    }
//
//    //Chọn kho xuất hàng
//    public void loadKhoXuatHang(boolean reload, boolean show) {
//        MenuFromApiSupport.createKhoChuaHang(getActivity(), tvChonKhoXh, reload, show, key -> {
//            khoChuaHang = key;
//            khoId = key;
//            cancelRequest = false;
//            prBarXh.setVisibility(View.GONE);
//            loadKhoHang();
//        });
//    }
//
//    public void loadKieuXuat(boolean reload, boolean show) {
//        MenuFromApiSupport.createKieuXuat(getActivity(), tvChonKieuXuat, reload, show, key -> kieuXuat = key);
//    }
//
//    public void loadKhoChuaHang(boolean reload, boolean show) {
//        MenuFromApiSupport.createKhoChuaHang(getActivity(), tvChonKhoXk, reload, show, key -> khoChuaHang = key);
//    }
//
//    private boolean validateId(String id) {
//        if (addNhapHangAdapter.checkTrungId(id)) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean isValidate() {
//        ghiChu = edtGhichu.getText().toString();
//        searchSanPham = searchChonSanPhamXh.getQuery().toString();
//        if (khoChuaHang.isEmpty() || khoChuaHang.equals("null")) {
//            showToast("Kho chứa hàng không được để trống");
//            return false;
//        }
//        if (kieuXuat.isEmpty() || kieuXuat.equals("null")) {
//            showToast("Kiểu nhập không được để trống");
//            return false;
//        }
//        return true;
//    }
//}
