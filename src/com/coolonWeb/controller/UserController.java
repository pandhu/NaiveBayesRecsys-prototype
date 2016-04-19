package com.coolonWeb.controller;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import javax.jms.Session;
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
 * Created by root on 19/04/16.
 */
public class UserController extends HttpServlet{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        if(path == null) path = "";
        switch (path) {
            case "":
                return;
            case "/transactions":
                this.viewTransactions(request, response);
            default:
                return;
        }
    }

    public void viewTransactions(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ArrayList<Transaction> transactions = user.getAllTransactions();
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/transaction.jsp").forward(request,response);
    }
}
