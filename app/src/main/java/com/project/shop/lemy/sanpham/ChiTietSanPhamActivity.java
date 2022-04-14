package com.project.shop.lemy.sanpham;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChiTietSanPhamActivity extends SwipeBackActivity implements View.OnClickListener {
    private EditText edEan;
    private EditText edtTenSP;
    private EditText edtGiaBuon;
    private EditText edtGiaSi;
    private EditText edtGiaLe;
    private EditText edtKhoiluong;
    private EditText edtPagelink;
    private EditText edtGhichu;
    private EditText edtGioithieu;
    private EditText edtLinkImage;
    private ScrollView scrollView;
    private TextView tvTrangthai;
    private TextView tvNgaytao;
    private TextView tvUpdateDate;
    private TextView tvCapNhat;
    private ProgressBar prBars;
    private ImageView imgEditImage;
    String tenSp, maEan, giaBuon, giaBanSi, giaBanLe, khoiLuong,
            linkAnh, gioiThieu, pageLink, status, note;
    TaskNet taskNet;
    String id = "";
    BaseObject baseObject;
    BaseModel baseModel;
    private SimpleDateFormat dateFormat;
    String intentEan=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        id = getIntent().getStringExtra("id");
        if (getIntent().hasExtra("ean"))
        intentEan = getIntent().getStringExtra("ean");
        initView();
    }

    public void initView() {
        scrollView = findViewById(R.id.scrollView);
        edEan = findViewById(R.id.edEan);
        edtTenSP = findViewById(R.id.edtTenSP);
        tvNgaytao = findViewById(R.id.tvNgaytao);
        edtGiaBuon = findViewById(R.id.edtGiaBuon);
        edtGiaSi = findViewById(R.id.edtGiaSi);
        edtGiaLe = findViewById(R.id.edtGiaLe);
        edtKhoiluong = findViewById(R.id.edtKhoiluong);
        edtPagelink = findViewById(R.id.edtPagelink);
        tvTrangthai = findViewById(R.id.tvTrangthai);
        edtGhichu = findViewById(R.id.edtGhichu);
        edtGioithieu = findViewById(R.id.edtGioithieu);
        tvUpdateDate = findViewById(R.id.tvUpdateDate);
        tvCapNhat = findViewById(R.id.tvCapNhat);
        imgEditImage = findViewById(R.id.imgEditImage);
        edtLinkImage = findViewById(R.id.edtLinkImage);
        prBars = findViewById(R.id.prBars);
        tvCapNhat.setOnClickListener(this);
        tvTrangthai.setOnClickListener(this);
        setTitle("Chi tiết sản phẩm");
        loadChiTietSP();
        responeData();
        loadTrangThaiDonHang(true, false);
    }

    public void loadChiTietSP() {
        prBars.setVisibility(View.VISIBLE);
        BaseObject parram = new BaseObject();
        parram.set(ProductObj.id, id);
        taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                baseObject = (BaseObject) data;
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date created = null;
                Date update = null;
                try {
                    created = dateFormat.parse(baseObject.get(ProductObj.created_at));
                    update = dateFormat.parse(baseObject.get(ProductObj.updated_at));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateFormat.applyPattern("dd/MM/yyyy");
                String createdDate = dateFormat.format(created);
                String updateDate = dateFormat.format(update);
                giaBuon = StringHelper.formatDoubleMoney(baseObject.get(ProductObj.cost_price));
                giaBanSi = StringHelper.formatDoubleMoney(baseObject.get(ProductObj.wholesale_price));
                giaBanLe = StringHelper.formatDoubleMoney(baseObject.get(ProductObj.retail_price));
                khoiLuong = StringHelper.formatDoubleMoney(baseObject.get(ProductObj.weight));
                tenSp = StringHelper.inhoaSupport(baseObject.get(ProductObj.name));
                edtTenSP.setText(tenSp);
                if (intentEan==null)
                edEan.setText(baseObject.get(ProductObj.ean));
                else {
                    String curEan=""+baseObject.get(ProductObj.ean);
                    if (curEan.toLowerCase().equals("null")) curEan="";
                    if (curEan.length()>1){
                        DialogSupport.dialogYesNo("SP đã có EAN cũ rồi ("+curEan+") bạn muốn thêm EAN không?",ChiTietSanPhamActivity.this,isYes -> {
                            if (!isYes) finish();
                        });
                    }
                    edEan.setText(curEan+","+intentEan);
                    edEan.setBackgroundColor(Color.RED);
                }
                tvNgaytao.setText(createdDate);
                edtGiaBuon.setText(giaBuon);
                edtGiaSi.setText(giaBanSi);
                edtGiaLe.setText(giaBanLe);
                edtKhoiluong.setText(khoiLuong);

                edtPagelink.setText(baseObject.get(ProductObj.page_link));
                ImageLoader.getInstance().displayImage(baseObject.get(ProductObj.image), imgEditImage);
                edtLinkImage.setText(baseObject.get(ProductObj.image));

                edtGiaBuon.addTextChangedListener(onTextChangedGiaBuon());
                edtGiaSi.addTextChangedListener(onTextChangedGiaSi());
                edtGiaLe.addTextChangedListener(onTextChangedGiaLe());
                edtKhoiluong.addTextChangedListener(onTextChangedKL());

                edtLinkImage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        linkAnh = edtLinkImage.getText().toString();
                        linkAnh = editable.toString();
                        ImageLoader.getInstance().displayImage(linkAnh, imgEditImage);
                    }
                });

                MenuFromApiSupport.setTextStatus(ChiTietSanPhamActivity.this, tvTrangthai, baseObject.get(ProductObj.status), TaskType.TASK_LIST_STATUS_PRODUCT);
                note = baseObject.get(ProductObj.note);
                edtGhichu.setText(StringHelper.inhoaSupport(note));
                edtGioithieu.setText(baseObject.get(ProductObj.introduction));
                tvUpdateDate.setText(updateDate);
                prBars.setVisibility(View.GONE);
                scrollView.setOnTouchListener((view1, motionEvent) -> {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Keyboard.hideKeyboard(ChiTietSanPhamActivity.this);
                            break;
                    }
                    return false;
                });
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }

        }, TaskType.TASK_CHITIET_SANPHAM, this);
        taskNet.setTaskParram("parram", parram);
        taskNet.exe();
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
                edtGiaSi.removeTextChangedListener(this);
                String editGiaSi = s.toString();
                editGiaSi = StringHelper.formatStr(editGiaSi);
                edtGiaSi.setText(StringHelper.formatDoubleMoney(editGiaSi));
                edtGiaSi.setSelection(edtGiaSi.getText().length());
                edtGiaSi.addTextChangedListener(this);

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
                edtGiaLe.removeTextChangedListener(this);
                String editGiaLe = s.toString();
                editGiaLe = StringHelper.formatStr(editGiaLe);
                edtGiaLe.setText(StringHelper.formatDoubleMoney(editGiaLe));
                edtGiaLe.setSelection(edtGiaLe.getText().length());
                edtGiaLe.addTextChangedListener(this);

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

    public void responeData() {
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_UPDATE_SANPHAM:
                        Toast.makeText(ChiTietSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    case TaskType.TASK_DELETE_SANPHAM:
                        finish();
                        Toast.makeText(ChiTietSanPhamActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                Toast.makeText(ChiTietSanPhamActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelete:
                DialogSupport.dialogXoasp(this, () -> {
                    BaseObject object = new BaseObject();
                    this.deleteProduct(object.get(ProductObj.id, id));
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteProduct(String delete) {
        BaseObject object = new BaseObject();
        object.set(ProductObj.id, delete);
        taskNet = new TaskNet(baseModel, TaskType.TASK_DELETE_SANPHAM, this);
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCapNhat:
                if (baseObject != null) {
                    updateProduct();
                }
                break;
            case R.id.tvTrangthai:
                loadTrangThaiDonHang(false, true);
                break;
        }
    }

    public void loadTrangThaiDonHang(boolean reload, boolean show) {
        MenuFromApiSupport.createStatusProduct(this, tvTrangthai, reload, show, key -> status = key);
    }

    private void updateProduct() {
        if (!isValidate()) return;
        BaseObject object = new BaseObject();
        giaBuon = StringHelper.formatStr(giaBuon);
        giaBanLe = StringHelper.formatStr(giaBanLe);
        giaBanSi = StringHelper.formatStr(giaBanSi);
        khoiLuong = StringHelper.formatStr(khoiLuong);
        object.set(ProductObj.name, tenSp);
        object.set(ProductObj.ean, maEan);
        object.set(ProductObj.cost_price, giaBuon);
        object.set(ProductObj.wholesale_price, giaBanSi);
        object.set(ProductObj.retail_price, giaBanLe);
        object.set(ProductObj.weight, khoiLuong);
        object.set(ProductObj.image, linkAnh);
        object.set(ProductObj.introduction, gioiThieu);
        object.set(ProductObj.page_link, pageLink);
        object.set(ProductObj.note, note);
        object.set(ProductObj.status, status);
        object.set(ProductObj.id, baseObject.get(ProductObj.id));
        taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATE_SANPHAM, this);
        taskNet.setTaskParram("parram", object);
        taskNet.exe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        loadChiTietSP();
        return true;
    }

    private boolean isValidate() {
        tenSp = edtTenSP.getText().toString();
        maEan = edEan.getText().toString();
        giaBuon = edtGiaBuon.getText().toString();
        giaBanSi = edtGiaSi.getText().toString();
        giaBanLe = edtGiaLe.getText().toString();
        khoiLuong = edtKhoiluong.getText().toString();
        linkAnh = edtLinkImage.getText().toString();
        gioiThieu = edtGioithieu.getText().toString();
        pageLink = edtPagelink.getText().toString();
        note = edtGhichu.getText().toString();
        status = tvTrangthai.getText().toString();
        if (tenSp.isEmpty()) showToast("Chưa nhập tên sản phẩm");

        if (giaBuon.isEmpty()) showToast("Chưa nhập Giá Buôn");
        if (giaBanSi.isEmpty()) showToast("Chưa nhập Giá Bán Sỉ");
        if (giaBanLe.isEmpty()) showToast("Chưa nhập Giá Bán Lẻ");
        if (khoiLuong.isEmpty()) showToast("Chưa nhập Giá khối lượng");
        if (gioiThieu.isEmpty()) showToast("Chưa nhập giới thiệu sản phẩm");

        return true;
    }

    private void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.popup_status, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            tvTrangthai.setText(item.getTitle());
            return false;
        });
        popup.show();
    }

}
