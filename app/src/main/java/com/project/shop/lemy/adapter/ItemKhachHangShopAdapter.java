package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class ItemKhachHangShopAdapter extends RecyclerView.Adapter<ItemKhachHangShopAdapter.ViewHolder> {
    Context context;
    ArrayList<BaseObject> listKhachHang = new ArrayList<>();
    Listerner.OnItemCheckboxClick checkboxClick;
    Listerner.OnAllItemCheckedListerner onAllItemCheckedListerner;

    public ItemKhachHangShopAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<BaseObject> listKH() {
        return listKhachHang;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kh_shop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseObject object = listKhachHang.get(position);
        holder.tvTenkh.setText(object.get(CustomerObj.name));
        holder.tvSdt.setText(object.get(CustomerObj.phone));
        holder.cbItemKh.setChecked(object.getBool("checked",false));
    }

    @Override
    public int getItemCount() {
        return listKhachHang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTenkh;
        private TextView tvSdt;
        private CheckBox cbItemKh;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTenkh = itemView.findViewById(R.id.tvTenkh);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            cbItemKh = itemView.findViewById(R.id.cbItemKh);
            cbItemKh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseObject item= listKhachHang.get(getAdapterPosition());
                    item.set("checked",!item.getBool("checked",false));
                    notifyItemChanged(getAdapterPosition());
                    checkAllItemChecked();
                }
            });
        }
    }

    public void checkAllItemChecked(){
        for (int i = 0; i < listKhachHang.size(); i++) {
            if (!listKhachHang.get(i).getBool("checked",false)){
                onAllItemCheckedListerner.onAllChecked(false);
                return;
            }
        }
        onAllItemCheckedListerner.onAllChecked(true);
    }

    public void setData(ArrayList<BaseObject> listKH) {
        this.listKhachHang.clear();
        listKhachHang.addAll(listKH);
        notifyDataSetChanged();
    }

    public void setAllChecked(boolean checked) {
        for (int i = 0; i < listKhachHang.size(); i++) {
            listKhachHang.get(i).set("checked",checked);
        }
        notifyDataSetChanged();
    }

    public void setOnAllItemCheckedListerner (Listerner.OnAllItemCheckedListerner onAllItemCheckedListerner){
        this.onAllItemCheckedListerner=onAllItemCheckedListerner;
    }

}
