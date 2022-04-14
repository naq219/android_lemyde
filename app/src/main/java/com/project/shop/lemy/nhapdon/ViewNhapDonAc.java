package com.project.shop.lemy.nhapdon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.TaskParams;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNetCustomer;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.Task.TaskNetOrder;
import com.project.shop.lemy.Task.TaskNetProduct;
import com.project.shop.lemy.Task.TaskNhapDonV2;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.adapter.GeneralAdapter;
import com.project.shop.lemy.adapter.SuggestPNhapDonAdapter;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.widget.TextWatcherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewNhapDonAc extends ViewNhapDonAcParent{

    TaskNetCustomer taskC;
    BaseModel baseModel;
    SuggestPNhapDonAdapter simpleAdapter;

    private int queuesearchp=1,queueSearchPhone=1;
    GeneralAdapter generalAdapter;

    public ViewNhapDonAc(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

    }

    public ViewNhapDonAc(Context context){
        super(context);

        generalAdapter = new GeneralAdapter(context,new ArrayList<>(),"field_name",R.layout.item_spnhapdon);
        simpleAdapter = new SuggestPNhapDonAdapter(context,null,R.layout.item_suggestpnhapdon);

//        lvSearch.setAdapter(simpleAdapter);
        lvSp.setAdapter(generalAdapter);

       edSdt.addTextChangedListener(new TextWatcherAdapter(edSdt,(view, text) -> {
           Mlog.D("change--"+text);
           queueSearchPhone++;
           if ((""+text).length()==0) return;

           if (text.length()>1&& text.charAt(0)==','){
               callsearchPhone(queueSearchPhone,text.substring(1),"id");
               return;
           }

           callsearchPhone(queueSearchPhone,text,"phone");

       }));

        edSearch.addTextChangedListener(new TextWatcherAdapter(edSearch,(view, text) -> {
            queuesearchp++;
            if (edSearch.getText().toString().isEmpty()){
                //showToast("trong roi");
                simpleAdapter.setData(null);
                return;
            }

            callsearch(queuesearchp,edSearch.getText().toString());
        }));


        taskC= new TaskNetCustomer();



        submit.setOnClickListener(view -> {

//            Intent dialogIntent = new Intent(context, ChiTietDonHangActivity.class);
//            dialogIntent.putExtra("order_id","1");
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(dialogIntent);
//            if (true) return;

            if (edUrl.getText().toString().isEmpty()&&vgUrl.getVisibility()==View.VISIBLE){
                showToast("Chưa nhập url nick fb khách");
                return;
            }
            String spLauQua="";
            JSONObject jo = new JSONObject();
            try {

            if (tvDaChonKH.getVisibility()==View.VISIBLE){
                String sTvDaChonKH=tvDaChonKH.getText().toString();
                String  phone= sTvDaChonKH.substring(sTvDaChonKH.indexOf("@")+1,sTvDaChonKH.length()-2);
                jo.put("phone",phone);

            }
            else if (isEmptyED(edTen)||isEmptyED(edDiachi)||isEmptyED(edSdt)){
                    showToast("Chưa nhập tên KH hoặc địa chỉ!");

                    return;
                }
            else {
                jo.put("phone",edSdt.getText().toString());
                jo.put("name",edTen.getText().toString());
                jo.put("address",edDiachi.getText().toString());
            }

            if (vgUrl.getVisibility()== View.VISIBLE)
            jo.put("nick_facebook",edUrl.getText());

            if (edNote.getVisibility()==View.VISIBLE&&isEmptyED(edNote)){
                showError("Chưa nhập ghi chú");

                return;
            }
            else {
                jo.put("note",edNote.getText());
            }
            if (!clickedRadioFreeship){
                    showError("Hãy chọn Freeship hoặc không!");
                    return;
                }

            jo.put("versioncode", SPRSupport.getString("version_code",context,"0"));
            jo.put("free_ship",radioFreeShip.isChecked()?1:0);


            jo.put("shop_id",tvShop.getText().toString().substring(0,tvShop.getText().toString().indexOf("-")));

            if (generalAdapter.getItemCount()==0){
                showError("Chưa chọn Sản Phẩm!");
                return;
            }



            JSONArray jaSP= new JSONArray();
                String spidCheck="#";
                for (int i = 0; i < generalAdapter.getItemCount(); i++) {
                    BaseObject ojSP=generalAdapter.getItem(i);
                    String pid=ojSP.get("id");
                    String mname=ojSP.get(ProductObj.name);
                    int curTime=(int) (Calendar.getInstance().getTimeInMillis()/1000);
                    long mPostTime= ojSP.getInt(ProductObj.post_at, curTime);
                    if (curTime-mPostTime>14*24*60*60){ //bài đăng lâu quá 14 ngày

                        String ngayqua= StringHelper.getTimeAgo(mPostTime*1000);
                        spLauQua+="\n"+ngayqua+" - "+StringHelper.joinIdProductName(pid,mname);
                    }


                    if (spidCheck.contains(pid)){ // trung san pham
                        showError("có sản phẩm được chọn 2 lần!");
                        return;
                    }
                    spidCheck+=pid+"#";
                    JSONObject joSp= new JSONObject();
                    joSp.put("quantity",ojSP.get("new_sl"));
                    joSp.put("gia_ban",ojSP.get("new_giaban").replaceAll("\\D+",""));
                    joSp.put("product_id",pid);
                    jaSP.put(joSp);
                }
            jo.put("sp",jaSP);
            String code= SPRSupport.getString("codenv",context,"nouser");
            jo.put("user",code);

            } catch (JSONException e) {
                e.printStackTrace();
                showDialog(true,"có lỗi "+e.getMessage(),null);
                return;
            }

            boolean isShowDl=false;

            String notSaiSdt="";
            if (jo.optString("phone").length()!=10) notSaiSdt="\n\n** SĐT HÌNH NHƯ KHÔNG ĐÚNG(10 số) "+jo.optString("phone");

//            if (spLauQua.length()>0){
//                isShowDl=true;
//                spLauQua="** CÓ SP ĐĂNG BÁN LÂU RỒI, LH MỸ"+spLauQua;
//            }
            spLauQua="";

            if (notSaiSdt.length()>0) {
                isShowDl=true;
                spLauQua+= notSaiSdt;
            }

             spLauQua+="\n\nVẪN TIẾP TỤC VÀO ĐƠN?";
            String finalSpLauQua = spLauQua;
            boolean finalIsShowDl = isShowDl;
            showDialog(isShowDl,spLauQua, isYes -> {
                closeDialog();
                if (finalIsShowDl)
                TaskNetGeneral.exTaskNotifyOnlyMe(finalSpLauQua,context);
                if (isYes){

                    showLoadingSubmit();
                    //TaskNetOrder.extaskNhapDon(jo.toString(),context,baseModel);12

                    extaskNhapDon(jo);
                }

            });

        });


    }

    private void extaskNhapDon(JSONObject param) {
        TaskNhapDonV2 taskNhapDonV2 = new TaskNhapDonV2(new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                closeLoadingSubmit();

                Intent dialogIntent = new Intent(context, ChiTietDonHangActivity.class);
                dialogIntent.putExtra("order_id",""+data);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);

                btnReset.performClick();
            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                closeLoadingSubmit();

                String msg1=""+msg;
                if (msg1.contains("#pex")){
                    showDialog(true,"Có sản phẩm nào đó đã đặt rồi, bạn muốn đặt thêm không?",isYes -> {
                        closeDialog();
                        if (isYes){
                            TaskNetGeneral.exTaskNotifyOnlyMe("Đặt thêm: "+param.toString(),context);
                            JSONObject param2 = param;
                            try {
                                param2.put("duplicate_insert",true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            extaskNhapDon(param2);
                        }
                    });

                }

                showToast(msg);
            }
        },TaskNhapDonV2.NHAPDON,context);

        TaskParams pr = new TaskParams();
        pr.setTaskParramDeFault(param.toString());
        taskNhapDonV2.exe(pr);
    }


//    {
//        "phone": "1145",
//            "address1": "74 Thợ nhuộm, Trần Hưng Đạo, Quận Hoàn Kiếm, Hà Nội",
//            "name": "nguyen anh que",
//            "note":"dsd",
//            "free_ship":1,
//            "shop_id":2,
//            "nick_facebook": "http://fb.com/acc",
//            "sp": [
//        {
//            "quantity": 1,
//                "tensp": "cha got chan",
//                "product_id": 22,
//                "gia_ban": 50000
//        }
//
//  ]
//    }

    private boolean isEmptyED(EditText edTen) {
        if (edTen.getText().toString().isEmpty()) return true;
        return false;
    }

    private void hideCustomerSuggest() {
        tvAddSuggest.setTag(false);
        tvPhoneSuggest.setTag(false);
        tvAddSuggest.setTextColor(Color.RED);
        tvPhoneSuggest.setTextColor(Color.RED);
        tvDaChonKH.setVisibility(View.GONE);
        tvDaChonKH.setText("");

        vgUrl.setVisibility(View.GONE);
        vgPhoneAddSuggest.setVisibility(View.GONE);
        tvPhoneSuggest.setText("");
        tvAddSuggest.setText("");

        tvShop.setText("Chọn shop ▼");
        tvShop.setTextColor(Color.parseColor("#FFEB3B"));

    }

    private void callsearch(int queuesearchp,String text) {
        new Handler().postDelayed(() -> {
            if (queuesearchp!=this.queuesearchp){
                //showToast("da khac roi: "+queuesearchp+" -- "+text);
                return;
            }
            TaskNetProduct.extaskSeachName(text,context,queuesearchp,baseModel);
        },1500);
    }

    private void callsearchPhone(int queuesearchPhone,String text,String field) {
        new Handler().postDelayed(() -> {
            if (queuesearchPhone!=this.queueSearchPhone){
                //showToast("da khac roi: "+queuesearchp+" -- "+text);
                return;
            }
            TaskNetCustomer.extaskSearch(text,field,context,queuesearchPhone,baseModel);
        },7500);
    }

    private boolean isValidate() {

        return true;
    }

    public View getView(){

        baseModel= new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);

                switch (taskType){
                    case TaskType.SEARCH_CUSTOMER_V2:
                        int queue_c= Integer.parseInt(msg);
                        if (queueSearchPhone!=queue_c) {
                            //Mlog.D("khong dung queue");
                            return;
                        }
                        hideCustomerSuggest();

                        JSONArray ja = (JSONArray) data;

                        if (ja==null||ja.length()!=1) {
                            hideCustomerSuggest();
                            edTen.setVisibility(View.VISIBLE);
                            vgAddress.setVisibility(View.VISIBLE);
                            return;
                        }

                        try {
                            JSONObject jo = ja.getJSONObject(0);
                            int customer_group= jo.optInt(CustomerObj.customer_group,-1);

                            tvAddSuggest.setText(jo.getString("address")+" ✓");
                            tvPhoneSuggest.setText(jo.getString("name")+"@"+jo.getString("phone")+" ✓");

                            String urlfb=jo.optString("nick_facebook","");
                            if (urlfb.equals("null")) urlfb="";
                            if (urlfb.length()==0){
                                vgUrl.setVisibility(View.VISIBLE);
                            }

                            if (customer_group==-1){ // kh hang nay bi chan
                                tvPhoneSuggest.setText("KH #BLOCK CHẶN :("+jo.optString("note","")+") "+tvPhoneSuggest.getText());
                            }

                            vgPhoneAddSuggest.setVisibility(View.VISIBLE);
                            edTen.setVisibility(View.GONE);
                            vgAddress.setVisibility(View.GONE);


                            //tu dong chon shop
//                            String suggestShop= jo.optString("buy_shop_id","ssasd").split(",")[0];
//                           if (getShopList()!=null){
//                               ArrayList<BaseObject> ojs1 = getShopList();
//                               for (int i = 0; i < ojs1.size(); i++) {
//                                   BaseObject oj1 = ojs1.get(i);
//                                   if (oj1.get("id","ko co id").equals(suggestShop))
//                                       listenSelectShop.onCallBackListerner(oj1,oj1.get("id_name"),null);
//                               }
//                           }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideCustomerSuggest();
                        }


                        break;

                    case TaskType.SEARCH_PRODUCT_V2:
                        int queue_s= Integer.parseInt(msg);
                        if (queuesearchp!=queue_s) {
                            //Mlog.D("khong dung queue");
                            return;
                        }
                        JSONArray jaProduct=null;

                        if (data instanceof JSONArray)
                        jaProduct= (JSONArray) data;
                        else Log.d("hehe","--"+data.toString());

                        try {
                            ArrayList<BaseObject> ojs = JsonSupport.jsonArray2BaseOj(jaProduct);
                            simpleAdapter = new SuggestPNhapDonAdapter(context,null,R.layout.item_suggestpnhapdon);
                            lvSearch.setAdapter(simpleAdapter);
                            simpleAdapter.setOnclickThem((oj, position) -> {

                                String giatri= oj.get("new_sl")+"x"+ MoneySupport.moneyEndK(oj.get("new_giaban").replaceAll("\\D+",""));
                                if (giatri.length()<11) giatri=giatri+".............".substring(0,11-giatri.length());
                                String name= giatri+" | "+oj.get(ProductObj.name);
                                oj.set("field_name",name);
                                generalAdapter.addData(oj);
                                simpleAdapter.setData(new ArrayList<BaseObject>());
                                edSearch.setText("");
                            });


                            simpleAdapter.setData(ojs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                        case TaskNetOrder.TASK_NHAP_DONV2:
                            closeLoadingSubmit();

                            Intent dialogIntent = new Intent(context, ChiTietDonHangActivity.class);
                            dialogIntent.putExtra("order_id",""+data);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(dialogIntent);

                            btnReset.performClick();

                            break;
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                showToast(msg);
                switch (taskType){
                    case TaskType.SEARCH_CUSTOMER_V2:
                        hideCustomerSuggest();
                    break;
                    case TaskNetOrder.TASK_NHAP_DONV2:
                        closeLoadingSubmit();
                        break;
                }
            }
        };


        return v;
    }

    public void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public void onCreateNew1(){

    }

}
