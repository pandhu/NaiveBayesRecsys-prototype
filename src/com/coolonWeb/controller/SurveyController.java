package com.coolonWeb.controller;

import com.coolonWeb.Config;
import com.coolonWeb.DBConnect;
import com.coolonWeb.Main;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.MemoryBasedModel;
import com.coolonWeb.model.NaiveBayesModel;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

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
            case "/buy":
                buyItem(request,response);
                return;
            case "/chooseItem":
                chooseInitialItem(request,response);
                return;
            case "/scenarioExplaination":
                scenarioExplaination(request,response);
                return;
            case "/transactionHistory":
                showHistoryTransactionUser(request,response);
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
            case "/stressTest":
                testRecommendation(request,response);
                return;
            case "/methodA/part1":
                methodWarning1A(request,response);
                return;
            case "/methodA/part2":
                methodWarning2A(request,response);
                return;
            case "/methodA/part3":
                methodWarning3A(request,response);
                return;
            case "/methodB/part1":
                methodWarning1B(request,response);
                return;
            case "/methodB/part2":
                methodWarning2B(request,response);
                return;
            case "/methodB/part3":
                methodWarning3B(request,response);
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
                try {
                    submitBasicInformation(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return;
            case "/submitRelevanceTest":
                submitRelevanceTest(request, response);
                return;
            case "/test/submit":
                submitFormTest(request, response);
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

    public void submitBasicInformation(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
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
        user.id = (""+System.currentTimeMillis());
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
    public void registerNewUser(User newUser) throws SQLException {
        DBConnect db = new DBConnect();
        String query = "INSERT INTO member (MEM_NO_ENC, AGE_GROUP, GENDER) values (?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(query);
        preparedStatement.setString(1, newUser.id);
        preparedStatement.setString(2, newUser.ageGroup);
        preparedStatement.setString(3, newUser.gender);
        preparedStatement.executeUpdate();
        db.closeConnection();
        Main.naiveBayesModel1.getDataset().users.add(newUser.id);
        Main.naiveBayesModel1.getDataset().userInterests.put(newUser.id, new ArrayList<String>());
        Main.naiveBayesModel2.getDataset().users.add(newUser.id);
        Main.naiveBayesModel2.getDataset().userInterests.put(newUser.id, new ArrayList<String>());
        Main.naiveBayesModel3.getDataset().users.add(newUser.id);
        Main.naiveBayesModel3.getDataset().userInterests.put(newUser.id, new ArrayList<String>());

    }
    public void chooseInitialItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if(user.itemTransactions.size()>=3){
            response.sendRedirect(Config.SITE_URL+"/survey/transactionHistory");
            return;
        }
        String q = "";
        q = request.getParameter("q");
        String query = "SELECT * FROM purchase_stage_1 WHERE MEM_NO_ENC = ? group by PRODUCT_NUMBER_ENC";
        DBConnect db = new DBConnect();
        ArrayList<Item> items = new ArrayList<>();
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, user.id);
            rs = preparedStatement.executeQuery();
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
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        ArrayList<Item> resultAbsoluteSearch = searchAbsolute(q);
        ArrayList<Item> resultSearch = search(q);
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
        String queryBuilder = "SELECT purchase_stage_1.*, count(PRODUCT_NUMBER_ENC) as sum FROM purchase_stage_1 WHERE PRODUCT_NAME LIKE ? GROUP BY(PRODUCT_NUMBER_ENC) ORDER BY sum DESC LIMIT 20";
        DBConnect db = new DBConnect();
        ArrayList<Item> items = new ArrayList<>();
        ResultSet rs;
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(queryBuilder);
            preparedStatement.setString(1, "%"+query+"%");
            rs = preparedStatement.executeQuery();
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
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return items;
    }

    public ArrayList<Item> search(String query){
        String queryBuilder = "WHERE ";
        String[] words = new String[0];
        if(query == null){
            queryBuilder = " GROUP BY(PRODUCT_NUMBER_ENC) ORDER BY sum DESC LIMIT 20";
        } else {
            words = query.split(" ");
            for (String word : words) {
                queryBuilder += "PRODUCT_NAME LIKE ? OR ";
            }
            queryBuilder = queryBuilder.substring(0, queryBuilder.length() - 3);
            queryBuilder += " GROUP BY(PRODUCT_NUMBER_ENC) ORDER BY sum DESC LIMIT 20";
        }
        DBConnect db = new DBConnect();
        queryBuilder = "SELECT purchase_stage_1.*, count(PRODUCT_NUMBER_ENC) as sum FROM purchase_stage_1 "+queryBuilder;

        ArrayList<Item> items = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(queryBuilder);

            for(int ii = 0; ii< words.length;ii++){
                preparedStatement.setString(ii+1, "%"+words[ii]+"%");
            }
            ResultSet rs = preparedStatement.executeQuery();

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
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return items;
    }
    public void buyItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        String idItem = request.getParameter("id");
        Item current = new Item();
        current.id = idItem;
        ArrayList<Item> history = getUserHistory(user);
        if(history.contains(current)){
            request.getSession().setAttribute("error", "Tidak dapat membeli barang yang sama");
            response.sendRedirect(Config.SITE_URL+"/survey/chooseItem");
        } else {
            buyItem(user, idItem);
            request.getSession().setAttribute("success", "Barang berhasil ditambahkan ke riwayat transaksi");
            response.sendRedirect(Config.SITE_URL + "/survey/chooseItem");
        }
    }
    public void buyItem(User user, String idItem){
        DBConnect db = new DBConnect();
        String query = "SELECT * FROM product WHERE PRODUCT_NUMBER_ENC = ?";
        ArrayList<Item> items = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, idItem);
            ResultSet rs = preparedStatement.executeQuery();
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
            preparedStatement.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(items.size() == 0){
            return;
        }
        user.itemTransactions.add(items.get(0));
        MemoryBasedModel respondenPurchase = new MemoryBasedModel();
        respondenPurchase.purchaseTable = "responden_purchase";
        buyMemoryBased(user.id, items.get(0), respondenPurchase);

        buyMemoryBased(user.id, items.get(0), Main.memoryBasedModelStage1);
        buyMemoryBased(user.id, items.get(0), Main.memoryBasedModelStage2);
        buyMemoryBased(user.id, items.get(0), Main.memoryBasedModelStage3);
        buyModelBased(user.id, items.get(0), Main.naiveBayesModel1);
        buyModelBased(user.id, items.get(0), Main.naiveBayesModel2);
        buyModelBased(user.id, items.get(0), Main.naiveBayesModel3);

    }
    public void buyMemoryBased(String idUser, Item item, MemoryBasedModel model){
        String todayAsString = new SimpleDateFormat("ddMMyy").format(new Date());
        DBConnect db = new DBConnect();
        String query = "INSERT INTO "+model.purchaseTable+" (ORD_MONTH, MEM_NO_ENC, PRODUCT_NUMBER_ENC, PRODUCT_NAME, LV1_CATEGORY, LV2_CATEGORY, LV3_CATEGORY, OPTIONS)";
        query += " values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, todayAsString);
            preparedStatement.setString(2, idUser);
            preparedStatement.setString(3, item.id);
            preparedStatement.setString(4, item.name);
            preparedStatement.setString(5, item.category1);
            preparedStatement.setString(6, item.category2);
            preparedStatement.setString(7, item.category3);
            preparedStatement.setString(8, "survey");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(query);
        db.closeConnection();
        return;
    }
    public void buyModelBased(String idUser, Item item, NaiveBayesModel model){
        ArrayList<String> userInterest = model.getDataset().userInterests.get(idUser);
        userInterest.add(item.id);
        return;
    }
    public void showHistoryTransactionUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String query = "SELECT * FROM purchase WHERE MEM_NO_ENC = ?";
        DBConnect db = new DBConnect();
        ArrayList<Item> items = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, user.id);
            ResultSet rs = preparedStatement.executeQuery();
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
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/transactionHistory.jsp").forward(request, response);

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
        ArrayList<Item> items = Main.memoryBasedModelStage1.getRecommendationByUser(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart1Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");
        request.setAttribute("identifier", "testRelevancePart1A");
        request.setAttribute("nextUrl", "/survey/methodB/part1");
        request.setAttribute("historyItems", getUserHistory(user));
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart1Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("identifier", "testRelevancePart1B");
        request.setAttribute("nextUrl", "/survey/stage2");
        request.setAttribute("historyItems", getUserHistory(user));
        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel1.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage1.getRecommendationByUser(user.id, 5);
        ArrayList<Item> recommendedItems = new ArrayList<>();
        ArrayList<Item> both = new ArrayList<>();
        for(Item item : modelItems){
            if(memoryItems.contains(item)){

                both.add(item);
                modelItems.remove(item);
                memoryItems.remove(item);
            }
        }
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        for(Item item: both){
            item.method= 3;
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
        ArrayList<Item> items = Main.memoryBasedModelStage2.getRecommendationByUser(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart2Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");
        request.setAttribute("identifier", "testRelevancePart2A");
        request.setAttribute("nextUrl", "/survey/methodB/part2");
        request.setAttribute("historyItems", getUserHistory(user));

        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.id);
        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart2Time", runtimeMillis);

        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("identifier", "testRelevancePart2B");
        request.setAttribute("nextUrl", "/survey/stage3");
        request.setAttribute("historyItems", getUserHistory(user));

        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart2", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel2.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage2.getRecommendationByUser(user.id, 5);
        ArrayList<Item> recommendedItems = new ArrayList<>();
        ArrayList<Item> both = new ArrayList<>();
        for(Item item : modelItems){
            if(memoryItems.contains(item)){
                both.add(item);
                modelItems.remove(item);
                memoryItems.remove(item);
            }
        }
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        for(Item item: both){
            item.method= 3;
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
        ArrayList<Item> items = Main.memoryBasedModelStage3.getRecommendationByUser(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodAPart3Time", runtimeMillis);
        request.setAttribute("items", items);
        request.setAttribute("method", "Metode A");
        request.setAttribute("identifier", "testRelevancePart3A");
        request.setAttribute("nextUrl", "/survey/methodB/part3");
        request.setAttribute("historyItems", getUserHistory(user));

        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testTimeMethodBPart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        long startMillis = System.currentTimeMillis();
        ArrayList<Item> items = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 5);
        long endMilis = System.currentTimeMillis();
        long runtimeMillis = endMilis - startMillis;
        request.getSession().setAttribute("testTimeMethodBPart3Time", runtimeMillis);

        request.setAttribute("items", items);
        request.setAttribute("method", "Metode B");
        request.setAttribute("identifier", "testRelevancePart3B");
        request.setAttribute("nextUrl", "/survey/final");
        request.setAttribute("testStage", "1");
        request.setAttribute("historyItems", getUserHistory(user));

        request.getRequestDispatcher("/views/timeTest.jsp").forward(request,response);
    }

    public void testRelevancePart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String input = request.getParameter("input");
        request.getSession().setAttribute("testTimeMethodBPart3", input);

        User user = (User)request.getSession().getAttribute("user");
        ArrayList<Item> modelItems = Main.naiveBayesModel3.makeTopNRecommendation(user.id, 5);
        ArrayList<Item> memoryItems = Main.memoryBasedModelStage3.getRecommendationByUser(user.id, 5);
        ArrayList<Item> both = new ArrayList<>();
        for(Item item : modelItems){
            if(memoryItems.contains(item)){
                both.add(item);
                modelItems.remove(item);
                memoryItems.remove(item);
            }
        }
        ArrayList<Item> recommendedItems = new ArrayList<>();
        for(Item item : memoryItems){
            item.method = 1;
            recommendedItems.add(item);
        }
        for(Item item: modelItems){
            item.method = 2;
            recommendedItems.add(item);
        }
        for(Item item: both){
            item.method= 3;
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
        request.setAttribute("nextUrl", "survey/methodA/part1");
        request.getRequestDispatcher("/views/timeTestWarning.jsp").forward(request, response);
    }
    public void testTimePart2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("nextUrl", "survey/methodA/part2");
        request.getRequestDispatcher("/views/timeTestWarning.jsp").forward(request, response);
    }
    public void testTimePart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("nextUrl", "survey/methodA/part3");
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
            User user = (User) request.getSession().getAttribute("user");
            for(String selectedItem : selectedItems){
                String[] selectedItemSplit = selectedItem.split("-");

                if(selectedItemSplit[0].equals("1"))
                    selectedMethodA++;
                else if (selectedItemSplit[0].equals("2"))
                    selectedMethodB++;
                else {
                    selectedMethodA++;
                    selectedMethodB++;
                }
                buyItem(user,selectedItemSplit[1]);
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
        String testTimeMethodAPart1 = (String) session.getAttribute("testRelevancePart1AT");
        int testTimeMethodAPart1Time = ((Long) session.getAttribute("testTimeMethodAPart1Time")).intValue();
        String testTimeMethodAPart2 = (String) session.getAttribute("testRelevancePart2AT");
        int testTimeMethodAPart2Time = ((Long)session.getAttribute("testTimeMethodAPart2Time")).intValue();
        String testTimeMethodAPart3 = (String) session.getAttribute("testRelevancePart3AT");
        int testTimeMethodAPart3Time = ((Long) session.getAttribute("testTimeMethodAPart3Time")).intValue();
        String testTimeMethodBPart1 = (String) session.getAttribute("testRelevancePart1BT");
        int testTimeMethodBPart1Time =  ((Long)session.getAttribute("testTimeMethodBPart1Time")).intValue();
        String testTimeMethodBPart2 = (String) session.getAttribute("testRelevancePart2BT");
        int testTimeMethodBPart2Time = ((Long)session.getAttribute("testTimeMethodBPart2Time")).intValue();
        String testTimeMethodBPart3 = (String) session.getAttribute("testRelevancePart3BT");
        int testTimeMethodBPart3Time =  ((Long)session.getAttribute("testTimeMethodBPart3Time")).intValue();
        int testRelevancePart1A = (int) session.getAttribute("testRelevancePart1A");
        int testRelevancePart1B = (int) session.getAttribute("testRelevancePart1B");
        int testRelevancePart2A = (int) session.getAttribute("testRelevancePart2A");
        int testRelevancePart2B = (int) session.getAttribute("testRelevancePart2B");
        int testRelevancePart3A = (int) session.getAttribute("testRelevancePart3A");
        int testRelevancePart3B = (int) session.getAttribute("testRelevancePart3B");
        String testRelevancePart1ADetails = (String) session.getAttribute("testRelevancePart1ADetails");
        String testRelevancePart1BDetails = (String) session.getAttribute("testRelevancePart1BDetails");
        String testRelevancePart2ADetails = (String) session.getAttribute("testRelevancePart2ADetails");
        String testRelevancePart2BDetails = (String) session.getAttribute("testRelevancePart2BDetails");
        String testRelevancePart3ADetails = (String) session.getAttribute("testRelevancePart3ADetails");
        String testRelevancePart3BDetails = (String) session.getAttribute("testRelevancePart3BDetails");

        DBConnect db = new DBConnect();
        String sql = "INSERT INTO survey (MEM_NO_ENC, email,hp,age,gender,is_ever,test_time_method_a_part_1,test_time_method_a_part_1_time,test_time_method_a_part_2,test_time_method_a_part_2_time,test_time_method_a_part_3,test_time_method_a_part_3_time,test_time_method_b_part_1,test_time_method_b_part_1_time,test_time_method_b_part_2,test_time_method_b_part_2_time,test_time_method_b_part_3,test_time_method_b_part_3_time, test_relevance_part_1_a, test_relevance_part_1_b, test_relevance_part_2_a,test_relevance_part_2_b, test_relevance_part_3_a, test_relevance_part_3_b, test_relevance_part_1_a_details, test_relevance_part_1_b_details, test_relevance_part_2_a_details, test_relevance_part_2_b_details, test_relevance_part_3_a_details, test_relevance_part_3_b_details)";
        sql = sql + "values ("+user.id+", '"+user.email+"','"+user.phone+"','"+user.ageGroup+"','"+user.gender+"',"+user.isEver+","+testTimeMethodAPart1+","+testTimeMethodAPart1Time+","+testTimeMethodAPart2+","+testTimeMethodAPart2Time+","+testTimeMethodAPart3+","+testTimeMethodAPart3Time+","+testTimeMethodBPart1+","+testTimeMethodBPart1Time+","+testTimeMethodBPart2+","+testTimeMethodBPart2Time+","+testTimeMethodBPart3+","+testTimeMethodBPart3Time+","+testRelevancePart1A+","+testRelevancePart1B+","+testRelevancePart2A+","+testRelevancePart2B+","+testRelevancePart3A+","+testRelevancePart3B+",'"+testRelevancePart1ADetails+"', '"+testRelevancePart1BDetails+"', '"+testRelevancePart2ADetails+"', '"+testRelevancePart2BDetails+"', '"+testRelevancePart3ADetails+"', '"+testRelevancePart3BDetails+"')";
        System.out.println(sql);
        db.setSql(sql);
        db.executeUpdate();
        db.closeConnection();
        cleanUpDB(user);
        response.sendRedirect(Config.SITE_URL+"/survey/thanks");
    }
    
    public void thanks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/thanks.jsp").forward(request,response);
    }

    public void submitFormTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] selectedItems = request.getParameterValues("selectedItem");
        String identifier = request.getParameter("identifier");
        String details="";
        int count = 0;
        if(selectedItems != null){
            User user = (User) request.getSession().getAttribute("user");
            for(String selectedItem : selectedItems){
                String[] selectedItemSplit = selectedItem.split("-");
                details += selectedItemSplit[1]+"-";
                buyItem(user,selectedItemSplit[0]);
            }
            count = selectedItems.length;
        }
        request.getSession().setAttribute(identifier+"Details", details);
        request.getSession().setAttribute(identifier+"T", request.getParameter("isTolarable"));
        request.getSession().setAttribute(identifier, count);
        response.sendRedirect(request.getParameter("nextUrl"));
    }

    public void cleanUpDB(User user){
        Main.memoryBasedModelStage1.removeUserFromTransaction(user.id);
        Main.memoryBasedModelStage2.removeUserFromTransaction(user.id);
        Main.memoryBasedModelStage3.removeUserFromTransaction(user.id);
    }
    public void testRecommendation(HttpServletRequest request, HttpServletResponse response){
        Main.memoryBasedModelStage3.getRecommendationByUser("2246391147", 5);
        Main.memoryBasedModelStage2.getRecommendationByUser("2246391147", 5);
        Main.memoryBasedModelStage1.getRecommendationByUser("2246391147", 5);
        Main.naiveBayesModel3.makeTopNRecommendation("2246391147", 5);
        Main.naiveBayesModel2.makeTopNRecommendation("2246391147", 5);
        Main.naiveBayesModel1.makeTopNRecommendation("2246391147", 5);
        System.out.println("stress test done");
    }

    public void methodWarning1A(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode A");
        request.setAttribute("nextUrl", "survey/testTime/methodA/part1");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }
    public void methodWarning2A(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode A");
        request.setAttribute("nextUrl", "survey/testTime/methodA/part2");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }
    public void methodWarning3A(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode A");
        request.setAttribute("nextUrl", "survey/testTime/methodA/part3");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }
    public void methodWarning1B(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "survey/testTime/methodB/part1");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }
    public void methodWarning2B(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "survey/testTime/methodB/part2");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }
    public void methodWarning3B(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("method", "Metode B");
        request.setAttribute("nextUrl", "survey/testTime/methodB/part3");
        request.getRequestDispatcher("/views/methodAWarning.jsp").forward(request, response);
    }

    public ArrayList<Item> getUserHistory(User user){
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
        return historyItems;
    }
}
