package com.project.shop.lemy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.shop.lemy.R;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Ducqv on 7/4/2018.
 */

public class SimpleAdapterTextImage extends RecyclerView.Adapter<SimpleAdapterTextImage.ViewHolder> {

    private JSONArray jas= new JSONArray();
    private int resource;
    private Context context;
    onClickRowListen listenBack;
    Integer ridClickBack=null;
    private onClickRowListen listenLongClick;

    public SimpleAdapterTextImage(Context context, int resource) {
        this.context = context;
        this.resource = resource;
    }

    public void init(Integer ridClickBack){
        this.ridClickBack = ridClickBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(resource, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        JSONObject jo= jas.optJSONObject(position);
        if (holder.tv!=null)
        holder.tv.setText(jo.optString("text"));

        if (holder.img!=null){
            Object valueImg = jo.opt("img");
            if (valueImg instanceof Drawable) holder.img.setImageDrawable((Drawable) valueImg);
            if (valueImg instanceof Integer) holder.img.setImageResource((Integer) valueImg);
        }




            if (listenBack!=null){
                if (ridClickBack==null){
                    if (holder.root!=null) ridClickBack= holder.root.getId();
                    else return;
                }
                 holder.itemView.findViewById(ridClickBack).setOnClickListener(view -> {
                    listenBack.onClickRow(position,jo);
                });
            }

        if (listenLongClick!=null){
            if (ridClickBack==null){
                if (holder.root!=null) ridClickBack= holder.root.getId();
                else return;
            }
            holder.itemView.findViewById(ridClickBack).setOnLongClickListener(view -> {
                listenLongClick.onClickRow(position,jo);
                return true;
            });
        }


    }

    public void setData(JSONArray jas){
        this.jas= jas;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return jas.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private ImageView img;
        private View root;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            tv = itemView.findViewById(R.id.tv);
            img=itemView.findViewById(R.id.img);
            root= itemView.findViewById(R.id.root);


        }
    }


    public void setOnclick(Integer ridClickBack ,onClickRowListen onclick) {
       this.listenBack=onclick;
       this.ridClickBack=ridClickBack;
    }
    public void setOnLongclick(Integer ridClickBack ,onClickRowListen onclick) {
        this.listenLongClick=onclick;
        this.ridClickBack=ridClickBack;
    }

    public interface onClickRowListen {
        void  onClickRow(int position,Object dataPost);
    }

    public  View initView(){
        RecyclerView lv = new RecyclerView(context);
        lv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        lv.setHasFixedSize(true);
        lv.setLayoutManager(new LinearLayoutManager(context));
       // SimpleAdapterTextImage adapter= new SimpleAdapterTextImage(context,resource);
        lv.setAdapter(this);


        return lv;
    }

}
