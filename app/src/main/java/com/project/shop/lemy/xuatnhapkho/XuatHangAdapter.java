package com.project.shop.lemy.xuatnhapkho;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.telpoo.frame.object.BaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Thao on 10/9/2018.
 */

public class XuatHangAdapter extends RecyclerView.Adapter<XuatHangAdapter.ViewHolder> {
    List<BaseObject> list = new ArrayList<>();

    Context context;
    String khoang;
    private String typeXuat="xuatthuong"; //nếu text là "xuatdh" thì là của đơn hàng nào

    String eanCheck="#";

    public XuatHangAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_xuatv2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list == null) return;
        BaseObject oj = list.get(position);
        int slXuat=0;
        slXuat= oj.getInt("quantity",1000);

        List<BaseObject> ojKhoangChon = NhapKhoSupport.getDataKhoangFromOj(oj);
        holder.parentKhoang.removeAllViews();
        int sl=0;
        for (int i = 0; i < ojKhoangChon.size(); i++) {

            View vt1= LayoutInflater.from(context).inflate(R.layout.itemtt01,null,false);
            if (i==ojKhoangChon.size()-1) vt1.findViewById(R.id.tmp19).setVisibility(View.INVISIBLE);
            TextView tvKhoangtt1= vt1.findViewById(R.id.tvKhoang);
            TextView tvSltt1= vt1.findViewById(R.id.edSl);
            tvKhoangtt1.setText("KHOANG: "+ojKhoangChon.get(i).get("khoang_id"));
            tvSltt1.setText("SL: "+ojKhoangChon.get(i).get("sl"));
            holder.parentKhoang.addView(vt1);

            try {
                sl =sl+Integer.parseInt(ojKhoangChon.get(i).get("sl"));
            } catch (Exception e){};
        }
        String tmp1="SL cần xuất: "+slXuat+"\nSL đã lấy:"+sl;
        if (slXuat>sl){
            holder.tongSl.setTextColor(Color.RED);
            //tmp1=tmp1+" (Chưa lấy đủ)";
        }
        holder.tongSl.setText(tmp1);
        String shortEan=""+oj.get("ean"); if (shortEan.contains("null")) shortEan="";

        if (!eanCheck.contains("#"+shortEan+"#"))holder.tvEan.setTextColor(Color.RED);
        else holder.tvEan.setTextColor(Color.parseColor("#4c5097"));

        if (shortEan.length()>4){
            shortEan=shortEan.substring(shortEan.length()-4);
            holder.tvEan.setText("("+shortEan+")");

        }
        else holder.tvEan.setText("");

        String infoKeepStock="";
        if(oj.getInt("keepstock_time",0)>0)
            infoKeepStock="(lưu kho "+ oj.getInt("keepstock_sl",0)+")";
        holder.tvNameSp.setText(oj.get("product_name")+infoKeepStock+"-"+shortEan);

        holder.lnItemSp.setOnClickListener( view -> {
            //todo show dialog sua khoang

            //click row xuat kho



//            new DialogSupport().dialogXNKChonKhoangSL(ojsKhoang, oj,context, object -> {
//                     list.set(position, (BaseObject) object);
//                     notifyDataSetChanged();
//                 });
        });

        holder.imgxoa.setOnClickListener(view -> {

            DialogSupport.dialogYesNo("Xoá sp: "+oj.get(ProductObj.name),context,isYes -> {
               if(isYes){
                   list.remove(position);
                   notifyItemRemoved(position);
                   notifyItemRangeChanged(position, list.size());
                   notifyDataSetChanged();
               }
            });


        });


        if (typeXuat.contains("xuatdh")){
            holder.imgxoa.setVisibility(View.GONE);
        }

    }

    public BaseObject getItem(int index) {
        return list.get(index);
    }

    public boolean checkTrungId(String id) {
        for (int i = 0; i < list.size(); i++) {
            if (getItem(i).get(ProductObj.id, "").equals(id)) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setIsXuatChoDH() {
        typeXuat="xuatdh";
    }

    public void addEanCheck(String eanCheck) {
        this.eanCheck=this.eanCheck+"#"+eanCheck+"#";
        notifyDataSetChanged();
        Toast.makeText(context,"da them ean "+eanCheck,Toast.LENGTH_LONG).show();
    }

    public void clearEanCheck() {
        this.eanCheck="#";
        notifyDataSetChanged();
    }

    /**
     * check đã quét ean từ sp đúng hết chưa
     * @return
     */
    public String validateEanCheck(){
        String ok="ok";
       if (list==null||eanCheck==null||list.size()==0) return "Có lỗi";

        for (int i = 0; i < list.size(); i++) {
            BaseObject oj = list.get(i);
            String ean=""+oj.get("ean"); if (ean.contains("null")) ean="";
            if (ean.length()<6) continue;
            String[] rEan = ean.split(",");
            boolean isOk=false;
            for (int j = 0; j < rEan.length; j++) {
                if (eanCheck.contains("#"+rEan[j]+"#")) isOk=true;
            }
            if (!isOk)
            return ""+oj.get("product_name");
        }

        return ok;
    }

    public void addEanCheckShort(String eanText) {
        for (int i = 0; i < list.size(); i++) {
            BaseObject oj = list.get(i);
            String ean=""+oj.get("ean"); if (ean.contains("null")) ean="";
            if (ean.contains(eanText)){
                addEanCheck(ean);
                return;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSp,tongSl,tvEan;
        private View lnItemSp;
        private View imgxoa;
        private ViewGroup parentKhoang;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameSp = itemView.findViewById(R.id.tvNameSp);
            tongSl = itemView.findViewById(R.id.tongSl12);
            imgxoa = itemView.findViewById(R.id.imgxoa);
            parentKhoang = itemView.findViewById(R.id.parentKhoang);
            tvEan=itemView.findViewById(R.id.tvEan);
            lnItemSp= itemView.findViewById(R.id.lnItemSp);
        }
    }

    public void addData(BaseObject baseObject) {

        list.add(0,baseObject);
        notifyDataSetChanged();
    }



    public void resetData(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setData(List<BaseObject> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public List<BaseObject> getProduct() {
        return list;
    }

    public void tuDongNhatHang(){
        for (BaseObject oj :list) {
            int slXuat= oj.getInt("quantity");

            ArrayList<BaseObject> ojsTon2 = NhapKhoSupport.getTonKhoXuatHangAsList(oj);
            if (ojsTon2.size()==0) continue;

            ArrayList<BaseObject> ojsTon1 = new ArrayList<>();
            for (int i = 0; i < ojsTon2.size(); i++) { // đưa khoang keep_* lên đầu list
                BaseObject ojTon2 = ojsTon2.get(i);

                if(ojTon2.get("khoang_id").contains("keep"))
                    ojsTon1.add(0,ojTon2);
                else ojsTon1.add(ojTon2);
            }

            if(oj.getInt("keepstock_time",0)==0|| oj.getInt("keepstock_sl",0)==0)
             Collections.reverse(ojsTon1);

//            Collections.sort(ojsTon,(oj1, oj2) -> {
//               // return oj1.getInt("sl") > oj2.getInt("sl") ? -1 : (oj1.getInt("sl") < oj2.getInt("sl")) ? 1 : 0;
//
////                if(oj.getInt("keepstock_time",0)>0&& oj.getInt("keepstock_sl",0)>0){
////                    if(oj1.get("khoang_id").contains("keep")) return -1;
////                    else return 0;
////                }
////                else if(oj1.get("khoang_id").contains("keep")) return 1;
////                return 0;
//
//
//
//            });

                ArrayList<BaseObject> ojsTon = ojsTon1;
            JSONArray ja1 = new JSONArray();

            for (int i = 0; i < ojsTon.size(); i++) {
                if(slXuat==0) break;

                try {
                    int sl1= ojsTon.get(i).getInt("sl");
                    if (sl1<=0) break;

                    int slLay1= sl1; if (slLay1>slXuat) slLay1=slXuat;


                    JSONObject ojT1= new JSONObject();
                    ojT1.put("khoang_id",ojsTon.get(i).get("khoang_id"));
                    ojT1.put("sl",slLay1);

                    ja1.put(ojT1);
                    slXuat=slXuat-slLay1;

                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }

            }

            if (slXuat==0){ //da lay du sl can thiet
                oj.set("dataKhoangSL",ja1.toString());
            }




        }

        notifyDataSetChanged();

    }

//    public boolean isDaCheckEan(){
//        for (BaseObject oj: list){
//            String ean=""+oj.get("ean"); if (ean.contains("null")) ean="";
//            if (ean.length()>4&&!eanCheck.contains(ean)) return false;
//        }
//        return true;
//    }

}
