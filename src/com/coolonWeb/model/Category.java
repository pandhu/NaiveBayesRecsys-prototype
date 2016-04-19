package com.coolonWeb.model;

import com.coolonWeb.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 19/04/16.
 */
public class Category {
    public String name;
    public String lvl1;
    public String lvl2;
    public String lvl3;

    public  static ArrayList<Category> getCategoryLvl1(){
        ArrayList<Category> categories = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM product GROUP BY LV1_CATEGORY");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Category category = new Category();
                //Retrieve by column name
                category.name = rs.getString("LV1_CATEGORY");
                category.lvl1 = rs.getString("LV1_CATEGORY");
                category.lvl2 = rs.getString("LV2_CATEGORY");
                category.lvl3 = rs.getString("LV3_CATEGORY");

                categories.add(category);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return categories;
    }

    public  static ArrayList<Category> getCategoryLvl2(String lvl1){
        ArrayList<Category> categories = new ArrayList<>();
        DBConnect db = new DBConnect();
        String sql = "SELECT * FROM product WHERE LV1_CATEGORY = '"+lvl1+"' GROUP BY LV2_CATEGORY";
        db.setSql(sql);
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Category category = new Category();
                //Retrieve by column name
                category.name = rs.getString("LV2_CATEGORY");
                category.lvl1 = rs.getString("LV1_CATEGORY");
                category.lvl2 = rs.getString("LV2_CATEGORY");
                category.lvl3 = rs.getString("LV3_CATEGORY");

                categories.add(category);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return categories;
    }

    public  static ArrayList<Category> getCategoryLvl3(String lvl1, String lvl2){
        ArrayList<Category> categories = new ArrayList<>();
        DBConnect db = new DBConnect();
        String sql = "SELECT * FROM product WHERE LV2_CATEGORY = '"+lvl2+"' AND LV1_CATEGORY = '"+lvl1+"' GROUP BY LV3_CATEGORY";
        db.setSql(sql);
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                Category category = new Category();
                //Retrieve by column name
                category.name = rs.getString("LV3_CATEGORY");
                category.lvl1 = rs.getString("LV1_CATEGORY");
                category.lvl2 = rs.getString("LV2_CATEGORY");
                category.lvl3 = rs.getString("LV3_CATEGORY");
                categories.add(category);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return categories;
    }

}
