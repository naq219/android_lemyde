//package com.project.shop.lemy.donhang;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lemy.telpoo2lib.model.BObject;
//import com.lemy.telpoo2lib.utils.TimeUtils;
//import com.project.shop.lemy.R;
//import com.project.shop.lemy.bean.OrderObj;
//import com.project.shop.lemy.helper.MoneySupport;
//import com.project.shop.lemy.helper.StringHelper;
//import com.project.shop.lemy.listener.ListenBack;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
///**
// * Created by Ducqv on 7/6/2018.
// */
//
//public class EditDhAdapter extends BaseExpandableListAdapter {
//    List<List<BObject>> ojdh =null;
//    private Context context;
//
//    public EditDhAdapter(Context context){
//        this.context = context;
//    }
//
//    public void setData(List<List<BObject>> ojdh ){
//        this.ojdh = ojdh;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getGroupCount() {
//        return ojdh.size();
//    }
//
//    @Override
//    public int getChildrenCount(int i) {
//        return ojdh.get(i).size();
//    }
//
//    @Override
//    public Object getGroup(int i) {
//        return ojdh.get(i);
//    }
//
//    @Override
//    public Object getChild(int i, int i1) {
//        return ojdh.get(i).get(i1);
//    }
//
//    @Override
//    public long getGroupId(int i) {
//        return 0;
//    }
//
//    @Override
//    public long getChildId(int i, int i1) {
//        return 0;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        return null;
//    }
//
//    @Override
//    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.adapter_fm_edit_dh_item, null);
//        }
//        TextView expandedListTextView = (TextView) convertView
//                .findViewById(R.id.expandedListItem);
//        expandedListTextView.setText(expandedListText);
//        return convertView;
//    }
//
//    @Override
//    public boolean isChildSelectable(int i, int i1) {
//        return false;
//    }
//}
