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
import java.util.ArrayList;

/**
 * Created by root on 04/04/16.
 */
public class HomeController extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "":
                this.showHome(request,response);
                return;
            default:
                return;
        }

    }

    public void showHome(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/views/welcome.jsp");
        rd.forward(request,response);

    }

    public void showDashboard(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/home.jsp");
        String newUsername = "usr"+Main.naiveBayesModel.getDataset().items.size();
        System.out.println("new username: "+newUsername);
        Main.naiveBayesModel.registerNewUser(newUsername);
        HttpSession session = request.getSession();
        session.setAttribute("username", newUsername);
        //ArrayList<Item> items = Main.model.makeTopNRecommendation(newUsername, 100);
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM product LIMIT 10");
        ResultSet rs = db.execute();
        ArrayList<Item> items = new ArrayList<>();
        try {
            while(rs.next()){
                Item item = new Item();
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                items.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        request.setAttribute("items", items);
        rd.forward(request,response);
    }
}
