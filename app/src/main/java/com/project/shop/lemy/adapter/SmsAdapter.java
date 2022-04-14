package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.SmsObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ducqv on 7/4/2018.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    private ArrayList<BaseObject> list = new ArrayList<>();
    private Context context;
    Listerner.OnEditclick onclickEdit;
    Listerner.setOnclickDelete onclickDelete;
    public SmsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_sms, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvChuDe.setText(list.get(position).get(SmsObj.title));
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(new Date());
        holder.tvTime.setText(formattedDate);
        holder.imgEditSms.setOnClickListener(view -> onclickEdit.clickEdit(position));
        holder.imgDeleteSms.setOnClickListener(view -> onclickDelete.clickDelete(position));
        holder.viewFake.setVisibility(View.GONE);
        if (position + 1 == list.size()) holder.viewFake.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChuDe, tvTime;
        private ImageView imgEditSms, imgDeleteSms;
        View viewFake;
        public ViewHolder(View itemView) {
            super(itemView);
            tvChuDe = itemView.findViewById(R.id.tvChuDe);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgEditSms = itemView.findViewById(R.id.imgEditSms);
            imgDeleteSms = itemView.findViewById(R.id.imgDeleteSms);
            viewFake = itemView.findViewById(R.id.viewFake);
        }
    }

    public void setData(ArrayList<BaseObject> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public BaseObject getItem(int position) {
        return list.get(position);
    }

    public void setOnclickEdit(Listerner.OnEditclick onclickEdit) {
        this.onclickEdit = onclickEdit;
    }

    public void setOnclickDelete(Listerner.setOnclickDelete onclickDelete) {
        this.onclickDelete = onclickDelete;
    }
}
