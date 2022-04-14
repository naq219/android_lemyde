package com.project.shop.lemy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.Detail_orders;
import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.helper.MoneySupport;
import com.project.shop.lemy.helper.StringHelper;
import com.project.shop.lemy.listener.ListenBack;
import com.telpoo.frame.object.BaseObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ducqv on 7/6/2018.
 */

public class DetailAdapter2 extends RecyclerView.Adapter<DetailAdapter2.ViewHolder> {
    ArrayList<ArrayList<BaseObject>> listSp = new ArrayList<>();
    private ListenBack onTickChanged;
    private boolean isshowInfoThem=false;
    //private boolean isEdit=false;
    int TYPE_CLICK_ITEM=0; //0 binh thuong, 1: sửa sản phẩm , 2: lưu kho
    int TYPE_CLICK_EDIT=1;
    int TYPE_CLICK_KEEPSTOCK=2;
    private ListenBack onClickEditListener;
    private ListenBack onClickKeepStock;
    private Context context;

    HashMap<Integer,Integer> hmTick= new HashMap<>();
    private boolean isCustomTachDon=false; //=true cho phép tách đơn bằng cách chọn
    private boolean isHangVeDu =true; // tat ca sp deu co hang
    private ListenBack listenHaveKeepStock;


    public DetailAdapter2(ArrayList<ArrayList<BaseObject>> listSp,Context context) {
        hmTick= new HashMap<>();
        isHangVeDu =true;
        this.listSp = listSp;
        this.context = context;
    }

    public void listenHaveKeepStock(ListenBack listenHaveKeepStock){
        this.listenHaveKeepStock = listenHaveKeepStock;
    }

    public void setData(ArrayList<BaseObject> lSp){
        listSp = new ArrayList<>();
        for (int i = 0; i < lSp.size(); i++) {
            if(lSp.get(i).getInt("keepstock_time",0)>0&&listenHaveKeepStock!=null)
                listenHaveKeepStock.OnListenBack(true);
            ArrayList<BaseObject> ll= new ArrayList<>();
            ll.add(lSp.get(i));
            ArrayList<BaseObject> llRemove= new ArrayList<>();
            for (int j = i+1; j < lSp.size(); j++) {
                if (lSp.get(i).get("product_id").equals(lSp.get(j).get("product_id"))){
                    ll.add(lSp.get(j));
                    llRemove.add(lSp.get(j));
                }

            }
            listSp.add(ll);
            lSp.removeAll(llRemove);
        }

        hmTick= new HashMap<>();
        isHangVeDu =true;
        notifyDataSetChanged();
    }
    public boolean isHangVeDu(){
        return isHangVeDu;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!hmTick.containsKey(position)) hmTick.put(position,-1);
        ArrayList<BaseObject> bo = listSp.get(position);
        BaseObject boo= bo.get(0);

        int createdAtM=0;
        int sttMax=-1;
        int slTong=0;
        int dave=0;
        int kho=boo.getInt("slkho");
        for (BaseObject bo1: bo) {
            int createdAtm1=bo1.getInt("created_atm",0);
            int stt1=bo1.getInt("stt",-1);
            int sl1=bo1.getInt(OrderObj.quantity);
            if (createdAtM==0||createdAtm1<createdAtM)
                createdAtM= createdAtm1;
            if (stt1>sttMax)sttMax=stt1;
            slTong+=sl1;
            int dave1= kho-stt1+sl1; if (dave1>sl1) dave1=sl1;
            if (dave1>0)dave+=dave1;
        }

        String infoKeepStock ="";
        if(boo.getInt("keepstock_time",0)>0)
            infoKeepStock=" (lưu kho "+boo.getInt("keepstock_sl",0)+"c)";
        String nameP= "(SP"+boo.get("product_id")+")"+boo.get(OrderObj.product_name)+infoKeepStock;
//        Calendar time1 = TimeUtils.String2Calendar(baseObject.get("created_at"),"yyyy-MM-dd HH:mm:ss");
//        time.add(Calendar.HOUR,7);
        holder.tvNameSP.setText(nameP);

        String cachDay=StringHelper.getTimeAgo(createdAtM*1000l);
        String infoThem= cachDay+" Kho:"+kho+" Số thứ tự:"+sttMax;

        if (showInfoThem()) {
            holder.tvInfoThem.setText(infoThem);
            holder.tvInfoThem.setVisibility(View.VISIBLE);
        }
        else holder.tvInfoThem.setVisibility(View.GONE);

        holder.tvSoLuong.setText("SL: "+slTong+"c");

        holder.stt.setText(""+position);

        holder.tvTong.setText(MoneySupport.moneyEndK(boo.get(OrderObj.gia_ban)));


        if (hmTick.get(position)==1)
        {
            holder.viewTick.setVisibility(View.VISIBLE);
            holder.root.setBackgroundColor(Color.parseColor("#4FFFEB3B"));
        }
        else if (hmTick.get(position)==-1) {
            holder.viewTick.setVisibility(View.GONE);
            holder.root.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
        else {
            holder.viewTick.setVisibility(View.VISIBLE);
            holder.root.setBackgroundColor(Color.parseColor("#ff9999"));
        }



        if (dave>=slTong){ //có thể xuất
            holder.tvNameSP.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvNameSP.setTypeface(holder.tvNameSP.getTypeface(), Typeface.BOLD);
            holder.tvSoLuong.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }
        else if (dave<slTong&&dave>0){ // về 1 phần
            holder.tvNameSP.setTextColor(context.getResources().getColor(R.color.hangve_1phan));
            holder.tvNameSP.setTypeface(null, Typeface.BOLD);
            holder.tvSoLuongVe.setText("Về: "+dave+"c");
            holder.tvSoLuongVe.setVisibility(View.VISIBLE);
            isHangVeDu =false;
        }
        else {                         //chưa về
            holder.tvNameSP.setTextColor(Color.parseColor("#FFB80202"));
            holder.tvNameSP.setTypeface(null, Typeface.NORMAL);
            isHangVeDu =false;
        }

        int finalDave = dave;
        int finalSlTong = slTong;
        holder.tvSoLuong.setOnClickListener(view -> {
            if (!isCustomTachDon) return;
            if (hmTick.get(position)==1||hmTick.get(position)==0){
                hmTick.put(position,-1);
            }
           else {
//                if (finalDave < finalSlTong && finalDave >0){
//                    DialogSupport.dialogYesNo(holder.tvNameSP.getText().toString()+"\nBấm Yes để chọn tất cả\nBấm No để chỉ chọn Số lượng đã có",context,status -> {
//                        if (status)hmTick.put(position,1);
//                        else hmTick.put(position,0);
//                        notifyDataSetChanged();
//                    });
//                }
                 hmTick.put(position,1);
            }
            notifyDataSetChanged();
            onTickChanged.OnListenBack("");

        });

        holder.tvSoLuong.setOnLongClickListener(view -> {
            isCustomTachDon=true;
            Toast.makeText(context,"Bật tính năng chọn đơn để tách",Toast.LENGTH_SHORT).show();
            return true;
        });


        holder.tvNameSP.setOnClickListener(view -> {
            if (TYPE_CLICK_ITEM==TYPE_CLICK_EDIT){
                onClickEditListener.OnListenBack(bo);
                return;
            }
            if (TYPE_CLICK_ITEM==TYPE_CLICK_KEEPSTOCK){
                Object[] oj = new Object[3];
                oj[0]= boo.get("product_id");
                oj[1]= finalDave;
                oj[2]= boo;
                onClickKeepStock.OnListenBack(oj);
                return;
            }

            isshowInfoThem=!isshowInfoThem;
            notifyDataSetChanged();

        });

        holder.tvNameSP.setOnLongClickListener(view -> {
            setIsEdit();
            return true;
        });


    }


    public void setOnClickEdit(ListenBack onClickEditListener){
        this.onClickEditListener = onClickEditListener;
    }
    public void setOnClickKeepStock(ListenBack onClickKeepStock){
        this.onClickKeepStock = onClickKeepStock;
    }

    private boolean showInfoThem() {
        return isshowInfoThem;
    }

    public void setOnTickChanged1(ListenBack onTickChanged){
        this.onTickChanged = onTickChanged;
    }

    public String getDOIdsTick(){
        return getValueTick("id");
    }
    public int getDOMoneyTick(){
        //return getValueTick("gia_ban");
        int tonggia=0;
        StringBuilder id= new StringBuilder();
        for (int i = 0; i < listSp.size(); i++) {
            for (int j = 0; j < listSp.get(i).size(); j++) {
                if (hmTick.get(i)==1||(hmTick.get(i)==0
                        &&listSp.get(i).get(j).getInt(Detail_orders.stt,999)<=listSp.get(i).get(j).getInt(Detail_orders.slkho,999))){
                    //id.append(listSp.get(i).get(j).get(key)+"_");
                    tonggia+= listSp.get(i).get(j).getInt("gia_ban")*listSp.get(i).get(j).getInt(OrderObj.quantity);
                }
            }

        }
       return tonggia;

    }

    private String getValueTick(String key){
        StringBuilder id= new StringBuilder();
        for (int i = 0; i < listSp.size(); i++) {
            for (int j = 0; j < listSp.get(i).size(); j++) {
                if (hmTick.get(i)==1||(hmTick.get(i)==0
                        &&listSp.get(i).get(j).getInt(Detail_orders.stt,999)<=listSp.get(i).get(j).getInt(Detail_orders.slkho,999))){
                    id.append(listSp.get(i).get(j).get(key)+"_");
                }
            }

        }
        if (id.length()==0) return null;
        return id.deleteCharAt(id.length()-1).toString();
    }

    @Override
    public int getItemCount() {
        return listSp.size();
    }


    public String getProductNameTick() {
        return getValueTick("product_name");
    }

    public void setIsEdit() {
        Toast.makeText(context,"chuyển sang chế độ sửa",Toast.LENGTH_SHORT).show();
        //isEdit=true;
        TYPE_CLICK_ITEM=TYPE_CLICK_EDIT;
    }
    public void setTypeKeppStock() {
        Toast.makeText(context,"chuyển sang chế độ CHỌN LƯU KHO",Toast.LENGTH_SHORT).show();
        //isEdit=true;
        TYPE_CLICK_ITEM=TYPE_CLICK_KEEPSTOCK;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSP;
        private TextView tvSoLuong,tvSoLuongVe;
        private TextView tvTong,stt;
        private TextView tvInfoThem;
        View viewTick,root;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNameSP = itemView.findViewById(R.id.tvNameSP);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvSoLuongVe = itemView.findViewById(R.id.tvSoLuongVe);
            tvTong = itemView.findViewById(R.id.tvTong);
            viewTick= itemView.findViewById(R.id.viewTick);
            root= itemView.findViewById(R.id.root);
            tvInfoThem= itemView.findViewById(R.id.tvInfoThem);
            stt= itemView.findViewById(R.id.tvStt);
        }
    }
}
