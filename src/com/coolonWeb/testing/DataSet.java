package com.coolonWeb.testing;

import com.coolonWeb.model.Item;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 03/05/16.
 */
public class DataSet {
    public ArrayList<Transaction> transactions;
    public ArrayList<String> users;
    public ArrayList<String> items;
    public HashMap<String, ArrayList<String>> userInterests;


    public DataSet(){
        this.transactions = new ArrayList<>();
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.userInterests = new HashMap<>();
    }
}
