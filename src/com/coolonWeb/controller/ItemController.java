package com.coolonWeb.controller;

import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;
import com.coolonWeb.model.Item;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by root on 11/04/16.
 */
public class ItemController extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/item-detail.jsp");
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
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("item", item);
        rd.forward(request,response);
    }
}
