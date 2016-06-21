package com.coolonWeb;
import com.coolonWeb.controller.TestingController;
import com.coolonWeb.model.*;
import com.coolonWeb.testing.*;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pandhu on 30/03/16.
 */
public class Main extends HttpServlet{
    public static NaiveBayesModel naiveBayesModel1;
    public static NaiveBayesModel naiveBayesModel2;
    public static NaiveBayesModel naiveBayesModel3;

    public static MemoryBasedModel memoryBasedModelStage1;
    public static MemoryBasedModel memoryBasedModelStage2;
    public static MemoryBasedModel memoryBasedModelStage3;

    public static DataSet dataset1;
    public static DataSet dataset2;
    public static DataSet dataset3;
    public void init() throws ServletException {

        /*
        try {
            System.out.println("building model");
            buildModel();
            System.out.println("model built successfuly");
        } catch (IOException e){
            System.out.println(e);
            return;
        }
        */
        startTest();
        System.out.println("init done");

    }
    public void startTest(){
        modelBasedTest();
        //memoryBasedTest();
    }
    public static void memoryBasedTest(){
        for(int ii = 8; ii <=20; ii += 3) {
            TestingMemoryBased testingMemoryBased3 = new TestingMemoryBased(new MemoryBasedModel("purchase_training"), "purchase_testing", ii);
            testingMemoryBased3.runTesting();
            TestingMemoryBased testingMemoryBased1 = new TestingMemoryBased(new MemoryBasedModel("purchase_stage_1_training"), "purchase_stage_1_testing", ii);
            testingMemoryBased1.runTesting();
            TestingMemoryBased testingMemoryBased2 = new TestingMemoryBased(new MemoryBasedModel("purchase_stage_2_training"), "purchase_stage_2_testing", ii);
            testingMemoryBased2.runTesting();

        }

    }
    public static void modelBasedTest(){
        DataSet trainingSet1 = new DataSet();
        DataSet trainingSet2 = new DataSet();
        DataSet trainingSet3 = new DataSet();
        DataSet testingSet1 = new DataSet();
        DataSet testingSet2 = new DataSet();
        DataSet testingSet3 = new DataSet();

        try {
            trainingSet3.users = readMemberFromSQL();
            trainingSet3.items = readProductFromSQL();
            trainingSet3.itemsData = readProductDataFromSQL();
            trainingSet3.transactions = readTransactionFromSQL("purchase_training");
            trainingSet3.assignUserInterests();
            trainingSet3.removeNonPurchasedFromUserInterests();
            trainingSet3.removeUnsoldItems();
            trainingSet3.printStatDataSet();

            testingSet3.users = readMemberFromSQL();
            testingSet3.items = readProductFromSQL();
            testingSet3.itemsData = readProductDataFromSQL();
            testingSet3.transactions = readTransactionFromSQL("purchase_testing");
            testingSet3.assignUserInterests();
            testingSet3.removeNonPurchasedFromUserInterests();
            testingSet3.removeUnsoldItems();
            testingSet3.printStatDataSet();

            trainingSet2.users = readMemberFromSQL();
            trainingSet2.items = readProductFromSQL();
            trainingSet2.itemsData = readProductDataFromSQL();
            trainingSet2.transactions = readTransactionFromSQL("purchase_stage_2_training");
            trainingSet2.assignUserInterests();
            trainingSet2.removeNonPurchasedFromUserInterests();
            trainingSet2.removeUnsoldItems();
            trainingSet2.printStatDataSet();

            testingSet2.users = readMemberFromSQL();
            testingSet2.items = readProductFromSQL();
            testingSet2.itemsData = readProductDataFromSQL();
            testingSet2.transactions = readTransactionFromSQL("purchase_stage_2_testing");
            testingSet2.assignUserInterests();
            testingSet2.removeNonPurchasedFromUserInterests();
            testingSet2.removeUnsoldItems();
            testingSet2.printStatDataSet();

            trainingSet1.users = readMemberFromSQL();
            trainingSet1.items = readProductFromSQL();
            trainingSet1.itemsData = readProductDataFromSQL();
            trainingSet1.transactions = readTransactionFromSQL("purchase_stage_1_training");
            trainingSet1.assignUserInterests();
            trainingSet1.removeNonPurchasedFromUserInterests();
            trainingSet1.removeUnsoldItems();
            trainingSet1.printStatDataSet();

            testingSet1.users = readMemberFromSQL();
            testingSet1.items = readProductFromSQL();
            testingSet1.itemsData = readProductDataFromSQL();
            testingSet1.transactions = readTransactionFromSQL("purchase_stage_1_testing");
            testingSet1.assignUserInterests();
            testingSet1.removeNonPurchasedFromUserInterests();
            testingSet1.removeUnsoldItems();
            testingSet1.printStatDataSet();

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int ii = 5; ii <= 20; ii+=3) {
            NaiveBayesModel model1 = new NaiveBayesModel();
            model1.setDataset(trainingSet1);
            model1.calculatePriorProb();
            model1.calculateConditionalProb();
            TestingModelBased testingModelBased1 = new TestingModelBased(trainingSet1, testingSet1, ii, model1);
            testingModelBased1.runTesting();

            NaiveBayesModel model2 = new NaiveBayesModel();
            model2.setDataset(trainingSet2);
            model2.setPriorProbs(model1.getPriorProbs());
            model2.setConditionalProbs(model1.getConditionalProbs());
            TestingModelBased testingModelBased2 = new TestingModelBased(trainingSet2, testingSet2, ii, model2);
            testingModelBased2.runTesting();

            NaiveBayesModel model3 = new NaiveBayesModel();
            model3.setDataset(trainingSet3);
            model3.setPriorProbs(model1.getPriorProbs());
            model3.setConditionalProbs(model1.getConditionalProbs());
            TestingModelBased testingModelBased3 = new TestingModelBased(trainingSet3, testingSet3, ii, model3);
            testingModelBased3.runTesting();
        }

    }
    public void buildModel() throws IOException {
        this.dataset3 = new DataSet();
        dataset3.users = readMemberFromSQL();
        dataset3.items = readProductFromSQL();
        dataset3.itemsData = readProductDataFromSQL();
        dataset3.transactions = readTransactionFromSQL("purchase");
        dataset3.assignUserInterests();
        //dataset.sortUserInterestsByNumberOfItem();
        dataset3.removeNonPurchasedFromUserInterests();
        dataset3.removeUnsoldItems();
        dataset3.filterUser(6);
        dataset3.printStatDataSet();

        //offlineTesting();
        this.dataset2 = new DataSet();
        dataset2.users = readMemberFromSQL();
        dataset2.items = readProductFromSQL();
        dataset2.itemsData = readProductDataFromSQL();
        dataset2.transactions = readTransactionFromSQL("purchase_stage_2");
        dataset2.assignUserInterests();
        dataset2.removeNonPurchasedFromUserInterests();
        dataset2.removeUnsoldItems();
        dataset2.filterUser(5);
        dataset2.printStatDataSet();

        this.dataset1 = new DataSet();
        dataset1.users = readMemberFromSQL();
        dataset1.items = readProductFromSQL();
        dataset1.itemsData = readProductDataFromSQL();
        dataset1.transactions = readTransactionFromSQL("purchase_stage_1");
        dataset1.assignUserInterests();
        dataset1.removeNonPurchasedFromUserInterests();
        dataset1.removeUnsoldItems();
        dataset1.filterUser(3);
        dataset1.printStatDataSet();

        naiveBayesModel1 = new NaiveBayesModel();
        naiveBayesModel1.setDataset(dataset1);
        naiveBayesModel1.calculatePriorProb();
        naiveBayesModel1.calculateConditionalProb();

        naiveBayesModel2 = new NaiveBayesModel();
        naiveBayesModel2.setDataset(dataset2);
        naiveBayesModel2.setPriorProbs(naiveBayesModel1.getPriorProbs());
        naiveBayesModel2.setConditionalProbs(naiveBayesModel1.getConditionalProbs());

        naiveBayesModel3 = new NaiveBayesModel();
        naiveBayesModel3.setDataset(dataset3);
        naiveBayesModel3.setPriorProbs(naiveBayesModel1.getPriorProbs());
        naiveBayesModel3.setConditionalProbs(naiveBayesModel1.getConditionalProbs());

        //svdModel = new SVDModel(dataset);
        this.memoryBasedModelStage1 = new MemoryBasedModel("purchase_stage_1");
        this.memoryBasedModelStage2 = new MemoryBasedModel("purchase_stage_2");
        this.memoryBasedModelStage3 = new MemoryBasedModel("purchase");

    }
    public static HashMap<String, Item> readProductData(BufferedReader productReader) throws IOException {
        System.out.println("Read Product Data");
        String line;
        HashMap<String, Item> itemsData =  new HashMap<>();
        while((line = productReader.readLine()) != null){
            String product = line.split(",")[0];
            Item item = new Item();
            item.id =product.substring(1,product.length()-1);
            item.name = line.split(",")[1].substring(1, line.split(",")[1].length()-1);
            itemsData.put(item.id, item);
        }
        System.out.println("Read Product Data done");
        return itemsData;
    }

    public static HashMap<String, Item> readProductDataFromSQL() throws IOException {
        System.out.println("Read Product Data");
        DBConnect db = new DBConnect();
        String query = "SELECT * FROM product";
        db.setSql(query);
        HashMap<String, Item> itemsData =  new HashMap<>();
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                itemsData.put(item.id, item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        System.out.println("Read Product Data done");
        return itemsData;
    }

    public static HashMap<String, Double> readConditionalProbs(BufferedReader conditionalProbReader) throws IOException{
        System.out.println("Read conditional probability");
        String line;
        HashMap<String, Double> conditionalProbs =  new HashMap<>();
        while((line = conditionalProbReader.readLine()) != null){
            String key = line.split(";")[0];
            Double value = Double.parseDouble(line.split(";")[1]);
            conditionalProbs.put(key, value);
        }
        System.out.println("Read conditional probability done");
        return conditionalProbs;
    }

    public static HashMap<String, Double> readConditionalProbs() throws IOException{
        System.out.println("Read conditional probability");
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM conditional_probability");
        ResultSet rs = db.execute();
        HashMap<String, Double> conditionalProbs =  new HashMap<>();
        try {
            while(rs.next()){
                //Retrieve by column name
                String tuple = rs.getString("tuple");
                Double value = Double.parseDouble(rs.getString("value"));
                conditionalProbs.put(tuple,value);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        System.out.println("Read conditional probability done");
        return conditionalProbs;
    }


    public static ArrayList<String> readProduct(BufferedReader productReader) throws IOException {
        System.out.println("Read Product");
        String line;
        ArrayList<String> products = new ArrayList<>();
        HashMap<String, Item> itemsData =  new HashMap<>();
        while((line = productReader.readLine()) != null){
            String product = line.split(",")[0];
            products.add(product.substring(1,product.length()-1));

            Item item = new Item();
            item.id =product.substring(1,product.length()-1);
            item.name = line.split(",")[0].substring(1, line.split(",")[0].length()-1);
            itemsData.put(item.id, item);
        }
        System.out.println("Read Product done");
        return products;
    }

    public static ArrayList<String> readProductFromSQL() throws IOException {
        System.out.println("Read Product");
        DBConnect db = new DBConnect();
        String query = "SELECT * FROM product";
        db.setSql(query);
        ArrayList<String> products = new ArrayList<>();
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                products.add(rs.getString("PRODUCT_NUMBER_ENC"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();

        System.out.println("Read Product done");
        return products;
    }

    public static ArrayList<String> readMember(BufferedReader memberReader) throws IOException {
        System.out.println("Read Member");

        String line;
        ArrayList<String> members = new ArrayList<>();
        while((line = memberReader.readLine()) != null){
            String product = line.split(",")[0];
            members.add(product.substring(1,product.length()-1));
        }
        System.out.println("Read Member done");
        return members;
    }

    public static ArrayList<String> readMemberFromSQL() throws IOException {
        System.out.println("Read Member");
        DBConnect db = new DBConnect();
        String query = "SELECT * FROM member";
        db.setSql(query);
        ArrayList<String> members = new ArrayList<>();
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                members.add(rs.getString("MEM_NO_ENC"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        System.out.println("Read Member done");
        return members;
    }

    public static ArrayList<Transaction> readTransaction(BufferedReader transactionReader) throws IOException {
        System.out.println("Read Transaction");
        String line;
        ArrayList<Transaction> transactions = new ArrayList<>();

        while((line = transactionReader.readLine()) != null){
            String user = line.split(",")[1];
            String item = line.split(",")[2];
            Transaction transaction = new Transaction();
            transaction.item = item.substring(1,item.length()-1);
            transaction.user = user.substring(1,user.length()-1);
            transactions.add(transaction);
        }
        System.out.println("Read Transaction done");
        return transactions;
    }

    public static ArrayList<Transaction> readTransactionFromSQL(String table) throws IOException {
        System.out.println("Read Transaction");

        DBConnect db = new DBConnect();
        String query = "SELECT * FROM "+table;
        db.setSql(query);
        ArrayList<Transaction> transactions = new ArrayList<>();
        ResultSet rs = db.execute();
        try {
            while(rs.next()){
                //Retrieve by column name
                Transaction transaction = new Transaction();
                transaction.item = rs.getString("PRODUCT_NUMBER_ENC");
                transaction.user = rs.getString("MEM_NO_ENC");
                transactions.add(transaction);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        System.out.println("Read Transaction done");
        return transactions;
    }

    public static void printHashMap(HashMap<String, Double> map) {
        Map mp = map;
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public static void offlineTesting(){
        Testing testing = new Testing();
        testing.ratio = 75;
        testing.splitDataByRatio();
        testing.trainingDataSet.printStatDataSet();
        testing.testingDataSet.printStatDataSet();
        testing.executeTest();
    }

}
