package com.coolonWeb.controller;

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
import java.util.ArrayList;

/**
 * Created by root on 04/04/16.
 */
public class RecommendController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "":
                this.showRecommendation(request,response);
                return;
            case "/topN":
                this.showTopNRecommendation(request, response);
                return;
            default:
                return;
        }
    }
    public void showRecommendation(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/views/recommendation.jsp");
        User user = (User) request.getSession().getAttribute("user");
        ArrayList<Item> recommendedItem = Main.model.makeTopNRecommendation(user.id, 5);
        request.getSession().setAttribute("recommendedItem", recommendedItem);
        rd.forward(request,response);
    }

    public void showTopNRecommendation(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/n-recommendation.jsp").forward(request,response);
    }
}
