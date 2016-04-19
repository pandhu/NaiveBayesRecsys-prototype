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
 * Created by root on 19/04/16.
 */
public class CategoryController extends HttpServlet{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        if(path == null) path = "";
        switch (path) {
            case "":
                return;
            case "/lvl1":
                showLvl1(request, response);
                return;
            case "/lvl2":
                showLvl2(request, response);
                return;
            case"/lvl3":
                showItemByCategoryLvl3(request, response);
                return;
            default:
                return;
        }
    }
    public void showLvl1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parent = request.getParameter("cat1");
        System.out.println(parent);
        ArrayList<Category> categories = Category.getCategoryLvl2(parent);
        request.setAttribute("categories", categories);
        request.setAttribute("cat1", parent);
        request.getRequestDispatcher("/category_lvl1.jsp").forward(request, response);
    }

    public void showLvl2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lvl1 = request.getParameter("cat1");
        String lvl2 = request.getParameter("cat2");
        ArrayList<Category> categories = Category.getCategoryLvl3(lvl1,lvl2);
        request.setAttribute("categories", categories);
        request.setAttribute("cat1", lvl1);
        request.setAttribute("cat2", lvl2);
        request.getRequestDispatcher("/category_lvl2.jsp").forward(request, response);
    }

    public void showItemByCategoryLvl3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lvl1 = request.getParameter("cat1");
        String lvl2 = request.getParameter("cat2");
        String lvl3 = request.getParameter("cat3");
        ArrayList<Item> items = Item.getItemByCategory(lvl1, lvl2, lvl3);
        request.setAttribute("cat1", lvl1);
        request.setAttribute("cat2", lvl2);
        request.setAttribute("cat3", lvl3);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/category_lvl3.jsp").forward(request, response);
    }

}
