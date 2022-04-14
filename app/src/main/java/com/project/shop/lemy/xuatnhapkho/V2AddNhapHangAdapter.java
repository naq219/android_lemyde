package com.project.shop.lemy.xuatnhapkho;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thao on 10/9/2018.
 */

public class V2AddNhapHangAdapter extends RecyclerView.Adapter<V2AddNhapHangAdapter.ViewHolder> {
    List<BaseObject> list = new ArrayList<>();
    List<BaseObject> ojsKhoang = null;
    Context context;
    String khoang, type;

    public V2AddNhapHangAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    public void setKhoang(List<BaseObject> ojsKhoang){
        this.ojsKhoang = ojsKhoang;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_nhaphangv2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list == null) return;
        BaseObject oj = list.get(position);

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
        holder.tongSl.setText("Tổng SL: "+sl);

        holder.tvNameSp.setText(oj.get(ProductObj.name));

        holder.lnItemSp.setOnClickListener( view -> {
            //todo show dialog sua khoang


            new DialogSupport().dialogXNKChonKhoangSL(ojsKhoang, oj,context, object -> {
                     list.set(position, (BaseObject) object);
                     notifyDataSetChanged();
                 });
        });

        holder.imgxoa.setOnClickListener(view -> {

            DialogSupport.dialogYesNo("Xoá sp: "+oj.get(ProductObj.name),context,isYes -> {

                if (!isYes) return;
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                notifyDataSetChanged();

            });


        });

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSp,tongSl;
        private View lnItemSp;
        private View imgxoa;
        private ViewGroup parentKhoang;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameSp = itemView.findViewById(R.id.tvNameSp);
            tongSl = itemView.findViewById(R.id.tongSl12);
            imgxoa = itemView.findViewById(R.id.imgxoa);
            parentKhoang = itemView.findViewById(R.id.parentKhoang);

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
}
