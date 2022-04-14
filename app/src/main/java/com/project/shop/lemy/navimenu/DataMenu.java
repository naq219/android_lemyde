package com.project.shop.lemy.navimenu;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.MenuObj;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;
import java.util.List;


public class DataMenu {
    public static final String TitleMenu[] = {"Sản phẩm", "Khách hàng", "Đơn hàng", "Shop", "Đơn vị vận chuyển", "Xuất nhập kho", "Quản lý SMS"};
    public static final String Sanpham[] = {"DS Sản phẩm","Thêm SP"};
    public static final String Khachhang[] = {"Danh sách khách hàng", "Thêm khách hàng"};
    public static final String Donhang[] = {"Danh sách đơn hàng", "Thêm đơn hàng", "Order-Product", "Thay đổi trạng thái ĐH"};
    public static final String Shop[] = {"Danh sách shop", "Thêm shop","Cài đặt"};
    public static final String DVVC[] = {"Danh sách DVVC", "Thêm DVVC"};
    public static final String XuatNhapKho[] = {"Nhập Kho", "Danh sách đơn nhập", "Xuất kho", "Danh sách đơn xuất"};
    public static final String SMS[] = {"Sms mẫu", "Nhắn tin đến khách hàng"};
    public static final int iconMenu[] = {R.drawable.home, R.drawable.ic_sanpham, R.drawable.ic_khachhang, R.drawable.ic_donhang,
            R.drawable.ic_shop, R.drawable.ic_dvvc, R.drawable.ic_xuatnhapkho, R.drawable.ic_sms};

//    static List<BaseObject> listHome() {
//        List<BaseObject> list = new ArrayList<>();
//        return list;
//    }

    static List<BaseObject> listSp() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < Sanpham.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, Sanpham[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listKh() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < Khachhang.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, Khachhang[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listDh() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < Donhang.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, Donhang[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listShop() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < Shop.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, Shop[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listSms() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < SMS.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, SMS[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listDvvc() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < DVVC.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, DVVC[i]);
            list.add(object);
        }
        return list;
    }

    static List<BaseObject> listXuatNhapKho() {
        List<BaseObject> list = new ArrayList<>();
        for (int i = 0; i < XuatNhapKho.length; i++) {
            BaseObject object = new BaseObject();
            object.set(MenuObj.childMenu, XuatNhapKho[i]);
            list.add(object);
        }
        return list;
    }

    public static ArrayList<List> listMenu() {
        ArrayList<List> listCategory = new ArrayList<>();
        //listCategory.add(listHome());
        listCategory.add(listSp());
        listCategory.add(listKh());
        listCategory.add(listDh());
        listCategory.add(listShop());
        listCategory.add(listDvvc());
        listCategory.add(listXuatNhapKho());
        listCategory.add(listSms());
        return listCategory;
    }
}
