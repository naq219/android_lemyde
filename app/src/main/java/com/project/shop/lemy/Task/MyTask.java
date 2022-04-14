package com.project.shop.lemy.Task;

import android.content.Context;

import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;

/**
 * Created by naq on 05/04/2015.
 */
public class MyTask extends BaseTask implements TaskType {
    public static final int START_TASKNET_GENERAL=100;
    public MyTask() {

    }

    public MyTask(BaseModel baseModel, Context context) {
        super(baseModel, context);
    }

    public MyTask(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

}
