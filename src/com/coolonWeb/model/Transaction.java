package com.coolonWeb.model;

/**
 * Created by root on 04/04/16.
 */
public class Transaction{
    public String user;
    public String item;

    @Override
    public String toString(){
        return user+" buys "+item;
    }
}

