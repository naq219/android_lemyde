package com.project.shop.lemy.navimenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.MenuObj;
import com.telpoo.frame.object.BaseObject;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class ListMenuViewHolder extends ChildViewHolder {
    private TextView menuName;
    private ImageView imgIcon;

    public ListMenuViewHolder(View itemView) {
        super(itemView);
        menuName = itemView.findViewById(R.id.tvCategory);
        imgIcon = itemView.findViewById(R.id.imgIcon);
    }

    public void onBind(BaseObject menuObj, int indext) {
        menuName.setText(menuObj.get(MenuObj.childMenu));
        if (indext == 0) imgIcon.setImageResource(R.drawable.ic_circle_vang);
        else imgIcon.setImageResource(R.drawable.ic_circle);
    }
}
