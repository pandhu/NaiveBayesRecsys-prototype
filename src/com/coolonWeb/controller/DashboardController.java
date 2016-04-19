package com.coolonWeb.controller;

import com.coolonWeb.Main;
import com.coolonWeb.model.Category;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 13/04/16.
 */
public class DashboardController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "":
                this.showDashboard(request,response);
                return;
            default:
                return;
        }
    }

    public static void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        ArrayList<Item> items = Main.model.makeTopNRecommendation(user.id,10);
        ArrayList<Category> categories = Category.getCategoryLvl1();
        request.setAttribute("recommendedItems", items);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
