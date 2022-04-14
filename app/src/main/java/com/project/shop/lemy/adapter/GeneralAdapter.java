package com.project.shop.lemy.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.telpoo.frame.object.BaseObject;

import java.util.List;

/**
 * Created by Ducqv on 7/4/2018.
 */

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.ViewHolder> {

    private List<BaseObject> ojs;
    private String fieldText;
    private int resource;
    private Context context;
    onClickRowListen listenBack;

    public GeneralAdapter(Context context, List<BaseObject> ojs,String fieldText, int resource) {
        this.context = context;
        this.ojs = ojs;
        this.fieldText = fieldText;
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
        holder.tv.setText(ojs.get(position).get(fieldText));
        holder.mRoot.setOnClickListener(view -> {
            if (listenBack!=null)
            listenBack.onClickRow(position);
        });

        if (holder.btnDelete!=null){
            holder.btnDelete.setOnClickListener(view -> {
                ojs.remove(position);
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return ojs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private View mRoot,btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            mRoot=itemView.findViewById(R.id.mroot);
            btnDelete=itemView.findViewById(R.id.btnDelete);

        }
    }

    public void setData(List<BaseObject> ojs) {
       this.ojs=ojs;
        notifyDataSetChanged();
    }

    public void addData(BaseObject oj) {
        ojs.add(0,oj);
        notifyDataSetChanged();
    }


    public BaseObject getItem(int position) {
        return ojs.get(position);
    }



    public void setOnclick(onClickRowListen onclick) {
       this.listenBack=onclick;
    }

    public interface onClickRowListen {
        void  onClickRow(int position);
    }
}
