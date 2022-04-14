package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;

/**
 * Created by Ducqv on 7/4/2018.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    String[] vl;
    private int resource;
    private Context context;
    onClickRowListen listenBack;

    public SimpleAdapter(Context context,String[] vl,int resource) {
        this.context = context;
        this.vl=vl;
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
        holder.tv.setText(vl[position]);
        holder.mRoot.setOnClickListener(view -> {
            listenBack.onClickRow(position);
        });
    }

    @Override
    public int getItemCount() {
        return vl.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private View mRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            mRoot=itemView.findViewById(R.id.mroot);

        }
    }

    public void setData(String [] vl) {
       this.vl=vl;
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return vl[position];
    }



    public void setOnclick(onClickRowListen onclick) {
       this.listenBack=onclick;
    }

    public interface onClickRowListen {
        void  onClickRow(int position);
    }
}
