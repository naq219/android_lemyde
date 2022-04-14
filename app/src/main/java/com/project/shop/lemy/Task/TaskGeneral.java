package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.BuildConfig;

public class TaskGeneral extends Task {
    public  static final int TASK_INSERT_CONNECTLEMY=TaskType.TASKTYPE_GENERAL+1;
    public  static final int TASK_GET_CONNECTLEMY=TaskType.TASKTYPE_GENERAL+2;
    public static final String CONNECT_LEMY_INSERT="http://connect.lemyde.com/insert";
    public static final String CONNECT_LEMY_GET = "http://connect.lemyde.com/get";
    public TaskGeneral(){

    }

    public TaskGeneral(Model baseModel, int taskType, Context context) {
        super(baseModel,taskType,context);
    }

    public static void exeTaskInsertApi(Context context, String sql, Integer queue, Model model) {
        if (sql.contains(";::;"))
            sql=sql.replace(";::;", BuildConfig.db_name);
        TaskGeneral taskGeneral = new TaskGeneral(model,TASK_INSERT_CONNECTLEMY,context);

        TaskParams param= new TaskParams(sql);
        taskGeneral.setQueue(queue);
        taskGeneral.exe(param);
    }

    public static void exeTaskGetApi(Model model, Context context, String sql) {
        if (sql.contains(";::;"))
            sql=sql.replace(";::;",  BuildConfig.db_name);
        TaskGeneral taskGeneral = new TaskGeneral(model,TASK_GET_CONNECTLEMY,context);

        TaskParams param= new TaskParams(sql);
        taskGeneral.exe(param);
    }


    @Override
    protected Dataget doInBackground(TaskParams... params) {

        switch (taskType){
            case TASK_INSERT_CONNECTLEMY:
                return apiInsertDb(params[0].getTaskParamDefaultString());

            case TASK_GET_CONNECTLEMY:
                return apiGetDb(params[0].getTaskParamDefaultString());
        }


        return super.doInBackground(params);
    }

    private Dataget apiGetDb(String sql) {
        String url= CONNECT_LEMY_GET;
        Dataget data = NetSupport.getInstance().simpleGet(url+"?sql=" + NetSupport.encodeUrl(sql), null);

        return data;
    }

    private Dataget apiInsertDb(Object taskParamDefaultString) {
        String sql= taskParamDefaultString.toString();
        String url= CONNECT_LEMY_INSERT;//+"?sql="+sql;

        Dataget data = NetSupport.getInstance().simpleGet(url+"?sql=" + NetSupport.encodeUrl(sql), null);

        
        return data;

    }
}
