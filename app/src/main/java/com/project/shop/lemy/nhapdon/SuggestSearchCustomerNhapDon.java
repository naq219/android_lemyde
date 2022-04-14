package com.project.shop.lemy.nhapdon;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.shop.lemy.R;
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

public class SuggestSearchCustomerNhapDon extends RecyclerView.Adapter<SuggestSearchCustomerNhapDon.ViewHolder> {

    private  List<BaseObject> ojs;
    //String[] vl;
    private int resource;
    private Context context;
    onClickRowListen listenBack;

    DecimalFormat nf =(DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());


    public SuggestSearchCustomerNhapDon(Context context, List<BaseObject> ojs, int resource) {
        DecimalFormatSymbols symbols = nf.getDecimalFormatSymbols();
        symbols.setMonetaryDecimalSeparator('.');
        nf.setDecimalFormatSymbols(symbols);

        this.context = context;
        this.ojs = ojs;
        List<BaseObject> o= new ArrayList<>();
        if (ojs==null) this.ojs=o;
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
        holder.tv.setText(oj.get(ProductObj.name)+" ("+ MoneySupport.moneyEndK(oj.get(ProductObj.retail_price))+")");
        holder.edGiaBan.setText(nf.format(Long.parseLong(oj.get(ProductObj.retail_price))));


        holder.mRoot.setOnClickListener(view -> {
            holder.parentThem.setVisibility(holder.parentThem.getVisibility()==View.GONE?View.VISIBLE: View.GONE);

        });
        holder.btnCong.setOnClickListener(view -> {
            String t1= ""+holder.edSl.getText().toString();
            if (t1.length()==0) t1="0";
            int oldSl= Integer.parseInt(t1)+1;
            holder.edSl.setText(String.valueOf(oldSl));
        });
        holder.btnTru.setOnClickListener(view -> {
            String t1= ""+holder.edSl.getText().toString();
            if (t1.length()==0) t1="0";
            int oldSl= Integer.parseInt(t1)-1;
            if (oldSl>-1)
            holder.edSl.setText(String.valueOf(oldSl));
        });

        holder.btnThem.setOnClickListener(view -> {
            if (holder.edSl.getText().toString().isEmpty()){
                Toast.makeText(context,"Chưa nhập số lượng!",Toast.LENGTH_SHORT).show();
                return;
            }
            String giaBan = holder.edSl.getText().toString().replaceAll("\\D+","");
            if (giaBan.isEmpty()){
                Toast.makeText(context,"ĐỊnh dạng số tiền ko đúng!",Toast.LENGTH_SHORT).show();
                return;
            }

            BaseObject oj1= ojs.get(position).clone();
            oj1.set("new_sl",holder.edSl.getText().toString());
            oj1.set("new_giaban",holder.edGiaBan.getText().toString());

            listenBack.onClickRow(oj1,position);
        });
    }

    @Override
    public int getItemCount() {
        return ojs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private View mRoot,parentThem,btnCong,btnTru,btnThem;
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

    public interface onClickRowListen {
        void  onClickRow(BaseObject oj, int position);
    }
}
