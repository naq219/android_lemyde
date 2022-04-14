package com.project.shop.lemy.donhang;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.dialog.DialogSupport;
import com.project.shop.lemy.helper.OrderHelper;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.StringSupport;
import com.telpoo.frame.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonHangAdapterV2 extends RecyclerView.Adapter<DonHangAdapterV2.Viewholder> {
    Context context;
    List<BObject> list = new ArrayList<>();

    public DonHangAdapterV2(Context context) {
        this.context = context;

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donhang_v2, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        if (list == null) return;
        BObject baseObject = list.get(position);
        int status= baseObject.getAsInt("status");
        holder.tvHeader.setText("DH "+OrderHelper.formatOid(baseObject.get("id"))+" - "+ OrderHelper.getStatusName(baseObject.getAsString("status")));
        int statusColor= Color.BLACK;
        String importainStr="";
        int keepstock_timemin = baseObject.getAsInt("keepstock_timemin");
        switch (status){
            case 1: statusColor= Color.GREEN;
            if(keepstock_timemin>0){
                importainStr+="Đã lưu kho cách đây "+ (TimeUtils.getTimeMillis()- keepstock_timemin*1000)/86400+" ngày";
            }

            break;
            case 2: statusColor= Color.YELLOW;break;
            case 3: statusColor= Color.CYAN;break;
            case 4: statusColor= Color.RED;break;
            case 5: statusColor= Color.YELLOW;break;

        }
        //holder.lnChinh.setBackgroundColor(statusColor);
        holder.bgMau.setBackgroundColor(statusColor);
        holder.tvHeader.setBackgroundColor(statusColor);

        if(importainStr.length()>0) holder.tvImportain.setText(importainStr);
        JSONObject joVal= (JSONObject) baseObject.get("oval");

        if(joVal!=null&&joVal.has("skipexport_to")){
            holder.tvSkipExport.setVisibility(View.VISIBLE);
            String skipText="(%sd) %s, Tạm dừng xuất đến %s";
//            Calendar calFrom = Calendar.getInstance();
//            calFrom.setTimeInMillis(joVal.optLong("skipexport_from",0)*1000);

            long ago=Calendar.getInstance().getTimeInMillis()-joVal.optLong("skipexport_from",0)*1000;
            long agoInt=ago/1000/60/60/24;

            Calendar calTo = Calendar.getInstance();
            calTo.setTimeInMillis(joVal.optLong("skipexport_to",0)*1000);
            skipText=String.format(skipText,agoInt , joVal.optString("skipexport_note",""), TimeUtils.calToString(calTo,"dd/MM/yyyy"));
            holder.tvSkipExport.setText(skipText);
        }
        else  holder.tvSkipExport.setVisibility(View.GONE);

        JSONObject joCustomer= (JSONObject) baseObject.get("customer");
        holder.tvShop.setText(baseObject.getAsString("shop_name"));
        holder.tvCustomer.setText(joCustomer.optString("name")+", "+joCustomer.optString("district_name")+", "+joCustomer.optString("province_name"));

        holder.tvSanpham.setText(baseObject.getAsString(OrderObj.product_name));

        String infoSp ="";
        JSONArray detailOrders = (JSONArray) baseObject.get("detail_orders");
        if (detailOrders.length()==0) infoSp="Đơn hàng không có SP nào ";
        for (int i = 0; i < detailOrders.length(); i++) {
            JSONObject doCon = detailOrders.optJSONObject(i);

            String infoCon= String.format("<font color='red'>%sC</font> - (SP%s)%s<br><br>", doCon.opt("quantity"), doCon.opt("product_id"),doCon.opt("product_name")) ;   //doCon.opt("quantity")+"C - "+ doCon.opt("(S")
            infoSp+=infoCon;
        }
        holder.tvSanpham.setText(Html.fromHtml(infoSp),TextView.BufferType.SPANNABLE);
        String ghichu=baseObject.getAsString("note","");
        if (ghichu.equalsIgnoreCase("null")) ghichu="";
        if (ghichu.length()>0)
        holder.tvNote.setText("Ghi chú: "+ghichu);

        holder.imgMenu.setOnClickListener(view -> showMenu(holder.imgMenu, baseObject));

        holder.itemView.setOnClickListener(view -> {
            ChiTietDonHangActivity.startActivity(context,baseObject.getAsString(OrderObj.id));
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvHeader;

        private TextView tvSanpham,tvCustomer,tvShop,tvImportain,tvNote,tvSkipExport;
        private ImageView imgMenu;
        View lnChinh,bgMau;
        public Viewholder(View v) {
            super(v);
            tvHeader = v.findViewById(R.id.tvHeader);
            lnChinh = v.findViewById(R.id.lnChinh);
            bgMau = v.findViewById(R.id.bgMau);
            tvSanpham = v.findViewById(R.id.tvSanpham);
            imgMenu = v.findViewById(R.id.imgMenu);
            tvCustomer = v.findViewById(R.id.tvCustomer);
            tvShop = v.findViewById(R.id.tvShop);
            tvImportain= v.findViewById(R.id.tvImportain);
            tvNote= v.findViewById(R.id.tvNote);
            tvSkipExport = v.findViewById(R.id.tvSkipExport);

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



    private void showMenu(View v, BObject object) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.popup_item_dh, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            BaseObject ojOld= new BaseObject();
            ojOld.setParams(object.getDataContentValue());
            switch (item.getItemId()) {
                case R.id.action_Sms:
                    ProcessDonHangActivity.sendSms(context, ojOld);
                    break;
                case R.id.action_In:
                    ProcessDonHangActivity.inDonHang(context, ojOld);
                    break;
                case R.id.action_pushvc:
                    ProcessDonHangActivity.pushDonHangHvc(context, ojOld,null);
                    break;
                case R.id.action_note:

                    boolean isFirstSkip=true;
                    if(object.convert2NetParramsNotEncode().contains("skipexport_from"))
                        isFirstSkip=false;
                    View viewNote= new ViewNoteTmpXuat(object.get("id"),isFirstSkip,context).getView();
                    DialogSupport.dialogCustom(viewNote, context);

                    break;
            }
            return false;
        });
        popup.show();
    }

}