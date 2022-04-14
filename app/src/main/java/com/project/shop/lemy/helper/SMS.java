package com.project.shop.lemy.helper;

import com.lemy.telpoo2lib.model.BObject;
import com.project.shop.lemy.common.MyObject;

import java.util.Date;

class SMS extends BObject {
    private final String phoneNumber;
    private final String smsContent;
    private final int type;
    private final String date;

    public SMS(String phoneNumber, String smsContent, int type, String date) {
        this.phoneNumber = phoneNumber;
        this.smsContent = smsContent;
        this.type = type;
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
