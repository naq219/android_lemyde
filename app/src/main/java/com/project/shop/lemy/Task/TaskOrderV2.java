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
import com.project.shop.lemy.Net.NetSupport2;
import com.project.shop.lemy.donhang.ChiTietDonHangActivity;
import com.telpoo.frame.utils.Mlog;

public class TaskOrderV2 extends Task {
    public static final int UPDATE_THEO_SP= TaskType.start_tasknetorderv2+1;
    private static final int TASK_TACH_DONV2 = TaskType.start_tasknetorderv2+2;
    private static final int TASK_TACH_DON_AUTO = TaskType.start_tasknetorderv2 + 3;
    private static final int UPDATE = TaskType.start_tasknetorderv2 + 4;
    private static final int SEARCH = TaskType.start_tasknetorderv2 + 5;

    public TaskOrderV2(Model baseModel, int taskType, Context context) {
        this.baseModel = baseModel;
        this.taskType = taskType;
        this.context = context;
    }

    public static void exeTaskTachDon(Model model, Context context, String orderId, String idsTick, String note, String money_received) {
        BObject oj = new BObject();
        oj.set("order_id",orderId);
        if (note!=null)
            oj.set("note",note);
        if (money_received!=null)
            oj.set("money_received",money_received);
        oj.set("products",idsTick);


        TaskOrderV2 task = new TaskOrderV2(model,TASK_TACH_DONV2,context);

        TaskParams param= new TaskParams();
        param.put("oj",oj);
        task.exe(param);
    }

    public static void exeTaskTachDonAuto(Model model, Context context, String orderId,String note,String noteOld,  String money_received) {
        BObject oj = new BObject();
        oj.set("order_id",orderId);
        if (note!=null)
            oj.set("note",note);
        if (money_received!=null)
            oj.set("money_received",money_received);
        if (noteOld!=null&&noteOld.length()>0)
            oj.set("note_old",noteOld);

        TaskOrderV2 task = new TaskOrderV2(model,TASK_TACH_DON_AUTO,context);

        TaskParams param= new TaskParams();
        param.put("oj",oj);
        task.exe(param);
    }

    public static void update(String orderId, BObject ojUpdate, Context context, Model model) {
        TaskOrderV2 task = new TaskOrderV2(model,UPDATE,context);
        TaskParams param=new TaskParams();
        param.put("oj",ojUpdate);
        param.put("id",orderId);
        task.exe(param);

    }

    public static void search(String rqWhere, String rqOrderby, int  rqLimit, Context context, Model model) {
        TaskOrderV2 task = new TaskOrderV2(model,SEARCH,context);
        TaskParams param=new TaskParams();
        param.put("rqWhere",rqWhere);
        param.put("rqOrderby",rqOrderby);
        param.put("rqLimit",""+rqLimit);
        task.exe(param);

    }



    @Override
    protected Dataget doInBackground(TaskParams... params) {
        TaskParams param0= params[0];
        switch (taskType){
            case UPDATE_THEO_SP:
                return updateTheoSp(param0);
            case TASK_TACH_DONV2:
                return tachDonV2(param0);
            case TASK_TACH_DON_AUTO:
            return tachDonAuto(param0);
            case UPDATE:
                return doUpdate(param0);

            case SEARCH:
                return doSearch(param0);
        }

        return super.doInBackground(params);
    }

    private Dataget doSearch(TaskParams param0) {
        String rqWhere= param0.getTaskParramString("rqWhere");
        String rqOrderby= param0.getTaskParramString("rqOrderby");
        String rqLimit= param0.getTaskParramString("rqLimit");
        BObject oj = new BObject();
        oj.set("where",rqWhere);
        oj.set("limit",rqLimit);
        oj.set("orderby",rqOrderby);
        return NetSupport.getInstance().simpleGet(NetSupport2.getBaseUrlv2()+MyUrl2.orderSearch+"?"+oj.convert2NetParrams(),null);

    }

    private Dataget doUpdate(TaskParams param0) {
        BObject oj = param0.getTaskParramBaseObject("oj");
        String id= param0.getTaskParramString("id");
        String url= NetSupport2.getBaseUrlv2()+ "orders/"+id;
        String pa=oj.convert2NetParramsNotEncode();
        if (pa.charAt(0)=='&') pa=pa.substring(1);
        return NetSupport.getInstance().request("PUT",url,pa);
    }

    private Dataget tachDonAuto(TaskParams param0) {
        BObject oj = param0.getTaskParramBaseObject("oj");
        String url= NetSupport2.getBaseUrlv2()+ MyUrl2.tach_orderAuto+oj.convert2NetParrams();
        return NetSupport.getInstance().simpleGet(url,null);
    }

    private Dataget tachDonV2(TaskParams param0) {
        BObject oj = param0.getTaskParramBaseObject("oj");
        String url= NetSupport2.getBaseUrlv2()+ MyUrl2.tach_orderv2+oj.convert2NetParrams();
        return NetSupport.getInstance().simpleGet(url,null);
    }


    private Dataget updateTheoSp(TaskParams param0) {
        String url= NetSupport2.getBaseUrlv2()+ MyUrl2.updateTheoSp;
        BObject bObject= param0.getTaskParramBaseObject("oj");

        Dataget da = NetSupport.getInstance().request("PUT", url, bObject.convert2NetParramsNotEncode());

        return da;
    }
}