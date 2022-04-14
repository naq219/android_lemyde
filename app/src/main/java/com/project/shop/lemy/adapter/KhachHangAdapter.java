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
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.Viewholder> {
    Context context;
    ArrayList<BaseObject> list = new ArrayList<>();
    Listerner.setOnclickItem onClickDelete;
    Listerner.OnclickEdit onclickEdit;
    String tenKh, diaChi;

    public KhachHangAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khachhang, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (list == null) return;
        BaseObject baseObject = list.get(position);
        tenKh = baseObject.get(CustomerObj.name);
        String inHoaKh = tenKh.substring(0,1).toUpperCase() + tenKh.substring(1);
        holder.tvTendskh.setText(inHoaKh);
        holder.tvDsdienthoai.setText(baseObject.get(CustomerObj.phone));
        diaChi = baseObject.get(CustomerObj.address);
        String inHoaDc = diaChi.substring(0,1).toUpperCase() + diaChi.substring(1);
        holder.tvDiachi.setText(inHoaDc);

        holder.imgMenukh.setOnClickListener(view -> showMenu(holder.imgMenukh, baseObject, position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvDiachi;
        private TextView tvTendskh;
        private TextView tvDsdienthoai;
        private ImageView imgMenukh;

        public Viewholder(View view) {
            super(view);
            tvDiachi = view.findViewById(R.id.tvDiachi);
            tvTendskh = view.findViewById(R.id.tvTendskh);
            tvDsdienthoai = view.findViewById(R.id.tvDsdienthoai);
            imgMenukh = view.findViewById(R.id.imgMenukh);
        }

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

    public void setData(ArrayList<BaseObject> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();

    }

    public void addData(ArrayList<BaseObject> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}