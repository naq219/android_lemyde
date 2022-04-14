package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

/**
 * Created by Thao on 7/6/2018.
 */

public class OrderProfuctAdapter extends RecyclerView.Adapter<OrderProfuctAdapter.Viewholder> {
    Context context;
    ArrayList<BaseObject> list = new ArrayList<>();

    public OrderProfuctAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_profuct, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (list == null) return;
        BaseObject baseObject = list.get(position);
        holder.tvTenSpOrder.setText(baseObject.get(ProductObj.name));
        MenuFromApiSupport.setTextStatus(context, holder.tvTrangThaiOrder, baseObject.get(OrderObj.status), TaskType.TASK_LIST_STATUS);
        holder.tvTongOrder.setText(baseObject.get(OrderObj.count));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tvTenSpOrder;
        TextView tvTrangThaiOrder;
        TextView tvTongOrder;

        public Viewholder(View itemView) {
            super(itemView);
            tvTenSpOrder = itemView.findViewById(R.id.tvTenSpOrder);
            tvTrangThaiOrder = itemView.findViewById(R.id.tvTrangThaiOrder);
            tvTongOrder = itemView.findViewById(R.id.tvTongOrder);
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