package com.project.shop.lemy.search;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.Task.TaskNet;
import com.project.shop.lemy.Task.TaskType;
import com.project.shop.lemy.bean.CustomerObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.model.BaseModel;
import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

public class SearchSugestQuanHuyen extends SimpleCursorAdapter {
    private static final String[] mFields = {"_id", "result"};
    private static final String[] mVisible = {"result"};
    private static final int[] mViewIds = {android.R.id.text1};
    TaskNet taskNet;
    BaseModel baseModel;
    BaseObject objectSearch = new BaseObject();
    ArrayList<BaseObject> list;
    String query = "";
    int type;
    Listerner.setOnclickItem onItemClickListerer;

    public SearchSugestQuanHuyen(Context context) {
        super(context, R.layout.item_search_qh, null, mVisible, mViewIds, 0);
        baseModel = new BaseModel() {
            @Override
            public void onSuccess(int taskType, Object data, String msg) {
                super.onSuccess(taskType, data, msg);
                switch (taskType) {
                    case TaskType.TASK_SEARCH_QUANHUYEN:
                        type = TaskType.TASK_SEARCH_QUANHUYEN;
                        break;
                }
                list = (ArrayList<BaseObject>) data;
                swapCursor(new SuggestionsCursor(list.size()));
            }

            @Override
            public void onFail(int taskType, String msg) {
                super.onFail(taskType, msg);
            }
        };

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        cursor.getNotificationUri();
        BaseObject object = list.get(cursor.getPosition());
        TextView tvTinh1 = view.findViewById(R.id.tvTinh1);
        TextView tvHuyen1 = view.findViewById(R.id.tvHuyen1);
        tvTinh1.setText(object.get(CustomerObj.provinces_name));
        tvHuyen1.setText(object.get(CustomerObj.districts_name));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListerer.clickItem(object, type);
            }
        });

    }

    public void setOnItemClickListerer(Listerner.setOnclickItem onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    public void textChangeQuanHuyen(final String value) {
        query = value;
        objectSearch.set("q", value);
        taskNet = new TaskNet(baseModel, TaskType.TASK_SEARCH_QUANHUYEN, mContext);
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