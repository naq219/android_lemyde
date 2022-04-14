package com.project.shop.lemy.db;


import com.project.shop.lemy.bean.ItemObj;
import com.project.shop.lemy.bean.SmsObj;

public class DbConfig {
    public static final String[] tables = {"shop", "sms"};
    public static final String[][] keys = {ItemObj.keysdb, SmsObj.keysdb};
    public static final String dbName = "LeMy";
    public static final Integer dbVersion = 1;
    public static final String SHOP = tables[0];
    public static final String SMS = tables[1];

}