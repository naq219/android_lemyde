package com.project.shop.lemy.listener;

import com.telpoo.frame.object.BaseObject;

import java.util.ArrayList;

/**
 * Created by Asus-GL on 03-Jul-18.
 */

public class Listerner {
    public interface OncChildMenulick {
        void onClick(String childMenu);
    }

    public interface OnEditclick {
        void clickEdit(int position);
    }

    public interface setOnclickDelete {
        void clickDelete(int position);
    }

    public interface setOnclickUpdate {
        void clickUpdate(int  status, int position);
    }

    public interface OnClick {
        void clickView();
    }

    public interface onClickTotalAmount {
        void clickTotalamount(String totalamount);
    }

    public interface setOnclickAdd {
        void clickAdd(String phone);
    }

    public interface setOnclickItem {
        void clickItem(BaseObject object, int position);
    }

    public interface OnclickEdit {
        void clickItem(BaseObject object, int position);
    }

    public interface OnItemCheckboxClick {
        void clickItemCheckboxClick(int position, BaseObject object);
    }

    public interface OnAllItemCheckedListerner {
        void onAllChecked(boolean checked);
    }

    public interface OnPoupMenuItemClick {
        void onItemClick(String key);
    }

    public interface OnListItemChangeList {
        void onChange(int size);
    }

    public interface OnOkDialogClick {
        void onOkDialogClick(BaseObject object);
    }
    public interface OnDeleteItemClick {
        void clickDeleteItem(BaseObject object, int position);
    }

    public interface YesNoDialog {
        void onClickYesNo(boolean isYes);
    }

    public interface callBack {
        void onCallBackListerner(Object oj1,Object oj2,Object oj3);
    }
}

