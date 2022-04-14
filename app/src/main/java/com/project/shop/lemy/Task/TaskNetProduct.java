package com.project.shop.lemy.Task;

import android.content.Context;

import com.project.shop.lemy.Net.NetCustomer;
import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetProduct;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.model.TaskParams;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;

public class TaskNetProduct extends BaseTask {

    NetData netData;
    public TaskNetProduct() {
        super();
    }

    public TaskNetProduct(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

    public TaskNetProduct(BaseModel model, Context context) {
        super(model, context);
    }

    public static void extaskSeachName(String text,Context context,int queue, BaseModel baseModel) {
        String type="name";
        text=""+text;
        if (text.length()>1&&text.charAt(0)=='.'){
            type="id";
            text=text.substring(1);
        }

        if (text.length()>1&&text.charAt(0)=='#'){
            type="ean";
            text=text.substring(1);
        }
       extaskSearch(text,type,context,queue,baseModel);
    }
    public static void extaskSearch(String text,String field,Context context,int queue, BaseModel baseModel){
        TaskNetProduct taskNet = new TaskNetProduct(baseModel, TaskType.SEARCH_PRODUCT_V2 , context);
        taskNet.setTaskParram("text", text);
        taskNet.setTaskParram("field", field);
        taskNet.setTaskParram("queue", queue);
        taskNet.exe();
    }

    public static void extaskUpdate(BaseModel baseModel, String product_id, BaseObject oj,Context context) {
        TaskNetProduct task = new TaskNetProduct(baseModel,TaskType.UPDATE_PRODUCT_V2,context);
        task.setTaskParram("product_id",product_id);
        task.setTaskParram("oj",oj);
        task.exe();
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

            case TaskType.SEARCH_PRODUCT_V2:
                String text= (String) getTaskParram("text");
                String field = (String) getTaskParram("field");
                String orderby = ""+(String) getTaskParram("orderby");
                String limit = ""+(String) getTaskParram("orderby");
                String page = ""+(String) getTaskParram("orderby");
                netData = NetProduct.seachProduct(text,field,orderby,limit,page);
                return statusByNetdata(netData,getTaskParram("queue"));

            case TaskType.UPDATE_PRODUCT_V2:
                String product_id= getTaskParramString("product_id");
                BaseObject oj= getTaskParramBaseObject("oj");
               netData= NetProduct.update(product_id,oj);
               return statusByNetdata(netData,null);
        }


        return super.doInBackground(params);
    }

    private Boolean statusByNetdata(NetData netData,Object queue) {

        if (netData.isFailed()) {
            msg = netData.getMsg()+"queue="+queue;
            return TASK_FAILED;
        }
        msg=""+queue;
        setDataReturn(netData.getData());
        return TASK_DONE;
    }


}
