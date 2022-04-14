package com.project.shop.lemy.Task;

import android.content.Context;

import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetOrder;
import com.project.shop.lemy.Net.NetProduct;
import com.project.shop.lemy.Net.NetSupport2;
import com.project.shop.lemy.chucnangkhac.SuaCodGhtkActivity;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.model.TaskParams;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.Mlog;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

public class TaskNetOrder extends BaseTask {
    private static final int start=TaskType.start_tasknetorder;
    public static final int TASK_UPDATE=start+1;
    public static final int TASK_NHAP_DONV2=start+2;
    public static final int TASK_SUA_COD_GHTK=start+3;
    public static final int TASK_ORDER_GHTK=start+4;
    public static final int TASK_DETAIL=start+5;

    public static void extaskDetail(String order_id,BaseModel baseModel, Context context,long queue) {
        TaskNetOrder taskNetOrder=new TaskNetOrder(baseModel,TASK_DETAIL,context);
        taskNetOrder.setTaskParram("order_id",order_id);
        taskNetOrder.setQueue(queue);
        taskNetOrder.exe();
    }



    public void setParamDefault2(Object param) {
        setTaskParram("setParamDefault2",param);
    }
    public Object getParamDefault2(){
        return getTaskParram("setParamDefault2");
    }

    ;

    public void setQueue(long queue){
        setTaskParram("queuetask09",queue);
    }

    public long  getQueue(){
        return (long) getTaskParram("queuetask09");
    }


    NetData netData;
    public TaskNetOrder() {
        super();
    }

    public TaskNetOrder(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

    public TaskNetOrder(BaseModel model, Context context) {
        super(model, context);
    }


    public static void extaskNhapDon( String data, Context context, BaseModel baseModel){
        TaskNetOrder taskNet = new TaskNetOrder(baseModel,TASK_NHAP_DONV2 , context);
        taskNet.setTaskParram("data", data);
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

            case TASK_NHAP_DONV2:
                String data= (String) getTaskParram("data");

                netData = new NetOrder().NhapDon(data);
                return statusByNetdata(netData,null);

            case TASK_SUA_COD_GHTK:
                if(SuaCodGhtkActivity.cookieGhtk==null||Calendar.getInstance().getTimeInMillis()-SuaCodGhtkActivity.lastTimeLogin>300000)
                    loginGhtk();

                return updateCodGhtk();

            case TASK_ORDER_GHTK:
                NetData dd = NetOrder.order_ghtk(getTaskParramString("madh"));
                return statusByNetdata(dd,1);

            case TASK_UPDATE:
                String order_id= getTaskParramString("order_id");
                BaseObject oj= getTaskParramBaseObject("oj");
                netData= NetOrder.update(order_id,oj);
                return statusByNetdata(netData,null);

            case TASK_DETAIL:
                return getDetail();



        }


        return super.doInBackground(params);
    }



    private Boolean getDetail() {
        String order_id1= getTaskParramString("order_id");
        NetData data= NetOrder.getDetail(order_id1);
        return statusByNetdata(data);

    }

    private boolean updateCodGhtk() {
        Connection.Response res=null;
        try {
            res = Jsoup.connect("https://khachhang.giaohangtietkiem.vn/khach-hang/dang-phan-hoi/package_alias:"+getTaskParramString("mavandon"))
                    .ignoreContentType(true)
                    .data("data[Feedback][feedback_type_id]", "9")
                    .data("data[Feedback][package_code]", getTaskParramString("mavandon"))
                    .data("data[Feedback][is_freeship]", getTaskParramString("is_freeship"))
                    .data("data[Feedback][pick_money]", getTaskParramString("pick_money"))
                    .data("data[Feedback][customer_tel]","0382571899")
                    .followRedirects(true)
                    .cookie("KHSESSID",SuaCodGhtkActivity.cookieGhtk)
                    .method(Connection.Method.POST)
                    .execute();
            String response= res.parse().text();
            setDataReturn(response);
            return TASK_DONE;


        } catch (IOException e) {
            e.printStackTrace();
            msg="Lỗi 9380";
            return TASK_FAILED;
        }


    }

    private void loginGhtk() {
        Connection.Response res=null;
        try {
            res = Jsoup.connect("https://khachhang.giaohangtietkiem.vn/khach-hang/dang_nhap")
                    .data("data[Shop][email]", getTaskParramString("user"))
                    .data("data[Shop][password]", getTaskParramString("pass"))
                    .followRedirects(true)
                    .method(Connection.Method.POST)
                    .execute();

            SuaCodGhtkActivity.cookieGhtk= res.cookie("KHSESSID");
            SuaCodGhtkActivity.lastTimeLogin= Calendar.getInstance().getTimeInMillis();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res==null)
        Mlog.D("loi roi");




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
    private Boolean statusByNetdata(NetData netData) {
        if (netData.isFailed()) {
            msg = netData.getMsg();
            return TASK_FAILED;
        }
        msg=""+getQueue();

        setDataReturn(netData.getData());
        return TASK_DONE;
    }


}
