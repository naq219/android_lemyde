package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.utils.ListenBack;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.object.BaseObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by naq on 11/17/18.
 */

public class TaskSupport {
    @Deprecated
    public static void callDetailOrder(String orderId, BaseModel baseModel, Context context){
        BaseObject oj = new BaseObject();
        oj.set("id",orderId);


        TaskNet taskNet = new TaskNet(baseModel, TaskType.TASK_DETAILORDER, context);
        taskNet.setTaskParram("parram", oj);
        taskNet.exe();

    }

    public static void callDetailOrderV2(String orderId, Context context, ListenBack listenBack){
        BaseObject oj = new BaseObject();
        oj.set("id",orderId);

        BaseModel model = new BaseModel(){
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                listenBack.OnListenBack(data);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
                listenBack.OnListenBack(null);

            }
        };

        TaskNet taskNet = new TaskNet(model, TaskType.TASK_DETAILORDER, context);
        taskNet.setTaskParram("parram", oj);
        taskNet.exe();

    }
}
