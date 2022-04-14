package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.Net.MyUrl2;
import com.project.shop.lemy.Net.NetSupport2;

import org.json.JSONArray;

public class TaskStock extends Task {
    public static final int ADD_OR_CHANGE= TaskType.start_taskStock+1;


    public TaskStock(Model baseModel, int taskType, Context context) {
        this.baseModel = baseModel;
        this.taskType = taskType;
        this.context = context;
    }

    @Override
    protected Dataget doInBackground(TaskParams... params) {
        TaskParams param0= params[0];
        switch (taskType){
            case ADD_OR_CHANGE:
                return addOrChange(param0);

        }

        return super.doInBackground(params);
    }



    private Dataget addOrChange(TaskParams param0) {
        String url= NetSupport2.getBaseUrlv2()+MyUrl2.stock_addorchange;

        return NetSupport.getInstance().request("POST",url,param0.getTaskParamDefaultString());

    }

    public static void exeAddOrChange(JSONArray data, Model model, Context mcontext){
        if (model==null) model = new Model();
        TaskStock taskStock = new TaskStock(model,ADD_OR_CHANGE,mcontext);
        TaskParams param=new TaskParams();
        param.setTaskParramDeFault(data);
        taskStock.exe(param);
    }
}
