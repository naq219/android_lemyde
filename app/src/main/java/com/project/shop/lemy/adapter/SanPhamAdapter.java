package com.project.shop.lemy.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.activity.UpdateProductActivity;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.common.MenuFromApiSupport;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.ListenBack;
import com.project.shop.lemy.sanpham.DetailProductActivity;
import com.project.shop.lemy.sanpham.SanPhamFm;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.Mlog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.Viewholder> {
    Context context;
    List<BObject> list = new ArrayList<>();
    Listerner.setOnclickItem setOnclickItem;


    private ListenBack listenBack;
    private ListenBack onLoadEndListener;

    public SanPhamAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(SanPhamAdapter.Viewholder holder, int position) {
        if (list.size()-1==position) {
            onLoadEndListener.OnListenBack(true);
        }

        //Toast.makeText(context,"cuoi",Toast.LENGTH_LONG).show();
        BObject baseObject = list.get(position);
        if (list == null) return;
        //ImageLoader.getInstance().displayImage(baseObject.getAsString(ProductObj.image), holder.imgHinhanh);
        holder.tvTendsspsp.setText("(SP"+baseObject.get(ProductObj.id)+") "+baseObject.get(ProductObj.name));
        holder.itemView.setOnClickListener(view -> {
            listenBack.OnListenBack(baseObject);
        });

        int mordertong=baseObject.getAsInt("order_tong",-9999);
        int order_24h=baseObject.getAsInt("order_24h",-9999);
        int slkho=baseObject.getAsInt("slkho",-9999);
        int tonkho=baseObject.getAsInt("tonkho",-9999);


        holder.tvorder_tong.setText("Order Tổng: "+mordertong);
        holder.tvorder_24h.setText("Order 24h: "+order_24h);
        holder.tvslkho.setText("SL kho: "+slkho);
        holder.tvtonkho.setText("Tồn dư: "+tonkho);


            if (mordertong>0)
                holder.tvorder_tong.setTextColor(Color.BLUE);
            else holder.tvorder_tong.setTextColor(Color.BLACK);
            if (order_24h>0)
                holder.tvorder_24h.setTextColor(Color.BLUE);
            else holder.tvorder_24h.setTextColor(Color.BLACK);
            if (slkho>0)
                holder.tvslkho.setTextColor(Color.BLUE);
            else holder.tvslkho.setTextColor(Color.BLACK);
            if (tonkho>0)
                holder.tvtonkho.setTextColor(Color.BLUE);
            else holder.tvtonkho.setTextColor(Color.BLACK);


        ProductObj oj = new ProductObj(baseObject.getData());
        holder.giaNhap.setText("Giá Nhập: "+ MoneySupport.moneyDot(""+oj.getGiaNhap()));
        holder.giaBan.setText("Giá Lẻ: "+ MoneySupport.moneyDot(""+oj.getGiaBanLe()));
        holder.giaBuon.setText("Giá CTV: "+ MoneySupport.moneyDot(""+oj.getGiaBuon()));
        holder.giaBuonSi.setText("Giá Buôn Sỉ: "+ MoneySupport.moneyDot(""+oj.getGiaBuonSi()));

        holder.imgNew.setOnClickListener(view -> {

            holder.imgNew.setBackgroundColor(Color.GRAY);
            holder.imgNew.postDelayed(() -> {
                holder.imgNew.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            },200);

            long last= (long) holder.imgNew.getTag();
            long time= Calendar.getInstance().getTimeInMillis()-last;
            holder.imgNew.setTag(Calendar.getInstance().getTimeInMillis());
            if (time>500) {
                holder.giaBuonSi.postDelayed(() -> {

                },1000);
                return;
            }



            updateTimeDangBai(oj.getId());
        });
        holder.imgNew.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("simpletext",holder.tvTendsspsp.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context,"đã sao chép tên",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void updateTimeDangBai(String productId) {

        String sql ="UPDATE ;::;.products SET post_at = UNIX_TIMESTAMP(NOW()) WHERE id="+productId;
        TaskGeneral.exeTaskInsertApi(context,sql,1,new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                Toast.makeText(context,"Cập nhật thời gian đăng bài thành công",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                Toast.makeText(context,"Lỗi cập nhật time đăng bài "+msg,Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnLoadEnd(ListenBack onLoadEndListener) {

        this.onLoadEndListener = onLoadEndListener;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgNew;
        TextView tvTendsspsp, giaNhap, giaBan, giaBuon, giaBuonSi;
        TextView tvtonkho,tvslkho,tvorder_24h,tvorder_tong;

        public Viewholder(View v) {
            super(v);
            imgNew = v.findViewById(R.id.imgNew);
            tvTendsspsp = v.findViewById(R.id.tvTendsspsp);
            giaNhap = v.findViewById(R.id.giaNhap);
            giaBan = v.findViewById(R.id.giaBan);
            giaBuon = v.findViewById(R.id.giaBuon);
            giaBuonSi = v.findViewById(R.id.giaBuonSi);
            tvorder_tong= v.findViewById(R.id.tvorder_tong);
            tvtonkho= v.findViewById(R.id.tvtonkho);
            tvorder_24h= v.findViewById(R.id.tvorder_24h);
            tvslkho= v.findViewById(R.id.tvslkho);
            imgNew.setTag(0l);
        }
    }

    public void setData(List<BObject> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BObject> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setItemClick(ListenBack listenBack) {

        this.listenBack = listenBack;
    }
}
