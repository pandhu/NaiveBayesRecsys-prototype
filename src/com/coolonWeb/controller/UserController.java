package com.coolonWeb.controller;

import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;
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
                return;
            case "/register":
                this.viewPageRegister(request, response);
                return;
            default:
                return;
        }
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        if(path == null) path = "";
        switch (path) {
            case "":
                return;
            case "/register":
                this.registerUser(request, response);
                return;
            default:
                return;
        }
    }

    public void viewTransactions(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ArrayList<Transaction> transactions = user.getAllTransactionsInModel();
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/transaction.jsp").forward(request,response);
    }

    public void viewPageRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/views/register.jsp").forward(request,response);
    }

    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User newUser = new User();
        int count = Main.naiveBayesModel.getDataset().users.size();
        newUser.id = count+"";
        Main.naiveBayesModel.registerNewUser(newUser.id);
        request.setAttribute("user", newUser);
        request.getSession().setAttribute("user", newUser);
        response.sendRedirect("/coldStartTest/chooseItem");
    }
}
