package com.coolonWeb.controller;

import com.coolonWeb.Config;
import com.coolonWeb.Main;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by root on 04/04/16.
 */
public class BuyController extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String idItem = request.getParameter("item");
        System.out.println(idItem);
        System.out.println(request.getHeader("referer"));
        User user = (User) request.getSession().getAttribute("user");
        Main.model.buy(user.id, idItem);
        Item item = Item.find(idItem);
        user.itemTransactions.add(item);
        if (user.itemTransactions.size() < 5    ) {
            response.sendRedirect(request.getHeader("referer"));
        } else {
            response.sendRedirect(Config.SITE_URL+"/recommendation");
        }
    }
}
