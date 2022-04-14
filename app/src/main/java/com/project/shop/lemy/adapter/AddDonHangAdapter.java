package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;


import java.util.ArrayList;

import static com.project.shop.lemy.bean.ProductObj.quantity_product;

/**
 * Created by Ducqv on 7/16/2018.
 */

public class AddDonHangAdapter extends RecyclerView.Adapter<AddDonHangAdapter.ViewHolder> {
    ArrayList<BaseObject> listOrder = new ArrayList<>();
    Context context;
    Listerner.onClickTotalAmount onClickTotalAmount;
    Listerner.OnListItemChangeList onClickDeleteChangeList;

    public AddDonHangAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemadd_donhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listOrder == null) {
            return;
        }
        BaseObject baseObject = listOrder.get(position);
        holder.tvNameSp.setText(baseObject.get(ProductObj.name));
        holder.tvGiaTien.setText(StringHelper.formatTien(baseObject.getInt(ProductObj.retail_price)) + "\n" + "(Bán lẻ)");
        holder.tvTongTien.setText(StringHelper.formatTien(baseObject.getInt(ProductObj.total_money)));
        holder.edtSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String soLuong = holder.edtSoLuong.getText().toString();
                if (soLuong.isEmpty() || soLuong == null) {
                    holder.tvTongTien.setText("0.d");
                    soLuong = "0";
                }
                String tong = String.valueOf(Integer.parseInt(soLuong)
                        * Integer.parseInt(baseObject.get(ProductObj.retail_price)));
                holder.tvTongTien.setText(tong + "d");
                baseObject.set(ProductObj.total_money, tong);
                baseObject.set(quantity_product, soLuong);
                onClickTotalAmount.clickTotalamount(String.valueOf(totalAmount()));

            }
        });
        holder.imgDeleteDH.setOnClickListener(view -> {
            listOrder.remove(position);
            onClickTotalAmount.clickTotalamount(String.valueOf(totalAmount()));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listOrder.size());
            onClickDeleteChangeList.onChange(listOrder.size());
        });

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameSp;
        EditText edtSoLuong;
        TextView tvGiaTien;
        TextView tvTongTien;
        ImageView imgDeleteDH;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameSp = itemView.findViewById(R.id.tvNameSp);
            edtSoLuong = itemView.findViewById(R.id.edtSoLuong);
            tvGiaTien = itemView.findViewById(R.id.tvGiaTien);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            imgDeleteDH = itemView.findViewById(R.id.imgDeleteDH);
        }
    }

    public void addData(BaseObject baseObject) {
        baseObject.set(ProductObj.total_money, baseObject.get(ProductObj.retail_price));
        this.listOrder.add(baseObject);
        onClickTotalAmount.clickTotalamount(String.valueOf(totalAmount()));
        notifyDataSetChanged();
    }

    public void onClickTotalAmount(Listerner.onClickTotalAmount onClickTotalAmount) {
        this.onClickTotalAmount = onClickTotalAmount;
    }

    public void setOnClickDeleteChangeList(Listerner.OnListItemChangeList onClickDeleteChangeList) {
        this.onClickDeleteChangeList = onClickDeleteChangeList;
    }

    public ArrayList<BaseObject> listData() {
        return listOrder;
    }

    private int totalAmount() {
        int total = 0;
        for (BaseObject object : listOrder) {
            total = total + Integer.parseInt(object.get(ProductObj.total_money));
        }
        return total;
    }
}
