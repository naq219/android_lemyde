package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.model.BObject;
import com.lemy.telpoo2lib.model.Model;
import com.lemy.telpoo2lib.model.Task;
import com.lemy.telpoo2lib.model.TaskParams;
import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.Net.MyUrl2;
import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetProduct;
import com.project.shop.lemy.Net.NetSupport2;

public class TaskProductv2 extends Task {
    public static final int CREATE= TaskType.start_taskproductv2+1;
    public static final int UPDATE = TaskType.start_taskproductv2 + 2;
    public static final int SEARCH = TaskType.start_taskproductv2 + 3;

    public TaskProductv2(Model baseModel, int taskType, Context context) {
        this.baseModel = baseModel;
        this.taskType = taskType;
        this.context = context;
    }

    public static void update(Object id,BObject ojUpdate,Context context, Model model) {
        TaskProductv2 task = new TaskProductv2(model,UPDATE,context);

        TaskParams param=new TaskParams();
        param.put("oj",ojUpdate);
        param.put("id",id);
        task.exe(param);
    }

    @Override
    protected Dataget doInBackground(TaskParams... params) {
        TaskParams param0= params[0];
        switch (taskType){
            case CREATE:
                return createProduct(param0);
            case UPDATE:
                return updateProduct(param0);
            case SEARCH:
                return searchProduct(param0);
        }

        return null;
    }

    public static TaskProductv2 searchProduct(String text,String orderBy,Integer limit,Integer page,Context context,Model model ){
        TaskProductv2 taskProductv2 = new TaskProductv2(model,SEARCH,context);
        TaskParams param =new TaskParams();
        String field="name";
        if (text.length()>1 &&text.charAt(0)=='.'){
            text=text.substring(1);
            field="id";
        }
        param.put("field",field);
        param.put("text",text);
        param.put("orderby",orderBy);
        param.put("limit",limit);
        param.put("page",page);
        taskProductv2.execute(param);
        return taskProductv2;

    }
    private Dataget searchProduct(TaskParams param0) {

        String field = param0.getTaskParramString("field");
        String text = param0.getTaskParramString("text");
        String orderBy= ""+param0.getTaskParramString("orderby");
        String limit= ""+param0.get("limit");
        String page= ""+param0.get("page");

        NetData netData = NetProduct.seachProduct(text, field,orderBy,limit,page);
        Dataget dg= new Dataget();
        dg.setData(netData.getData());
        dg.setCode(netData.getcode());
        dg.setMessage(netData.getMsg());

        return dg;
    }

    private Dataget updateProduct(TaskParams param0) {
        BObject oj = param0.getTaskParramBaseObject("oj");
        String productId= param0.getTaskParramString("id");
        return updateProduct(productId,oj);
    }

    public static Dataget updateProduct (Object productId, BObject ojUpdate){
        String url= NetSupport2.getBaseUrlv2()+ MyUrl2.product+productId;
        String pa=ojUpdate.convert2NetParrams();
        if (pa.charAt(0)=='&') pa=pa.substring(1);
        return NetSupport.getInstance().request("PUT",url,pa);
    }

    private Dataget createProduct(TaskParams param0) {
        BObject oj = param0.getTaskParramBaseObject("oj");
        String url= NetSupport2.getBaseUrlv2()+ MyUrl2.product;
        return NetSupport.getInstance().request("POST",url,oj.convert2NetParramsNotEncode());

    }
}
