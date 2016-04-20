package com.coolonWeb.controller;

import com.coolonWeb.Main;
import com.coolonWeb.model.Category;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 20/04/16.
 */
public class ColdStartTestController extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "/chooseItem":
                this.showChooseItemPage(request,response);
                return;
            default:
                return;
        }

    }

    public void showChooseItemPage(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        ArrayList<Transaction> historyTransactions = user.getAllTransactions();
        ArrayList<Category> categories = Category.getCategoryLvl1();
        request.setAttribute("historyTransaction", historyTransactions);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/views/chooseItemPage.jsp").forward(request, response);
    }
}
