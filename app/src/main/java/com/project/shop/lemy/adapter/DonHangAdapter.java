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
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.donhang.ProcessDonHangActivity;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.Viewholder> {
    Context context;
    String status = "";
    ArrayList<BaseObject> list = new ArrayList<>();
    Listerner.setOnclickUpdate onclickUpdate;

    public DonHangAdapter(Context context) {
        this.context = context;

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_don_hang_fm_decrepted, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (list == null) return;
        MenuFromApiSupport.createTrangThaiDonHang(context, holder.tvTrangthai, true, false, key -> status = key);
        BaseObject baseObject = list.get(position);

        holder.tvMakh.setText(baseObject.get(OrderObj.id));
        holder.tvTong.setText(StringHelper.formatTien(baseObject.getInt(OrderObj.cod)));

        holder.tvSanpham.setText(baseObject.get(OrderObj.product_name));
        MenuFromApiSupport.setTextStatus(context, holder.tvTrangthai, baseObject.get(OrderObj.status), TaskType.TASK_LIST_STATUS);

        holder.tvTrangthai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuFromApiSupport.createTrangThaiDonHang(context, holder.tvTrangthai, false, true, key -> {
                    status = key;
                    int stt = Integer.parseInt(status);
                    if (onclickUpdate == null) return;
                    if (stt == 0) return;
                    if (status.equals(getSelectedIndex(baseObject))) return;

                });
            }
        });

        holder.imgMenu.setOnClickListener(view -> showMenu(holder.imgMenu, baseObject));

        holder.itemView.setOnClickListener(view -> {
            ChiTietDonHangActivity.startActivity(context,baseObject.get(OrderObj.id));
        });
    }

    public int getSelectedIndex(BaseObject baseObject) {
        switch (baseObject.getInt(OrderObj.status)) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvMakh;
        private TextView tvTong;
        private TextView tvTrangthai;
        private TextView tvSanpham;
        private ImageView imgMenu;

        public Viewholder(View v) {
            super(v);
            tvMakh = v.findViewById(R.id.tvMakh);
            tvTong = v.findViewById(R.id.tvTong);
            tvTrangthai = v.findViewById(R.id.tvTrangthai);
            tvSanpham = v.findViewById(R.id.tvSanpham);
            imgMenu = v.findViewById(R.id.imgMenu);
        }
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

    public ArrayList<BaseObject> getAll() {
        return list;
    }

    public void setOnclickUpdate(Listerner.setOnclickUpdate onclickUpdate) {
        this.onclickUpdate = onclickUpdate;
    }

    private void showMenu(View v, BaseObject object) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.popup_item_dh, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_Sms:
                    ProcessDonHangActivity.sendSms(context, object);
                    break;
                case R.id.action_In:
                    ProcessDonHangActivity.inDonHang(context, object);
                    break;
                case R.id.action_pushvc:
                    ProcessDonHangActivity.pushDonHangHvc(context, object,null);
                    break;
                case R.id.action_note:
                    break;
            }
            return false;
        });
        popup.show();
    }

}