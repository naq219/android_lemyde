package com.project.shop.lemy.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

/**
 * Created by Ducqv on 8/31/2018.
 */

public class ThayDoiTrangThaiDhAdapter extends RecyclerView.Adapter<ThayDoiTrangThaiDhAdapter.ViewHolder> {
    Context context;
    ArrayList<BaseObject> listDH = new ArrayList<>();
    Listerner.OnListItemChangeList onListItemChangeList;
    String trangthai;

    public ThayDoiTrangThaiDhAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thaydoi_trangthai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseObject baseObject = listDH.get(position);
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject object = (BaseObject) data;
                ArrayList<String> list = object.getKeys();
                BaseObject objStatusList = new BaseObject();
                for (int i = 0; i < list.size(); i++) {
                    if (objStatusList != null)
                        objStatusList.set(list.get(i), object.get(list.get(i)));
                }
                trangthai = objStatusList.get(baseObject.get(OrderObj.status), "");
                holder.tvDonHang.setText(Html.fromHtml(getTextDisplay(baseObject)), TextView.BufferType.SPANNABLE);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_STATUS, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();

        String name = baseObject.get(OrderObj.customers_name, "");
        if (name.isEmpty()) holder.itemView.setEnabled(false);
        else holder.itemView.setEnabled(true);
        holder.imgDelete.setOnClickListener(view -> clickDelete(position));
        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, ChiTietDonHangActivity.class);
//            intent.putExtra("obj", baseObject);
//            context.startActivity(intent);
            ChiTietDonHangActivity.startActivity(context,baseObject.get(OrderObj.id));
        });

        holder.stt.setText(""+(position+1));
    }

    public String getTextDisplay(BaseObject item) {
        String mDH = item.get(OrderObj.id, "");
        String dvvc = item.get(OrderObj.dvvc, "");
        String nameKH = item.get(OrderObj.customers_name, "");
        String tinh = item.get(OrderObj.customers_province_name);
        if (item.getBool("id_sai", false))
            return "<font color=\"#000000\">" + "MDH" + mDH + "</font>" + " - <font color=\"#FF0000\">" + "Không đúng" + "</font>";
        if (nameKH.isEmpty()) return "DMH" + mDH;
        if (trangthai.isEmpty()) trangthai = "Chưa rõ";
        if (dvvc.isEmpty() || dvvc.equals("null")) dvvc = "Chưa cập nhật";
        return "<font color=\"#000000\">" + "MDH" + mDH + "</font>"
                + " - <font color=\"#3F23F5\">" + nameKH + "(" + tinh + ")" + "</font>"
                + " - <font color=\"#006400\">" + trangthai + "</font>"
                + " - <font color=\"#DF7401\">" + dvvc + "</font>";

    }

    public void clickDelete(int position) {
        listDH.remove(position);
        notifyDataSetChanged();
        onListItemChangeList.onChange(listDH.size());
    }

    @Override
    public int getItemCount() {
        return listDH.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDonHang,stt;
        private ImageView imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDonHang = itemView.findViewById(R.id.tvDonHang);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            stt = itemView.findViewById(R.id.stt);
        }
    }

    public void addData(BaseObject baseObject) {
        listDH.add(0, baseObject);
        onListItemChangeList.onChange(listDH.size());
        notifyDataSetChanged();
    }

    public void addDataSai(BaseObject baseObject) {
        listDH.add(baseObject);
        onListItemChangeList.onChange(listDH.size());
        notifyDataSetChanged();
    }

    public ArrayList<BaseObject> listData() {
        return listDH;
    }

    public void setData(ArrayList<BaseObject> list) {
        listDH.clear();
        listDH.addAll(0, list);
        notifyDataSetChanged();
        onListItemChangeList.onChange(listDH.size());
    }

    public BaseObject getItem(int index) {
        return listDH.get(index);
    }

    public String getListId() {
        String list = "";
        for (int i = 0; i < listDH.size(); i++) {
            if (i == 0) list += getItem(i).get(OrderObj.id);
            else list += ", " + getItem(i).get(OrderObj.id);
        }
        return list;
    }

    public String getListIdCheck() {
        String list = "#";
        for (int i = 0; i < listDH.size(); i++) {
            list += getItem(i).get(OrderObj.id) + "#";
        }
        return list;
    }

    public boolean checkTrungId(String id) {
        for (int i = 0; i < listDH.size(); i++) {
            if (getItem(i).get(OrderObj.id, "").equals(id)) return true;
        }
        return false;
    }

    public void setOnListItemChangeList(Listerner.OnListItemChangeList onClickDeleteChangeList) {
        this.onListItemChangeList = onClickDeleteChangeList;
    }
}
