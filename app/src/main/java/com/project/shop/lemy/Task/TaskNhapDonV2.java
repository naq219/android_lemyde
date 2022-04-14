package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetConfig;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.Net.MyUrl2;
import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetProduct;
import com.project.shop.lemy.Net.NetSupport2;

public class TaskNhapDonV2 extends Task {
    public static final int NHAPDON= TaskType.start_taskNhapDonV2+1;


    public TaskNhapDonV2(Model baseModel, int taskType, Context context) {
        this.baseModel = baseModel;
        this.taskType = taskType;
        this.context = context;
    }

    @Override
    protected Dataget doInBackground(TaskParams... params) {
        TaskParams param0= params[0];
        switch (taskType){
            case NHAPDON:
                return nhapDon(param0);

        }

        return super.doInBackground(params);
    }



    private Dataget nhapDon(TaskParams param0) {
        String url= NetSupport2.getBaseUrlv2()+MyUrl2.nhapdon;

        return NetSupport.getInstance().request("POST",url,param0.getTaskParamDefaultString());

    }
}
