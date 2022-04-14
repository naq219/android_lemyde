package com.project.shop.lemy.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.smskhachhang.ItemKhachHangShopActivity;
import com.project.shop.lemy.bean.ShopObj;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class SmskhachHangAdapter extends RecyclerView.Adapter<SmskhachHangAdapter.Viewholder> {
    Context context;
    ArrayList<BaseObject> listShop = new ArrayList<>();
    String tenShop;
    String xoaChuDau;
    String inHoa;

    public SmskhachHangAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_kh, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        if (listShop == null) return;
        BaseObject object = listShop.get(position);
        tenShop = object.get(ShopObj.name);
        char first = tenShop.charAt(0);
        inHoa = String.valueOf(first);
        inHoa = inHoa.toUpperCase();
        xoaChuDau = tenShop.substring(1);
        holder.tvNameShop.setText(inHoa + xoaChuDau);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ItemKhachHangShopActivity.class);
            intent.putExtra("name", object.get(ShopObj.name));
            intent.putExtra("id",object.get(ShopObj.id));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listShop.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvNameShop;

        public Viewholder(View itemView) {
            super(itemView);
            tvNameShop = itemView.findViewById(R.id.tvNameShop);
        }
    }


    public void setData(ArrayList<BaseObject> list) {
        this.listShop.clear();
        this.listShop = list;
        notifyDataSetChanged();
    }
}
