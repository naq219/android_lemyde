//package com.project.shop.lemy.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.project.shop.lemy.BaseFragment;
//import com.project.shop.lemy.R;
//import com.project.shop.lemy.Task.TaskNet;
//import com.project.shop.lemy.Task.TaskType;
//import com.project.shop.lemy.bean.OrderObj;
//import com.project.shop.lemy.bean.ShopObj;
//import com.telpoo.frame.model.BaseModel;
//import com.telpoo.frame.object.BaseObject;
//
//import java.util.ArrayList;
//
//public class ThemShopFm extends BaseFragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
//    private BaseModel baseModel;
//    private TaskNet taskNet;
//    private EditText edtTenshop, edtFacebook;
//    private Spinner spnChonQl;
//    private TextView tvThemshop;
//    private ProgressBar prBarAddShop;
//    String nameShop, nameFace;
//    String[] user;
//    String idNguoiban;
//    ArrayList<BaseObject> listNguoiban;
//
//    public ThemShopFm() {
//    }
//
//    public static ThemShopFm newInstance(String param1, String param2) {
//        ThemShopFm fragment = new ThemShopFm();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//            responeData();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.them_shop_fm, container, false);
//        initView(view);
//        return view;
//    }
//
//    private void initView(View v) {
//        edtTenshop = v.findViewById(R.id.edtTenshop);
//        edtFacebook = v.findViewById(R.id.edtFacebook);
//        spnChonQl = v.findViewById(R.id.spnChonQl);
//        tvThemshop = v.findViewById(R.id.tvThemshop);
//        prBarAddShop = v.findViewById(R.id.prBarAddShop);
//
//        spnChonQl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (listNguoiban != null) {
//                    idNguoiban = listNguoiban.get(i).get(ShopObj.id);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        tvThemshop.setOnClickListener(view -> addShop());
//        loadUser();
//    }
//
//    public void responeData() {
//        baseModel = new BaseModel() {
//            @Override
//            public void onSuccess(int taskType, Object data, String msg) {
//                super.onSuccess(taskType, data, msg);
//                switch (taskType) {
//                    case TaskType.TASK_ADD_SHOP:
//                        prBarAddShop.setVisibility(View.GONE);
//                        spnChonQl.clearFocus();
//                        Toast.makeText(getActivity(), "Thêm mới Shop thành công", Toast.LENGTH_LONG).show();
//                        break;
//                    case TaskType.TASK_LIST_NGUOIBAN:
//                        listNguoiban = ((ArrayList<BaseObject>) data);
//                        if (listNguoiban.size() < 1) return;
//                        user = new String[listNguoiban.size()];
//                        for (int i = 0; i < listNguoiban.size(); i++) {
//                            user[i] = listNguoiban.get(i).get(OrderObj.username);
//                        }
//                        ArrayAdapter<String> adapterNguoiBan = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_ttdh, user);
//                        spnChonQl.setAdapter(adapterNguoiBan);
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(int taskType, String msg) {
//                super.onFail(taskType, msg);
//                prBarAddShop.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//            }
//        };
//    }
//
//    public void loadUser() {
//        BaseObject baseObject = new BaseObject();
//        taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_NGUOIBAN, getActivity());
//        taskNet.setTaskParram("parram", baseObject);
//        taskNet.exe();
//    }
//
//    private void addShop() {
//        if (!isValidate()) return;
//        BaseObject object = new BaseObject();
//        object.set(ShopObj.name, nameShop);
//        object.set(ShopObj.facebook, nameFace);
//        object.set(ShopObj.user_id, idNguoiban);
//        taskNet = new TaskNet(baseModel, TaskType.TASK_ADD_SHOP, getActivity());
//        taskNet.setTaskParram("parram", object);
//        taskNet.exe();
//        prBarAddShop.setVisibility(View.VISIBLE);
//    }
//
//    private boolean isValidate() {
//        nameShop = edtTenshop.getText().toString();
//        nameFace = edtFacebook.getText().toString();
//        if (nameShop.isEmpty()) {
//            Toast.makeText(getActivity(), "Tên shop không được để trống", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (nameFace.isEmpty()) {
//            Toast.makeText(getActivity(), "Facebook không được để trống", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//}
