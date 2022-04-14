package com.project.shop.lemy.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Handler;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskNetProduct;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;
import com.telpoo.frame.utils.JsonSupport;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SearchSugestAdapter extends SimpleCursorAdapter {
    private static final String[] mFields = {"_id", "result"};
    private static final String[] mVisible = {"result"};
    private static final int[] mViewIds = {android.R.id.text1};
    TaskNet taskNet;
    BaseModel baseModel;
    BaseObject objectSearch = new BaseObject();
    ArrayList<BaseObject> list;
    String query1 = "";
    int type;
    Listerner.setOnclickItem onItemClickListerer;
    private int queuesearchp=0;
    private String q="";

    public SearchSugestAdapter(Context context) {
        super(context, R.layout.item_search, null, mVisible, mViewIds, 0);
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                type=taskType;
                switch (taskType) {
                    case TaskType.TASK_SEARCH_CUSTOMERS:
                        list = (ArrayList<BaseObject>) data;
                        break;
                    case TaskType.TASK_SEARCHPRODUCTS:


                        if (list.size()==0){
                            BaseObject ojtmp1= new BaseObject();
                            ojtmp1.set("name","Thêm Barcode vào sản phẩm? ==>> Bấm vào đây để cập nhật");
                            ojtmp1.set("id",999999);
                            ojtmp1.set("ean",msg);
                            list.add(ojtmp1);
                        }
                        break;
                    case TaskType.TASK_SHOP:
                        list = (ArrayList<BaseObject>) data;
                        break;

                    case TaskType.SEARCH_PRODUCT_V2:
                        int queue_s= Integer.parseInt(msg);
                        if (queuesearchp!=queue_s) {
                            //Mlog.D("khong dung queue");
                            return;
                        }
                        JSONArray jaProduct=null;

                        if (data instanceof JSONArray)
                            jaProduct= (JSONArray) data;
                        else Log.d("hehe","--"+data.toString());

                        try {
                            list = JsonSupport.jsonArray2BaseOj(jaProduct);
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).set("name","("+list.get(i).get("id")+")"+list.get(i).get("name")+" #Tồn: "+list.get(i).get("tonkho"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if ((list==null||list.size()==0)&&q.charAt(0)=='#'&&q.length()>4){
                            BaseObject ojtmp1= new BaseObject();
                            ojtmp1.set("name","Thêm Barcode vào sản phẩm? ==>> Bấm vào đây để cập nhật");
                            ojtmp1.set("id",999999);
                            ojtmp1.set("ean",q.substring(1));
                            list.add(ojtmp1);
                        }

                        break;
                }

                swapCursor(new SuggestionsCursor(list.size()));
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);

                list = new ArrayList<>();
                swapCursor(new SuggestionsCursor(list.size()));
            }
        };

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        cursor.getNotificationUri();
        if (list==null||list.size()==0) return;
        if (list.size()-1<cursor.getPosition()) return;
        BaseObject object = list.get(cursor.getPosition());
        TextView tvNameSearch = view.findViewById(R.id.tvNameSearch);
        TextView tvSdtSearch = view.findViewById(R.id.tvSdtSearch);
        tvNameSearch.setText(object.get(CustomerObj.name));
        tvSdtSearch.setText(object.get(CustomerObj.phone));

        view.setOnClickListener(view1 -> onItemClickListerer.clickItem(object, type));

    }

    public void setCurrentTextSearch(String q){
        if (q!=null)
        this.q = q;
    }

    public void setOnItemClickListerer(Listerner.setOnclickItem onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    @SuppressLint("RestrictedApi")
    public void textChangeCustomer(final String value) {
        //query = value;
        objectSearch.set("q", value);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SEARCH_CUSTOMERS, mContext);
        taskNet.setTaskParram("parram", objectSearch);
        taskNet.exe();
    }

    public void textChangeProduct(final String value) {
//        query = value;
//        objectSearch.set("q", value);
//        taskNet = new TaskNet(baseModel, TaskType.TASK_SEARCHPRODUCTS, mContext);
//        taskNet.setTaskParram("parram", objectSearch);
//        taskNet.exe();
        queuesearchp++;
        callsearch(queuesearchp,value);

    }

    private void callsearch(int queuesearchp1,String text) {

        new Handler().postDelayed(() -> {
            if (queuesearchp1!=this.queuesearchp){
                //showToast("da khac roi: "+queuesearchp+" -- "+text);
                return;
            }
            TaskNetProduct.extaskSeachName(text,mContext,queuesearchp1,baseModel);
        },500);
    }

    public void textChangeShop(final String value) {
        //query = value;
        objectSearch.set("q", value);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SHOP, mContext);
        taskNet.setTaskParram("parram", objectSearch);
        taskNet.exe();
    }

    public class SuggestionsCursor extends AbstractCursor {
        int count = 5;

        public SuggestionsCursor(int count) {
            this.count = count;

        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public String[] getColumnNames() {
            return mFields;
        }

        @Override
        public long getLong(int column) {
            return 0;
        }

        @Override
        public String getString(int column) {
            return "";
        }

        @Override
        public short getShort(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public int getInt(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public float getFloat(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public double getDouble(int column) {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
}