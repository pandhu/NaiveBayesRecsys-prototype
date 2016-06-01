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
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
            case "/chooseItem":
                chooseInitialItem(request,response);
                return;
            case "/scenarioExplaination":
                scenarioExplaination(request,response);
                return;
            case "/transactionHistory":
                showHistoryTransaction(request,response);
                return;
            case "/testTime/part1":
                testTimePart1(request,response);
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
            case "/testTime/part2":
                testTimePart2(request,response);
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
            case "/testTime/part3":
                testTimePart3(request,response);
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
            case "/thanks":
                thanks(request,response);
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
            case "/submitRelevanceTest":
                submitRelevanceTest(request, response);
                return;
            default:
                return;
        }
    }

    public void welcome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/views/welcome.jsp").forward(request, response);
    }
    public void scenarioExplaination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/views/scenarioExplaination.jsp").forward(request, response);
    }
    public void basicInformation(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/basicInformation.jsp").forward(request, response);
    }

    public void submitBasicInformation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String isEver = request.getParameter("isEver");
        if(request.getParameter("age").equals("") || gender == null){
            request.setAttribute("error", "Error, form tidak diisi dengan benar");
            response.sendRedirect(Config.SITE_URL+"/survey/basicInformation");
        }
        int age = Integer.parseInt(request.getParameter("age"));
        User user = new User();
        user.gender = gender;
        user.email = email;
        user.phone = phone;
        user.isEver = isEver;
        user.id = (""+System.currentTimeMillis()).substring(0,9);
        String ageGroup;
        if(age < 18)
            ageGroup = "<18";
        else if(age <= 24)
            ageGroup = "18-24";
        else if (age <=34)
            ageGroup = "25-34";
        else if(age <= 35)
            ageGroup = "35-44";
        else
            ageGroup = ">44";
        user.ageGroup = ageGroup;
        registerNewUser(user);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect(Config.SITE_URL+"/survey/scenarioExplaination");
    }
    public void chooseInitialItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String q = "";
        q = request.getParameter("q");
        String query = "SELECT * FROM purchase_stage_1 WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC";
        DBConnect db = new DBConnect();
        db.setSql(query);
        ArrayList<Item> items = new ArrayList<>();
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
        db.closeConnection();
        ArrayList<Item> resultAbsoluteSearch = searchAbsolute(q);
        ArrayList<Item> resultSearch = searchAbsolute(q);
        ArrayList<Item> result = new ArrayList<>();
        for(Item item : resultAbsoluteSearch){
            if(resultSearch.contains(item)){
                resultSearch.remove(resultSearch.indexOf(item));
            }
            result.add(item);
        }
        for(Item item: resultSearch){
            result.add(item);
        }
        request.setAttribute("historyItems", items);
        request.setAttribute("resultItems", result);
        request.getRequestDispatcher("/views/browseItem.jsp").forward(request, response);
    }

    public ArrayList<Item> searchAbsolute(String query){
        String queryBuilder = "SELECT * FROM product WHERE PRODUCT_NAME LIKE '%"+query+"%' LIMIT 20";
        DBConnect db = new DBConnect();
        db.setSql(queryBuilder);
        ResultSet rs = db.execute();
        ArrayList<Item> items = new ArrayList<>();
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
        db.closeConnection();
        return items;
    }
    public ArrayList<Item> search(String query){
        if(query == null){
            return new ArrayList<Item>();
        }
        String[] words = query.split(" ");
        String queryBuilder = "WHERE ";
        for(String word : words){
            queryBuilder += "PRODUCT_NAME LIKE '%"+word+"%' OR ";
        }
        queryBuilder = queryBuilder.substring(0, queryBuilder.length()-3);
        queryBuilder += " LIMIT 100";
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM product "+queryBuilder);
        ArrayList<Item> items = new ArrayList<>();
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
        db.closeConnection();
        return items;
    }
    public void buyItem(String idUser, String idItem){

    }


    public void registerNewUser(User newUser){
        DBConnect db = new DBConnect();
        String query = "INSERT INTO member (MEM_NO_ENC, AGE_GROUP, GENDER) values ("+ newUser.id+",'"+ newUser.ageGroup+"','"+ newUser.gender+"')";
        db.setSql(query);
        db.executeUpdate();
        db.closeConnection();
        Main.naiveBayesModel1.getDataset().users.add(newUser.id);
        Main.naiveBayesModel2.getDataset().users.add(newUser.id);
        Main.naiveBayesModel3.getDataset().users.add(newUser.id);
    }
    public void showHistoryTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnect db = new DBConnect();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int age = Integer.parseInt(user.ageGroup);
        String ageGroup;
        if(age < 18)
            ageGroup = "<18";
        else if(age <= 24)
            ageGroup = "18-24";
        else if (age <=34)
            ageGroup = "25-34";
        else if(age <= 35)
            ageGroup = "35-44";
        else
            ageGroup = ">44";

        String gender = user.gender;
        String query ="SELECT member.MEM_NO_ENC, count(member_product.MEM_NO_ENC) as products FROM (SELECT DISTINCT MEM_NO_ENC, PRODUCT_NUMBER_ENC FROM purchase_stage_1) member_product, member where member_product.MEM_NO_ENC = member.MEM_NO_ENC and member.AGE_GROUP = '"+ageGroup+"' and member.GENDER = '"+gender+"' GROUP BY member_product.MEM_NO_ENC having count(member_product.PRODUCT_NUMBER_ENC) = 3";
        System.out.println(query);
        db.setSql(query);
        ResultSet rs = db.execute();
        ArrayList<String> possibleUsers = new ArrayList<>();
        try {
            while(rs.next()){
                //Retrieve by column name
                possibleUsers.add(rs.getString("MEM_NO_ENC"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(possibleUsers.size());
        int index = ((int) (Math.random()* (possibleUsers.size()+1)))-1;

        String idUser = possibleUsers.get(index);
        user.id = idUser;
        session.setAttribute("user", user);
        ArrayList<Item> items = new ArrayList<>();
        db = new DBConnect();
        query = "SELECT * FROM purchase_stage_1 WHERE MEM_NO_ENC = "+user.id+" group by PRODUCT_NUMBER_ENC";
        System.out.println(query);
        db.setSql(query);
        rs = db.execute();
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
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.memoryBasedModelStage1.getRecommendationByUser(user.id);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart1Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");

        request.setAttribute("nextUrl", "/survey/testTime/methodB/part1");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart1", input);

        User user = (User) request.getSession().getAttribute("user");
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart1Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "/survey/testRelevance/part1");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart1", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage1.getRecommendationByUser(user.id);
        ArrayList<Item> recommendedItems = new ArrayList<>();
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        Collections.shuffle(recommendedItems);

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

        request.setAttribute("recommendedItems", recommendedItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("stage", 1);
        request.setAttribute("nextUrl", "/survey/stage2");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void stage2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart1", input);

        request.getRequestDispatcher("/views/stage2.jsp").forward(request, response);
    }


    public void testTimeMethodAPart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.memoryBasedModelStage2.getRecommendationByUser(user.id);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart2Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");
        request.setAttribute("nextUrl", "/survey/testTime/methodB/part2");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart2", input);

        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart2Time", runtimeMillis);

        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "/survey/testRelevance/part2");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart2", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage2.getRecommendationByUser(user.id);
        ArrayList<Item> recommendedItems = new ArrayList<>();
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        Collections.shuffle(recommendedItems);

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

        request.setAttribute("recommendedItems", recommendedItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("stage", 2);

        request.setAttribute("nextUrl", "/survey/stage3");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void stage3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart2", input);

        request.getRequestDispatcher("/views/stage3.jsp").forward(request, response);
    }

    public void testTimeMethodAPart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.memoryBasedModelStage3.getRecommendationByUser(user.id);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart3Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");
        request.setAttribute("nextUrl", "/survey/testTime/methodB/part3");
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodAPart3", input);

        User user = (User) request.getSession().getAttribute("user");

        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart3Time", runtimeMillis);

        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "/survey/testRelevance/part3");
        request.setAttribute("testStage", "1");
        System.out.println(items.size());
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart3", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage3.getRecommendationByUser(user.id);
        ArrayList<Item> recommendedItems = new ArrayList<>();
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        Collections.shuffle(recommendedItems);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();

        request.setAttribute("recommendedItems", recommendedItems);
        request.setAttribute("historyItems", historyItems);
        request.setAttribute("stage", 3);

        request.setAttribute("nextUrl", "/survey/final");

        request.getRequestDispatcher("/views/showRelevanceTest.jsp").forward(request,response);

    }

    public void testTimePart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("nextUrl", "survey/testTime/methodA/part1");
        request.getRequestDispatcher("/views/timeTestWarning.jsp").forward(request, response);
    }
    public void testTimePart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("nextUrl", "survey/testTime/methodA/part2");
        request.getRequestDispatcher("/views/timeTestWarning.jsp").forward(request, response);
    }
    public void testTimePart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("nextUrl", "survey/testTime/methodA/part3");
        request.getRequestDispatcher("/views/timeTestWarning.jsp").forward(request, response);
    }

    public void stageFinal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testRelevancePart3", input);

        request.getRequestDispatcher("/views/final.jsp").forward(request, response);
    }
    public void submitRelevanceTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] selectedItems = request.getParameterValues("selectedItem");
        int stage = Integer.parseInt(request.getParameter("stage"));
        int selectedMethodA = 0;
        int selectedMethodB = 0;
        if(selectedItems != null){
            for(String selectedItem : selectedItems){
                if(selectedItem.equals("1"))
                    selectedMethodA++;
                else
                    selectedMethodB++;
            }
        }
        request.getSession().setAttribute("testRelevancePart"+stage+"A", selectedMethodA);
        request.getSession().setAttribute("testRelevancePart"+stage+"B", selectedMethodB);
        if(stage < 3)
            response.sendRedirect(Config.SITE_URL+"/survey/stage"+(stage+1));
        else
            response.sendRedirect(Config.SITE_URL+"/survey/final");
    }
    public void submit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String testTimeMethodAPart1 = (String) session.getAttribute("testTimeMethodAPart1");
        int testTimeMethodAPart1Time = ((Long) session.getAttribute("testTimeMethodAPart1Time")).intValue();
        String testTimeMethodAPart2 = (String) session.getAttribute("testTimeMethodAPart2");
        int testTimeMethodAPart2Time = ((Long)session.getAttribute("testTimeMethodAPart2Time")).intValue();
        String testTimeMethodAPart3 = (String) session.getAttribute("testTimeMethodAPart3");
        int testTimeMethodAPart3Time = ((Long) session.getAttribute("testTimeMethodAPart3Time")).intValue();
        String testTimeMethodBPart1 = (String) session.getAttribute("testTimeMethodBPart1");
        int testTimeMethodBPart1Time =  ((Long)session.getAttribute("testTimeMethodBPart1Time")).intValue();
        String testTimeMethodBPart2 = (String) session.getAttribute("testTimeMethodBPart2");
        int testTimeMethodBPart2Time = ((Long)session.getAttribute("testTimeMethodBPart2Time")).intValue();
        String testTimeMethodBPart3 = (String) session.getAttribute("testTimeMethodBPart3");
        int testTimeMethodBPart3Time =  ((Long)session.getAttribute("testTimeMethodBPart3Time")).intValue();
        int testRelevancePart1A = (int) session.getAttribute("testRelevancePart1A");
        int testRelevancePart1B = (int) session.getAttribute("testRelevancePart1B");
        int testRelevancePart2A = (int) session.getAttribute("testRelevancePart2A");
        int testRelevancePart2B = (int) session.getAttribute("testRelevancePart2B");
        int testRelevancePart3A = (int) session.getAttribute("testRelevancePart3A");
        int testRelevancePart3B = (int) session.getAttribute("testRelevancePart3B");
        DBConnect db = new DBConnect();
        String sql = "INSERT INTO survey (email,hp,age,gender,is_ever,test_time_method_a_part_1,test_time_method_a_part_1_time,test_time_method_a_part_2,test_time_method_a_part_2_time,test_time_method_a_part_3,test_time_method_a_part_3_time,test_time_method_b_part_1,test_time_method_b_part_1_time,test_time_method_b_part_2,test_time_method_b_part_2_time,test_time_method_b_part_3,test_time_method_b_part_3_time, test_relevance_part_1_a, test_relevance_part_1_b, test_relevance_part_2_a,test_relevance_part_2_b, test_relevance_part_3_a, test_relevance_part_3_b)";
        sql = sql + "values ('"+user.email+"','"+user.phone+"',"+user.ageGroup+",'"+user.gender+"',"+user.isEver+","+testTimeMethodAPart1+","+testTimeMethodAPart1Time+","+testTimeMethodAPart2+","+testTimeMethodAPart2Time+","+testTimeMethodAPart3+","+testTimeMethodAPart3Time+","+testTimeMethodBPart1+","+testTimeMethodBPart1Time+","+testTimeMethodBPart2+","+testTimeMethodBPart2Time+","+testTimeMethodBPart3+","+testTimeMethodBPart3Time+","+testRelevancePart1A+","+testRelevancePart1B+","+testRelevancePart2A+","+testRelevancePart2B+","+testRelevancePart3A+","+testRelevancePart3B+")";
        System.out.println(sql);
        db.setSql(sql);
        db.executeUpdate();
        db.closeConnection();

        response.sendRedirect(Config.SITE_URL+"/survey/thanks");
    }
    
    public void thanks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/thanks.jsp").forward(request,response);
    }
}
