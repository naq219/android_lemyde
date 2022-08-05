package com.project.shop.lemy.donhang;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.project.shop.lemy.BaseFragment;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.ThayDoiTrangThaiDhAdapter;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.PermissionSupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ThayDoiTrangThaiDonHangFm extends BaseFragment implements View.OnClickListener {
    private ThayDoiTrangThaiDhAdapter adapter;
    private TextView tvTrangthaidh;
    private TextView tvDVVC;
    private TextView tvTong;
    private EditText edtIdDonHang;
    private ImageView imgBarCode;
    private RecyclerView rcView;
    private LinearLayout lnTile;
    View btnLayThongTin, btnSaveChange,printVC,copyId;
    private ImageView btnStart;
    private CompoundBarcodeView barCodeView;
    private RelativeLayout rlBarcode;
    private ImageView imgCanceBarCode;
    private ImageView imgFlash;
    boolean isStart = false;
    boolean isFlash;
    BaseModel baseModel = new BaseModel() {
        @Override
        public void onSuccess(int taskType, Object data, String msg) {
            super.onSuccess(taskType, data, msg);
            switch (taskType) {
                case TaskType.TASK_LIST_DONHANG:
                    try {
                        Object[] mdata = (Object[]) data;
                        BaseObject objectRes = (BaseObject) mdata[1];
                        ArrayList<BaseObject> listCu = new ArrayList<>();
                        listCu.addAll(adapter.listData());
                        adapter.setData(JsonSupport.jsonArray2BaseOj(objectRes.get("datas")));
                        checkIdSai(listCu);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case TaskType.TASK_UPDATEORDER:
                    showToast("Cập nhật trạng thái đơn hàng thành công!");
                    loadDataListOrder();
                    tvTrangthaidh.setText("Trạng thái");
                    tvDVVC.setText("Đơn vị vận chuyển");
                    break;

            }
        }

        @Override
        public void onFail(int taskType, String msg) {
            super.onFail(taskType, msg);
            switch (taskType) {
                case TaskType.TASK_LIST_DONHANG:
                    showToast(msg);
                    break;
                case TaskType.TASK_UPDATEORDER:
                    DialogSupport.dialogThongBao(msg,getActivity(),null);
                    break;
            }
        }
    };

    String status = "";
    String dvvc = "";

    public ThayDoiTrangThaiDonHangFm() {
    }

    public static ThayDoiTrangThaiDonHangFm newInstance() {
        return new ThayDoiTrangThaiDonHangFm();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thay_doi_trang_thai_fm, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        printVC= view.findViewById(R.id.printVC);
        copyId= view.findViewById(R.id.copyId);
        tvTrangthaidh = view.findViewById(R.id.tvTrangthaidh);
        tvDVVC = view.findViewById(R.id.tvDVVC);
        tvTong = view.findViewById(R.id.tvTong);
        edtIdDonHang = view.findViewById(R.id.edtIdDonHang);

        btnSaveChange = view.findViewById(R.id.btnSaveChange);
        btnLayThongTin = view.findViewById(R.id.btnLayThongTin);
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setImageResource(R.drawable.ic_pause);

        imgBarCode = view.findViewById(R.id.imgBarCode);
        imgCanceBarCode = view.findViewById(R.id.imgCanceBarCode);
        imgFlash = view.findViewById(R.id.imgFlash);
        imgFlash.setImageResource(R.drawable.ic_flash_on);
        lnTile = view.findViewById(R.id.lnTile);
        rlBarcode = view.findViewById(R.id.rlBarcode);
        barCodeView = view.findViewById(R.id.barCodeView);

        rcView = view.findViewById(R.id.rcView);
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ThayDoiTrangThaiDhAdapter(getActivity());
        rcView.setAdapter(adapter);

        tvTrangthaidh.setOnClickListener(this);
        tvDVVC.setOnClickListener(this);
        btnSaveChange.setOnClickListener(this);
        btnLayThongTin.setOnClickListener(this);
        imgBarCode.setOnClickListener(this);
        imgCanceBarCode.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        imgFlash.setOnClickListener(this);
        printVC.setOnClickListener(this);
        copyId.setOnClickListener(this);
        edtIdDonHang.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addIdOrder();
                edtIdDonHang.setText("");
                edtIdDonHang.postDelayed(() -> {
                    edtIdDonHang.requestFocus();
                }, 100);

                return true;
            }
            return false;
        });
        adapter.setOnListItemChangeList(list -> {
            if (list == 0) {
                btnSaveChange.setEnabled(false);
                btnLayThongTin.setEnabled(false);
                lnTile.setVisibility(View.GONE);
                return;
            }
            lnTile.setVisibility(View.VISIBLE);
            tvTong.setText("" + list);
            btnSaveChange.setEnabled(true);
            btnLayThongTin.setEnabled(true);
        });

        rcView.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Keyboard.hideKeyboard(getActivity());
                    break;
            }
            return false;
        });
        loadTrangThaiDonHang(true, false);
        loadDonViVanChuyen(true, false);
    }

    public void loadTrangThaiDonHang(boolean reload, boolean show) {
        if (!BaseNetSupport.isNetworkAvailable(getActivity())) return;
        MenuFromApiSupport.createTrangThaiDonHang(getActivity(), tvTrangthaidh, reload, show, key -> status = key);
    }

    public void loadDonViVanChuyen(boolean reload, boolean show) {
        if (!BaseNetSupport.isNetworkAvailable(getActivity())) return;
        MenuFromApiSupport.createDonViVanChuyen(getActivity(), tvDVVC, reload, show, key -> dvvc = key);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTrangthaidh:
                loadTrangThaiDonHang(false, true);
                break;
            case R.id.tvDVVC:
                loadDonViVanChuyen(false, true);
                break;
            case R.id.btnSaveChange:
                if (adapter.getItemCount() == 0) {
                    showToast("Không có đơn hàng cần thay đổi");
                    return;
                }
                if (status.isEmpty() && dvvc.isEmpty()) {
                    showToast("Bạn không thay đổi gi cả");
                    return;
                }

                if(!status.equals("2")){
                    showToast("Lỗi hệ thống ");
                    TaskNetGeneral.exTaskNotifyImportain("Cố tình đổi trạng thái status="+status+" . "+edtIdDonHang.getText(),getContext());
                    return ;
                }

                DialogSupport.dialogUpdateTrangThai(getActivity(), () ->
                        loadUpdateOrder());
                break;
            case R.id.btnLayThongTin:
                loadDataListOrder();
                break;
            case R.id.imgBarCode:
                scandBarCode();
                break;
            case R.id.imgCanceBarCode:
                isFlash = false;
                rlBarcode.setVisibility(View.GONE);
                imgFlash.setImageResource(R.drawable.ic_flash_on);
                barCodeView.setTorchOff();
                barCodeView.pause();
                break;
            case R.id.btnStart:
                clickStartStop();
                break;
            case R.id.imgFlash:
                clickFlash();
                break;

            case R.id.printVC:
                showToast("ec ec");
                String url= BuildConfig.url_printvc+ adapter.getListId();
                PrintActivity.inDonHang(getActivity(),url);
                break;

            case R.id.copyId:
                EditText ed = getView().findViewById(R.id.edPaste);
                ed.setText(adapter.getListId());
                ed.setVisibility(View.VISIBLE);


                break;

        }
    }

    public void checkIdSai(ArrayList<BaseObject> listCu) {
        String listId = adapter.getListIdCheck();
        for (int i = 0; i < listCu.size(); i++) {
            String id = listCu.get(i).get(OrderObj.id);
            if (!listId.contains("#" + id + "#")) {
                BaseObject item = new BaseObject();
                item.set(OrderObj.id, id);
                item.set("id_sai", true);
                adapter.addDataSai(item);
            }
        }
    }

    public void loadDataListOrder() {
        String listId = adapter.getListId();
        if (listId.isEmpty()) {
            showToast("Không có id để truy vấn");
            return;
        }
        BaseObject baseObject = new BaseObject();
        baseObject.set("list_id", listId);
        TaskNet taskNet = new TaskNet(baseModel, TaskType.TASK_LIST_DONHANG, getActivity());
        taskNet.setTaskParram("parram", baseObject);
        taskNet.exe();
    }

    public void addIdOrder() {
        String nhapId = edtIdDonHang.getText().toString();
        if (!validateId(nhapId)) return;
        BaseObject object = new BaseObject();
        object.set(OrderObj.id, nhapId);
        adapter.addData(object);
    }

    public void loadUpdateOrder() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            BaseObject baseObject = new BaseObject();
            baseObject.set(OrderObj.id, adapter.getItem(i).get(OrderObj.id));
            baseObject.set(OrderObj.status, status);
            baseObject.set(OrderObj.dvvc, dvvc);
            baseObject.removeEmpty();
            TaskNet taskNet = new TaskNet(baseModel, TaskType.TASK_UPDATEORDER, getActivity());
            taskNet.setTaskParram("parram", baseObject);
            taskNet.exe();
        }
    }

    private void clickStartStop() {
        if (isStart) {
            barCodeView.resume();
            btnStart.setImageResource(R.drawable.ic_pause);
            isStart = false;
        } else {
            barCodeView.pause();
            btnStart.setImageResource(R.drawable.ic_start);
            isStart = true;
        }
    }

    private void clickFlash() {
        if (!isFlash) {
            barCodeView.setTorchOn();
            imgFlash.setImageResource(R.drawable.ic_flash_off);
            isFlash = true;
        } else {
            barCodeView.setTorchOff();
            imgFlash.setImageResource(R.drawable.ic_flash_on);
            isFlash = false;
        }
    }


    public void scandBarCode() {
        if (!PermissionSupport.getInstall(getActivity()).requestPermissionCamera()) return;
        barCodeView.setTorchOff();
        barCodeView.resume();
        barCodeView.setStatusText("");
        barCodeView.decodeContinuous(callback);
        rlBarcode.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionSupport.READ_PERMISSIONS_CAMERA) scandBarCode();
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barCodeView.setStatusText(result.getText());
                String macode = result.getText();
                if (adapter.checkTrungId(macode)) {
                    BeepManager bm = new BeepManager(getActivity());
                    bm.playBeepSoundAndVibrate();
                    return;
                }
                BaseObject object = new BaseObject();
                object.set(OrderObj.id, macode);
                adapter.addData(object);
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
                mediaPlayer.start();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private boolean validateId(String id) {
        if (id.isEmpty()) {
            showToast("Bạn chưa nhập ID");
            return false;
        }

        if (adapter.checkTrungId(id)) {
            BeepManager bm = new BeepManager(getActivity());
            bm.playBeepSoundAndVibrate();
            showToast("Id đã có trong danh sách");
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        barCodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barCodeView.pause();
        super.onPause();
    }
}
