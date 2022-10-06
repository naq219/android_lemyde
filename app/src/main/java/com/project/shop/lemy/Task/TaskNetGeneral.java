package com.project.shop.lemy.Task;

import android.content.Context;

import com.lemy.telpoo2lib.net.Dataget;
import com.lemy.telpoo2lib.net.NetSupport;
import com.project.shop.lemy.Net.MyUrl2;
import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetSupport2;
import com.project.shop.lemy.chucnangkhac.SuaCodGhtkActivity;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.model.TaskParams;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.Mlog;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;

public class TaskNetGeneral extends BaseTask {
    private static final int start= MyTask.START_TASKNET_GENERAL;

    public static final int LOAD_GHTK_WEB_INFO = start+1;
    public static final int OTHER_DATAV2=start+2;
    public static final int NOTIFI=start+3;


    NetData netData;
    public TaskNetGeneral() {
        super();
    }

    public TaskNetGeneral(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

    public TaskNetGeneral(BaseModel model, Context context) {
        super(model, context);
    }


    public static void extaskOtherData(Context context,BaseModel baseModel){
        TaskNetGeneral taskNet = new TaskNetGeneral(baseModel,OTHER_DATAV2 , context);
        taskNet.exe();
    }

    public static void exTaskNotifyImportain(String content,Context context) {
        BaseModel baseModel = new BaseModel();

        TaskNetGeneral taskNet = new TaskNetGeneral(baseModel,NOTIFI , context);
        taskNet.setTaskParram("content",content);
        taskNet.setTaskParram("ott","telegram");
        taskNet.setTaskParram("channel","@quan_trong");
        taskNet.exe();
    }
    public static void exTaskNotifyOnlyMe(String content,Context context) {
        BaseModel baseModel = new BaseModel();
        TaskNetGeneral taskNet = new TaskNetGeneral(baseModel,NOTIFI , context);
        taskNet.setTaskParram("content",content);
        taskNet.setTaskParram("ott","telegram");
        taskNet.setTaskParram("channel","@crm_que");
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

            case OTHER_DATAV2:

                netData = NetSupport2.getOtherData();
                return statusByNetdata(netData);

            case LOAD_GHTK_WEB_INFO:
                return loadWebGhtk();

            case NOTIFI:
                return notifiOtt();
        }


        return super.doInBackground(params);
    }

    private Boolean notifiOtt() {


        BaseObject oj = new BaseObject();
        oj.set("ott",getTaskParramString("ott"));
        oj.set("content",getTaskParramString("content"));
        oj.set("channel",getTaskParramString("channel"));
        Dataget data = NetSupport.getInstance().simplePost(NetSupport2.getBaseUrlv2()+ MyUrl2.notify,oj.convert2NetParramsNotEncode());
        //NetSupport2.simpleRequest("POST", NetSupport2.getBaseUrlv2()+ MyUrl2.notify,oj.convert2NetParramsNotEncode());
        return false;
    }

    private boolean loadWebGhtk() {

        if(SuaCodGhtkActivity.cookieGhtk==null|| Calendar.getInstance().getTimeInMillis()-SuaCodGhtkActivity.lastTimeLogin>300000)
           loginGhtk();
        String url= getTaskParramString("url");
       // =null;
        try {
            Connection.Response res = Jsoup.connect(url)
                    .ignoreContentType(true)

                    .followRedirects(true)
                    .cookie("KHSESSID", ""+SuaCodGhtkActivity.cookieGhtk)
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = res.parse();
            Elements se = doc.select("table#table-packages").attr("width","2500");



            setDataReturn(se.outerHtml());
            return TASK_DONE;


        } catch (IOException e) {
            e.printStackTrace();
            msg="Lỗi 32346";
            return TASK_FAILED;
        }
    }

    private void loginGhtk() {
        Connection.Response res=null;
//        try {
//            res = Jsoup.connect("https://khachhang.giaohangtietkiem.vn/khach-hang/dang_nhap")
//                    .data("data[Shop][email]", getTaskParramString("user"))
//                    .data("data[Shop][password]", getTaskParramString("pass"))
//                    .followRedirects(false)
//                    .method(Connection.Method.POST)
//                    .execute();
//            Document doc = res.parse();
//
//            Connection.Response res1 = Jsoup.connect(res.header("location")).method(Connection.Method.GET).followRedirects(true).execute();
//            Document doc1 = res1.parse();
//            SuaCodGhtkActivity.cookieGhtk= res1.cookie("KHSESSID");
//            SuaCodGhtkActivity.lastTimeLogin= Calendar.getInstance().getTimeInMillis();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (res==null)
            Mlog.D("loi roi");




    }

    private Boolean statusByNetdata(NetData netData) {
        if (netData.isFailed()) {
            msg = netData.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(netData.getData());
        return TASK_DONE;
    }


}
