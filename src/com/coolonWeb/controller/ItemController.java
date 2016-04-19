package com.coolonWeb.controller;

import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 11/04/16.
 */
public class ItemController extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        if(path == null) path = "";
        switch (path){
            case "":
                return;
            case "/detail":
                this.itemDetail(request,response);
            default:
                return;
        }


    }

    public static void itemDetail(HttpServletRequest request,
                                  HttpServletResponse response) throws ServletException, IOException {
        String idItem = request.getParameter("id");
        System.out.println(idItem);
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM product WHERE PRODUCT_NUMBER_ENC = "+idItem);
        ResultSet rs = db.execute();
        Item item = new Item();
        try {
            while(rs.next()){
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Item> recommendedItems = Main.model.makeTopNRecommendation(((User) request.getSession().getAttribute("user")).id,10);
        request.setAttribute("item", item);
        request.setAttribute("recommendedItems", recommendedItems);
        request.getRequestDispatcher("/item-detail.jsp").forward(request,response);
    }


}
