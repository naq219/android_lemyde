package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.helper.MoneySupport;
import com.telpoo.frame.object.BaseObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ducqv on 7/4/2018.
 */

public class SuggestPNhapDonAdapter extends RecyclerView.Adapter<SuggestPNhapDonAdapter.ViewHolder> {

    private  List<BaseObject> ojs;
    //String[] vl;
    private int resource;
    private Context context;
    onClickRowListen listenBack;
    Locale localeVN = new Locale("vi", "VN");
    DecimalFormat nf =(DecimalFormat) DecimalFormat.getCurrencyInstance(localeVN);
    private SuggestPNhapDonAdapter.onShowToast onShowToast;

    public SuggestPNhapDonAdapter(Context context, List<BaseObject> ojs, int resource) {
        DecimalFormatSymbols symbols = nf.getDecimalFormatSymbols();
        symbols.setMonetaryDecimalSeparator('.');
        nf.setDecimalFormatSymbols(symbols);

        this.context = context;
        this.ojs = ojs;
        List<BaseObject> o= new ArrayList<>();
        if (ojs==null) this.ojs=o;
        // this.vl=vl;
        this.resource = resource;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(resource, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BaseObject oj= ojs.get(position);
        String thongke= "{"+oj.get("order_tong")+"/K"+oj.get("slkho")+"}";
        holder.tv.setText(oj.get(ProductObj.name)+" ("+ MoneySupport.moneyEndK(oj.get(ProductObj.retail_price))+")"+thongke);



        holder.edGiaBan.setOnFocusChangeListener((view, b) -> {
            String s=holder.edGiaBan.getText().toString();
            if (s.length()!=s.replaceAll("[^\\d]", "" ).length())holder.edGiaBan.setText("");
        });
        //holder.edGiaBan.setText(MoneySupport.moneyDot(oj.get(ProductObj.retail_price)));

        holder.mRoot.setOnClickListener(view -> {
            holder.parentThem.setVisibility(holder.parentThem.getVisibility()==View.GONE?View.VISIBLE: View.GONE);
//            if (listenBack!=null)
//            listenBack.onClickRow(position);
        });
        holder.btnCong.setOnClickListener(view -> {
            String t1= ""+holder.edSl.getText().toString();
            if (t1.length()==0) t1="0";
            int oldSl= Integer.parseInt(t1)+1;
            holder.edSl.setText(String.valueOf(oldSl));
            holder.edGiaBan.setText(MoneySupport.moneyDot(holder.edGiaBan.getText().toString()));
        });

        holder.btnTru.setOnClickListener(view -> {
            String t1= ""+holder.edSl.getText().toString();
            if (t1.length()==0) t1="0";
            int oldSl= Integer.parseInt(t1)-1;
            if (oldSl>-1)
            holder.edSl.setText(String.valueOf(oldSl));
        });

        holder.viewEditGia.setOnClickListener(view -> {
            holder.viewEditGia.setVisibility(View.GONE);
        });




        holder.btnThem.setOnClickListener(view -> {

            if (holder.viewEditGia.getVisibility()==View.VISIBLE){
                onShowToast.onShowToast("Chưa xác nhận giá tiền!");
                return;
            }

            if (holder.edSl.getText().toString().isEmpty()){
                onShowToast.onShowToast("Chưa nhập số lượng!");
                return;
            }
            String giaBan = holder.edGiaBan.getText().toString().replaceAll("\\D+","");
            if (giaBan.isEmpty()){
                onShowToast.onShowToast("Định dạng số tiền ko đúng!");
                return;
            }
            int gibanI= Integer.parseInt(giaBan);
            if (gibanI>5&&!giaBan.equals(oj.get(ProductObj.retail_price))){
                holder.tmpHoi.setVisibility(View.VISIBLE);
                holder.btnThem.setVisibility(View.GONE);
                return;
            }

            BaseObject oj1= ojs.get(position).clone();
            oj1.set("new_sl",holder.edSl.getText().toString());
            oj1.set("new_giaban",holder.edGiaBan.getText().toString());

            listenBack.onClickRow(oj1,position);
        });


        holder.btnXXChange.setOnClickListener(view -> {
            BaseObject oj1= ojs.get(position).clone();
            oj1.set("new_sl",holder.edSl.getText().toString());
            oj1.set("new_giaban",holder.edGiaBan.getText().toString());
            listenBack.onClickRow(oj1,position);
            TaskGeneral.exeTaskInsertApi(context, SqlTaskGeneral.clearGiaNhapGiaBan+oj1.get(ProductObj.id),1,new Model());

        });
        holder.btnXXSale.setOnClickListener(view -> {
            BaseObject oj1= ojs.get(position).clone();
            oj1.set("new_sl",holder.edSl.getText().toString());
            oj1.set("new_giaban",holder.edGiaBan.getText().toString());
            listenBack.onClickRow(oj1,position);
        });
        holder.btnXXNhapSai.setOnClickListener(view -> {
            holder.tmpHoi.setVisibility(View.GONE);
            holder.btnThem.setVisibility(View.VISIBLE);
            holder.edGiaBan.setText("");
        });
    }

    @Override
    public int getItemCount() {
        if (ojs==null) return 0;
        return ojs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private View mRoot,parentThem,btnCong,btnTru,btnThem,viewEditGia,
                btnXXSale,btnXXNhapSai,btnXXChange,tmpHoi;
        EditText edGiaBan,edSl;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            mRoot=itemView.findViewById(R.id.mroot);
            parentThem=itemView.findViewById(R.id.parentThem);
            edGiaBan=itemView.findViewById(R.id.edGiaBan);
            edSl=itemView.findViewById(R.id.edSl);
            btnCong=itemView.findViewById(R.id.btnCong);
            btnTru=itemView.findViewById(R.id.btnTru);
            btnThem=itemView.findViewById(R.id.btnThem);
            viewEditGia= itemView.findViewById(R.id.viewEditGia);

            btnXXChange= itemView.findViewById(R.id.btnXXChange);
            btnXXNhapSai= itemView.findViewById(R.id.btnXXNhapSai);
            btnXXSale= itemView.findViewById(R.id.btnXXSale);
            tmpHoi= itemView.findViewById(R.id.tmpHoi);
        }
    }

    public void setData(List<BaseObject> ojs) {
       this.ojs=ojs;
       if (ojs==null)this.ojs= new ArrayList<>();
        notifyDataSetChanged();
    }

    public BaseObject getItem(int position) {
        return ojs.get(position);
    }



    public void setOnclickThem(onClickRowListen onclick) {
       this.listenBack=onclick;
    }
    public void setOnShowToast(onShowToast onShowToast) {
        this.onShowToast = onShowToast;
    }

    public interface onClickRowListen {
        void  onClickRow(BaseObject oj, int position);
    }
    public interface onShowToast {
        void  onShowToast(String msg);
    }
}
