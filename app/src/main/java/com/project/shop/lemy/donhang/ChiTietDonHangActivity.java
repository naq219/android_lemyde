package com.project.shop.lemy.donhang;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskOrderV2;
import com.project.shop.lemy.Task.TaskProductv2;
import com.project.shop.lemy.Task.TaskSupport;
import com.project.shop.lemy.activity.IncludeFmActivity;

import com.project.shop.lemy.adapter.DetailAdapter2;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.Detail_orders;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.chucnangkhac.SuaCodGhtkActivity;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.OrderHelper;
import com.project.shop.lemy.xuatnhapkho.ViewChuyenKhoang;
import com.project.shop.lemy.xuatnhapkho.XuatKhoActivity;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.KeyboardSupport;
import com.telpoo.frame.utils.TimeUtils;
import com.telpoo.frame.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ChiTietDonHangActivity extends SwipeBackActivity {

    private RecyclerView rcSanPham;
    private DetailAdapter2 adapter;
    View btnTachDon;
    View tvSendSMS, tvInDH, btnDayVanChuyen, viewXuat;
    TextView tvMaDh, tvShop, tvDiaChi, tvTongTien,tvThongBao;
    private Long curQueue = null;
    String orderId;
    private BaseObject oj;
    private Context context;
    private SensorEventListener proximitySensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context= ChiTietDonHangActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //oj1 = getIntent().getParcelableExtra("obj");
        orderId = getIntent().getStringExtra("order_id");
        init();

        tvShop.setOnLongClickListener(view -> {
            Intent intent=new Intent(this, SuaCodGhtkActivity.class);
            intent.putExtra("orderid",orderId);
            startActivity(intent);
            return false;
        });

        tvShop.setOnClickListener(view -> {
            if (oj.getInt(OrderObj.status)!=1&&!SprSupport.isAdmin(ChiTietDonHangActivity.this)){
                showToast("chỉ đơn đang order mới sửa ghi chú được, liên hệ Q");
                return;
            }
            final EditText input = new EditText(ChiTietDonHangActivity.this);
            String note=oj.get(OrderObj.note);
            if ("null".equalsIgnoreCase(note)) note="";
            input.setText(note);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            DialogSupport.dialogYesNoWithView("Sửa ghi chú","",input,ChiTietDonHangActivity.this,isYes -> {
                if (!isYes) return;

                String mnote=input.getText().toString();

                Model model= new Model(){
                    @Override
                    public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                        super.onSuccess(taskType, data, msg, queue);
                       showToast("Cập nhật ghi chú thành công");
                        apiDetail(true);

                    }

                    @Override
                    public void onFail(int taskType, String msg, Integer queue) {
                        super.onFail(taskType, msg, queue);
                        closeProcessDialog();
                        showDl(msg);
                        apiDetail(true);
                    }
                };
                BObject ojUpdate= new BObject();
                ojUpdate.set(OrderObj.note,mnote);
                //TaskProductv2.update(orderId,ojUpdate,ChiTietDonHangActivity.this,model);
                TaskOrderV2.update(orderId,ojUpdate,ChiTietDonHangActivity.this,model);
                showProcessDialog();
            });
        });

    }




    public static void startActivity(Context activityFrom,String order_id){
        Intent intent= new Intent(activityFrom,ChiTietDonHangActivity.class);
        intent.putExtra("order_id",order_id);
        activityFrom.startActivity(intent);
    }


    void init() {
        tvThongBao= findViewById(R.id.tvThongBao);
        tvSendSMS = findViewById(R.id.tvSendSMS);
        tvInDH = findViewById(R.id.tvInDH);
        btnDayVanChuyen = findViewById(R.id.btnDayVanChuyen);
        tvSendSMS.setOnClickListener(view -> ProcessDonHangActivity.sendSms(ChiTietDonHangActivity.this, oj));
        tvInDH.setOnClickListener(view -> ProcessDonHangActivity.inDonHang(ChiTietDonHangActivity.this, oj));
        btnDayVanChuyen.setOnClickListener(view -> {
            DialogSupport.manyButton("Chọn chức năng","Bắn đơn VC","Chế độ chọn Lưu Kho",null,null,context,dl22 -> {
               int postionClick= (int) dl22;
                if (postionClick==1){
                    TaskSupport.callDetailOrderV2(orderId,this,objectDetail -> {
                        if (objectDetail==null){
                            showToast("Lỗi 940202"); return;
                        }
                        ProcessDonHangActivity.pushDonHangHvc(context, (BaseObject) objectDetail,null);
                    });
                }
                if (postionClick==2){
                    adapter.setTypeKeppStock();
                }
            });
        });
        tvMaDh = findViewById(R.id.tvMaDh);
        tvShop = findViewById(R.id.tvShop);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvTongTien = findViewById(R.id.tvTongTien);
        viewXuat = findViewById(R.id.viewXuat);
        btnTachDon = findViewById(R.id.btnTachDon);

        rcSanPham = findViewById(R.id.rcSanPham);
        rcSanPham.setHasFixedSize(true);
        rcSanPham.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DetailAdapter2(new ArrayList<>(),context);
        rcSanPham.setAdapter(adapter);

        adapter.listenHaveKeepStock(object -> {
            setTextInfoThem("ĐANG LƯU KHO / ");
        });
        adapter.setOnClickEdit(object -> {
            onClickEdit(object);
        });
        adapter.setOnClickKeepStock(object -> {
            onClickKeepStock(object);
        });


        //setTitle("Hệ thống do LEMY GERMANY phát triển");

        adapter.setOnTickChanged1(object -> {
            btnTachDon.setVisibility(View.VISIBLE);

        });

        btnTachDon.setOnClickListener(view -> {
            showProcessDialog();
            new Handler().postDelayed(() -> {
                tachVaXuat();
            },300);

        });

        tvDiaChi.setOnClickListener(view -> {

            Class<DonHangFm> cl = DonHangFm.class;
            Intent it = new Intent(this, IncludeFmActivity.class);
            BaseObject ojCustomer = new BaseObject();
            try {
                ojCustomer = JsonSupport.jsonObject2BaseOj(oj.get("customer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            it.putExtra("keysearch",ojCustomer.get(CustomerObj.phone));
            it.putExtra("class",cl.toString());
            startActivity(it);
        });

        tvTongTien.setOnClickListener(view -> {
            Intent it = new Intent(this,SuaCodGhtkActivity.class);
            it.putExtra("orderid",orderId);
            startActivity(it);
        });

    }

    private void setTextInfoThem(String infoluukho) {
        String infoThem=TimeUtils.calToString(Calendar.getInstance(),"Check ngày dd/MM/yyyy");
        if (infoluukho!=null){
            infoThem=infoluukho+infoThem;
            tvMaDh.setTextColor(Color.RED);
        }
        else tvMaDh.setTextColor(Color.BLACK);

        tvMaDh.setText(infoThem);


    }

    private void onClickKeepStock(Object object) {
        Object[] oj= (Object[]) object;
        BaseObject ojDetail = (BaseObject) oj[2];
        ViewChuyenKhoang v = new ViewChuyenKhoang(oj[0],context);
        v.setProductName(ojDetail.get("product_name"));
        v.setKhoangChuyenDen("keep_tudo");
        v.setKhoChuyenDen("KoTanTrieu");
        v.setUpdateDetailOrder(orderId);
        v.setMaxSl((int)oj[1]);
        try {
            JSONArray jaTOnKho= new JSONArray( ojDetail.get("kho"));
            v.setTonKho(jaTOnKho);
            DialogSupport.dialogCustom(v.getView(),context);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(" Lỗi 300839 "+e.getMessage());
        }


    }

    private void onClickEdit(Object object) {
        ArrayList<BaseObject> bo= (ArrayList<BaseObject>) object;
        BaseObject bo0= bo.get(0);
        int slTong=0;
        for (BaseObject bo1: bo) {
            int sl1=bo1.getInt(OrderObj.quantity);
            slTong+=sl1;
        }

        View v = getLayoutInflater().inflate(R.layout.dialog_edit_prodorder,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        Dialog dl = builder.create();
        TextView tvName= v.findViewById(R.id.tvName);
        EditText edSl = v.findViewById(R.id.edSl);
        EditText edGiaBan = v.findViewById(R.id.edGiaBan);
        View tru = v.findViewById(R.id.tru);
        View cong = v.findViewById(R.id.cong);
        View xoa = v.findViewById(R.id.xoa);
        View btnSave = v.findViewById(R.id.btnSave);
        View btnCancel = v.findViewById(R.id.btnCancel);
        xoa.setOnClickListener(view -> {
            edSl.setText("0");
            runTaskEditOd(bo0.get(Detail_orders.order_id),bo0.get(Detail_orders.product_id),0,null,dl);

        });
        btnSave.setOnClickListener(view -> {
            int slNew= Integer.parseInt(edSl.getText().toString());
            runTaskEditOd(bo0.get(Detail_orders.order_id),bo0.get(Detail_orders.product_id),slNew,Integer.parseInt(edGiaBan.getText().toString())*1000,dl);


        });

        tvName.setText("(SP"+bo0.get("product_id")+")"+bo0.get(OrderObj.product_name));
        edSl.setText(""+slTong);
        edGiaBan.setText(""+bo0.getInt(Detail_orders.gia_ban)/1000);

        View.OnClickListener tanggiam= view -> {
            int vl= 0;

            try {
                vl=Integer.parseInt(edSl.getText().toString());
            } catch (Exception e){
                e.printStackTrace();
            };
            if (view.getId()==R.id.tru) vl=vl-1;
            else  vl=vl+1;
            if (vl<1) vl=1;
            edSl.setText(String.valueOf(vl));

        };

        tru.setOnClickListener(tanggiam);
        cong.setOnClickListener(tanggiam);


        dl.show();
        btnCancel.setOnClickListener(view -> {
            dl.dismiss();
        }
        );


    }

    private void runTaskEditOd(String orderId,String productId,Integer sl,Integer giaBan,Dialog dl){

        TextView tv= dl.findViewById(R.id.tvThongBao);
        View btnSave = dl.findViewById(R.id.btnSave);
        Button btnCancel = dl.findViewById(R.id.btnCancel);
        TaskOrderV2 task= new TaskOrderV2(new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                dl.findViewById(R.id.loading).setVisibility(View.GONE);
                JSONObject jo= (JSONObject) data;
                String od=jo.optString("order_id","sd");
                String pro=jo.optString("product_id","ss");
                if (!orderId.equals(od)||!productId.equals(pro))
                    tv.setText("Có lỗi cập nhật NHẦM sản phẩm. hãy xem lại đh");
                else{
                    tv.setText("Cập nhật thành công!\n DHM"+od+"  SP"+pro+"  SL:"+jo.optString("sl"));
                    tv.setBackgroundColor(Color.GREEN);
                }

                btnSave.setVisibility(View.GONE);
                btnCancel.setText("Đóng lại");
                btnCancel.setOnClickListener(view -> {
                    dl.dismiss();
                    apiDetail(false);
                });

            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                dl.findViewById(R.id.loading).setVisibility(View.GONE);
                tv.setText("Có lỗi: "+msg);
            }
        }, TaskOrderV2.UPDATE_THEO_SP,context);
        TaskParams param=new TaskParams();
        BObject oj = new BObject();

        if(SprSupport.isAdmin(context))
        oj.set("force_edit",1);
        oj.set("order_id",orderId);
        oj.set("product_id",productId);
        if (sl!=null)
        oj.set("sl",sl);
        if (giaBan!=null)
        oj.set("gia_ban",giaBan);
        param.put("oj",oj);
        dl.findViewById(R.id.loading).setVisibility(View.VISIBLE);
        task.exe(param);
    }

    private void autoTach(){
        String thongBao= getThongBaoTachDon(oj);
        int moneyR=oj.getInt(OrderObj.money_received,0);
        if (thongBao.length()>0||moneyR>0){
            if (!SprSupport.isAdmin(context)){

                TaskNetGeneral.exTaskNotifyOnlyMe("Đơn chặn tách DHM "+oj.get("id"), context);

                DialogSupport.dialogThongBao("Đơn này có thông báo hoặc có tiền đã nhận\n"+thongBao+"\nTiền đã nhận: "+moneyR,context,object -> {
                    closeProcessDialog();

                });
                return;
            }

        }

        View v = getLayoutInflater().inflate(R.layout.dialog_tachdon,null);
        EditText edNote= v.findViewById(R.id.edNote);
        EditText edNoteOld= v.findViewById(R.id.edNoteOld);
        String noteOld=""+oj.get(OrderObj.note); if ("null".equals(noteOld))noteOld="";
        edNoteOld.setText(noteOld);
        EditText edMoney= v.findViewById(R.id.edMoney);
        if (!SprSupport.isAdmin(context)){
            edMoney.setVisibility(View.GONE);
//            edNote.setVisibility(View.GONE);
//            edNoteOld.setVisibility(View.GONE);
        }
        String finalNoteOld = noteOld;
        String strThongBao=""+getThongBao(oj);
        DialogSupport.dialogYesNoWithView("Đồng ý tách ra?",strThongBao,v,context, isYes -> {
            if (!isYes) {
                closeProcessDialog();
                return;
            }

            if (strThongBao.length()>4){
                String noteNotifi="\n";
                if (!edNote.getText().toString().isEmpty())noteNotifi+="Note mới "+edNote.getText().toString();
                if (!edNoteOld.getText().toString().isEmpty())noteNotifi+="\nNote cũ "+edNoteOld.getText().toString();
                TaskGeneralTh.exeTaskNhacViecSimple(this,0,"Tách đơn  "+oj.get("id")+" -- \n"+strThongBao+noteNotifi,Calendar.getInstance().getTimeInMillis()+"","DHM"+oj.get("id")+"#");
            }


            String noteOldNew="";
            if (!finalNoteOld.equals(edNoteOld.getText().toString())) noteOldNew=edNoteOld.getText().toString();
            String note= edNote.getText().toString(); if (note.isEmpty())note=null;
            String money= edMoney.getText().toString();if (money.isEmpty()) money=null;
            //TaskNetOrder.exeTaskTachDon(baseModel,context,orderId, idsTick,note,money,123L);

            Model model= new Model(){
                @Override
                public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                    closeProcessDialog();
                    DialogSupport.dialogThongBao("Thành công, đơn mới: DHM"+data,context,object -> {
                        ChiTietDonHangActivity.startActivity(context, String.valueOf(data));

                    });
                }

                @Override
                public void onFail(int taskType, String msg, Integer queue) {
                    closeProcessDialog();
                    showToast("tách đơn k thành công "+msg);
                    apiDetail(false);
                }

            };
            TaskOrderV2.exeTaskTachDonAuto(model,context,orderId,note,noteOldNew,money);

        });

    }
    private void tachVaXuat() {

        String thongBao= getThongBaoTachDon(oj);
        int moneyR=oj.getInt(OrderObj.money_received);
        if (thongBao.length()>0||moneyR>0){

            DialogSupport.dialogThongBao("Đơn này có thông báo hoặc có tiền đã nhận\n"+thongBao+"\nTiền đã nhận: "+moneyR,context,object -> {
                closeProcessDialog();
            });

            if (!SprSupport.isAdmin(context)){
                TaskNetGeneral.exTaskNotifyOnlyMe("Đơn chặn tách DHM "+oj.get("id"), context);
                return;
            }



        }

        String idsTick = adapter.getDOIdsTick();
        if (idsTick == null) {
            showToast("bạn chưa chọn sp để tách");
            return;
        }

        // tính xem tổng tiền của đơn tách
        int gbt2= adapter.getDOMoneyTick();

        View v = getLayoutInflater().inflate(R.layout.dialog_tachdon,null);
        EditText edNote= v.findViewById(R.id.edNote);
        EditText edMoney= v.findViewById(R.id.edMoney);
        if (!SprSupport.isAdmin(context)){
            edMoney.setVisibility(View.GONE);
            edNote.setVisibility(View.GONE);
        }
        edMoney.setHint("Tổng : "+ MoneySupport.moneyDot(String.valueOf(gbt2)) );
        if (moneyR>0){
            if (moneyR>gbt2) edMoney.setText(""+gbt2);
            else edMoney.setText(""+moneyR);
        }


        DialogSupport.dialogYesNoWithView("Đồng ý tách ra?",getThongBao(oj),v,context,isYes -> {
            if (!isYes) {
                closeProcessDialog();
                return;
            }
            String note= edNote.getText().toString(); if (note.isEmpty())note=null;
            String money= edMoney.getText().toString();if (money.isEmpty()) money=null;
            //TaskNetOrder.exeTaskTachDon(baseModel,context,orderId, idsTick,note,money,123L);

            Model model= new Model(){
                @Override
                public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                    closeProcessDialog();
                    DialogSupport.dialogThongBao("Thành công, đơn mới: DHM"+data,context,object -> {
                        ChiTietDonHangActivity.startActivity(context, String.valueOf(data));

                    });
                }

                @Override
                public void onFail(int taskType, String msg, Integer queue) {
                    closeProcessDialog();
                    showToast("tách đơn k thành công "+msg);
                    apiDetail(false);
                }

            };
            TaskOrderV2.exeTaskTachDon(model,context,orderId, idsTick,note,money);

        });

    }

    private void apiDetail(boolean silent) {
        if (!silent)
        showProcessDialog();
//        else showToast("vại");
        BaseModel baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {

                if (curQueue.toString().equals(msg)) {
                    closeProcessDialog();
                    try {
                        oj = JsonSupport.jsonObject2BaseOj((JSONObject) data);
                        loadData(oj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogSupport.dialogThongBao("lỗi 123 " + e.getMessage(), ChiTietDonHangActivity.this, object -> {
                            finish();
                        });
                    }

                } else {
                    //showToast(" Có lỗi, hãy thử lại"); finish();
                    clearView();
                    closeProcessDialog();
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                closeProcessDialog();
//                DialogSupport.dialogThongBao("lỗi " + msg, ChiTietDonHangActivity.this, object -> {
//                    finish();
//                });

                DialogSupport.dialogYesNo(msg+" lỗi tải dữ liệu, thử lại?",context,isYes -> {
                    if (isYes) apiDetail(false);
                    else  finish();
                });

            }
        };

        curQueue = Calendar.getInstance().getTimeInMillis();
        TaskNetOrder.extaskDetail(orderId, baseModel, this, curQueue);
    }

    private void clearView() {
        setTitle("Lỗi tải thông tin");
        tvTongTien.setText("");
        tvShop.setText("");
        tvDiaChi.setText("");
        adapter.setData(new ArrayList<>());
        setTextInfoThem(null);
    }

    private void loadData(BaseObject data) {
        setTextInfoThem(null);
        if(oj.getInt(OrderObj.keep_stock,0)==1){
            if (oj.getInt(OrderObj.status)==5)
                tvThongBao.setText("Đơn này đang lưu kho ?");
        }
        try {
            BaseObject ojCustomer = JsonSupport.jsonObject2BaseOj(oj.get("customer"));
            tvDiaChi.setText(ojCustomer.get(CustomerObj.name) + "(" + ojCustomer.get(CustomerObj.Ten_fb) + ") * " + ojCustomer.get(CustomerObj.phone) + " * " + ojCustomer.get("full_address"));

            ArrayList<BaseObject> listProducts = JsonSupport.jsonArray2BaseOj(oj.get("detail_orders"));
            adapter.setData(listProducts);

        } catch (JSONException e) {
            e.printStackTrace();
            DialogSupport.dialogThongBao("lấy thông tin lỗi " + e.getMessage(), this, object -> {
                finish();
            });
        }
        int shopId=oj.getInt("shop_id",-1);
        tvMaDh.setVisibility(View.VISIBLE);

//        if (shopId==2||shopId==3||shopId==39){
//            tvMaDh.setText(TimeUtils.calToString(Calendar.getInstance(),"dd/MM/yyyy Tải lại để xem dl mới nhất")+"\nHệ Thống Order Phát Triển Bởi LEMY GERMANY");
//        }


        //tvMaDh.setVisibility(View.GONE);
        setTitle("DHM" + oj.get(OrderObj.id) +" - KH"+oj.get("customer_id")+ " - " + OrderHelper.getStatusName(oj.get(OrderObj.status)));
        String ghichucc=oj.get(OrderObj.note); if (ghichucc==null||ghichucc.equals("null")) ghichucc="";
        tvShop.setText("Shop: " + oj.get(OrderObj.shop_name) + ". Ghi chú: " + ghichucc);
        String freeship="Thu phí ship"; if(oj.getInt("free_ship")==1)freeship= "Miễn phí ship";
        tvTongTien.setText("Tổng: " + MoneySupport.moneyEndK(oj.get(OrderObj.total_amount)) + "  Đã ck: " + MoneySupport.moneyEndK(oj.get(OrderObj.money_received))+" * "+freeship);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View v = getLayoutInflater().inflate(R.layout.view_edsearch_dhm,null);
        EditText ed = v.findViewById(R.id.ed);
        View btn = v.findViewById(R.id.btn);
        ed.setShowSoftInputOnFocus(true);
        ed.postDelayed(() -> {
            ed.requestFocus();
            KeyboardSupport.showKeyboard(context,ed);
        },200);

        AlertDialog dl = DialogSupport.dialogCustom(v, context);
        btn.setOnClickListener(view -> {
            dl.dismiss();
            String dhm = ed.getText().toString();
            if(dhm.length()>0){
                orderId=dhm;
                apiDetail(false);
            }

        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_dhm,menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onclickXuat(View view) {
        if (adapter.isHangVeDu()){
            xuatAll(); return;
        }

        View v= getLayoutInflater().inflate(R.layout.dialog_tachxuatdh,null);
        AlertDialog dl = DialogSupport.dialogCustom(v, context);
        View viewXuatAll= v.findViewById(R.id.viewXuatAll);
        View viewTachDaco= v.findViewById(R.id.viewTachDaco);
        viewXuatAll.setOnClickListener(view1 -> {

            TaskGeneralTh.exeTaskNhacViecSimple(context,99,"Xuất ưu tiên "+orderId,"","");

            xuatAll();
            dl.dismiss();
        });
        viewTachDaco.setOnClickListener(view1 -> {
            autoTach();
            dl.dismiss();
        });




    }

    private void xuatAll() {

        BaseObject baseObject = oj;
        String status = oj.get(OrderObj.status);
        String orderIdDHM = "DHM" + orderId;
        if (!"1".equals(status)){
            showToast("chỉ có thể xuất đơn đang Order"); return;
        }


        BaseObject ojCustomer = null;
        try {
            ojCustomer = JsonSupport.jsonObject2BaseOj(baseObject.get("customer"));

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(" có lỗi 7291 " + e.getMessage());
            return;
        }

        String districk_id = ojCustomer.get(CustomerObj.district_id);
        if (districk_id.equals("1000") || districk_id.equals("1001")) {
            Toast.makeText(this, "Đơn Chưa có địa chỉ", Toast.LENGTH_SHORT).show();
            TaskNetGeneral.exTaskNotifyImportain(orderIdDHM + "\n" + "chưa có địa chỉ"+ojCustomer.get(CustomerObj.phone), this);
            return;
        }




        String thongBao= getThongBao(baseObject);

        if (thongBao.length()>0 && thongBao.contains("chanxuat")&&!SprSupport.isAdmin(context)){
            DialogSupport.dialogThongBao("Đơn này báo QUẾ trước khi xuất ",this,null);
            TaskNetGeneral.exTaskNotifyImportain("đơn chặn XUẤT "+thongBao, this);
            return;
        }

        if (thongBao.length() > 0) {
            String finalThongBao = thongBao;
            DialogSupport.dialogThongBao(orderIdDHM + "\n" + thongBao, this, object -> {
                baseObject.set(OrderObj.status, status);
                baseObject.set("obj", String.valueOf(baseObject));
                Intent it = new Intent(this, XuatKhoActivity.class);
                it.putExtra("orderid", baseObject.get("id"));
                it.putExtra("ghichu", finalThongBao);
                this.startActivity(it);
            });
            TaskNetGeneral.exTaskNotifyImportain(orderIdDHM + "\n" + thongBao, this);
            return;
        }

        baseObject.set(OrderObj.status, status);
        baseObject.set("obj", String.valueOf(baseObject));
        Intent it = new Intent(this, XuatKhoActivity.class);
        it.putExtra("orderid", baseObject.get("id"));

        this.startActivity(it);


    }







    private String getThongBaoTachDon(BaseObject ojIn) {
        BaseObject ojCustomer = null;
        try {
            ojCustomer = JsonSupport.jsonObject2BaseOj(ojIn.get("customer"));

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(" có lỗi 7291 " + e.getMessage());

        }

        String pttt = "" + ojCustomer.get(CustomerObj.pttt);
        String orderId = ojIn.get("id");

        int totalAmount = ojIn.getInt(OrderObj.total_amount, 123);
        int moneyReceived = ojIn.getInt(OrderObj.money_received, 0);
        String noteXuatKho = ojCustomer.get(CustomerObj.note_xuatkho);
        String noteOrder = ojIn.get(OrderObj.note);
        String thongBao = "";
        if (totalAmount - moneyReceived != 0 && moneyReceived > 0)
            thongBao += "";

        if (noteXuatKho != null && noteXuatKho.contains("chantach"))
            thongBao += "* " + noteXuatKho + "\n";
        if (noteOrder != null && noteOrder.contains("chantach"))
            thongBao += "* Ghi chú:" + noteOrder + "\n";

        if ("1".equals(pttt) && totalAmount - moneyReceived != 0) thongBao += "";


        return thongBao;
    }

    private String getThongBao(BaseObject ojIn) {
        BaseObject ojCustomer = null;
        try {
            ojCustomer = JsonSupport.jsonObject2BaseOj(ojIn.get("customer"));

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(" có lỗi 7291 " + e.getMessage());

        }

        String pttt = "" + ojCustomer.get(CustomerObj.pttt);
        String orderId = ojIn.get("id");

        int totalAmount = ojIn.getInt(OrderObj.total_amount, 123);
        int moneyReceived = ojIn.getInt(OrderObj.money_received, 0);
        String noteXuatKho = ojCustomer.get(CustomerObj.note_xuatkho);
        String noteOrder = ojIn.get(OrderObj.note);
        String thongBao = "";


        if (noteXuatKho != null && !noteXuatKho.equals("null"))
            thongBao += "* " + noteXuatKho + "\n";
        if (noteOrder != null && !noteOrder.equals("null") && noteOrder.length() > 0)
            thongBao += "* Ghi chú:" + noteOrder + "\n";

        if ("1".equals(pttt) && totalAmount - moneyReceived != 0) thongBao += "* Báo khách ck\n";

        if ("1".equals(ojIn.get(OrderObj.customers_group)))
            thongBao += "* Đơn của khách buôn\n";
        if ("-1".equals(ojIn.get(OrderObj.customers_group)))
            thongBao += "* Đơn của khách bị chặn\n";

        return thongBao;
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiDetail(true);
    }




}
