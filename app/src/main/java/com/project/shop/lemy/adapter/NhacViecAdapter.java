package com.project.shop.lemy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.helper.MySpr;
import com.project.shop.lemy.listener.ListenBack;
import com.project.shop.lemy.nhacviec.NhacViecService;
import com.telpoo.frame.utils.Mlog;
import com.telpoo.frame.utils.SPRSupport;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ducqv on 7/5/2018.
 */

public class NhacViecAdapter extends RecyclerView.Adapter<NhacViecAdapter.ViewHolder> {
    private List<BObject> list = new ArrayList<>();
    Context context;
    private ListenBack listenClickDone;
    private ListenBack listenNewData;
    private ListenBack listenReload;
    HashMap<String, String> hm = new HashMap<>();
    private long timeResetHm=0;
    private String codenv= "";
    boolean isAHoa=false;
    private int colorXanh=Color.parseColor("#A8009688");

    public NhacViecAdapter( Context context) {
        this.context = context;
        codenv = SPRSupport.getString("codenv",context,"");
        if (codenv.equals("ahoachat")||codenv.equals("naq")) isAHoa=true;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_nhacviec, parent, false);
        return new NhacViecAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String content = list.get(position).getAsString("content");
        String note = ""+list.get(position).getAsString("note","");
        String note1=note.toLowerCase();
        String content1=content.toLowerCase();
        holder.tvDone.setVisibility(View.GONE);
        if(note1.contains("dhm")){
            //String dho= note1.substring(note1.lastIndexOf("dhm"), note1.indexOf("."));
            String str = note1.replaceAll("\\D+","");
            str="dhm"+str;
            if (hm.containsKey(str)){
                holder.tvDone.setText(""+hm.get(str));
                holder.tvDone.setVisibility(View.VISIBLE);
                if (note.contains("#ahoachat")||note.contains("#naq")){
                    holder.tvDone.setBackgroundColor(colorXanh);
                }
                else holder.tvDone.setBackgroundColor(Color.GRAY);
            }
        }

        if(content1.contains("dhm")){
            String dho= content1.substring(content1.lastIndexOf("dhm"));
            int lastDot= dho.indexOf(".");
            if (lastDot>0)
                dho= dho.substring(0,lastDot);
            if (hm.containsKey(dho)){
                holder.tvDone.setText(""+hm.get(dho));
                holder.tvDone.setVisibility(View.VISIBLE);
            }
        }

        if(!note.toLowerCase().contains("dhm")&&!content1.contains("money_received") &&!content1.contains("pick_money")){
            holder.lnroot.setBackgroundColor(Color.parseColor("#F44336"));
        }
        else   holder.lnroot.setBackgroundColor(Color.parseColor("#ffffff"));


        holder.content.setText("("+list.get(position).get("id")+") "+content);

        holder.tvNote.setText(note);
//        holder.content.setOnClickListener(view -> {
//            String check= list.get(position).convert2NetParramsNotEncode();
//            if (check.toLowerCase().contains("dhm")){
//                String dhm= ""+StringUtils.substringBetween(check,"DHM","#");
//
//                if (dhm.length()>0)
//                    ChiTietDonHangActivity.startActivity(context,dhm);
//            }
//        });

        holder.btnDone.setOnClickListener(view -> {
            listenClickDone.OnListenBack(list.get(position));
        });

        holder.content.setOnClickListener(view -> {
            holder.viewNote.setVisibility(holder.viewNote.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
        });

        holder.tvGui.setOnClickListener(view -> {
            holder.viewNote.setVisibility(View.GONE);
            String vl = holder.edNote.getText()+"";
            if (vl.length()==5&& NumberUtils.isCreatable(vl)) vl = "dhm"+vl;
            if (vl.length()==0) return;
            vl=vl+" #"+codenv;
            String sqlNote="UPDATE "+ BuildConfig.db_nhacviec+
            ".task t SET note = CONCAT(IFNULL(note,''),'\\n%s') WHERE id = "+list.get(position).get("id");

            sqlNote= String.format(sqlNote,vl);
            Mlog.D(sqlNote);
            TaskGeneralTh.exeTaskInsertApi(context,sqlNote,1,new Model());
            holder.edNote.postDelayed(() -> {
                listenReload.OnListenBack("");
            },1000);
        });

    }

    public void onClickDone(ListenBack listenClickDone){
        this.listenClickDone = listenClickDone;
    }
    public void onNeedReload(ListenBack listenReload){
        this.listenReload = listenReload;
    }

    public void onNewDataAvaiable(ListenBack listenNewData){
        this.listenNewData = listenNewData;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    int lastNumberUseTag=1;
    public void setData(List<BObject> ojs1,View root) {
        if (MySpr.isVietCombank(context)) return;
//        if (Calendar.getInstance().getTimeInMillis()-timeResetHm>3*60*1000){
//            timeResetHm= Calendar.getInstance().getTimeInMillis();
//            hm = new HashMap<>();
//        }
            int a=1;

        List<BObject> ojs= new ArrayList<>();
        for (int i = 0; i < ojs1.size(); i++) {
            BObject tmp1 = ojs1.get(i);
            String note = "" + tmp1.getAsString("note");
            if(!note.toLowerCase().contains("dhm")&&!tmp1.getAsString("content").contains("money_received") &&!tmp1.getAsString("content").contains("pick_money")){
                ojs.add(0,tmp1);
            }
            else ojs.add(tmp1);
        }

        for (BObject oj :
                ojs) {
            String note = "" + oj.getAsString("note");
            String note1 = note.toLowerCase();
            if (note1.contains("dhm")) {
                String dh0 = "dhm"+note1.replaceAll("\\D+","");
                if (!hm.containsKey(dh0)){

                    while (hm.containsValue(""+lastNumberUseTag)){
                        lastNumberUseTag++;
                    }

                    hm.put(dh0, ""+lastNumberUseTag);
                    lastNumberUseTag++;
                }


            }
        }




        String check1="1";
        String check2="1";
        for (BObject oj :
                list) {
            check1+=oj.convert2NetParramsNotEncode();
        }
        for (BObject oj2 :
                ojs) {
            check2+=oj2.convert2NetParramsNotEncode();
        }
        if (check1.equals(check2)) return; // k co gi moi


        this.list=ojs;
        notifyDataSetChanged();

        if (check1.length()>0){
            root.setVisibility(View.VISIBLE);
            listenNewData.OnListenBack("");
            NhacViecService.tagOpenView=true;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content, tvNote,tvGui,tvDone;
        private View btnDone,viewNote,lnroot;
        private EditText edNote;

        public ViewHolder(View itemView) {
            super(itemView);
            lnroot= itemView.findViewById(R.id.lnroot);
            content = itemView.findViewById(R.id.content);
            tvNote= itemView.findViewById(R.id.tvNote);
            btnDone = itemView.findViewById(R.id.btnDone);
            edNote=itemView.findViewById(R.id.edNote);
            tvGui=itemView.findViewById(R.id.tvGui);
            viewNote=itemView.findViewById(R.id.viewNote);
            tvDone=itemView.findViewById(R.id.tvDone);

        }

    }


}
