package com.project.shop.lemy.Task;

import android.content.Context;
import android.util.Log;

import com.project.shop.lemy.Net.NetData;
import com.project.shop.lemy.Net.NetSupport;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.common.StaticData;
import com.project.shop.lemy.donhang.DonHangFm;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.model.TaskParams;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.SPRSupport;
import com.telpoo.frame.utils.TimeUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by naq on 05/04/2015.
 */
public class TaskNet extends MyTask {
    NetData dataApi;

    public TaskNet() {
        super();
    }

    public TaskNet(BaseModel model, int taskType, Context context) {
        super(model, taskType, context);
    }

    public TaskNet(BaseModel model, Context context) {
        super(model, context);
    }





    @Override
    protected Boolean doInBackground(TaskParams... params) {
        if (context == null) {
            return TASK_FAILED;
        }
//        if (context != null && !BaseNetSupport.isNetworkAvailable(context)) {
//            msg = "Không có kết nối internet";
//            return TASK_FAILED;
//
//        }

        switch (taskType) {
            case TASK_SESRCH:
                return search();
            case TASK_LIST_DONHANG:
                return listDonhang();
            case TASK_LIST_NGUOIBAN:
                return listNguoiban();
            case TASK_LIST_SANPHAM:
                return listSanpham();
            case TASK_LIST_ORDERPROFUCT:
                return listOrderprofuct();
            case TASK_DETAILORDER:
                return detailOrder();
            case TASK_UPDATEORDER:
                return updateOrder();
            case TASK_SEARCHPRODUCTS:
                return searchProducts();
            case TASK_CHITIET_SANPHAM:
                return chiTietSanPham();
            case TASK_SEARCH_CUSTOMERS:
                return searchCustomers();
            case TASK_UPDATE_SANPHAM:
                return updateSanPham();
            case TASK_ADD_SANPHAM:
                return addSanPham();
            case TASK_ADD_ORDER:
                return addOrder();
            case TASK_SHOP:
                return searchShop();
            case TASK_DELETE_SANPHAM:
                return deleteProduct();
            case TASK_ADD_KHACHHANG:
                return addKhachHang();
            case TASK_LIST_SHOP:
                return listShop();
            case TASK_ADD_SHOP:
                return addShop();
            case TASK_DELETE_SHOP:
                return deleteShop();
            case TASK_UPDATE_SHOP:
                return updateShop();
            case TASK_SEARCH_QUANHUYEN:
                return searchQuanHuyen();
            case TASK_DELETE_CUSTOMERS:
                return deleteKhachHang();
            case TASK_UPDATE_CUSTOMERS:
                return updateKhachHang();
            case TASK_PUSH_HVC:
                return pushHvc();
            case TASK_LIST_STATUS:
                return listStatus();
            case TASK_LIST_TYPES_CUSTOMER:
                return listLoaiKhachHang();
            case TASK_LIST_STATUS_PRODUCT:
                return listStatusSanPham();
            case TASK_LIST_PAYMENT:
                return listPayment();
            case TASK_LIST_DVVC:
                return listDonViVanChuyen();
            case TASK_UPDATE_DVVC:
                return updateDvvc();
            case TASK_ADD_DVVC:
                return addDvvc();
            case TASK_DELETE_DVVC:
                return deleteDvvc();
            case TASK_LIST_KHO:
                return listKho();
            case TASK_LIST_KIEUNHAP:
                return listKieuNhap();
            case TASK_LIST_KHOANG:
                return listKhoang();
            case TASK_ADD_NHAPHANG:
                return addNhapHang();
            case TASK_LIST_NHAPHANG:
                return listNhapHang();
            case TASK_LIST_DONXUAT:
                return listDonXuat();
            case TASK_LIST_KIEUXUAT:
                return listKieuXuat();
            case TASK_ADD_XUATKHO:
                return addXuatKho();
            case TASK_LIST_KHOANGSLSP:
                return listKhoangSlSp();
        }
        return super.doInBackground(params);
    }

    private Boolean listKhoangSlSp() {
        String khoId = getTaskParramString("kho_id");
        ArrayList<BaseObject> products = (ArrayList<BaseObject>) getTaskParram("products");
        dataApi = NetSupport.listKhoangSlSp(khoId, products, context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean addXuatKho() {
        dataApi = NetSupport.postAddXuatKho(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listKho() {
        dataApi = NetSupport.listKho(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listKieuNhap() {
        dataApi = NetSupport.listKieuNhap(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listKieuXuat() {
        dataApi = NetSupport.listKieuXuat(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listKhoang() {
        dataApi = NetSupport.listKhoang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean addNhapHang() {
        dataApi = NetSupport.postAddNhapHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listPayment() {
        dataApi = NetSupport.listPayment(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listStatusSanPham() {
        dataApi = NetSupport.listStatusSanPham(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listLoaiKhachHang() {
        dataApi = NetSupport.listLoaiKhachHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean pushHvc() {
        int orderId=getTaskParramInt("orderId");
        dataApi = NetSupport.pushHvc(orderId, (JSONObject) getTaskParram("param"), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        String curLog= SPRSupport.getString(SprSupport.KEY_LOGPUSHDVVC,context);
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        c.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
       curLog= orderId+"      "+ TimeUtils.calToString(c,"HH:mm:ss dd/MM/yyyy")+"\n"+curLog;
       int ind= curLog.length();
       if (ind>1000)ind=1000;
       curLog= curLog.substring(0,ind);
        SPRSupport.save(SprSupport.KEY_LOGPUSHDVVC,curLog,context);

        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean updateKhachHang() {
        dataApi = NetSupport.updateKhachHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean deleteKhachHang() {
        dataApi = NetSupport.deleteKhachHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean searchQuanHuyen() {
        dataApi = NetSupport.searchQuanHuyen(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }


    private Boolean addKhachHang() {
        dataApi = NetSupport.postAddKhachHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }


    private Boolean addOrder() {
        dataApi = NetSupport.postAddOrder(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean deleteProduct() {
        dataApi = NetSupport.deleteProduct(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean deleteShop() {
        dataApi = NetSupport.deleteShop(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean deleteDvvc() {
        dataApi = NetSupport.deleteDvvc(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean addSanPham() {
        dataApi = NetSupport.postAddSanPham(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean addShop() {
        dataApi = NetSupport.postAddShop(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean addDvvc() {
        dataApi = NetSupport.postAddDvvc(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean updateSanPham() {
        dataApi = NetSupport.putUpdateSanpham(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean updateShop() {
        dataApi = NetSupport.putUpdateShop(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean updateDvvc() {
        dataApi = NetSupport.putUpdateDvvc(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean chiTietSanPham() {
        dataApi = NetSupport.getChiTietSanPham(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean searchProducts() {
        dataApi = NetSupport.searchProducts(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }

        msg= getParramObject().get("q");
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean updateOrder() {
        dataApi = NetSupport.updateOrder(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean detailOrder() {
        dataApi = NetSupport.detailOrder(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }

        setDataReturn(dataApi.getData());

        return TASK_DONE;
    }

    private Boolean listSanpham() {
        dataApi = NetSupport.listSanpham(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listShop() {
        dataApi = NetSupport.listShop(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listStatus() {
        dataApi = NetSupport.listStatus(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listDonViVanChuyen() {
        dataApi = NetSupport.listDVVC(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listNguoiban() {
        dataApi = NetSupport.listNguoiban(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }

        setDataReturn(dataApi.getData());

        return TASK_DONE;
    }

    private Boolean listOrderprofuct() {
        dataApi = NetSupport.listOrderprofuct(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listDonhang() {
        BaseObject ojRequestListDon = getParramObject();
        dataApi = NetSupport.listDonhang(ojRequestListDon, context);
        if (dataApi.getcode() != 1) {
            //msg = dataApi.getMsg();
            msg = ojRequestListDon.get(DonHangFm.KEYRQ_IDLOADING);
            return TASK_FAILED;
        }
        Object[] ojsResListDon = {ojRequestListDon.get(DonHangFm.KEYRQ_IDLOADING), dataApi.getData()};

        setDataReturn(ojsResListDon);

        return TASK_DONE;
    }

    private Boolean listNhapHang() {
        dataApi = NetSupport.listNhapHang(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean listDonXuat() {
        dataApi = NetSupport.listDonXuat(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean search() {
        dataApi = NetSupport.search(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean searchCustomers() {
        dataApi = NetSupport.searchCustomers(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    private Boolean searchShop() {
        dataApi = NetSupport.searchShop(getParramObject(), context);
        if (dataApi.getcode() != 1) {
            msg = dataApi.getMsg();
            return TASK_FAILED;
        }
        setDataReturn(dataApi.getData());
        return TASK_DONE;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

    }


    public TaskNet clone() {
        TaskNet task = new TaskNet();
        task.setAllData(this.getAllData());
        return task;
    }


    public BaseObject getParramObject() {
        BaseObject object = getTaskParramBaseObject("parram");
        if (object == null) object = new BaseObject();
        return object;
    }
}
