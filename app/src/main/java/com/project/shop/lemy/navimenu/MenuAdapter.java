package com.project.shop.lemy.navimenu;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.shop.lemy.R;
import com.project.shop.lemy.bean.MenuObj;
import com.project.shop.lemy.listener.Listerner;
import com.telpoo.frame.object.BaseObject;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class MenuAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder, ListMenuViewHolder> {
    List<? extends ExpandableGroup> groups;
    int position;
    RecyclerView recyclerView;
    Listerner.OncChildMenulick childMenu;

    public MenuAdapter(List<? extends ExpandableGroup> groups, RecyclerView recyclerView) {
        super(groups);
        this.groups = groups;
        this.recyclerView = recyclerView;
    }

    CategoryViewHolder categoryViewHolder;

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_category, parent, false);
        return categoryViewHolder = new CategoryViewHolder(view, recyclerView);
    }

    @Override
    public ListMenuViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_child, parent, false);
        return new ListMenuViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ListMenuViewHolder holder, int flatPosition, ExpandableGroup group, final int childIndex) {
        final BaseObject baseObject = (BaseObject) group.getItems().get(childIndex);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childMenu.onClick(baseObject.get(MenuObj.childMenu));
            }
        });
        holder.onBind(baseObject, childIndex);
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group, flatPosition);
    }

    public void setOnclickChildMenu(Listerner.OncChildMenulick childMenu) {
        this.childMenu = childMenu;

    }
}
