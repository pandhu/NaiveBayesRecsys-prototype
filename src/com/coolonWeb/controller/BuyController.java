package com.coolonWeb.controller;

import com.coolonWeb.Main;

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
        HttpSession session = request.getSession(true);
        String username = (String)session.getAttribute("username");
        Main.model.buy(username,idItem);
        response.sendRedirect("/NaiveBayesRecSys/recommend");
    }
}
