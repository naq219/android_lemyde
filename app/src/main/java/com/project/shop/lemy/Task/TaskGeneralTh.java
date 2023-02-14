package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.BuildConfig;
import com.telpoo.frame.utils.Mlog;

public class TaskGeneralTh extends Task {
    public  static final int TASK_INSERT=TaskType.TASKTYPE_GENERALTH+1;
    public  static final int TASK_GET=TaskType.TASKTYPE_GENERALTH+2;
    public  static final int TASK_STATEMENT=TaskType.TASKTYPE_GENERALTH+3;

    public TaskGeneralTh(){

    }

    public TaskGeneralTh(Model baseModel, int taskType, Context context) {
        super(baseModel,taskType,context);
    }

    public static void exeTaskInsertApi(Context context, String sql, Integer queue, Model model) {
        if (sql.contains(";::;"))
            sql=sql.replace(";::;",  BuildConfig.db_name);
        TaskGeneralTh taskGeneral = new TaskGeneralTh(model,TASK_INSERT,context);

        TaskParams param= new TaskParams(sql);
        taskGeneral.setQueue(queue);
        taskGeneral.exe(param);
    }

    public static void exeTaskNhacViecHeThong(Context context,String content){
        exeTaskNhacViecSimple(context,0,content,null,null);
    }
    public static void exeTaskNhacViecSimple(Context context,Object type,String content,String param1,String param2){
        String sql=SqlTaskGeneral.INSERT_NHACVIEC_SIMPLE;
        sql=String.format(sql,type,content,param1,param2);

        Mlog.D("naq2"+sql);

        exeTaskStatement(context,sql,"312sd",1123,new Model());
    }

    public static void exeTaskStatement(Context context, String sql, String split, Integer queue, Model model) {
        if (sql.contains(";::;"))
            sql=sql.replace(";::;",  BuildConfig.db_name);
        TaskGeneralTh taskGeneral = new TaskGeneralTh(model,TASK_STATEMENT,context);

        TaskParams param= new TaskParams();
        param.put("sql",sql);
        param.put("split",split);
        taskGeneral.setQueue(queue);
        taskGeneral.exe(param);
    }

    public static void exeTaskGetApi(Model model, Context context, String sql) {
        if (sql.contains(";::;"))
            sql=sql.replace(";::;",  BuildConfig.db_name);
        TaskGeneralTh taskGeneral = new TaskGeneralTh(model,TASK_GET,context);

        TaskParams param= new TaskParams(sql);
        taskGeneral.exe(param);
    }


    @Override
    protected Dataget doInBackground(TaskParams... params) {

        switch (taskType){
            case TASK_INSERT:
                return apiInsertDb(params[0].getTaskParamDefaultString());

            case TASK_GET:
                return apiGetDb(params[0].getTaskParamDefaultString());

            case TASK_STATEMENT:
                return apiStatement(params[0]);
        }


        return super.doInBackground(params);
    }

    private Dataget apiStatement(TaskParams param) {
        String sql= param.getTaskParramString("sql");
        String split= param.getTaskParramString("split");
        String url= MyUrl11.CONNECT_HETHONG_STATEMENT;
        String murl=url+"?sql=" + NetSupport.encodeUrl(sql)+"&split="+split;
        Dataget data = NetSupport.getInstance().simpleGet(murl, null);

        return data;

    }



    private Dataget apiGetDb(String sql) {
        String url= MyUrl11.CONNECT_HETHONG_GET;

        Dataget data = NetSupport.getInstance().simpleGet(url+"?sql=" + NetSupport.encodeUrl(sql), null);


        return data;
    }

    private Dataget apiInsertDb(Object taskParamDefaultString) {
        String sql= taskParamDefaultString.toString();
        String url= MyUrl11.CONNECT_HETHONG_INSERT;//+"?sql="+sql;

        Dataget data = NetSupport.getInstance().simpleGet(url+"?sql=" + NetSupport.encodeUrl(sql), null);

        
        return data;

    }

    private class MyUrl11 {
        public static final String CONNECT_HETHONG_INSERT ="http://connectht.lemyde.com/insert";
        public static final String CONNECT_HETHONG_STATEMENT = "http://connectht.lemyde.com/sql/statement";
        public static final String CONNECT_HETHONG_GET = "http://connectht.lemyde.com/get";
    }
}
