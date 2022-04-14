package com.project.shop.lemy.donhang;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.AddDonHangAdapter;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.bean.ShopObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.search.SearchSugestAdapter;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThemDonhangFm extends BaseFragment implements View.OnClickListener {
    ArrayList<BaseObject> listDonhang = new ArrayList<>();
    public SearchView searchFb;
    private SearchView searchChonSanPham;
    private SearchView searchShop;

    private TaskNet taskNet;
    private BaseModel baseModel;
    private RecyclerView recycleView;

    TextView tvTrangThaiDH;
    TextView tvPhuongThucTT;

    private TextView tvDate;
    private TextView tvName;
    private TextView tvTongTien;
    private TextView tvAddDonHang;
    private TextView tvDiaChi;
    private TextView tvShowHide;

    private EditText edtThuThem;
    private EditText edtNhan;
    private EditText edtPhiShip;
    private EditText edtThemGhiChu;

    private CheckBox cboxFree;

    private LinearLayout lnShowHide;
    private LinearLayout lnChitiet;
    private LinearLayout lnListSP;
    private RelativeLayout rlKH;
    private View viewDV;

    private ImageView imgShowHide;
    private ImageView imgCanceKH;

    private SearchSugestAdapter searchSugestAdapter;
    private AddDonHangAdapter addDonHangAdapter;
    Calendar cal;
    Date dateFinish;
    String status = "";
    String payment = "";

    String thuthem;
    String danhan;
    String phiship;
    String ghichu;
    String date;
    String name;
    String tongtien;
    String customerID;
    String id_shop;
    String name_shop;
    String facebook_shop;
    boolean isShowHide = false;

    public ThemDonhangFm() {
    }

    public static ThemDonhangFm newInstance(String param1, String param2) {
        ThemDonhangFm fragment = new ThemDonhangFm();
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
        View v = inflater.inflate(R.layout.them_donhang_fm, container, false);
        initView(v);
        return v;
    }

    public void initView(View view) {
        recycleView = view.findViewById(R.id.recycleView);
        searchFb = view.findViewById(R.id.searchFb);
        searchChonSanPham = view.findViewById(R.id.searchChonSanPham);
        searchShop = view.findViewById(R.id.searchShop);
        tvTrangThaiDH = view.findViewById(R.id.tvTrangThaiDH);
        tvPhuongThucTT = view.findViewById(R.id.tvPhuongThucTT);
        tvDate = view.findViewById(R.id.tvDate);
        tvName = view.findViewById(R.id.tvName);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        tvAddDonHang = view.findViewById(R.id.tvAddDonHang);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvShowHide = view.findViewById(R.id.tvShowHide);
        edtThuThem = view.findViewById(R.id.edtThuThem);
        edtNhan = view.findViewById(R.id.edtNhan);
        edtPhiShip = view.findViewById(R.id.edtPhiShip);
        edtThemGhiChu = view.findViewById(R.id.edtThemGhiChu);
        cboxFree = view.findViewById(R.id.cboxFree);
        lnShowHide = view.findViewById(R.id.lnShowHide);
        lnChitiet = view.findViewById(R.id.lnChitiet);
        lnListSP = view.findViewById(R.id.lnListSP);
        rlKH = view.findViewById(R.id.rlKH);
        imgShowHide = view.findViewById(R.id.imgShowHide);
        imgCanceKH = view.findViewById(R.id.imgCanceKH);
        viewDV = view.findViewById(R.id.viewDV);
        responeData();
        getDefaultInfor();
        handleSearchCustomer();
        loadTrangThaiDonHang(true, false);
        loadPayment(true, false);
        tvDate.setOnClickListener(this);
        tvAddDonHang.setOnClickListener(this);
        imgCanceKH.setOnClickListener(this);
        lnShowHide.setOnClickListener(this);
        tvTrangThaiDH.setOnClickListener(this);
        tvPhuongThucTT.setOnClickListener(this);
        StringHelper.covenrtGia(edtThuThem);
        StringHelper.covenrtGia(edtNhan);
        StringHelper.covenrtGia(edtPhiShip);
    }

    private void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                resetAdd();
                Toast.makeText(getActivity(), "Thêm mới đơn hàng thành công", Toast.LENGTH_LONG).show();
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
            case R.id.tvDate:
                chonDate();
                break;
            case R.id.tvAddDonHang:
                addDonHang();
                break;
            case R.id.imgCanceKH:
                rlKH.setVisibility(View.GONE);
                tvName.setText("");
                tvDiaChi.setText("");
                break;
            case R.id.lnShowHide:
                clickShowHinde();
                break;
            case R.id.tvTrangThaiDH:
                loadTrangThaiDonHang(false, true);
                break;
            case R.id.tvPhuongThucTT:
                loadPayment(false, true);
                break;
        }
    }

    public void loadTrangThaiDonHang(boolean reload, boolean show) {
        MenuFromApiSupport.createTrangThaiDonHang(getActivity(), tvTrangThaiDH, reload, show, key -> status = key);
    }

    public void loadPayment(boolean reload, boolean show) {
        MenuFromApiSupport.createPayment(getActivity(), tvPhuongThucTT, reload, show, key -> payment = key);
    }

    private void handleSearchCustomer() {
        searchSugestAdapter = new SearchSugestAdapter(getActivity());
        addDonHangAdapter = new AddDonHangAdapter(getActivity());
        searchSugestAdapter.setOnItemClickListerer((object, position) -> {
            if (position == TaskType.TASK_SEARCH_CUSTOMERS) {
                if (!object.get(CustomerObj.name).isEmpty())
                    Toast.makeText(getActivity(), "Đã thêm khách hàng", Toast.LENGTH_SHORT).show();
                tvName.setText(object.get(CustomerObj.name));
                tvDiaChi.setText(object.get(CustomerObj.address) + "\n" + object.get(CustomerObj.phone));
                name = tvName.getText().toString();
                if (!name.isEmpty()) rlKH.setVisibility(View.VISIBLE);
                else rlKH.setVisibility(View.GONE);
                customerID = object.get(CustomerObj.id);
                searchFb.clearFocus();
                searchFb.setQuery("", false);
            } else if (position == TaskType.TASK_SEARCHPRODUCTS) {
                searchChonSanPham.clearFocus();
                searchChonSanPham.setQuery("", false);
                recycleView.setHasFixedSize(true);
                recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                object.set(ProductObj.quantity_product, 1);
                addDonHangAdapter.addData(object);
                listDonhang = addDonHangAdapter.listData();
                if (listDonhang.size() != 0) lnListSP.setVisibility(View.VISIBLE);
                recycleView.setAdapter(addDonHangAdapter);
            } else if (position == TaskType.TASK_SHOP) {
                searchShop.clearFocus();
                searchShop.setQuery(object.get(ShopObj.name), true);
                id_shop = object.get(ShopObj.id);
                name_shop = object.get(ShopObj.name);
                facebook_shop = object.get(ShopObj.facebook);
            }
        });

        addDonHangAdapter.onClickTotalAmount(totalamount -> {
            tvTongTien.setText(StringHelper.formatTien(Integer.parseInt(totalamount)));
        });

        addDonHangAdapter.setOnClickDeleteChangeList(list -> {
            if (listDonhang.size() == 0) lnListSP.setVisibility(View.GONE);
        });

        clickNameSearch();
        clickChonSanPhamSearch();
        clickChonShop();
    }

    void addDonHang() {
        if (addDonHangAdapter == null) return;
        listDonhang = addDonHangAdapter.listData();
        if (!isValidate()) return;
        converDate();
        JSONObject order = new JSONObject();
        JSONArray products = new JSONArray();
        for (BaseObject baseObject : listDonhang) {
            try {
                JSONObject product = new JSONObject();
                product.put(ProductObj.id, Integer.parseInt(baseObject.get(ProductObj.id)));
                product.put(OrderObj.quantity, Integer.parseInt(baseObject.get(ProductObj.quantity_product)));
                product.put(ProductObj.retail_price, Integer.parseInt(baseObject.get(ProductObj.retail_price)));
                product.put(ProductObj.wholesale_price, Integer.parseInt(baseObject.get(ProductObj.wholesale_price)));
                product.put(ProductObj.cost_price, Integer.parseInt(baseObject.get(ProductObj.cost_price)));
                products.put(product);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            order.put(OrderObj.customer_id, customerID);
            order.put(OrderObj.additional_cost, StringHelper.formatStr(thuthem));
            order.put(OrderObj.money_received, StringHelper.formatStr(danhan));
            if (tongtien.contains("K")) tongtien = tongtien.replace("K", "000");
            if (tongtien.contains(",")) tongtien = tongtien.replace(",", "");
            order.put(OrderObj.total_amount, tongtien);
            order.put(OrderObj.status, status);
            order.put(OrderObj.payment, payment);
            order.put(OrderObj.shop_id, id_shop);
            order.put(OrderObj.date_created, date);
            order.put(OrderObj.note, ghichu);
            order.put(OrderObj.phi_ship, StringHelper.formatStr(phiship));
            order.put("products", products);
            if (cboxFree.isChecked()) order.put(OrderObj.free_ship, "1");
            else order.put(OrderObj.free_ship, "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseObject objectAddSP = new BaseObject();
        objectAddSP.set("param", order.toString());
        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_ORDER, getActivity());
        taskNet.setTaskParram("parram", objectAddSP);
        taskNet.exe();

    }

    private void clickShowHinde() {
        if (isShowHide) {
            lnChitiet.setVisibility(View.GONE);
            viewDV.setVisibility(View.GONE);
            tvShowHide.setText("Hiện thông tin chi tiết");
            imgShowHide.setImageResource(R.drawable.ic_show);
            isShowHide = false;
        } else {
            lnChitiet.setVisibility(View.VISIBLE);
            viewDV.setVisibility(View.VISIBLE);
            tvShowHide.setText("Ẩn thông tin chi tiết");
            imgShowHide.setImageResource(R.drawable.ic_hide);
            isShowHide = true;
        }
    }

    void clickNameSearch() {
        setSizeSearchView();
        searchFb.setQueryHint("Tìm tên,SDT hoặc facebook");
        searchFb.setIconifiedByDefault(false);
        searchFb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchSugestAdapter.textChangeCustomer(s);
                searchFb.setSuggestionsAdapter(searchSugestAdapter);
                return false;
            }
        });
    }

    void clickChonSanPhamSearch() {
        setSizeSearchView();
        searchChonSanPham.setQueryHint("Tìm tên sản phẩm");
        searchChonSanPham.setIconifiedByDefault(false);
        searchChonSanPham.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSugestAdapter.textChangeProduct(newText);
                searchChonSanPham.setSuggestionsAdapter(searchSugestAdapter);
                return false;
            }
        });
    }

    void clickChonShop() {
        setSizeSearchView();
        searchShop.setQueryHint("Tìm tên shop");
        searchShop.setIconifiedByDefault(false);
        searchShop.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSugestAdapter.textChangeShop(newText);
                searchShop.setSuggestionsAdapter(searchSugestAdapter);
                return false;
            }
        });
    }

    void setSizeSearchView() {
        EditText searchFace = searchFb.findViewById(androidx.appcompat.R.id.search_src_text);
        EditText searchSanPham = searchChonSanPham.findViewById(androidx.appcompat.R.id.search_src_text);
        EditText searchChonShop = searchShop.findViewById(androidx.appcompat.R.id.search_src_text);
        searchFace.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchSanPham.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchChonShop.setTextColor(ResourcesCompat.getColor(getResources(), R.color.mau_nen_chung, null));
        searchFace.setTextSize(14);
        searchSanPham.setTextSize(14);
        searchChonShop.setTextSize(14);
    }

    public void getDefaultInfor() {
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        tvDate.setText(strDate);
    }

    void chonDate() {
        DatePickerDialog.OnDateSetListener callback = (view, year, monthOfYear, dayOfMonth) -> {
            tvDate.setText((dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
            cal.set(dayOfMonth, monthOfYear, year);
            dateFinish = cal.getTime();
        };
        String s = tvDate.getText() + "";
        String strArrtmp[] = s.split("-");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(getActivity(), callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày");
        pic.show();

    }

    void converDate() {
        String s = tvDate.getText() + "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String newFormat = formatter.format(testDate);
        tvDate.setText(newFormat);
        date = tvDate.getText().toString();
    }

    boolean isValidate() {
        thuthem = edtThuThem.getText().toString();
        danhan = edtNhan.getText().toString();
        phiship = edtPhiShip.getText().toString();
        ghichu = edtThemGhiChu.getText().toString();
        date = tvDate.getText().toString();
        name = tvName.getText().toString();
        tongtien = tvTongTien.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa có khách hàng", Toast.LENGTH_LONG).show();
            return false;
        }
        if (status == "0") {
            Toast.makeText(getActivity(), "Chưa chọn trạng thái", Toast.LENGTH_LONG).show();
            return false;
        }
        if (payment == "0") {
            Toast.makeText(getActivity(), "Chưa chọn phương thức thanh toán", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tongtien.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa chọn sản phẩm", Toast.LENGTH_LONG).show();
            return false;
        }
        if (id_shop == null) {
            Toast.makeText(getActivity(), "Chưa chọn Shop", Toast.LENGTH_LONG).show();
            return false;
        }
        if (phiship.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa nhập phí ship", Toast.LENGTH_LONG).show();
            return false;
        }
        if (thuthem.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa nhập phí thu thêm", Toast.LENGTH_LONG).show();
            return false;
        }
        if (danhan.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa nhập số tiền đã nhận", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void resetAdd() {
        Fragment newFragment = new ThemDonhangFm();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
