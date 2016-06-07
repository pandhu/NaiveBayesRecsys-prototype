package com.coolonWeb;
import com.coolonWeb.controller.TestingController;
import com.coolonWeb.model.*;
import com.coolonWeb.testing.DataSet;
import com.coolonWeb.testing.Testing;
import com.coolonWeb.testing.TestingMemoryBased;
import com.coolonWeb.testing.TestingModelBased;

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
        try {
            System.out.println("building model");
            buildModel();
            System.out.println("model built successfuly");
        } catch (IOException e){
            System.out.println(e);
            return;
        }
        System.out.println("init done");
    }
    public void startTest(){
        modelBasedTest();
        memoryBasedTest();
    }
    public void memoryBasedTest(){

    }
    public void modelBasedTest(){
        DataSet trainingSet = new DataSet();
        DataSet testingSet = new DataSet();
        try {
            trainingSet.users = readMemberFromSQL();
            trainingSet.items = readProductFromSQL();
            trainingSet.itemsData = readProductDataFromSQL();
            trainingSet.transactions = readTransactionFromSQL("purchase_training");
            trainingSet.assignUserInterests();
            trainingSet.removeNonPurchasedFromUserInterests();
            trainingSet.removeUnsoldItems();
            trainingSet.printStatDataSet();

            testingSet.users = readMemberFromSQL();
            testingSet.items = readProductFromSQL();
            testingSet.itemsData = readProductDataFromSQL();
            testingSet.transactions = readTransactionFromSQL("purchase_testing");
            testingSet.assignUserInterests();
            testingSet.removeNonPurchasedFromUserInterests();
            testingSet.removeUnsoldItems();
            testingSet.printStatDataSet();

        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int ii = 5; ii <= 20; ii+=3){

            TestingModelBased testingModelBased = new TestingModelBased(trainingSet,testingSet,ii);
            testingModelBased.runTesting();
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
