package com.project.shop.lemy.Task;

import android.content.Context;

import com.project.shop.lemy.Net.NetCustomer;
import com.project.shop.lemy.Net.NetData;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.model.TaskParams;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;

public class TaskNetCustomer extends BaseTask {

    NetData netData;
    public TaskNetCustomer() {
        super();
    }

    public TaskNetCustomer(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

    public TaskNetCustomer(BaseModel model, Context context) {
        super(model, context);
    }

    public static void extaskSeachPhone(String text,Context context,int queue, BaseModel baseModel) {
       extaskSearch(text,"phone",context,queue,baseModel);
    }
    public static void extaskSeachId(String text,Context context,int queue, BaseModel baseModel) {
        extaskSearch(text,"id",context,queue,baseModel);
    }

    public static void extaskSearch(String text,String field,Context context,int queue, BaseModel baseModel){
        TaskNetCustomer taskNet = new TaskNetCustomer(baseModel,TaskType.SEARCH_CUSTOMER_V2 , context);
        taskNet.setTaskParram("text", text);
        taskNet.setTaskParram("field", field);
        taskNet.setTaskParram("queue", queue);
        taskNet.exe();
    }

    @Override
    protected Boolean doInBackground(TaskParams... params) {
        if (context == null) {
            return TASK_FAILED;
        }
//        if (context != null && !BaseNetSupport.isNetworkAvailable(context)) {
//            msg = "Không có kết nối internet";
//            return TASK_FAILED;
//        }

        switch (taskType){

            case TaskType.SEARCH_CUSTOMER_V2:
                String text= (String) getTaskParram("text");
                String field = (String) getTaskParram("field");
                netData = NetCustomer.seachCustomer(text,field);
                return statusByNetdata(netData,getTaskParram("queue"));
        }


        return super.doInBackground(params);
    }

    private Boolean statusByNetdata(NetData netData,Object queue) {
        if (netData.isFailed()) {
            msg = netData.getMsg();
            return TASK_FAILED;
        }
        msg=""+queue;
        setDataReturn(netData.getData());
        return TASK_DONE;
    }


}
