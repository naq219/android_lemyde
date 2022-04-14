package com.project.shop.lemy.navimenu;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.shop.lemy.R;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class CategoryViewHolder extends GroupViewHolder {
    private TextView genreTitle;
    ImageView imgIcon, imgCtrl;
    RecyclerView recyclerView;
    static int cuInd = -1;

    public CategoryViewHolder(View itemView, RecyclerView recyclerView) {
        super(itemView);
        genreTitle = itemView.findViewById(R.id.tvCategory);
        imgIcon = itemView.findViewById(R.id.imgMenu);
        imgCtrl = itemView.findViewById(R.id.imgCtrl);
        this.recyclerView = recyclerView;
    }

    public void setGenreTitle(ExpandableGroup group, int indext) {
        genreTitle.setText(group.getTitle());
        if (indext < DataMenu.iconMenu.length)
            imgIcon.setImageResource(DataMenu.iconMenu[indext]);
        if (indext != 0) imgCtrl.setImageResource(R.drawable.ic_down_arrow);
    }

    @Override
    public void expand() {
        super.expand();
        imgCtrl.setImageResource(R.drawable.ic_up);
        if (cuInd != -1 && recyclerView.findViewHolderForAdapterPosition(cuInd) != null)
//            recyclerView.findViewHolderForAdapterPosition(cuInd).itemView.performClick();
            recyclerView.getChildAt(cuInd).performClick();
        cuInd = getAdapterPosition();


    }

    @Override
    public void collapse() {
        super.collapse();
        imgCtrl.setImageResource(R.drawable.ic_down_arrow);
        cuInd = -1;

    }

    @Override
    public void setOnGroupClickListener(OnGroupClickListener listener) {
        super.setOnGroupClickListener(listener);
    }


}
