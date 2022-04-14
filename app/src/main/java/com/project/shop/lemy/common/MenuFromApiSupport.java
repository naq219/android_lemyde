package com.project.shop.lemy.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.KhoangOj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MenuFromApiSupport {

    private static BaseObject objectMenuTrangThaiDonHang = null;
    private static BaseObject objectPayment = null;
    private static BaseObject objectMenuDonViVanChuyen = null;
    private static BaseObject objectStatusProduct = null;
    //private static BaseObject objectKhoChuaHang = null;
    private static BaseObject objectKieuNhap = null;
    private static BaseObject objectKieuXuat = null;
    //private static BaseObject objectKhoangChua = null;
    public static BaseObject objStatusListOrder;
    public static BaseObject objStatusList;
    private static List<BaseObject> ojsListKhoang=null;

    public static void createTrangThaiDonHang(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectMenuTrangThaiDonHang != null && objectMenuTrangThaiDonHang.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectMenuTrangThaiDonHang, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject objStt = (BaseObject) data;
                ArrayList<String> listStt = objStt.getKeys();
                objectMenuTrangThaiDonHang = new BaseObject();
                for (int i = 0; i < listStt.size(); i++) {
                    objectMenuTrangThaiDonHang.set(objStt.get(listStt.get(i)), listStt.get(i));
                }
                PoupMenuSupport.showMenu(context, view, objectMenuTrangThaiDonHang, show, onPoupMenuItemClick);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_STATUS, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void setTextStatus(Context context, TextView textView, String str, int Type) {
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject object = (BaseObject) data;
                ArrayList<String> list = object.getKeys();
                objStatusListOrder = new BaseObject();
                for (int i = 0; i < list.size(); i++) {
                    String key = list.get(i);
                    if (objStatusListOrder != null)
                        objStatusListOrder.set(key, object.get(key));

                }
                textView.setText(objStatusListOrder.get(str, "Chưa rõ"));
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, Type, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void createPayment(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectPayment != null && objectPayment.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectPayment, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject objPayment = (BaseObject) data;
                ArrayList<String> listPayment = objPayment.getKeys();
                objectPayment = new BaseObject();
                for (int i = 0; i < listPayment.size(); i++) {
                    objectPayment.set(objPayment.get(listPayment.get(i)), listPayment.get(i));
                }
                PoupMenuSupport.showMenu(context, view, objectPayment, show, onPoupMenuItemClick);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_PAYMENT, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void createDonViVanChuyen(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectMenuDonViVanChuyen != null && objectMenuDonViVanChuyen.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectMenuDonViVanChuyen, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                objectMenuDonViVanChuyen = new BaseObject();
                for (int i = 0; i < list.size(); i++) {
                    objectMenuDonViVanChuyen.set(list.get(i).get("name"), list.get(i).get("id"));
                }
                PoupMenuSupport.showMenu(context, view, objectMenuDonViVanChuyen, show, onPoupMenuItemClick);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_DVVC, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void createStatusProduct(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectStatusProduct != null && objectStatusProduct.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectStatusProduct, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject objSttProduct = (BaseObject) data;
                ArrayList<String> list = objSttProduct.getKeys();
                objectStatusProduct = new BaseObject();
                for (int i = 0; i < list.size(); i++) {
                    objectStatusProduct.set(objSttProduct.get(list.get(i)), list.get(i));
                }
                PoupMenuSupport.showMenu(context, view, objectStatusProduct, show, onPoupMenuItemClick);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_STATUS_PRODUCT, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }


    //todo viet call api goi toan bo setting cung luc
    public static void createKieuXuat(Context context, TextView view,Listerner.OnPoupMenuItemClick onPoupMenuItemClick){
        BaseObject ojKieuXuat= new BaseObject();
        ojKieuXuat.set("1","Xuất Đơn");
        ojKieuXuat.set("2","Đổi SP");
        ojKieuXuat.set("3","Khác");

        PoupMenuSupport.showMenu(context, view, ojKieuXuat, true, onPoupMenuItemClick);
    }

    public static void createKieuNhap(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectKieuNhap != null && objectKieuNhap.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectKieuNhap, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject objType = (BaseObject) data;
                ArrayList<String> listType = objType.getKeys();
                objectKieuNhap = new BaseObject();
                for (int i = 0; i < listType.size(); i++) {
                    objectKieuNhap.set(objType.get(listType.get(i)), listType.get(i));
                }
                view.postDelayed(() -> {
                    PoupMenuSupport.showMenu(context, view, objectKieuNhap, show, onPoupMenuItemClick);
                },200);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_KIEUNHAP, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void createKieuXuat(Context context, TextView view, boolean reload, boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if (!reload || (objectKieuXuat != null && objectKieuXuat.getKeys().size() > 0)) {
            PoupMenuSupport.showMenu(context, view, objectKieuXuat, show, onPoupMenuItemClick);
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                BaseObject objType = (BaseObject) data;
                ArrayList<String> listType = objType.getKeys();
                objectKieuXuat = new BaseObject();
                for (int i = 0; i < listType.size(); i++) {
                    objectKieuXuat.set(objType.get(listType.get(i)), listType.get(i));
                }
                PoupMenuSupport.showMenu(context, view, objectKieuXuat, show, onPoupMenuItemClick);
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_KIEUXUAT, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void createKhoChua(Context context, TextView view,boolean show, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        BaseObject objectKhoChuaHang=new BaseObject();
        if ((ojsListKhoang != null && ojsListKhoang.size() > 0)) {

            for (int i = 0; i < ojsListKhoang.size(); i++) {

                objectKhoChuaHang.set(ojsListKhoang.get(i).get("kho_name"), ojsListKhoang.get(i).get("kho_id"));
            }

            PoupMenuSupport.showMenu(context, view, objectKhoChuaHang, show, onPoupMenuItemClick);
            if (objectKhoChuaHang.getKeys().size()==1){ // chi co 1 kho
                onPoupMenuItemClick.onItemClick(ojsListKhoang.get(0).get("kho_id"));
                view.setText(ojsListKhoang.get(0).get("kho_name"));
            }
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                ojsListKhoang=list;
                BaseObject objectKhoChuaHang = new BaseObject();
                for (int i = 0; i < list.size(); i++) {

                    objectKhoChuaHang.set(list.get(i).get("kho_name"), list.get(i).get("kho_id"));
                }
                PoupMenuSupport.showMenu(context, view, objectKhoChuaHang, show, onPoupMenuItemClick);
                if (objectKhoChuaHang.getKeys().size()==1){ // chi co 1 kho
                    onPoupMenuItemClick.onItemClick(ojsListKhoang.get(0).get("kho_id"));
                    view.setText(ojsListKhoang.get(0).get("kho_name"));
                }
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_KHOANG, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void showKhoangNhap( List<BaseObject> khoangOjs, Context context, SearchableSpinner view, Listerner.OnPoupMenuItemClick onPoupMenuItemClick){

        String[] vl =new String[khoangOjs.size()];
        for (int i = 0; i < khoangOjs.size(); i++) {
            String khoangId=khoangOjs.get(i).get(KhoangOj.KHOANG_ID);
            String tonkho="";
            if (khoangOjs.get(i).containsKey(KhoangOj.TONKHOTHEOSP)) tonkho= "(tồn: "+khoangOjs.get(i).get(KhoangOj.TONKHOTHEOSP)+")" ;
            vl[i]= khoangId+tonkho;
        }
        PoupMenuSupport.showSearchMenu(view,"Chọn khoang hàng",context,vl,khoangSelected -> {
            onPoupMenuItemClick.onItemClick((String) khoangSelected);
        });



    }

    public static void createKhoangChuav2(String khoId, Context context, SearchableSpinner view, Listerner.OnPoupMenuItemClick onPoupMenuItemClick) {
        if ((ojsListKhoang != null && ojsListKhoang.size() > 0)) {
            //PoupMenuSupport.showMenu(context, view, objectKhoangChua, show, onPoupMenuItemClick);

            BaseObject objectKhoangChua= new BaseObject();

            for (BaseObject ojListKhoang :
                    ojsListKhoang) {
                if (ojListKhoang.get("kho_id","ss").equals(khoId))objectKhoangChua.set(ojListKhoang.get("khoang_id"),ojListKhoang.get("khoang_id"));
            }

            PoupMenuSupport.showSearchMenu(view,"Title",context,objectKhoangChua.getKeys().toArray(new String[0]),khoangSelected -> {
                onPoupMenuItemClick.onItemClick((String) khoangSelected);
            });
            return;
        }
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                BaseObject objectKhoangChua = new BaseObject();

                //#1 gắn cho kho thông, khoang còn xem thuộc kho nào nữa
                ojsListKhoang=list;

                //end #1

                for (BaseObject ojListKhoang :
                        ojsListKhoang) {
                    if (ojListKhoang.get("kho_id","ss").equals(khoId))objectKhoangChua.set(ojListKhoang.get("khoang_id"),ojListKhoang.get("khoang_id"));
                }

                // PoupMenuSupport.showMenu(context, view, objectKhoangChua, show, onPoupMenuItemClick);
                PoupMenuSupport.showSearchMenu(view,"Title",context,objectKhoangChua.getKeys().toArray(new String[0]),khoangSelected -> {
                    onPoupMenuItemClick.onItemClick((String) khoangSelected);
                });
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, TaskType.TASK_LIST_KHOANG, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static void setTextStatusArray(Context context, TextView textView, String stt, int Type) {
        TaskNet taskNet = new TaskNet(new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                ArrayList<BaseObject> list = (ArrayList<BaseObject>) data;
                objStatusList = new BaseObject();
                for (int i = 0; i < list.size(); i++) {
                    if (objStatusList != null)
                        objStatusList.set(list.get(i).get("id"), list.get(i).get("name"));
                }
                textView.setText(objStatusList.get(stt, "--Chọn khoang chứa--"));
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        }, Type, context);
        taskNet.setTaskParram("parram", new BaseObject());
        taskNet.exe();
    }

    public static List<BaseObject> getKhoangByKho(String khoId) {
        List<BaseObject>  ojsKhoangChua= new ArrayList<>();
        for (BaseObject ojListKhoang :
                ojsListKhoang) {
            if (ojListKhoang.get("kho_id","ss").equals(khoId))
                ojsKhoangChua.add(ojListKhoang);
        }
        return ojsKhoangChua;
    }
}
