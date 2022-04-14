package com.project.shop.lemy.nhapdon;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNetGeneral;
import com.project.shop.lemy.common.PoupMenuSupport;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ViewNhapDonAcParent extends View{
    EditText edSdt,edTen,edDiachi,edUrl,edSearch,edNote;
    View v,vgPhoneAddSuggest,vgUrl;
    CheckBox radioFreeShip;
    TextView tvPhoneSuggest,tvAddSuggest;
    TextView tvDaChonKH;
    protected Context context;
    RecyclerView lvSearch;
    RecyclerView lvSp;
    View submit,vgAddress,vgOpen,vgSuccess,vgLoading;
    Button btnReset;
    WebView wv;

    TextView dl_title;
    View dl_yes,dl_no,vg_dl;

    TextView tvShop;
    ArrayList<BaseObject> ojsShop=null;
    Boolean clickedRadioFreeship=false;

    protected Listerner.callBack listenSelectShop= new Listerner.callBack() {
        @Override
        public void onCallBackListerner(Object oj1, Object oj2, Object oj3) {

            tvShop.postDelayed(() -> {
                String key= (String) oj2;

                if (key.length()>13) key=key.substring(0,13)+"..";
                tvShop.setText(key);
            },200);

            tvShop.setTextColor(Color.GREEN);

        }


    };
    public ViewNhapDonAcParent(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public ViewNhapDonAcParent(Context context){
        super(context);

        v= LayoutInflater.from(context).inflate(R.layout.layout_floating_widget, null);
        this.context = context;
        dl_title= v.findViewById(R.id.dl_title);
        dl_yes= v.findViewById(R.id.dl_yes);
        dl_no= v.findViewById(R.id.dl_no);
        vg_dl= v.findViewById(R.id.vg_dl);

        tvShop= v.findViewById(R.id.tvShop);
        edSdt = v.findViewById(R.id.edSdt);
        edTen = v.findViewById(R.id.edTen);
        edDiachi = v.findViewById(R.id.edDiachi);
        edUrl = v.findViewById(R.id.edUrl);
        edSearch= v.findViewById(R.id.edSearch);
        lvSearch= v.findViewById(R.id.lvSearch);
        vgPhoneAddSuggest=v.findViewById(R.id.vgPhoneAddSuggest);
        tvPhoneSuggest= v.findViewById(R.id.tvPhoneSuggest);
        tvAddSuggest= v.findViewById(R.id.tvAddSuggest);
        submit= v.findViewById(R.id.submit);
        tvDaChonKH=v.findViewById(R.id.tvDaChonKH);
        wv=v.findViewById(R.id.wv);
        vgSuccess=v.findViewById(R.id.vgSuccess);
        btnReset=v.findViewById(R.id.btnReset);
        edNote=v.findViewById(R.id.edNote);
        vgLoading=v.findViewById(R.id.vgLoading);
        vgAddress= v.findViewById(R.id.vgAddress);
        v.findViewById(R.id.tvShowNote).setOnClickListener(view -> {
            edNote.setVisibility(edNote.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
        });
        vgOpen=v.findViewById(R.id.vgOpen);
        lvSearch.setLayoutManager(new LinearLayoutManager(context));
        radioFreeShip= v.findViewById(R.id.radioFreeship);
        lvSp= v.findViewById(R.id.lvSp);
        lvSp.setLayoutManager(new LinearLayoutManager(context));

        vgUrl= v.findViewById(R.id.vgUrl);

        tvShop.setOnClickListener(view -> {
            loadShop(true);
        });

        loadShop(false);

        radioFreeShip.setOnClickListener(view -> {
            clickedRadioFreeship=true;
            radioFreeShip.setTextColor(Color.GREEN);
        });

        View.OnClickListener clickSS= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv= (TextView) view;
                if (tv.getText().toString().toLowerCase().contains("#block")){ // kh bi chan
                    showError("Khách hàng này bị chặn, liên hệ admin");
                    return;
                }
                tv.setTag(true);
                tv.setTextColor(Color.GREEN);
                if (((boolean)tvPhoneSuggest.getTag())==true&&((boolean)tvAddSuggest.getTag())==true){
                    tvDaChonKH.setText("KH: "+tvPhoneSuggest.getText());
                    hideViewWhenSelectedCustomer();


                }
            }
        };

        tvAddSuggest.setOnClickListener(clickSS);
        tvPhoneSuggest.setOnClickListener(clickSS);
        tvDaChonKH.setOnClickListener(view -> {
            edSdt.setVisibility(View.VISIBLE);
        });

        v.findViewById(R.id.viewPaste).setOnClickListener(view -> {
            pasteAddress();

        });

        v.findViewById(R.id.viewPasteUrl).setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData==null) {
                showError("Chưa sao chép");
                return;
            }
            int itemCount = clipData.getItemCount();
            String ss="";
            if(itemCount > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                String text = item.getText().toString();
                ss+=text;
            }
            if (ss.length()==0) {
                showError("chưa sao chép");
                return;
            }

            edUrl.setText(ss);

        });

        btnReset.setOnClickListener(view -> {

        });



    }

    protected ArrayList<BaseObject> getShopList(){
        return ojsShop;
    }

    protected void showError(String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        //DialogSupport.dialogThongBao(s,context,null);
    }



    private void pasteAddress() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData==null) return;

        int itemCount = clipData.getItemCount();
        String ss="";
        if(itemCount > 0) {
            ClipData.Item item = clipData.getItemAt(0);
            String text = item.getText().toString();
            ss+=text;
        }
        if (ss.length()==0) return;

        Pattern pattern = Pattern.compile("0[0-9\\s.-]{9,13}");
        Matcher matcher = pattern.matcher(ss);

        while (matcher.find()) {
            //edSdt.setText( matcher.group(1));
            //Mlog.D();
           String p= matcher.group().replaceAll("[^\\d]", "");
            if (p.length()==10)edSdt.setText(p);
        }
        edDiachi.setText(ss);

    }

    private void hideViewWhenSelectedCustomer() {
        edSdt.setVisibility(View.GONE);
        tvDaChonKH.setVisibility(View.VISIBLE);
        tvPhoneSuggest.postDelayed(() -> {
            vgPhoneAddSuggest.setVisibility(View.GONE);
        },500);

    }


    private void loadShop(boolean isShow){
        if (ojsShop!=null&&isShow){
            PoupMenuSupport.showMenuFromOj(context,tvShop,ojsShop,"id_name",listenSelectShop);
            return;
        }
        TaskNetGeneral.extaskOtherData(context,new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                if (data instanceof JSONObject){

                    try {
                        JSONObject jo = (JSONObject) data;
                        JSONArray ja = jo.getJSONArray("shop");
                        JSONObject joShop_nv=jo.getJSONObject("shop_nv");
                        String listShopnv= joShop_nv.getString(SPRSupport.getString("codenv2",context));
                        ojsShop= new ArrayList<>();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject oo = ja.getJSONObject(i);
                            BaseObject ojitem =JsonSupport.jsonObject2BaseOj(oo);
                            if (!listShopnv.contains("-"+oo.get("id")+"-"))continue;
                            ojitem.set("id_name",oo.get("id")+"- "+oo.getString("name").replace("Shop ",""));
                            ojsShop.add(ojitem);
                        }

                        if (isShow)
                        PoupMenuSupport.showMenuFromOj(context,tvShop,ojsShop,"id_name",listenSelectShop);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void showLoadingSubmit() {
        vgLoading.setVisibility(View.VISIBLE);
        submit.setClickable(false);
    }

    protected void closeLoadingSubmit(){
        submit.setClickable(true);
        vgLoading.setVisibility(View.GONE);
    }

    protected void showDialog(boolean isShowDl,String title, Listerner.YesNoDialog yesNoDialog){
        if (!isShowDl){
            yesNoDialog.onClickYesNo(true);
            return;
        }
        vg_dl.setVisibility(View.VISIBLE);
        dl_title.setText(title);
        dl_yes.setOnClickListener(view -> {
            yesNoDialog.onClickYesNo(true);
        });
        dl_no.setOnClickListener(view -> {
            yesNoDialog.onClickYesNo(false);
        });
    }
    protected void closeDialog(){
        vg_dl.setVisibility(View.GONE);
    }




}

