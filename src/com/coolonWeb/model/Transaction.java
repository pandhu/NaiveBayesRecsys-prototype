package com.coolonWeb.model;

/**
 * Created by root on 04/04/16.
 */
public class Transaction{
    public String user;
    public String item;
    public String itemName;
    public String ordmonth;
    public String lv1cat;
    public String lv2cat;
    public String lv3cat;
    public String opt;

    @Override
    public String toString(){
        return user+" buys "+item;
    }
}

