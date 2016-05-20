package com.coolonWeb.controller;

import com.coolonWeb.Config;
import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.User;
import com.coolonWeb.testing.DataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 13/05/16.
 */
public class SurveyController extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if(path == null) path = "";
        System.out.println(path);
        switch (path){
            case "/welcome":
                welcome(request, response);
                return;
            case "/basicInformation":
                basicInformation(request,response);
                return;
            case "/transactionHistory":
                showHistoryTransaction(request,response);
                return;
            case "/testTime/methodA/part1":
                testTimeMethodAPart1(request,response);
                return;
            case "/testTime/methodB/part1":
                testTimeMethodBPart1(request,response);
                return;
            case "/testRelevance/part1":
                testRelevancePart1(request,response);
                return;
            case "/testTime/methodA/part2":
                testTimeMethodAPart2(request,response);
                return;
            case "/testTime/methodB/part2":
                testTimeMethodBPart2(request,response);
                return;
            case "/testRelevance/part2":
                testRelevancePart2(request,response);
                return;
            case "/testTime/methodA/part3":
                testTimeMethodAPart3(request,response);
                return;
            case "/testTime/methodB/part3":
                testTimeMethodBPart3(request,response);
                return;
            case "/testRelevance/part3":
                testRelevancePart3(request,response);
                return;
            case "/stage2":
                stage2(request,response);
                return;
            case "/stage3":
                stage3(request,response);
                return;
            case "/final":
                stageFinal(request,response);
                return;
            case "/submit":
                submit(request,response);
                return;
            default:
                return;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "/basicInformation":
                submitBasicInformation(request, response);
                return;
            default:
                return;
        }
    }

    public void welcome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/views/welcome.jsp").forward(request, response);
    }

    public void basicInformation(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/basicInformation.jsp").forward(request, response);
    }

    public void submitBasicInformation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");

        User user = new User();
        user.ageGroup = age;
        user.gender = gender;
        user.email = email;
        user.phone = phone;

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect(Config.SITE_URL+"/survey/transactionHistory");
    }

    public void showHistoryTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUser = Main.dataset1.getRandomUser();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user.id = idUser;
        session.setAttribute("user", user);
        ArrayList<Item> items = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase_stage_1 WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                items.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/transactionHistory.jsp").forward(request, response);
    }

    public void testTimeMethodAPart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.memoryBasedModelStage1.getRecommendationByUser(user.id);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testTime/methodB/part1");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart1", input);

        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 10);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testRelevance/part1");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart1", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 10);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage1.getRecommendationByUser(user.id);
        ArrayList<Item> historyItems = new ArrayList<>();

        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase_stage_1 WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                historyItems.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("modelItems", modelItems);
        request.setAttribute("memoryItems", memoryItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("nextUrl", "/survey/stage2");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void stage2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart1", input);

        request.getRequestDispatcher("/views/stage2.jsp").forward(request, response);
    }

    public void showHistoryTransactionStage2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUser = Main.dataset1.getRandomUser();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user.id = idUser;
        session.setAttribute("user", user);
        ArrayList<Item> items = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase_stage_2 WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                items.add(item);
            }
            rs.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/transactionHistory.jsp").forward(request, response);
    }

    public void testTimeMethodAPart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.memoryBasedModelStage2.getRecommendationByUser(user.id);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testTime/methodB/part2");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart2", input);

        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 10);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testRelevance/part2");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart2", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 10);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage2.getRecommendationByUser(user.id);
        ArrayList<Item> historyItems = new ArrayList<>();

        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                historyItems.add(item);
            }
            rs.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("modelItems", modelItems);
        request.setAttribute("memoryItems", memoryItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("nextUrl", "/survey/stage3");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void stage3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart2", input);

        request.getRequestDispatcher("/views/stage3.jsp").forward(request, response);
    }

    public void showHistoryTransactionStage3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUser = Main.dataset1.getRandomUser();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user.id = idUser;
        session.setAttribute("user", user);
        ArrayList<Item> items = new ArrayList<>();
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                items.add(item);
            }
            rs.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/transactionHistory.jsp").forward(request, response);
    }

    public void testTimeMethodAPart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.memoryBasedModelStage3.getRecommendationByUser(user.id);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testTime/methodB/part3");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart3", input);

        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        ArrayList<Item> items = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 10);
        request.setAttribute("items", items);
        request.setAttribute("nextUrl", "/survey/testRelevance/part3");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart3", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 10);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage3.getRecommendationByUser(user.id);
        ArrayList<Item> historyItems = new ArrayList<>();

        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC");
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
                historyItems.add(item);
            }
            rs.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("modelItems", modelItems);
        request.setAttribute("memoryItems", memoryItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("nextUrl", "/survey/final");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void stageFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart3", input);

        request.getRequestDispatcher("/views/final.jsp").forward(request, response);
    }

    public void submit(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String testTimeMethodAPart1 = (String) session.getAttribute("testTimeMethodAPart1");
        String testTimeMethodAPart2 = (String) session.getAttribute("testTimeMethodAPart2");
        String testTimeMethodAPart3 = (String) session.getAttribute("testTimeMethodAPart3");
        String testTimeMethodBPart1 = (String) session.getAttribute("testTimeMethodBPart1");
        String testTimeMethodBPart2 = (String) session.getAttribute("testTimeMethodBPart2");
        String testTimeMethodBPart3 = (String) session.getAttribute("testTimeMethodBPart3");
        String testRelevancePart1 = (String) session.getAttribute("testRelevancePart1");
        String testRelevancePart2 = (String) session.getAttribute("testRelevancePart2");
        String testRelevancePart3 = (String) session.getAttribute("testRelevancePart3");
        DBConnect db = new DBConnect();
        String sql = "INSERT INTO survey (email,hp,age,gender,test_time_method_a_part_1,test_time_method_a_part_2,test_time_method_a_part_3,test_time_method_b_part_1,test_time_method_b_part_2,test_time_method_b_part_3, test_relevance_part_1, test_relevance_part_2, test_relevance_part_3)";
        sql = sql + "values ('"+user.email+"','"+user.phone+"',"+user.ageGroup+",'"+user.gender+"',"+testTimeMethodAPart1+","+testTimeMethodAPart2+","+testTimeMethodAPart3+","+testTimeMethodBPart1+","+testTimeMethodBPart2+","+testTimeMethodBPart3+",'"+testRelevancePart1+"','"+testRelevancePart2+"','"+testRelevancePart3+"')";
        System.out.println(sql);
        db.setSql(sql);
        db.executeUpdate();
        db.closeConnection();
    }
}
