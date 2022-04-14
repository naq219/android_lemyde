package com.project.shop.lemy.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.bean.SmsObj;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ducqv on 7/5/2018.
 */

public class ListTieuDeAdapter extends RecyclerView.Adapter<ListTieuDeAdapter.ViewHolder> {
    private ArrayList<BaseObject> list = new ArrayList<>();
    private ArrayList<BaseObject> listPhone = new ArrayList<>();
    BaseObject object = new BaseObject();
    Context context;
    String type = "";
    Listerner.OnClick onClick;

    public ListTieuDeAdapter(ArrayList<BaseObject> list, Context context, BaseObject object) {
        this.list = list;
        this.context = context;
        this.object = object;
    }

    public ListTieuDeAdapter(ArrayList<BaseObject> list, Context context, ArrayList<BaseObject> listPhone, String type) {
        this.list = list;
        this.context = context;
        this.listPhone = listPhone;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_sms, parent, false);
        return new ListTieuDeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvChuDe.setText(list.get(position).get(SmsObj.title));
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(new Date());
        holder.tvTime.setText(formattedDate);

        holder.itemView.setOnClickListener(view -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");

            if (type.equals("type")) {

                for (int i = 0; i < listPhone.size(); i++) {
                    object = listPhone.get(i);
                    String phone = object.get(CustomerObj.phone);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,
                            null, list.get(position).get(SmsObj.content), null, null);
                    onClick.clickView();
                }
            } else {
                smsIntent.putExtra("address", list.get(position).get(CustomerObj.phone));
                smsIntent.putExtra("sms_body", StringHelper.replaceBody(list.get(position)));
                context.startActivity(smsIntent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChuDe, tvTime;
        private ImageView imgEditSms, imgDeleteSms;

        public ViewHolder(View itemView) {
            super(itemView);
            tvChuDe = itemView.findViewById(R.id.tvChuDe);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgEditSms = itemView.findViewById(R.id.imgEditSms);
            imgDeleteSms = itemView.findViewById(R.id.imgDeleteSms);
            imgDeleteSms.setVisibility(View.GONE);
            imgEditSms.setVisibility(View.GONE);
        }
    }

    public void setOnClick(Listerner.OnClick onClick) {
        this.onClick = onClick;
    }
}
