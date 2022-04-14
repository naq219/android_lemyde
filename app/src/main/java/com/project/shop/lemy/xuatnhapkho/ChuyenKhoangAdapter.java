package com.project.shop.lemy.xuatnhapkho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.utils.ListenBack;
import com.project.shop.lemy.R;

import java.util.ArrayList;
import java.util.List;



public class ChuyenKhoangAdapter extends RecyclerView.Adapter<ChuyenKhoangAdapter.ViewHolder> {
    List<BObject> list = new ArrayList<>();
    Context context;
    private ListenBack listenSlChange;
    private int maxSl=3000;
    private ListenBack listenPassMaxsl;

    public ChuyenKhoangAdapter(Context context,List<BObject> list) {
        if (list!=null)
        this.list=list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chuyenkhoangadapter, parent, false);
        return new ViewHolder(view);
    }

    public void setOnSlChange(ListenBack listenSlChange){

        this.listenSlChange = listenSlChange;
    }

    public void setOnSlPassMaxSl(ListenBack listenPassMaxsl){

        this.listenPassMaxsl = listenPassMaxsl;
    }
    public void setMaxSl(int maxSl){
        this.maxSl = maxSl;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (list == null) return;
        BObject oj = list.get(position);
        int sl =oj.getAsInt("sl");
        holder.tvSl.setText(""+sl);
        holder.tvKhoang.setText(oj.getAsString("khoang_id"));

        int slTru= oj.getAsInt("chuyenkhoang_sltru",0);
        setTextSlTru(holder.tvSlTru,slTru);

        holder.cong.setOnClickListener(v -> {
            int slTru1= oj.getAsInt("chuyenkhoang_sltru",0);
            slTru1++;
            if (slTru1>sl) return;
            if (getTotalSlTru()>=maxSl){
                listenPassMaxsl.OnListenBack(maxSl);
                return;
            }
            list.get(position).set("chuyenkhoang_sltru",slTru1);
            setTextSlTru(holder.tvSlTru,slTru1);
            listenSlChange.OnListenBack(getTotalSlTru());

        });

        holder.tru.setOnClickListener(v -> {
            int slTru2= oj.getAsInt("chuyenkhoang_sltru",0);
            slTru2--;
            if (slTru2<0) return;
            list.get(position).set("chuyenkhoang_sltru",slTru2);
            setTextSlTru(holder.tvSlTru,slTru2);
            listenSlChange.OnListenBack(getTotalSlTru());
        });


    }

    private void setTextSlTru(TextView tvSlTru, int slTru) {
        tvSlTru.setText("-"+slTru);
        if (slTru==0) tvSlTru.setText("");
    }

    private int getTotalSlTru() {
        int tong=0;
        for (int i = 0; i < list.size(); i++) {
            tong+=list.get(i).getAsInt("chuyenkhoang_sltru",0);
        }
        return tong;
    }

    public BObject getItem(int index) {
        return list.get(index);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<BObject> getData() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKhoang,tvSl,tvSlTru;
        private View tru,cong;

        public ViewHolder(View itemView) {
            super(itemView);
            tvKhoang = itemView.findViewById(R.id.tvKhoang);
            tvSl = itemView.findViewById(R.id.tvSl);

            tru = itemView.findViewById(R.id.tru);
            cong = itemView.findViewById(R.id.cong);

            tvSlTru= itemView.findViewById(R.id.tvSlTru);
        }
    }



    public void resetData(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public void setData(List<BObject> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }


}
