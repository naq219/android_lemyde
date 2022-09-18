package com.project.shop.lemy.nhacviec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.project.shop.lemy.BuildConfig;
import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.SqlTaskGeneral;
import com.project.shop.lemy.Task.TaskGeneralTh;
import com.project.shop.lemy.adapter.NhacViecAdapter;
import com.project.shop.lemy.common.SprSupport;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NhacViecViewLayout {
    private  String codenv="";
    public long timeButtonCLose=0;
    private Context context;
    RecyclerView lv;
    View v;
    NhacViecAdapter adapter;
    View dl_yes,dl_no,vg_dl,root;
    TextView dl_title, tvKiemTra;
    View vgOpen;
    //check lien tuc
    int countClt=-1;
    long lastCheck=0;
    private NhacViecService nvService;

    public NhacViecViewLayout(Context context, NhacViecService nvService){
        codenv = SPRSupport.getString("codenv",context,"");
        this.nvService = nvService;
        this.context = context;
        v= LayoutInflater.from(context).inflate(R.layout.nhacviec_widget, null);
        lv = v.findViewById(R.id.lv);
        tvKiemTra = v.findViewById(R.id.tmp1);
        dl_title= v.findViewById(R.id.dl_title);
        dl_yes= v.findViewById(R.id.dl_yes);
        dl_no= v.findViewById(R.id.dl_no);
        vg_dl= v.findViewById(R.id.vg_dl);
        root= v.findViewById(R.id.root);
        vgOpen=v.findViewById(R.id.vgOpen);
        lv.setLayoutManager(new LinearLayoutManager(context));
        adapter= new NhacViecAdapter(context);
        root.setVisibility(View.GONE);
        adapter.onNewDataAvaiable(object -> {
            root.setVisibility(View.VISIBLE);
        });
        adapter.onClickDone(object -> {
            BObject oj = (BObject) object;
            showDialogXoa(oj.get("id"));
        });
        adapter.onNeedReload(object -> {
            checkViec();
        });

        lv.setAdapter(adapter);
        TimerTask taskCheckOpenView =  new TimerTask() {
            @Override
            public void run() {



                root.post(() -> {
                    if(countClt>0 && Calendar.getInstance().getTimeInMillis()- lastCheck>5000){
                        lastCheck= Calendar.getInstance().getTimeInMillis();
                        countClt-=5;
                        tvKiemTra.setText(String.format("----%s---",countClt));
                        checkViec();


                    }
                    if (timeButtonCLose>0 && Calendar.getInstance().getTimeInMillis()-timeButtonCLose>3000){
                        // hide nút close
                        v.findViewById(R.id.root).setVisibility(View.GONE);
                        timeButtonCLose=0;
                    }
                });
                if (NhacViecService.tagOpenView){

                    root.post(() -> {
                        checkViec();
                        root.setVisibility(View.VISIBLE);
                        vgOpen.setVisibility(View.VISIBLE);
                        nvService.enableKeyPadInput(true);
                    });


                    NhacViecService.tagOpenView=false;
                }
            }
        };
        new Timer().schedule(taskCheckOpenView,0,1000);

        TimerTask task =  new TimerTask() {
            @Override
            public void run() {
                checkViec();
            }
        };
        new Timer().schedule(task,1000,3*60*1000);
        checkViec();

        tvKiemTra.setOnClickListener(view -> {
            countClt=120;
        });

    }


    private void xoaNhacNho(Object id) {
        String sql= SqlTaskGeneral.nhacviecSetActive0(id);
        TaskGeneralTh.exeTaskStatement(context,sql,"sdasd",123,new Model());
        lv.postDelayed(() -> {checkViec();},2000);
    }

    private void checkViec() {
        String sql="SELECT * FROM "+ BuildConfig.db_nhacviec+ ".task WHERE active =1 and type =3 ";

        Model model=new Model(){
            @Override
            public void onSuccess(int taskType, Object data, String msg, Integer queue) {
                super.onSuccess(taskType, data, msg, queue);
                List<BObject> ojs = BObject.JsonArray2List((JSONArray) data);
                adapter.setData(ojs,v.findViewById(R.id.root));

            }

            @Override
            public void onFail(int taskType, String msg, Integer queue) {
                super.onFail(taskType, msg, queue);

            }
        };
        TaskGeneralTh.exeTaskGetApi(model,context,sql);
    }

    Object curIdDelete;
    protected void showDialogXoa(Object id){

        if (!SprSupport.isAdmin(context)){
            Toast.makeText(context,"Bạn không có quyền xóa task", Toast.LENGTH_LONG).show();
            return;
        }
        curIdDelete=id;
        vg_dl.setVisibility(View.VISIBLE);
        dl_title.setText("Xóa nhắc nhở ("+id+")");
        dl_yes.setOnClickListener(view -> {
            xoaNhacNho(curIdDelete);
            closeDialog();
        });
        dl_no.setOnClickListener(view -> {

            closeDialog();
        });
    }
    protected void closeDialog(){
        vg_dl.setVisibility(View.GONE);
    }

    private boolean isAdminNhapDon() {
        String codenv= SPRSupport.getString("codenv",context,"nn");
        String codeCheck="#ahoachat#naq#lehoa#";
        if (!codeCheck.contains("#"+codenv+"#")){

            return false;
        }
        return true;
    }


    public  View getView(){

        return v;
    }
}
