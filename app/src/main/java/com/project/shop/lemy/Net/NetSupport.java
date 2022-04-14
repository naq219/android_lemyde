package com.project.shop.lemy.Net;

import android.content.Context;
import android.util.Log;

import com.project.shop.lemy.bean.OrderObj;
import com.project.shop.lemy.bean.ProductObj;
import com.project.shop.lemy.bean.ShopObj;
import com.project.shop.lemy.common.SettingSupport;
import com.project.shop.lemy.common.SprSupport;
import com.project.shop.lemy.db.DbSupport;
import com.project.shop.lemy.db.MyDb;
import com.telpoo.frame.database.BaseDBSupport;
import com.telpoo.frame.model.BaseTask;
import com.telpoo.frame.net.BaseNetSupport;
import com.telpoo.frame.net.NetConfig;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;
import com.telpoo.frame.utils.SPRSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @deprecated chuyen sang {@link NetSupport2}
 */
public class NetSupport {

    public static boolean isJSONValid(String test) {
        if (test == null) return false;
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static final NetData simpleGet(String url) {
        NetData data = new NetData();
        String res = BaseNetSupport.getInstance().simpleGetHttp(url);
        Log.d("telpoo", "url: " + url);
        Log.d("telpoo", "Respone: " + res);

        try {
            JSONObject dataResponse = new JSONObject(""+res);
            if (dataResponse.optBoolean("error", false)) {
                data.setCode(NetData.CODE_FALSE);
                data.setMessage(dataResponse.optString("message", "Có lỗi api"));
                return data;
            }
            data.setCode(NetData.CODE_SUCCESS);
            if (dataResponse.has("data")) data.setSuccess(dataResponse.getString("data"));
            else data.setSuccess(dataResponse);
            return data;
        } catch (JSONException e) {
            data.setCode(NetData.CODE_FALSE);
            data.setMessage("Không đúng địng dạng json");
        }
        return data;
    }

    private static final NetData methodPost(String url, String parram) {
        NetData data = new NetData();
        String res = BaseNetSupport.getInstance().simplePostHttp(url, parram);
        if (res == null) {
            data.setConnectError();
            return data;
        }
        data.setSuccess(res);
        return data;

    }

    public static String addParramToUrl(String url, BaseObject object, Context context) {
        url = SettingSupport.getBaseUrl(context) + url;
        if (object == null) return url;
        return url + "?" + object.convert2NetParrams();
    }


    public static NetData search(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.search, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            data.setSuccess(JsonSupport.jsonArray2BaseOj(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listDonhang(BaseObject parramObject, Context context) {
        String url = addParramToUrl(MyUrl.listDonhang, parramObject, context);
        NetData data = simpleGet(url);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listNhapHang(BaseObject parramObject, Context context) {
        String url = addParramToUrl(MyUrl.listNhapHang, parramObject, context);
        NetData data = simpleGet(url);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonArray2BaseOj(res));
        } catch (JSONException e) {
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listDonXuat(BaseObject parramObject, Context context) {
        String url = addParramToUrl(MyUrl.listDonXuat, parramObject, context);
        NetData data = simpleGet(url);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonArray2BaseOj(res));
        } catch (JSONException e) {
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listNguoiban(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listNguoiban, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listuser = JsonSupport.jsonArray2BaseOj(jsonArray);
            data.setSuccess(listuser);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listSanpham(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listSanpham, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listSp = JsonSupport.jsonArray2BaseOj(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                BaseObject spproductObj = new BaseObject();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                spproductObj.set(ProductObj.image, jsonObject.getString(ProductObj.image));
                spproductObj.set(ProductObj.name, jsonObject.getString(ProductObj.name));
                spproductObj.set(ProductObj.cost_price, jsonObject.getString(ProductObj.cost_price));
                spproductObj.set(ProductObj.wholesale_price, jsonObject.getString(ProductObj.wholesale_price));
                spproductObj.set(ProductObj.retail_price, jsonObject.getString(ProductObj.retail_price));
            }
            data.setSuccess(listSp);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listShop(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.shop, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listShop = JsonSupport.jsonArray2BaseOj(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                BaseObject orderShopObj = new BaseObject();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                orderShopObj.set(ShopObj.name, jsonObject.getString(ShopObj.name));
                orderShopObj.set(ShopObj.facebook, jsonObject.getString(ShopObj.facebook));
            }
            data.setSuccess(listShop);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listStatus(BaseObject parramObject, Context context) {
        String url = addParramToUrl(MyUrl.listStatus, parramObject, context);

        NetData data = simpleGet(url);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject object = JsonSupport.jsonObject2BaseOj(res);
             data.setSuccess(object);
        } catch (JSONException e) {
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listPayment(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listPayment, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listKho(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listKho, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listKho = JsonSupport.jsonArray2BaseOj(jsonArray);
            data.setSuccess(listKho);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listKieuNhap(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listKieuNhap, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listKieuXuat(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listKieuXuat, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listKhoang(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listKhoang, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonArray2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listKhoangSlSp(String khoId, ArrayList<BaseObject> products, Context context) {
        JSONArray root = new JSONArray();
        NetData dataReturn = new NetData();
        for (int i = 0; i < products.size(); i++) {
            BaseObject item = new BaseObject();
            String productId = products.get(i).get("product_id", "");
            String productName = products.get(i).get("product_name", "");
            item.set("kho_id", khoId);
            item.set("product_id", productId);

            String url = addParramToUrl(MyUrl.listKhoangSlSp, item, context);
            NetData data = simpleGet(url);
            if (data.getcode() != 1) return data;
            String res = data.getDataAsString();
            try {
                ArrayList<BaseObject> list = JsonSupport.jsonArray2BaseOj(res);
                JSONArray sanPhamTrongKho = new JSONArray();
                for (int j = 0; j < list.size(); j++) {
                    sanPhamTrongKho.put(list.get(j).toJson());
                }
                JSONObject productItem = new JSONObject();
                productItem.put(ProductObj.id, productId);
                productItem.put(ProductObj.name, productName);
                productItem.put("ton_kho", sanPhamTrongKho);
                root.put(productItem);

            } catch (JSONException e) {
                e.printStackTrace();
                data.setCodeErrorServer();
            }
            dataReturn.setSuccess(root);
        }
        return dataReturn;
    }

    public static NetData postAddNhapHang(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJsonObject(SettingSupport.getBaseUrl(context)
                + MyUrl.addNhapHang, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData postAddXuatKho(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJsonObject(SettingSupport.getBaseUrl(context)
                + MyUrl.addXuatKho, parramObject);
        return data;
//        if (data.getcode() != 1) return data;
//        String res = data.getDataAsString();
//        try {
//            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
//            data.setSuccess(baseObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            data.setCodeErrorServer();
//        }
//        return data;
    }

    public static NetData listStatusSanPham(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listStatusSanPham, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listDVVC(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.dvvc, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listStatus = JsonSupport.jsonArray2BaseOj(jsonArray);
            data.setSuccess(listStatus);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listOrderprofuct(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listOrderprofuct, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listOp = JsonSupport.jsonArray2BaseOj(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                BaseObject orderproductObj = new BaseObject();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                orderproductObj.set(ProductObj.name, jsonObject.getString(ProductObj.name));
                orderproductObj.set(OrderObj.status, jsonObject.getString(OrderObj.status));
                orderproductObj.set(OrderObj.count, jsonObject.getString(OrderObj.count));
            }
            data.setSuccess(listOp);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData detailOrder(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.detailOrder, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject object = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(object);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData updateOrder(BaseObject parram, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePutJson(SettingSupport.getBaseUrl(context)
                + MyUrl.order, parram);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject object = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(object);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();

        }
        return data;
    }

    public static NetData searchProducts(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.searchProducts, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            ArrayList<BaseObject> listSearchSp = JsonSupport.jsonArray2BaseOj(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                BaseObject searchSpObj = new BaseObject();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                searchSpObj.set(ProductObj.image, jsonObject.getString(ProductObj.image));
                searchSpObj.set(ProductObj.name, jsonObject.getString(ProductObj.name));
                searchSpObj.set(ProductObj.cost_price, jsonObject.getString(ProductObj.cost_price));
                searchSpObj.set(ProductObj.wholesale_price, jsonObject.getString(ProductObj.wholesale_price));
                searchSpObj.set(ProductObj.retail_price, jsonObject.getString(ProductObj.retail_price));
            }
            data.setSuccess(listSearchSp);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData searchCustomers(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.search, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            data.setSuccess(JsonSupport.jsonArray2BaseOj(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData searchQuanHuyen(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.searchQuanHuyen, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            data.setSuccess(JsonSupport.jsonArray2BaseOj(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData searchShop(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.shop, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONArray jsonArray = new JSONArray(res);
            data.setSuccess(JsonSupport.jsonArray2BaseOj(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData getChiTietSanPham(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.chiTietSanPham, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData putUpdateSanpham(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePutJson(SettingSupport.getBaseUrl(context) + MyUrl.chiTietSanPham, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData putUpdateShop(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePutJson(SettingSupport.getBaseUrl(context) + MyUrl.addshop, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData putUpdateDvvc(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePutJson(SettingSupport.getBaseUrl(context) + MyUrl.dvvc, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }


    public static NetData pushHvc(int orderId, JSONObject parramObject, Context context) {
       // NetData data = CustomBaseNetSupport.getInstance().simplePostJson(SettingSupport.getUrlHvc(context), parramObject);

        NetConfig.Builder builder = new NetConfig.Builder();
        builder.contentType("application/json");
        builder.connectTimeout(60000);
        CustomBaseNetSupport.getInstance().init(builder.build());
        String res = CustomBaseNetSupport.getInstance().methodHttp("POST",SettingSupport.getUrlHvc(context),parramObject.toString());
        NetData da= new NetData();
        try {
            JSONObject jo = new JSONObject(""+res);
            if (!jo.optBoolean("success",false)) {
                da.setFail(jo.optString("message"));
                return da;
            }



        } catch (JSONException e) {
            String ss="";
            if (res.length()>50)ss=res.substring(0,50);
            da.setFail("Lỗi hệ thống "+ss);
           return da;
        }


        NetData data= new NetData();
       // String res = data.getDataAsString();
        try {
            JSONObject response = new JSONObject(res);
            if (response.getBoolean("success")) {
                String trackingId = response.getJSONObject("order").getString("tracking_id");
                BaseObject dataUpdate = new BaseObject();
                dataUpdate.set("id", orderId);
                dataUpdate.set("order_shipper_lb_id", trackingId);
                dataUpdate.set("phi_ship", response.getJSONObject("order").getString("fee"));

                data = updateOrder(dataUpdate, context);
                if (data.isSuccess()) {
                    data.setSuccess("Thành Công! Mã Vận Đơn: " + trackingId);
                    return data;
                }
                data.setMessage(data.getMsg() + " -Mã Vận Đơn: " + trackingId);
                data.setCode(NetData.CODE_FALSE);
                return data;
            }
            data.setMessage(response.getString("message"));
            data.setCode(NetData.CODE_FALSE);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData updateKhachHang(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePutJson(SettingSupport.getBaseUrl(context) + MyUrl.addKhachHang, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData listLoaiKhachHang(BaseObject parramObject, Context context) {
        NetData data = simpleGet(addParramToUrl(MyUrl.listLoaiKhachHang, parramObject, context));
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            data.setSuccess(JsonSupport.jsonObject2BaseOj(res));
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData postAddSanPham(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJson(SettingSupport.getBaseUrl(context)
                + MyUrl.chiTietSanPham, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData postAddShop(BaseObject paramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJson(SettingSupport.getBaseUrl(context)
                + MyUrl.addshop, paramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData postAddDvvc(BaseObject paramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJson(SettingSupport.getBaseUrl(context)
                + MyUrl.dvvc, paramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData postAddOrder(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJsonObject(SettingSupport.getBaseUrl(context)
                + MyUrl.order, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            JSONObject ketqua = new JSONObject(res);

            if (ketqua.optBoolean("error", true)) {
                data.setMessage(ketqua.optString("message", "Có lỗi!"));
                data.setCode(NetData.CODE_FALSE);
                return data;
            }

            data.setSuccess();

        } catch (JSONException e) {

            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }

    public static NetData deleteProduct(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance()
                .simpleDeleteJson(addParramToUrl(MyUrl.chiTietSanPham, parramObject, context), parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;

    }

    public static NetData deleteShop(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance()
                .simpleDeleteJson(addParramToUrl(MyUrl.addshop, parramObject, context), parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;

    }

    public static NetData deleteDvvc(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance()
                .simpleDeleteJson(addParramToUrl(MyUrl.dvvc, parramObject, context), parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;

    }

    public static NetData deleteKhachHang(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance()
                .simpleDeleteJson(addParramToUrl(MyUrl.addKhachHang, parramObject, context), parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;

    }

    public static NetData postAddKhachHang(BaseObject parramObject, Context context) {
        NetData data = CustomBaseNetSupport.getInstance().simplePostJson(SettingSupport.getBaseUrl(context)
                + MyUrl.addKhachHang, parramObject);
        if (data.getcode() != 1) return data;
        String res = data.getDataAsString();
        try {
            BaseObject baseObject = JsonSupport.jsonObject2BaseOj(res);
            data.setSuccess(baseObject);
        } catch (JSONException e) {
            e.printStackTrace();
            data.setCodeErrorServer();
        }
        return data;
    }
}