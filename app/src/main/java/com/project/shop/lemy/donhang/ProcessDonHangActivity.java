package com.project.shop.lemy.donhang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;
import com.project.shop.lemy.SwipeBackActivity;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.ListTieuDeAdapter;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.common.PoupMenuSupport;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.db.DbSupport;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.widget.TextWatcherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProcessDonHangActivity extends SwipeBackActivity {
    BaseObject objectDonHang;
    String action;
    WebView webviewPrint;
    EditText edtTen, edtSdt, edtIdHuyen, edtIdTinh, edtDiaChi;

    public static final String ACTION_IN = "ACTION_IN";
    public static final String ACTION_DAY_HVC = "ACTION_DAY_HVC";

    public static final String ghtk_fly = "ghtk_fly";
    public static final String ghtk_road = "ghtk_road";

    BaseObject objectHvc = new BaseObject();
    String hvc;
    JSONObject objectRequestHvc = new JSONObject();
    boolean isPrint = false;
    ScrollView scrollView;
    EditText tvPickMoney;
    TextView edTrongLuong;
    String newcod;
    int cod;
    TextView tvShiper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proccess_donhang);
        initView();

        tvPickMoney = findViewById(R.id.tvPickMoney);
        scrollView = findViewById(R.id.scrollView);
        if (!SprSupport.getPass(this).equals("@")) {
            tvPickMoney.setFocusable(false);
            tvPickMoney.setOnClickListener(view -> showToast("B???n kh??ng ph???i admin"));
        }
        objectHvc.set(ghtk_fly, "Giao h??ng ti???t ki???m Bay");
        objectHvc.set(ghtk_road, "Giao h??ng ti???t ki???m B???");

        objectDonHang = getIntent().getParcelableExtra("data");
        hvc = objectDonHang.get(OrderObj.dvvc);
        action = getIntent().getStringExtra("action");

        if (getIntent().hasExtra("dvvc")){
            hvc=getIntent().getStringExtra("dvvc");
            tvShiper.setText(objectHvc.get(hvc));
        }

        if (action.equals(ACTION_IN)) inDonHang();
        if (action.equals(ACTION_DAY_HVC)) dayDonQuaHVC();

        scrollView.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_MOVE:
                    Keyboard.hideKeyboard(this);
                    break;
            }
            return false;
        });

//        edTrongLuong.addTextChangedListener(new TextWatcherAdapter(edTrongLuong,(view, text) -> {
//            try {
//                objectRequestHvc.put("total_weight", edTrongLuong.getText().toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }));

        edTrongLuong.setOnClickListener(view -> {
            View v = View.inflate(this,R.layout.view_dialog_cannang,null);
            View btnSubmit= v.findViewById(R.id.btnSubmit);
            EditText ed = v.findViewById(R.id.ed);
            EditText chieu1= v.findViewById(R.id.chieu1);
            EditText chieu2= v.findViewById(R.id.chieu2);
            EditText chieu3= v.findViewById(R.id.chieu3);
            TextWatcherAdapter.TextWatcherListener listen= new TextWatcherAdapter.TextWatcherListener() {
                @Override
                public void onTextChanged(EditText view, String text) {


                    try {
                        int x= Integer.parseInt(chieu1.getText().toString());
                        int y= Integer.parseInt(chieu2.getText().toString());
                        int z= Integer.parseInt(chieu3.getText().toString());
                        int to=(x*y*z)/6;
                        ed.setText(String.valueOf(to));
                    }
                    catch (Exception e){

                    }



                }
            };
            chieu1.addTextChangedListener(new TextWatcherAdapter(chieu1,listen));
            chieu2.addTextChangedListener(new TextWatcherAdapter(chieu2,listen));
            chieu3.addTextChangedListener(new TextWatcherAdapter(chieu3,listen));

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            AlertDialog dd = builder.create();
            dd.show();

            btnSubmit.setOnClickListener(view1 -> {
                try {
                    int x= Integer.parseInt(ed.getText().toString());
                    edTrongLuong.setText(ed.getText());
                }
                catch (Exception e){
                    edTrongLuong.setText("0");
                }

               dd.dismiss();

            });


        });
    }

    private void initView() {
        tvShiper= findViewById(R.id.tvShiper);
        edTrongLuong = findViewById(R.id.edTrongLuong);
    }

    public void dayDonQuaHVC() {
        JSONArray listProduct = new JSONArray();

        String isFreShip = "C??";
        int weight = 0;
        cod = objectDonHang.getInt(OrderObj.cod, 0);
        if (objectDonHang.getInt(OrderObj.free_ship) == 0) isFreShip = "Kh??ng";

        try {
            ArrayList<BaseObject> products = JsonSupport.jsonArray2BaseOj(objectDonHang.get(OrderObj.products));
            for (int i = 0; i < products.size(); i++) {
                BaseObject item = products.get(i);
                int productWeight = item.getInt("product_weight");
                int quantity = item.getInt("quantity");
                weight += quantity * productWeight;

                JSONObject itemProductHVC = new JSONObject();
                itemProductHVC.put("name", item.get("product_name"));
                itemProductHVC.put("quantity", quantity);
                itemProductHVC.put("weight", productWeight);

                listProduct.put(itemProductHVC);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showThongBao("C?? l???i x??? l?? d??? li???u! 90180228-" + e.getMessage(), object -> {
                finish();
            });
        }
        setTitle("Chuy???n qua h??ng v???n chuy???n");

        TextView tvTen = findViewById(R.id.tvTen);
        TextView tvSdt = findViewById(R.id.tvSdt);
        TextView tvDiaChi = findViewById(R.id.tvDiaChi);
        TextView tvHuyen = findViewById(R.id.tvHuyen);
        TextView tvTinh = findViewById(R.id.tvTinh);

         tvShiper = findViewById(R.id.tvShiper);
        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvNote = findViewById(R.id.tvNote);


        TextView tvIsFreShip = findViewById(R.id.tvIsFreShip);
        TextView tvIsPhiShip = findViewById(R.id.tvIsPhiShip);

        edtTen = findViewById(R.id.edtTen);
        edtSdt = findViewById(R.id.edtSdt);
        edtIdHuyen = findViewById(R.id.edtIdHuyen);
        edtIdTinh = findViewById(R.id.edtIdTinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);

        formatText(tvTen, "T??n", objectDonHang.get(OrderObj.customers_name, ""));
        formatText(tvSdt, "S??? ??i???n tho???i", objectDonHang.get(OrderObj.customers_phone, ""));
        formatText(tvDiaChi, "?????a ch???", objectDonHang.get(OrderObj.customers_address, ""));
        formatText(tvHuyen, "Qu???n/Huy???n", objectDonHang.get(OrderObj.customers_district_name, ""));
        formatText(tvTinh, "T???nh/Th??nh ph???", objectDonHang.get(OrderObj.customers_province_name, ""));
        formatText(tvTinh, "T???nh/Th??nh ph???", objectDonHang.get(OrderObj.customers_province_name, ""));

        formatText(tvNote, "Ghi ch??", objectDonHang.get(OrderObj.note, ""));
        formatText(tvOrderId, "M?? ????n h??ng", "DHM" + objectDonHang.get(OrderObj.id));
        formatText(tvIsFreShip, "Free Ship", isFreShip);
        formatText(tvIsPhiShip, "Ph?? Ship", objectDonHang.get(OrderObj.phi_ship, ""));
        formatText(tvIsPhiShip, "Ph?? Ship", StringHelper.formatTien(objectDonHang.getInt(OrderObj.phi_ship)));
        //formatText(tvTrongLuong, "Tr???ng l?????ng", weight + "g");

        if (edTrongLuong.getText().toString().isEmpty())
        edTrongLuong.setText(""+weight);
        tvPickMoney.setText(StringHelper.formatDoubleMoney(objectDonHang.get(OrderObj.cod)));

        tvPickMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvPickMoney.removeTextChangedListener(this);
                newcod = editable.toString();
                newcod = StringHelper.formatStr(newcod);
                if (newcod.isEmpty()) newcod = "0";
                tvPickMoney.setText(StringHelper.formatDoubleMoney(newcod));
                tvPickMoney.setSelection(tvPickMoney.getText().length());
                tvPickMoney.addTextChangedListener(this);
            }
        });

        try {
            objectRequestHvc.put("products", listProduct);
            objectRequestHvc.put("shipper", hvc);
            objectRequestHvc.put("order_id", "DHM" + objectDonHang.get(OrderObj.id));
            objectRequestHvc.put("note", objectDonHang.get(OrderObj.note, ""));
            objectRequestHvc.put("weight_option", "gram");
            objectRequestHvc.put("total_weight", edTrongLuong.getText().toString());
            objectRequestHvc.put("is_freeship", objectDonHang.getInt(OrderObj.free_ship));
        } catch (Exception e) {
            showThongBao("C?? l???i x??? l?? d??? li???u! 31121-" + e.getMessage(), object -> {
                finish();
            });
        }

        BaseObject objectGui = new BaseObject();
        objectGui.set("name", edtTen.getText().toString());
        objectGui.set("phone", edtSdt.getText().toString());
        objectGui.set("address", edtDiaChi.getText().toString());
        objectGui.set("province_id", edtIdTinh.getText().toString());
        objectGui.set("district_id", edtIdHuyen.getText().toString());

        BaseObject objectNhan = new BaseObject();
        objectNhan.set("name", objectDonHang.get(OrderObj.customers_name, ""));
        objectNhan.set("phone", objectDonHang.get(OrderObj.customers_phone, ""));
        objectNhan.set("address", objectDonHang.get(OrderObj.customers_address, ""));
        objectNhan.set("province_id", objectDonHang.get(OrderObj.customers_province_id, ""));
        objectNhan.set("district_id", objectDonHang.get(OrderObj.customers_district_id, ""));

        try {
            objectRequestHvc.put("address_from", objectGui.toJson());
            objectRequestHvc.put("address_to", objectNhan.toJson());
            objectRequestHvc.put("value_bh",objectDonHang.get(OrderObj.total_amount));
        } catch (Exception e) {
            showThongBao("C?? l???i d??? li???u 812821 -" + e.getMessage(), object -> {
                finish();
            });
        }

        findViewById(R.id.btnOk).setOnClickListener(v -> {
            String pickmoney = tvPickMoney.getText().toString();
            cod = Integer.parseInt(StringHelper.formatStr(pickmoney));

            int curTrongLuong= Integer.parseInt(""+edTrongLuong.getText().toString());
            if (curTrongLuong==0){
                showToast("c??n n???ng ch??a nh???p");
                return;
            }
            if (hvc.equals(objectDonHang.get(OrderObj.dvvc))) {
                showToast("B???n ch??a ch???n ????n v??? v???n chuy???n");
                return;
            }
                try {
                objectRequestHvc.put("pick_money", cod);

                objectRequestHvc.put("total_weight", curTrongLuong);
                if (cod == 0) {
                    DialogSupport.dialogCOD(ProcessDonHangActivity.this, "????n n??y kh??ng thu COD ?? baby?", () -> guiVanChuyen());
                    return;
                }
                if (cod > 0 && objectDonHang.getInt(OrderObj.payment, 0) == 2) {
                    DialogSupport.dialogCOD(ProcessDonHangActivity.this, "Kh??ch ch??a ck ????? ti???n, c?? cho g???i ??i kh??ng?", () -> guiVanChuyen());
                    return;
                }
                guiVanChuyen();
            } catch (JSONException e) {
                e.printStackTrace();
                DialogSupport.dialogThongBao("C?? l???i kh??ng g???i ????n ??i ???????c",ProcessDonHangActivity.this,null);
            }

        });
        tvShiper.setOnClickListener(v -> PoupMenuSupport.showMenuValue(ProcessDonHangActivity.this, tvShiper, objectHvc, new Listerner.OnPoupMenuItemClick() {
            @Override
            public void onItemClick(String key) {
                hvc = key;
                dayDonQuaHVC();
            }
        }));

        findViewById(R.id.layoutChuyenDonHang).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        try {
            String s = objectRequestHvc.toString(2);
            ((TextView) findViewById(R.id.tvJson)).setText(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void guiVanChuyen() {
        showProcessDialog("??ang ?????y ????n h??ng sang h??ng v???n chuy???n. Vui l??ng ch??? trong gi??y l??t...");

        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                closeProcessDialog();
                showThongBao((String) data, object -> {
                            finish();
                        }
                );

            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                closeProcessDialog();
                showToast(msg);

            }
        }, TaskType.TASK_PUSH_HVC, this);
        taskNet.setTaskParram("param", objectRequestHvc);
        taskNet.setTaskParram("orderId", objectDonHang.getInt(OrderObj.id));
        taskNet.exe();
    }

    public void formatText(TextView textView, String title, String value) {
        textView.setText(Html.fromHtml(title + ": <b>" + value + "</b>"));
    }

    public void inDonHang() {
        webviewPrint = findViewById(R.id.webviewPrint);
        webviewPrint.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("inDonHang", "url: " + url);
                if (url.contains("login")) {
                    webviewPrint.setVisibility(View.VISIBLE);
                    return;
                }
                webviewPrint.setVisibility(View.GONE);

                isPrint = true;
                PrintManager printManager = (PrintManager) getBaseContext()
                        .getSystemService(Context.PRINT_SERVICE);

                // Get a print adapter instance
                PrintDocumentAdapter printAdapter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    printAdapter = webviewPrint.createPrintDocumentAdapter("printcrm");
                else printAdapter = webviewPrint.createPrintDocumentAdapter();

                // Create a print job with name and adapter instance
                String jobName = getString(R.string.app_name) + " Document";
                PrintJob printJob = printManager.print(jobName, printAdapter,
                        new PrintAttributes.Builder().build());

            }
        });
        webviewPrint.loadUrl(BuildConfig.url_in + objectDonHang.get(OrderObj.id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPrint) finish();
    }

    public static void inDonHang(Context context, BaseObject object) {
        String checkStt = object.get(OrderObj.status, "");
        if (checkStt.equals("1")) {
            Toast.makeText(context, "????n h??ng ??ang Order, kh??ng th??? in", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, ProcessDonHangActivity.class);
        intent.putExtra("data", object);
        intent.putExtra("action", ProcessDonHangActivity.ACTION_IN);
        context.startActivity(intent);
    }



    public static void pushDonHangHvc(Context context, BaseObject object,String dvvc){
        pushDonHangHvc(context,object,dvvc,false);
    }



    public static void pushDonHangHvc(Context context, BaseObject object,String dvvc,boolean acceptAll) {
        String checkStt = object.get(OrderObj.status, "");
        String districk_id=""+object.get(OrderObj.customers_district_id);
        if (districk_id.equals("1000")||districk_id.equals("1001")){
            Toast.makeText(context, "????n Ch??a c?? ?????a ch???", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkStt.equals("5")&&!acceptAll) {
            DialogSupport.dialogYesNo("????n ch??a xu???t, v???n ti???p t???c b???n ????n?",context,isYes -> {
                if (!isYes) return;
                pushDonHangHvc(context,object,dvvc,true);
            });
            //Toast.makeText(context, "????n ch??a l???y h??ng, kh??ng th??? chuy???n", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, ProcessDonHangActivity.class);
        intent.putExtra("data", object);
        if (dvvc!=null)
            intent.putExtra("dvvc", dvvc);
        intent.putExtra("action", ProcessDonHangActivity.ACTION_DAY_HVC);
        context.startActivity(intent);

    }



    public static void sendSms(Context context, BaseObject item) {

        if (item.getInt(OrderObj.status)==1&&!SprSupport.isAdmin(context)){
            Toast.makeText(context,"H??y l???y h??ng tr?????c khi g???i tin nh???n",Toast.LENGTH_LONG).show();
            return;
        }

        BaseObject ojCustomer;
        ArrayList<BaseObject> listPr;
        try {
            listPr = JsonSupport.jsonArray2BaseOj(item.get("detail_orders"));
            ojCustomer= JsonSupport.jsonObject2BaseOj(item.get("customer"));

            String noteXuat= ""+ojCustomer.get("note_xuatkho");
            if (noteXuat.contains("#chansms")){
                Toast.makeText(context,"Kh??ch kh??ng mu???n nh???n tin nh???n th??ng b??o",Toast.LENGTH_LONG).show();
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(context,"C?? l???i "+e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }


        ListTieuDeAdapter listTieuDeAdapter;
        ArrayList<BaseObject> listSms = new ArrayList<>();
        Dialog dialog;
        dialog = new Dialog(context);
        dialog.setTitle("Tin nh???n m???u");
        dialog.setContentView(R.layout.list_tieude_dialog);
        int width = (context.getResources().getDisplayMetrics().widthPixels * 1);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog.getWindow().setLayout(width, height);

        RecyclerView rcListTieuDe = dialog.findViewById(R.id.rcListTieuDe);
        rcListTieuDe.setHasFixedSize(true);
        rcListTieuDe.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<BaseObject> smsDb = DbSupport.getChude();

        listTieuDeAdapter = new ListTieuDeAdapter(smsDb, context, item);
        if (smsDb.size() == 0) {
            Toast.makeText(context, "Ch??a c?? SMS m???u", Toast.LENGTH_SHORT).show();
            return;
        }
        //ArrayList<BaseObject> listPr = getListProductOrder(item);
        for (int i = 0; i < smsDb.size(); i++) {
            String sp = "";
            BaseObject object = smsDb.get(i);
            object.set(CustomerObj.phone, ojCustomer.get(CustomerObj.phone));
            object.set(OrderObj.id, item.get(OrderObj.id));
            object.set(OrderObj.total_amount, item.get(OrderObj.total_amount));
            for (int j = 0; j < listPr.size(); j++) {
                BaseObject objProduct = listPr.get(j);
                String proDuct = objProduct.get("product_name") + ": " + "\n"
                        + objProduct.get("quantity") + " x "
                        + MoneySupport.moneyEndK(objProduct.get("gia_ban"))  + "\n";

                sp = sp + proDuct;
            }
            sp = sp +   "Tong:" + object.get(OrderObj.total_amount, "") + ".";
            object.set("thongtinsp", StringHelper.deAccent(sp));
            listSms.add(object);
            rcListTieuDe.setAdapter(listTieuDeAdapter);
            dialog.show();
        }
    }



    public static ArrayList<BaseObject> getListProductOrder(BaseObject object) {
        ArrayList<BaseObject> list = new ArrayList<>();
        try {
            list = JsonSupport.jsonArray2BaseOj(object.get(OrderObj.products));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
