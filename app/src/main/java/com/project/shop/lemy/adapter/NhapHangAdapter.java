package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.helper.StringHelper;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

/**
 * Created by Thao on 10/9/2018.
 */

public class NhapHangAdapter extends RecyclerView.Adapter<NhapHangAdapter.Viewholder> {
    Context context;
    ArrayList<BaseObject> list = new ArrayList<>();

    public NhapHangAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ds_nhap_hang_fm, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (list == null) return;
        BaseObject baseObject = list.get(position);
        holder.tvKhoNhapHang.setText(baseObject.get(ProductObj.kho_name));
        MenuFromApiSupport.setTextStatus(context, holder.tvKieuNhap, baseObject.get(ProductObj.type), TaskType.TASK_LIST_KIEUNHAP);
        holder.tvIdDonHang.setText(StringHelper.formatID(baseObject.get(ProductObj.order_id, "")));
        holder.tvSanpham.setText(baseObject.get(ProductObj.name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvKhoNhapHang;
        private TextView tvKieuNhap;
        private TextView tvIdDonHang;
        private TextView tvSanpham;

        public Viewholder(View itemView) {
            super(itemView);
            tvKhoNhapHang = itemView.findViewById(R.id.tvKhoNhapHang);
            tvKieuNhap = itemView.findViewById(R.id.tvKieuNhap);
            tvIdDonHang = itemView.findViewById(R.id.tvIdDonHang);
            tvSanpham = itemView.findViewById(R.id.tvSanpham);
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

}
