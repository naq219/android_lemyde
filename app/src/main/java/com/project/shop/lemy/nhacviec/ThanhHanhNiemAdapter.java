package com.project.shop.lemy.nhacviec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.adapter.MyAdapter;
import com.project.shop.lemy.donhang.DonHangFmv2;
import com.telpoo.frame.utils.DialogSupport;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * danh sach don hang {@link DonHangFmv2}
 */
public class ThanhHanhNiemAdapter extends RecyclerView.Adapter<ThanhHanhNiemAdapter.Viewholder> {

    Context context;
    JSONArray ja =new JSONArray();
    long lastClickSave=0;
    public ThanhHanhNiemAdapter(Context context) {
        this.context = context;
        loadDataSpr();

    }

    private void loadDataSpr() {
          String data1 = SPRSupport.getString("thanhanhniem",context);

        if (data1!=null && data1.length()>10){
            try {
                ja= new JSONArray(data1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (ja.length()==0){
            //[ { "enable":1,"min":5000, "max":10000,"data":"Đang làm gì ?" }, { "enable":0,"min":5000, "max":10000,"data":"Quan sát thân" } ]
            try {
                ja = new JSONArray("[ { \"enable\":0,\"min\":5000, \"max\":10000,\"content\":\"Đang làm gì ?\" } ]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thanhanhniem, parent, false);
        return new Viewholder(view);
    }



    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (ja.length()==0) return;
        JSONObject jo = ja.optJSONObject(position);
        holder.edMin.setText(jo.optString("min"));
        holder.edMax.setText(jo.optString("max"));
        holder.edContent.setText(jo.optString("content"));
        holder.checkbox.setChecked(jo.optInt("enable",0)==1?true:false);
        holder.tvEnable.setText(jo.optString("enable"));

        holder.tvContent.setText(jo.optString("content"));
        holder.tvContent.setVisibility(View.VISIBLE);
        holder.edContent.setVisibility(View.GONE);
        holder.tvContent.setOnClickListener(view -> {
           boolean edContentShow = holder.edContent.getVisibility()==View.VISIBLE;
            if(!edContentShow){
                holder.edContent.setVisibility(View.VISIBLE);
                holder.tvContent.setVisibility(View.GONE);
            }
        });



        holder.btnSave.setOnClickListener(view -> {
          if (TimeUtils.getTimeMillis()-lastClickSave<200&&lastClickSave>0)okSave(jo,holder,position);
          else lastClickSave=TimeUtils.getTimeMillis();

       });

        holder.btnSave.setOnLongClickListener(view -> {
            JSONObject jo01=new JSONObject();
            try {
                jo01.put("min","60p");
                jo01.put("max","90p");
                jo01.put("enable",0);
                jo01.put("content","content");

                ja.put(jo01);
                save2Spr();
                loadDataSpr();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        });

    }

    public void saveAll(){
        save2Spr();
        loadDataSpr();
        showToast("Lưu thành công1!");
    }

    private void okSave(JSONObject jo, Viewholder holder, int position) {
        try {
            jo.put("min",holder.edMin.getText().toString());
            jo.put("max",holder.edMax.getText().toString());
            jo.put("enable",holder.checkbox.isChecked()?1:0);
            jo.put("content",holder.edContent.getText().toString());

            if (holder.edContent.getText().toString().isEmpty()){
                ja.remove(position);
            }
            if (holder.edContent.getText().toString().contains("gggg"))ja.remove(position);
            save2Spr();
            loadDataSpr();
            showToast("Lưu thành công!");

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Lỗi "+e.getMessage());

        }
//        DialogSupport.simpleYesNo(context,"Yes","No","OK SAVE?","",(value, where) -> {
//            if (where==1){
//
//
//
//            }
//        });
    }

    private void showToast(String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    private void save2Spr() {
        SPRSupport.save("thanhanhniem",ja.toString(),context);
       ThanHanhNiem.reloadDataThn=true;
    }


    @Override
    public int getItemCount() {
        return ja.length();
    }

    public void start() {
        loadDataSpr();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.optJSONObject(i);
            if (jo.optInt("enable")==-1) {
                try {
                    jo.put("enable",1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        save2Spr();
        loadDataSpr();
    }

    public void stop() {
        loadDataSpr();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.optJSONObject(i);
            if (jo.optInt("enable")==1) {
                try {
                    jo.put("enable",-1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        save2Spr();
        loadDataSpr();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        private EditText edMin,edMax,edContent;
        CheckBox checkbox;
        View btnSave;
        TextView tvEnable,tvContent;
        public Viewholder(View v) {
            super(v);
            edMin = v.findViewById(R.id.edMin);
            edMax = v.findViewById(R.id.edMax);
            edContent = v.findViewById(R.id.edContent);
            btnSave = v.findViewById(R.id.btnSave);
            checkbox = v.findViewById(R.id.checkbox);
            tvEnable= v.findViewById(R.id.tvEnable);
            tvContent= v.findViewById(R.id.tvContent);
        }
    }

    public void setData(JSONArray ja) {
        this.ja= ja;
        notifyDataSetChanged();

    }






}