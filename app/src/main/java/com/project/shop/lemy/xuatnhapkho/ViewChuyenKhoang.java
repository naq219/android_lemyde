package com.project.shop.lemy.xuatnhapkho;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.Task.TaskStock;
import com.project.shop.lemy.dialog.DialogSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewChuyenKhoang  {
    private  Object productId;
    private  String khoangChuyen;
    private final Context context;
    TextView tvKhoangDen,tvProductName,tvMaxSl,tvMsg;
    EditText edSlDen;
    RecyclerView rc;

    View v,btnAskSave,btnCancel,btnDoSave,loading;
    private String khoChuyenDen;
    private int maxSl=2000;
    ChuyenKhoangAdapter adapter;
    private List<BObject> ojsTonKho=null;
    Model model;
    private int curSlCong =-11;
    private Object oid =null; // neu doid !=null thi update them detail order

    public ViewChuyenKhoang(Object productId,Context context){
        this.productId = productId;

        this.context = context;
        v = View.inflate(context, R.layout.view_chuyenkhoang,null);
        tvKhoangDen = v.findViewById(R.id.khoangDen);
        tvMsg = v.findViewById(R.id.tvMsg);
        edSlDen = v.findViewById(R.id.edSlDen);
        tvProductName = v.findViewById(R.id.tvProductName);
        rc = v.findViewById(R.id.rc);
        loading= v.findViewById(R.id.loading);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(context));
        tvMaxSl =v.findViewById(R.id.tvMaxSl);
        adapter = new ChuyenKhoangAdapter(context,null);
        adapter.setOnSlChange(object -> {
            edSlDen.setText("+"+object);
            if ((""+object).equals("0"))
                edSlDen.setText("");
        });
        adapter.setOnSlPassMaxSl(object -> {
            showMsg("SP này không vượt quá "+object);
        });
        edSlDen.setText("0");

        rc.setAdapter(adapter);
        btnAskSave = v.findViewById(R.id.btnAskSave);
        btnCancel= v.findViewById(R.id.btnCancel);
        btnDoSave= v.findViewById(R.id.btnDoSave);
        btnAskSave.setOnClickListener(v1 -> {
            clickSave(false);
        });
        btnCancel.setOnClickListener(v1 -> {
            rc.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
            btnDoSave.setVisibility(View.GONE);
            btnAskSave.setVisibility(View.VISIBLE);
        });
        btnDoSave.setOnClickListener(v1 -> {
            clickSave(true);
        });

        model = new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {

                if (oid !=null){
                    String sqlUpdateKeepstoreDo= SqlTaskGeneral.updateKeepStoreDo(oid, productId, curSlCong);
                    TaskGeneralTh.exeTaskStatement(context,sqlUpdateKeepstoreDo,"abc123123",123,new Model());
                }

                btnCancel.setVisibility(View.GONE);
                btnDoSave.setVisibility(View.GONE);
                hideLoading();

               JSONArray ja = (JSONArray) data;
               String print="Thành công !\n";
               try {
                   for (int i = 0; i < ja.length(); i++) {
                       JSONObject jo =ja.getJSONObject(i);
                       String print1= ""+jo.optString("name","SP không có tên?")+"\n"+
                        jo.get("khoang_id")+  ": "+jo.get("sl_moi")+" - "+jo.get("sl_cu")+" = "+(jo.getInt("sl_moi")-jo.getInt("sl_cu"))+"\n-----------\n";
                       print+=print1;
                   }
               }catch (Exception e){
                    print="09787 có lỗi xảy ra: "+data;
               }

                showMsg(print,true);

            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                showMsg("\n\n có lỗi xảy ra "+msg,true);
                hideLoading();
            }
        };

    }

    public void setUpdateDetailOrder(Object oid){

        this.oid = oid;
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    private void hideLoading() {
        loading.setVisibility(View.GONE);
    }

    private void clickSave(boolean xacNhan) {
        List<BObject> list = adapter.getData();
        JSONArray ja = new JSONArray();
        String strXacNhan="";
        int slCong=0;
        for (int i = 0; i < list.size(); i++) {
            JSONObject jo = new JSONObject();
            try {
                String khoangid=list.get(i).getAsString("khoang_id");
                int slTru=list.get(i).getAsInt("chuyenkhoang_sltru",0);
                slCong+=slTru;
                if (slTru==0) continue;
                int sl=list.get(i).getAsInt("sl");
                jo.put("khoang_id",khoangid);
                jo.put("product_id",productId);
                jo.put("sl_change",-slTru);
                ja.put(jo);
                String str="%s GIẢM: %s - CÒN LẠI: %s";
                str= String.format(str, khoangid,slTru,sl);
                strXacNhan+=str+"\n";
            } catch (JSONException e) {
                e.printStackTrace();
                DialogSupport.dialogThongBao("0908 Lỗi xảy ra "+e.getMessage(),context,null);
                return;
            }

            if(slCong==0){
                showMsg("Hãy chọn khoang cần chuyển");
                return ;
            }

            JSONObject joDen = new JSONObject();
            try {
                joDen.put("khoang_id",khoangChuyen);
                joDen.put("product_id",productId);
                joDen.put("sl_change",slCong);
                ja.put(joDen);

                String str="%s TĂNG: %s ";
                str= String.format(str, khoangChuyen,slCong);
                strXacNhan+=str+"\n";

            } catch (JSONException e) {
                e.printStackTrace();
                DialogSupport.dialogThongBao("0908 Lỗi xảy ra "+e.getMessage(),context,null);
                return;
            }

        }



        if (!xacNhan){
            rc.setVisibility(View.GONE);
            showMsg(strXacNhan,true);
            btnAskSave.setVisibility(View.GONE);
            btnDoSave.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

        }
        else{
            showLoading();
            showMsg("Đang xử lý !",true);
            TaskStock.exeAddOrChange(ja,model,context);
            curSlCong = slCong;
        }


    }

    public View getView(){

        return v;
    }

    public void showMsg(String msg){
        tvMsg.setText(""+msg);
        tvMsg.setVisibility(View.VISIBLE);
        tvMsg.postDelayed(() -> {
            tvMsg.setVisibility(View.GONE);
        },3000);
    }
    public void showMsg(String msg,boolean dontHide){
        tvMsg.setText(""+msg);
        tvMsg.setVisibility(View.VISIBLE);

    }

    public void setProductName(String productName) {
        tvProductName.setText("(SP"+productId+") "+productName);
    }

    public void setKhoangChuyenDen(String khoangChuyenDen) {
        this.khoangChuyen = khoangChuyenDen;
        tvKhoangDen.setText(khoangChuyenDen);
    }

    public void setKhoChuyenDen(String KhoChuyenDen) {

        khoChuyenDen = KhoChuyenDen;
    }

    public void setTonKho(JSONArray jaTOnKho) {
        ojsTonKho = BObject.JsonArray2List(jaTOnKho);
        adapter.setData(ojsTonKho);
    }

    // giới hạn số lượng SP sẽ chuyển khoang. mặc định là không giới hạn
    public void setMaxSl(int maxSl) {
        tvMaxSl.setText("Số lượng cần chuyển đi: "+maxSl);
        this.maxSl = maxSl;
        adapter.setMaxSl(maxSl);
    }
}
