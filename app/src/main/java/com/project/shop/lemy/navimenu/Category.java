package com.project.shop.lemy.navimenu;

import com.telpoo.frame.object.BaseObject;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Asus-GL on 25-Jun-18.
 */

public class Category extends ExpandableGroup<BaseObject> {
    public Category(String title, List<BaseObject> items) {
        super(title, items);
    }
}
