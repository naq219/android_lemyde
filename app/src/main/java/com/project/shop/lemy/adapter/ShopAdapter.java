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
import com.project.shop.lemy.bean.ShopObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.Viewholder> {
    Context context;
    ArrayList<BaseObject> listShop = new ArrayList<>();
    Listerner.setOnclickItem onClickDelete;
    Listerner.OnclickEdit onclickEdit;

    public ShopAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quan_ly, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(ShopAdapter.Viewholder holder, int position) {
        if (listShop == null) return;
        BaseObject object = listShop.get(position);
//        holder.tvSttitem.setText(object.get(ShopObj.id));
        holder.tvTenitemql.setText(object.get(ShopObj.name));
        holder.tvFbitemql.setText(object.get(ShopObj.facebook));

        holder.imgMenuShop.setOnClickListener(view -> showMenu(holder.imgMenuShop, object, position));
    }

    @Override
    public int getItemCount() {
        return listShop.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvTenitemql;
        private TextView tvFbitemql;
        private ImageView imgMenuShop;

        public Viewholder(View v) {
            super(v);
            tvTenitemql = v.findViewById(R.id.tvTenitemql);
            tvFbitemql = v.findViewById(R.id.tvFbitemql);
            imgMenuShop = v.findViewById(R.id.imgMenuShop);
        }
    }

    public void setData(ArrayList<BaseObject> list) {
        this.listShop.clear();
        this.listShop = list;
        notifyDataSetChanged();
    }

    private void showMenu(View v, BaseObject object, int position) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.popup_shop, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    onclickEdit.clickItem(object, position);
                    break;
                case R.id.delete:
                    onClickDelete.clickItem(object, position);
                    break;
            }
            return false;
        });
        popup.show();
    }



    public void setClickDelete(Listerner.setOnclickItem onClickDelete) {
        this.onClickDelete = onClickDelete;
    }

    public void onclickEdit(Listerner.OnclickEdit onclickEdit) {
        this.onclickEdit = onclickEdit;
    }
}
