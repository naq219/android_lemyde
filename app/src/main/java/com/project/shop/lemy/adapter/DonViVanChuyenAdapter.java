package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.DvvcObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

/**
 * Created by Ducqv on 9/15/2018.
 */

public class DonViVanChuyenAdapter extends RecyclerView.Adapter<DonViVanChuyenAdapter.ViewHolder> {
    Context context;
    ArrayList<BaseObject> listDvvc = new ArrayList<>();
    Listerner.OnclickEdit onClickEdit;
    Listerner.OnDeleteItemClick onDeleteItemClick;

    public DonViVanChuyenAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dvvc, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listDvvc == null) return;
        BaseObject objListDvvc = listDvvc.get(position);
        holder.tvIdDvvc.setText(objListDvvc.get(DvvcObj.id));
        holder.tvNameDvvc.setText(objListDvvc.get(DvvcObj.name));
        holder.btnMenu.setOnClickListener(view -> showMenu(holder.tvNameDvvc, objListDvvc, position));
    }

    @Override
    public int getItemCount() {
        return listDvvc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIdDvvc;
        private TextView tvNameDvvc;
        private ImageView btnMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            tvIdDvvc = itemView.findViewById(R.id.tvIdDvvc);
            tvNameDvvc = itemView.findViewById(R.id.tvNameDvvc);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }

    public void setData(ArrayList<BaseObject> list) {
        this.listDvvc.clear();
        this.listDvvc = list;
        notifyDataSetChanged();
    }

    private void showMenu(View v, BaseObject object, int position) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.popup_shop, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    onClickEdit.clickItem(object, position);
                    break;
                case R.id.delete:
                    onDeleteItemClick.clickDeleteItem(object, position);
                    break;
            }
            return false;
        });
        popup.show();
    }

    public void setOnclickEdit(Listerner.OnclickEdit onClickEdit) {
        this.onClickEdit = onClickEdit;
    }

    public void setOnDeleteItemClick(Listerner.OnDeleteItemClick onDeleteItemClick) {
        this.onDeleteItemClick = onDeleteItemClick;
    }
}
