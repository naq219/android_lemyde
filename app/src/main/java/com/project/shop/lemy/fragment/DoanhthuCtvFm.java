package com.project.shop.lemy.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.R;

public class DoanhthuCtvFm extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recycleView;

    private LinearLayout lnChonCtv;
    private LinearLayout lnThoiGian;
    private LinearLayout lnSapXep;
    private LinearLayout lnTrangThai;

    private Spinner spnChonCtv;
    private Spinner spnThoiGian;
    private Spinner spnSapXep;
    private Spinner spnTrangThai;

    private TextView tvLoc;
    private TextView tvReset;
    private TextView tvSearch;

    private EditText edtSearch;

    public DoanhthuCtvFm() {
    }

    String[] chonctv = {
            "--Chon ctv--",
            "Delhi",
            "Bangalore",
            "Hyderabad",
            "Ahmedabad",
            "Chennai",
            "Kolkata",
            "Pune",
            "Jabalpur"
    };
    String[] thoigian = {
            "--Thời Gian--",
            "Hôm qua",
            "Hôm nay",
            "Tháng này",
            "Tháng trước",
            "Tùy Chọn"
    };
    String[] sapxep = {
            "--Sắp Xếp--",
            "Mới nhất",
            "Cũ Nhất"
    };
    String[] trangthai = {
            "--Trạng Thái--",
            "Order",
            "Lấy Hàng",
            "Gửi Ship",
            "Đã Nhận",
            "Bị Hủy"
    };

    public static DoanhthuCtvFm newInstance(String param1, String param2) {
        DoanhthuCtvFm fragment = new DoanhthuCtvFm();
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
        View view = inflater.inflate(R.layout.doanhthu_ctv_fm, container, false);
        recycleView = view.findViewById(R.id.recycleView);

        lnChonCtv = view.findViewById(R.id.lnChonCtv);
        lnThoiGian = view.findViewById(R.id.lnThoiGian);
        lnSapXep = view.findViewById(R.id.lnSapXep);
        lnTrangThai = view.findViewById(R.id.lnTrangThai);

        tvLoc = view.findViewById(R.id.tvLoc);
        tvReset = view.findViewById(R.id.tvReset);
        tvSearch = view.findViewById(R.id.tvSearch);

        edtSearch = view.findViewById(R.id.edtSearch);

        spnChonCtv = view.findViewById(R.id.spnChonCtv);
        spnThoiGian = view.findViewById(R.id.spnThoiGian);
        spnSapXep = view.findViewById(R.id.spnSapXep);
        spnTrangThai = view.findViewById(R.id.spnTrangThai);

        ArrayAdapter<String> adapterCTV = new ArrayAdapter<>(getActivity(), android.
                R.layout.simple_spinner_dropdown_item, chonctv);
        ArrayAdapter<String> adapterThoiGian = new ArrayAdapter<>(getActivity(), android.
                R.layout.simple_spinner_dropdown_item, thoigian);
        ArrayAdapter<String> adapterSapXep = new ArrayAdapter<>(getActivity(), android.
                R.layout.simple_spinner_dropdown_item, sapxep);
        ArrayAdapter<String> adapterTrangThai = new ArrayAdapter<>(getActivity(), android.
                R.layout.simple_spinner_dropdown_item, trangthai);

        spnChonCtv.setAdapter(adapterCTV);
        spnThoiGian.setAdapter(adapterThoiGian);
        spnSapXep.setAdapter(adapterSapXep);
        spnTrangThai.setAdapter(adapterTrangThai);

        return view;
    }
}
