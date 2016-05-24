package com.coolonWeb.model;

import com.coolonWeb.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Item{
    public String id;
    public String name;
    public String category1;
    public String category2;
    public String category3;
    public boolean upvoted;
    public byte method;

    public static ArrayList<Item> getItemByCategory(String lvl1, String lvl2, String lvl3){
        ArrayList<Item> items = new ArrayList<>();
        DBConnect db = new DBConnect();
        String sql = "SELECT * FROM product WHERE LV1_CATEGORY = '"+lvl1+"' AND LV2_CATEGORY = '"+lvl2+"' AND LV3_CATEGORY = '"+lvl3+"'";
        db.setSql(sql);
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Item item = new Item();
                //Retrieve by column name
                item.name = rs.getString("PRODUCT_NAME");
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                items.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return items;
    }

    public static Item find(String id){
        DBConnect db = new DBConnect();
        String sql = "SELECT * FROM product WHERE PRODUCT_NUMBER_ENC='"+id+"'";
        db.setSql(sql);
        ResultSet rs = db.execute();
        Item item = new Item();
        try {
            while(rs.next()){
                //Retrieve by column name
                item.name = rs.getString("PRODUCT_NAME");
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return item;
    }
    @Override
    public String toString(){
        return this.id+"#"+this.name;
    }
}

