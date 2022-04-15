package com.project.shop.lemy.sanpham;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lemy.telpoo2lib.model.Model;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.MainActivity;
import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.Net.MyUrl2;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.utils.Mlog;

import org.json.JSONArray;
import org.json.JSONException;


public class SPFmView extends BaseFragment  {
    public static final String OBJ_PARAM_ID = "param2";
    public static final String OBJ_PARAM_ADDNEW =  "OBJ_PARAM_ADDNEW";
    DisplayImageOptions optionLoadImage= new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_loading).showImageOnFail(R.drawable.ic_hinhanhsp).build();

    protected String productId =null;
    protected TextView tvAddSp;
    protected EditText edtTensp, edGiaNhap, edGiaBan, edGiaBuon, edGiaBuonSi, edtLinkanh, edtGioithieu,
            edtPagelink, edtGhiChu,edEan;

    protected ProgressBar prBars;
    TaskNet taskNet;
    Model baseModel;
    CheckBox cbChanBan;

    View tvUpdateTimeDangBai, btnDownload, btnUpload;
    private TextView tvMaDh,tvCtvSuggest,tvBuonSuggest;
    private LinearLayout rootImage;
    JSONArray jaImage=null;
    protected SPFmView() {
    }

    public static SPFmView newInstance( String productId,String newSpName) {
        SPFm fragment = new SPFm();
        Bundle args = new Bundle();
        if (productId!=null)
        args.putString(OBJ_PARAM_ID, productId);

        if (newSpName!=null)
            args.putString(OBJ_PARAM_ADDNEW, newSpName);

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null&&getArguments().containsKey(OBJ_PARAM_ID)) {
            productId =getArguments().getString(OBJ_PARAM_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spfm, container, false);
        initView(view);

        if (getArguments() != null&&getArguments().containsKey(OBJ_PARAM_ADDNEW)) {
            edtTensp.setText(getArguments().getString(OBJ_PARAM_ADDNEW));
        }

        return view;
    }



    private void initView(View view) {
        edEan= view.findViewById(R.id.edEan);
        tvAddSp = view.findViewById(R.id.tvAddSp);
        edtTensp = view.findViewById(R.id.edtTensp);
        tvUpdateTimeDangBai= view.findViewById(R.id.tvUpdateTimeDangBai);
        tvUpdateTimeDangBai.setTag(0l);
        edGiaNhap = view.findViewById(R.id.edGiaNhap123);
        edGiaBan = view.findViewById(R.id.edGiaBan);
        edGiaBuon = view.findViewById(R.id.edGiaBuon);
        edGiaBuonSi = view.findViewById(R.id.edGiaBuonSi);

        rootImage = view.findViewById(R.id.rootImage);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnDownload = view.findViewById(R.id.btnDownload);

        edtLinkanh = view.findViewById(R.id.edtLinkanh);
        edtGioithieu = view.findViewById(R.id.edtGioithieu);
        edtPagelink = view.findViewById(R.id.edtPagelink);
        edtGhiChu = view.findViewById(R.id.edtGhiChu);
        prBars = view.findViewById(R.id.prBars);
        tvMaDh = view.findViewById(R.id.tvMaDh);
        tvCtvSuggest= view.findViewById(R.id.tvCtvSuggest);
        tvBuonSuggest= view.findViewById(R.id.tvBuonSuggest);
        edGiaNhap.addTextChangedListener(edGiaNhap());
        edGiaBan.addTextChangedListener(onTextChangedGiaBan());
        edGiaBuon.addTextChangedListener(onTextChangedGiaBuon());
        edGiaBuonSi.addTextChangedListener(onTextChangedGiaBuonSi());

        cbChanBan= view.findViewById(R.id.cbChanBan);


        tvCtvSuggest.setOnClickListener(view1 -> {
            String t=tvCtvSuggest.getText().toString();
            t= t.substring(0,t.indexOf(" ")).replace(".",",");
            edGiaBuon.setText(t);
        });
        tvBuonSuggest.setOnClickListener(view1 -> {
            String t=tvBuonSuggest.getText().toString();
            t= t.substring(0,t.indexOf(" ")).replace(".",",");
            edGiaBuonSi.setText(t);
        });

        if (getArguments().getString(OBJ_PARAM_ID) !=null) {
            tvAddSp.setText("Cập nhật");

        } else
            tvAddSp.setText("Thêm SP");


        tvAddSp.setTag(0l);
    }

    private TextWatcher edGiaNhap() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edGiaNhap.removeTextChangedListener(this);
                String editGiaBuon = s.toString();
                editGiaBuon = StringHelper.formatStr(editGiaBuon);
                edGiaNhap.setText(StringHelper.formatDoubleMoney(editGiaBuon));
                edGiaNhap.setSelection(edGiaNhap.getText().length());
                edGiaNhap.addTextChangedListener(this);

                suggestGia();


            }
        };
    }

    private void suggestGia() {
        String nhap=edGiaNhap.getText().toString().replace(".","");
        String le=edGiaBan.getText().toString().replace(".","");
        nhap=nhap.replaceAll("\\D+","");
        le=le.replaceAll("\\D+","");
        try {
            int ctv= (Integer.parseInt(nhap)+Integer.parseInt(le))/2;
            tvCtvSuggest.setText(MoneySupport.moneyDot(""+ctv)+" #lãi "+MoneySupport.moneyDot(""+(ctv-Integer.parseInt(nhap))));
            int buon= 0;

            int c1=(Integer.parseInt(le)-Integer.parseInt(nhap))/2;
            if (c1<150000) buon=Integer.parseInt(nhap)+c1;
            else buon=Integer.parseInt(nhap)+150000;

            tvBuonSuggest.setText(MoneySupport.moneyDot(""+buon)+" #lãi "+MoneySupport.moneyDot(""+(buon-Integer.parseInt(nhap))));
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private TextWatcher onTextChangedGiaBan() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edGiaBan.removeTextChangedListener(this);
                String editGiaSi = s.toString();
                editGiaSi = StringHelper.formatStr(editGiaSi);
                edGiaBan.setText(StringHelper.formatDoubleMoney(editGiaSi));
                edGiaBan.setSelection(edGiaBan.getText().length());
                edGiaBan.addTextChangedListener(this);
                suggestGia();
            }
        };
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
                edGiaBuon.removeTextChangedListener(this);
                String editGiaLe = s.toString();
                editGiaLe = StringHelper.formatStr(editGiaLe);
                edGiaBuon.setText(StringHelper.formatDoubleMoney(editGiaLe));
                edGiaBuon.setSelection(edGiaBuon.getText().length());
                edGiaBuon.addTextChangedListener(this);

            }
        };
    }

    private TextWatcher onTextChangedGiaBuonSi() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edGiaBuonSi.removeTextChangedListener(this);
                String editKL = s.toString();
                editKL = StringHelper.formatStr(editKL);
                edGiaBuonSi.setText(StringHelper.formatDoubleMoney(editKL));
                edGiaBuonSi.setSelection(edGiaBuonSi.getText().length());
                edGiaBuonSi.addTextChangedListener(this);

            }
        };
    }


    public boolean isValidate() {
        String tenSp, giaBuon, giaBanSi, giaBanLe, khoiLuong, linkAnh, gioiThieu, pageLink, note;

        tenSp = edtTensp.getText().toString();
        giaBuon = edGiaNhap.getText().toString();
        giaBanSi = edGiaBan.getText().toString();
        giaBanLe = edGiaBuon.getText().toString();

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

        return true;
    }

    void pushFm(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlLayout1, fragment);
        fragmentTransaction.commit();
    }

    protected void loadInfo() {
        rootImage.removeAllViews();
        if (productId == null) return;

        showProgressDialog();
        String sql = "SELECT * FROM ;::;.products p where id=%s ORDER BY p.id DESC LIMIT 10";
        sql = String.format(sql, productId);

        TaskGeneral.exeTaskGetApi(new Model() {
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                super.onSuccess(taskType, data, msg, queue);
                closeProgressDialog();
                JSONArray ja = (JSONArray) data;
                ProductObj oj = new ProductObj(ja.optJSONObject(0));
                edtTensp.setText(oj.getName());
                edGiaNhap.setText("" + oj.getGiaNhap());

                edGiaBan.setText("" + oj.getGiaBanLe());
                edGiaBuon.setText("" + oj.getGiaBuon());
                edGiaBuonSi.setText("" + oj.getGiaBuonSi());
                edtLinkanh.setText(oj.getImage());
                edtLinkanh.setText(oj.getImage());
                edtGioithieu.setText(oj.getAsString(ProductObj.introduction));
                String ean = "" + oj.getAsString("ean");
                if (ean.toLowerCase().equals("null")) ean = "";
                edEan.setText(ean);
                edtGhiChu.setText(oj.getAsString(ProductObj.note));
                tvMaDh.setText("(SP" + oj.getId() + ")");

                if (oj.isLimit()) {
                    cbChanBan.setChecked(true);
                } else cbChanBan.setChecked(false);

                try {
                    String tmp1 = oj.getAsString("images");
                    if (tmp1 == null) return;
                    jaImage = new JSONArray(tmp1);
                    if (jaImage == null) return;
                    for (int i = 0; i < jaImage.length(); i++) {
                        ImageView iv = new ImageView(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        android:layout_marginTop="5dp"
//                        android:layout_gravity="center_horizontal"
                        lp.gravity = Gravity.CENTER_HORIZONTAL;
                        lp.topMargin = 5;

                        iv.setLayoutParams(lp);
                        rootImage.addView(iv);
                        ImageLoader.getInstance().displayImage(MyUrl2.getRealUrlImage(jaImage.optString(i)), iv, optionLoadImage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                super.onFail(taskType, msg, queue);
                closeProgressDialog();
                Mlog.D("sds" + msg);
            }
        }, getContext(), sql);
        }
    }
