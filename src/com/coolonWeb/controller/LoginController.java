package com.coolonWeb.controller;

import com.coolonWeb.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by root on 13/04/16.
 */
public class LoginController extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response){
        String path = request.getPathInfo();
        if(path.equals("logout"));
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println(path);
        if(path == null) path = "";
        switch (path){
            case "":
                return;
            case "/doLogin":
                this.login(request,response);
            default:
                return;
        }
    }
    public static void login(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("vusername");
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);
        response.sendRedirect(Config.SITE_URL+"/dashboard");
    }

    public static void logout(){

    }
}
