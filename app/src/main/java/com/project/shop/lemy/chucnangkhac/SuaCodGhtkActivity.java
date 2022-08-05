package com.project.shop.lemy.chucnangkhac;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.MyActivity;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskOrderV2;
import com.project.shop.lemy.common.Keyboard;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.helper.MoneySupport;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.utils.TimeUtils;
import com.telpoo.frame.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SuaCodGhtkActivity extends MyActivity {
    public static String cookieGhtk=null;
    public static long lastTimeLogin=0;
    TextView tvInfo;
    EditText edUser,edPass,edMadh,edVanDon,edMoney,edMadhCrm,edMoneyCrm;
    CheckBox radioFreeship;
    String maVanDon=null;
    WebView wv;
    private Context context= SuaCodGhtkActivity.this;
    private String curMaDH=null;
    String codenv="nn";
    private String hiddenMoneyCrm="",hiddenDHM="";
    View btnGoCrm;
    View tmp3,tmp4,tmp51;
    EditText edOidUpdate,edTrackingUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_cod_ghtk);
        codenv= SPRSupport.getString("codenv",this,"nn");

        tmp3= findViewById(R.id.tmp3);
        tmp4= findViewById(R.id.tmp4);
        tmp51= findViewById(R.id.tmp51); tmp51.setVisibility(View.GONE);
        edOidUpdate= findViewById(R.id.edOidUpdate);
        edTrackingUpdate= findViewById(R.id.edTrackingUpdate);

        edUser= findViewById(R.id.edUser);
        edPass= findViewById(R.id.edPass);
        edMadh= findViewById(R.id.edMadh);
        edVanDon= findViewById(R.id.edVanDon);
        edMoney= findViewById(R.id.edMoney);
        radioFreeship = findViewById(R.id.radioFreeship);
        tvInfo= findViewById(R.id.tvInfo);
        btnGoCrm= findViewById(R.id.btnGoCrm);
        btnGoCrm.setOnClickListener(view -> {
            ChiTietDonHangActivity.startActivity(this,hiddenDHM);
        });
        tvInfo.setOnClickListener(view -> {
            showToast("DHM"+hiddenDHM+"\n"+"Tiền crm "+MoneySupport.moneyEndK(hiddenMoneyCrm));
        });

        tvInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                edMoneyCrm.setText(hiddenMoneyCrm);
                return false;
            }
        });

        if (edUser.getText().toString().length()==0){
            edUser.setText(SPRSupport.getString("user",this,""));
            edPass.setText(SPRSupport.getString("pass",this,""));
        }


        wv= findViewById(R.id.wv);
        edMadhCrm = findViewById(R.id.edMadhCrm);
        edMoneyCrm= findViewById(R.id.edMoneyCrm);

        edMadh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edVl=edMadh.getText().toString();
                if (!edVl.isEmpty()&&!edVanDon.getText().toString().isEmpty())edVanDon.setText("");
                tvInfo.setText("");
                maVanDon=null;
                setValueOrderId1(null);
                if (edVl.length()>=6) edMadh.setText("");

                if (edVl.equals("0000")) { // bật chức năng thêm mã vận đơn vào hệ thống
                    tmp3.setVisibility(View.GONE);
                    tmp4.setVisibility(View.GONE);
                    tmp51.setVisibility(View.VISIBLE);
                }

            }
        });

        Keyboard.keyEnterListener(edMadh,false,object -> {
            if (edMadh.getText().toString().length()==0) return;
            getInfo(edMadh.getText().toString());
            Keyboard.hideKeyboard(this);
            edMadh.postDelayed(() -> {
                edMadh.requestFocus();
            }, 100);
        });

        edVanDon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edVanDon.getText().toString().equals("001")){
                    findViewById(R.id.parentUserPass).setVisibility(View.VISIBLE);
                    edVanDon.postDelayed(() -> {
                        findViewById(R.id.parentUserPass).setVisibility(View.GONE);
                    },15000);
                }
                if(edVanDon.getText().toString().equals("002")){
                    findViewById(R.id.parentUserPass).setVisibility(View.VISIBLE);
                    edVanDon.postDelayed(() -> {
                        findViewById(R.id.parentUserPass).setVisibility(View.GONE);
                    },15000);

                    edUser.setText("lemy1604@gmail.com");
                    edPass.setText("naq222");
                }

                if (!edVanDon.getText().toString().isEmpty()&&!edMadh.getText().toString().isEmpty())edMadh.setText("");
                tvInfo.setText("");
                maVanDon=null;
                setValueOrderId1(null);
            }
        });

        wv.setWebViewClient(new WebViewClient(){

        });
        wv.getSettings().setJavaScriptEnabled(true);

        if (getIntent().hasExtra("orderid"))
            edMadh.setText(getIntent().getStringExtra("orderid"));

    }

    public void clickUpdate(View view) {

        updateGhtk();

    }

    void setValueOrderId1(String orderId1){
        curMaDH=orderId1;
        if (orderId1!=null&&maVanDon!=null)
        ((TextView)findViewById(R.id.tvInfo1)).setText("Đang sửa cod cho ĐH DHM"+orderId1+" - Mã Vận Đơn "+maVanDon);
        else  ((TextView)findViewById(R.id.tvInfo1)).setText("");

    }

    @Override
    protected void onResume() {
        super.onResume();
        edMadh.postDelayed(() -> {
            edMadh.requestFocus();
            Keyboard.showKeyboard(this);
        }, 500);
    }

    public void clickCheck(View view) {
        showProgressDialog();
        maVanDon=null;


        SPRSupport.save("user",edUser.getText().toString(),this);
        SPRSupport.save("pass",edPass.getText().toString(),this);
        String mmedVandon=""+edVanDon.getText().toString();
        if (mmedVandon.length()>8){
            String sql="SELECT * FROM ;::;.orders o WHERE o.order_shipper_lb_id= "+mmedVandon;
            TaskGeneral.exeTaskGetApi(new Model(){
                @Override
                public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                    closeProgressDialog();
                    JSONArray ja = (JSONArray) data;
                    if (ja.length()!=1){
                        showToast("Không tìm thấy đơn hàng"); return;
                    }

                    getInfo(ja.optJSONObject(0).optString("id"));

                    Mlog.D("sss "+data );

                }

                @Override
                public void onFail(int taskType, String msg, Integer queue) {
                    showToast("có lỗi "+msg);
                    closeProgressDialog();
                }
            },context,sql);
        }
        else
        getInfo(edMadh.getText().toString());
    }

    private void getInfo(String mMaDh) {

        //TaskNetGeneral.exTaskNotifyImportain("sads",context);

        wv.loadUrl("about:blank");
        BaseModel basemodel= new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                closeProgressDialog();
                try {

                    JSONObject jo = new JSONObject(""+data);
                    if (!mMaDh.equals(jo.get("order_id01"))){
                        return;
                    }
                    hiddenMoneyCrm=jo.getString("tien_hang_crm");
                    hiddenDHM=""+jo.get("order_id01");
                    edMadhCrm.setText(mMaDh);
                    edOidUpdate.setText(mMaDh);
                    //edMoneyCrm.setText(jo.getString("tien_hang_crm"));
                    maVanDon=jo.optString("ghtk_label_id",null);
                    setValueOrderId1(mMaDh);
                    String vl="";
                    vl+="DHM"+jo.get("order_id01");
                    vl+= "\nTiền đã nhận CRM: "+jo.getString("tien_da_nhan_crm");
                    vl+= "\nTổng tiền hàng CRM: <Click vào> ";
                    vl+="\nTrạng thái crm: "+jo.get("crm_trangthai");
                    if (maVanDon!=null){
                        int freeShip = jo.getInt("ghtk_is_freeship");
                        if (freeShip==1) radioFreeship.setChecked(true);
                        else radioFreeship.setChecked(false);
                        int pickMoney=(jo.getInt("ghtk_pick_money")-jo.getInt("phi_ship")+jo.getInt("phi_bh"));

                        //edMoney.setText(""+pickMoney);

                        vl+="\n\nTrạng thái GHTK: "+jo.getString("trang_thai_ghtk");
                        vl+="\nCOD GHTK: "+ MoneySupport.moneyDot(""+pickMoney);
                    }
                    else {
                        edMoney.setText("");
                        vl+="\n\nChưa có thông tin trên GHTK";
                    }

                    tvInfo.setText(vl);




                } catch (JSONException e) {
                    e.printStackTrace();
                    tvInfo.setText("Có lỗi! 13s2 ");
                    maVanDon=null;
                    setValueOrderId1(null);
                }


            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                tvInfo.setText("Có lỗi! "+msg);
                closeProgressDialog();
                setValueOrderId1(null);
                maVanDon=null;
            }
        };

        TaskNetOrder taskNetOrder = new TaskNetOrder(basemodel, TaskNetOrder.TASK_ORDER_GHTK,this);
        taskNetOrder.setTaskParram("user",edUser.getText());
        taskNetOrder.setTaskParram("pass",edPass.getText());
        taskNetOrder.setTaskParram("madh",mMaDh);
        //taskNetOrder.setTaskParram("mavandon",edVanDon.getText());

        taskNetOrder.exe();

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_YEAR,-80);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_YEAR,1);

        String url="https://khachhang.giaohangtietkiem.vn/khachhang?customer_created_from=%s+00:00:00&package_status_id=&customer_tel=&code=&is_freeship=-1&is_cancel=0&is_returned=0&customer_fullname=&customer_email=&audited_from=&audited_to=&filter-form-show=min-expand-filter-button";
        url+="&customer_created_to=%s+23:59:59&client_id=DHM"+mMaDh;
        url= String.format(url, TimeUtils.calToString(cal1,"yyyy-MM-dd"),TimeUtils.calToString(cal2,"yyyy-MM-dd"));

       // wv.loadUrl("https://khachhang.giaohangtietkiem.vn/khachhang?customer_created_from=2019-10-21+00:00:00&package_status_id=&customer_tel=&code=&is_freeship=-1&is_cancel=0&is_returned=0&customer_fullname=&customer_email=&audited_from=&audited_to=&filter-form-show=min-expand-filter-button&customer_created_to=2020-11-05+23:59:59&client_id=DHM19991");
        int delay=1;
        if(SuaCodGhtkActivity.cookieGhtk==null|| Calendar.getInstance().getTimeInMillis()-SuaCodGhtkActivity.lastTimeLogin>300000)
            delay = 2;

        String finalUrl = url;
        new Handler().postDelayed(() -> {
            BaseModel basemodel1=new BaseModel(){
                @Override
                public void onSuccess(int taskType, Object data, String msg) {
                    super.onSuccess(taskType, data, msg);
                    ViewUtils.loadDataWv(wv,""+data);
                }



                @Override
                public void onFail(int taskType, String msg) {
                    super.onFail(taskType, msg);
                }
            };
            TaskNetGeneral tt = new TaskNetGeneral(basemodel1,TaskNetGeneral.LOAD_GHTK_WEB_INFO,this);
            tt.setTaskParram("url", finalUrl);
            SuaCodGhtkActivity.cookieGhtk=null;
            tt.setTaskParram("user",edUser.getText());
            tt.setTaskParram("pass",edPass.getText());
            tt.exe();
        },delay);
    }

    public void updateGhtk(){

        if (!isCanEditCod()) return;

        String user= edUser.getText().toString();
        String pass=edPass.getText().toString();
        String pickMoney= edMoney.getText().toString();
        String isFreeship="23";
        if(radioFreeship.isChecked())isFreeship="1";
        else isFreeship="0";

        TaskNetGeneral.exTaskNotifyOnlyMe(codenv+" Sửa cod GHTK DHM"+curMaDH+" - mvd: "
                +maVanDon+" - freeship "+radioFreeship.isChecked()+" số tiền: "+pickMoney,context);


        if (user.isEmpty()||pass.isEmpty()||pickMoney==null|| pickMoney.length()==0){
            showToast("Chưa nhập dữ liệu");
            return;
        }
        showProgressDialog();
        BaseModel basemodel= new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                closeProgressDialog();

                try {
                    JSONObject jo = new JSONObject(""+data);
                    String msggg= jo.getString("msg");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvInfo.setText(Html.fromHtml(msggg, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvInfo.setText(Html.fromHtml(msggg));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvInfo.setText(""+data);
                }

            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                closeProgressDialog();
                tvInfo.setText("Lỗi "+msg);
            }
        };

        TaskNetOrder taskNetOrder = new TaskNetOrder(basemodel, TaskNetOrder.TASK_SUA_COD_GHTK,this);
        taskNetOrder.setTaskParram("user",edUser.getText());
        taskNetOrder.setTaskParram("pass",edPass.getText());
        taskNetOrder.setTaskParram("madh",curMaDH);
        taskNetOrder.setTaskParram("mavandon",maVanDon);
        taskNetOrder.setTaskParram("pick_money",pickMoney);
        taskNetOrder.setTaskParram("is_freeship",isFreeship);


        if (maVanDon==null||maVanDon.length()==0){
            DialogSupport.Message(this,"KhÔng có mã vận đơn",null);
            closeProgressDialog();
            return;
        }

        taskNetOrder.exe();

        String sq="mavandon %s, dhm%s. pick_money %s, is_freeship %s";
        notifiQueCoNguoiSuaCod(String.format(sq,maVanDon,curMaDH,pickMoney,isFreeship));

    }

    private boolean isCanEditCod() {
        String codeCheck="#ahoachat#naq#lehoa#";
        if (!codeCheck.contains("#"+codenv+"#")){
            showDl("Bạn không có quyền sửa cod",null);
            return false;
        }
        return true;
    }

    private void notifiQueCoNguoiSuaCod(String vl) {
        String date= "";
        date= TimeUtils.calToString(Calendar.getInstance()," (dd.MM HHh)");
        vl=vl+date;
        String sql="INSERT INTO "+ BuildConfig.db_nhacviec+ ".`task` (`type`, `content`) VALUES ('3', '%s')";
        sql= String.format(sql,codenv+" "+vl);

        TaskGeneralTh.exeTaskStatement(context,sql,"sadas",123,new Model());


    }

    public void clickUpdateCrm(View view) {
        if (!isCanEditCod())return;
        TaskNetGeneral.exTaskNotifyOnlyMe(codenv+"  Sửa cod CRM DHM"+edMadhCrm.getText().toString(),context);
        showProgressDialog();
        if (edMadhCrm.getText().toString().isEmpty()||edMoneyCrm.getText().toString().isEmpty()){
            showToast("chưa nhập thông tin");
            return;
        }
        BaseModel basemodel= new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                closeProgressDialog();
                showToast("Cập nhật thành công");
                edMadh.setText(edMadhCrm.getText());
                clickCheck(null);

            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                closeProgressDialog();
                tvInfo.setText("Lỗi 33232 "+msg);
            }
        };

        BaseObject oj = new BaseObject();
        oj.set("money_received",edMoneyCrm.getText().toString());

        TaskNetOrder taskNetOrder = new TaskNetOrder(basemodel, TaskNetOrder.TASK_UPDATE,this);
        taskNetOrder.setTaskParram("order_id",edMadhCrm.getText());
        taskNetOrder.setTaskParram("oj",oj);
        taskNetOrder.exe();
        notifiQueCoNguoiSuaCod(String.format("dhm%s. money_received %s",edMadhCrm.getText(),edMoneyCrm.getText().toString()));
    }


    // cập nhật  mã vận đơn ghtk vào hệ thống
    public void clickUpdateTracking(View view) {

        String oidVl=edOidUpdate.getText().toString();
        String trackingVl=edTrackingUpdate.getText().toString();
        if (oidVl.length()==0||trackingVl.length()==0){
            showToast("Chưa nhập dữ liệu"); return;
        }

        BObject ojUpdate= new BObject();
        ojUpdate.set("dvvc",2);
        ojUpdate.set("order_shipper_lb_id",trackingVl);
        Model model=new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                super.onSuccess(taskType, data, msg, queue);
                showToast("Cập nhật thành công");
                ( (TextView)findViewById(R.id.tvLog44)).setText(((TextView) findViewById(R.id.tvLog44)).getText()
                        +"\n Thêm thành công DHM"+oidVl+" mã ghtk: "+trackingVl);
            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                super.onFail(taskType, msg, queue);
                showDl("Lỗi cập nhật: "+msg);
                ( (TextView)findViewById(R.id.tvLog44)).setText((TextView)((TextView) findViewById(R.id.tvLog44)).getText()
                        +"\n *** Thêm Lỗi DHM"+oidVl+" mã ghtk: "+trackingVl);
            }
        };
        TaskOrderV2.update(oidVl,ojUpdate,SuaCodGhtkActivity.this,model);
        edOidUpdate.setText("");
        edTrackingUpdate.setText("");
    }
}
