package com.coolonWeb.model;

import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 19/04/16.
 */
public class User {
    public String id;
    public String ageGroup;
    public String gender;
    public ArrayList<Item> itemTransactions;

    public User(){
        this.itemTransactions = new ArrayList<>();
    }
    public ArrayList<Transaction> getTransactions(int numberOfTransaction){
        ArrayList<Transaction> transactions = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+this.id+" limit "+numberOfTransaction);
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Transaction transaction = new Transaction();
                //Retrieve by column name
                transaction.user = rs.getString("MEM_NO_ENC");
                transaction.item = rs.getString("PRODUCT_NUMBER_ENC");
                transaction.itemName = rs.getString("PRODUCT_NAME");
                transactions.add(transaction);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+this.id);
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Transaction transaction = new Transaction();
                //Retrieve by column name
                transaction.user = rs.getString("MEM_NO_ENC");
                transaction.item = rs.getString("PRODUCT_NUMBER_ENC");
                transaction.itemName = rs.getString("PRODUCT_NAME");
                transactions.add(transaction);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    public ArrayList<Transaction> getAllTransactionsInModel(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        for(Item item:Main.model.getUserHistoryTransaction(this.id) ){
            Transaction transaction = new Transaction();
            transaction.user = this.id;
            transaction.item = item.id;
            transaction.itemName = item.name;
            transactions.add(transaction);
        }
        return transactions;
    }
    public static User find(String id){
        ArrayList<Transaction> transactions = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM member WHERE MEM_NO_ENC = "+id);
        ResultSet rs = db.execute();
        User user = new User();
        try {
            while(rs.next()){
                //Retrieve by column name
                user.id = rs.getString("MEM_NO_ENC");
                user.ageGroup = rs.getString("AGE_GROUP");
                user.gender = rs.getString("GENDER");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return user;
    }
}
