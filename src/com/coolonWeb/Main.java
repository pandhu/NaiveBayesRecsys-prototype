package com.coolonWeb;
import com.coolonWeb.model.*;
import com.coolonWeb.testing.DataSet;
import com.coolonWeb.testing.Testing;

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
            ServletContext context = getServletContext();

            System.out.println("building model");
            InputStream isProductReader1 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductReader2 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductReader3 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductDataReader1 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductDataReader2 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductDataReader3 = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isTransactionReader1 = context.getResourceAsStream("/WEB-INF/data/purchase_1.csv");
            InputStream isTransactionReader2 = context.getResourceAsStream("/WEB-INF/data/purchase_2.csv");
            InputStream isTransactionReader3 = context.getResourceAsStream("/WEB-INF/data/purchase.csv");

            InputStream isMemberReader1 = context.getResourceAsStream("/WEB-INF/data/member.csv");
            InputStream isMemberReader2 = context.getResourceAsStream("/WEB-INF/data/member.csv");
            InputStream isMemberReader3 = context.getResourceAsStream("/WEB-INF/data/member.csv");


            BufferedReader productReader1 = new BufferedReader(new InputStreamReader(isProductReader1));
            BufferedReader productReader2 = new BufferedReader(new InputStreamReader(isProductReader2));
            BufferedReader productReader3 = new BufferedReader(new InputStreamReader(isProductReader3));
            BufferedReader productDataReader1 = new BufferedReader(new InputStreamReader(isProductDataReader1));
            BufferedReader productDataReader2 = new BufferedReader(new InputStreamReader(isProductDataReader2));
            BufferedReader productDataReader3 = new BufferedReader(new InputStreamReader(isProductDataReader3));
            BufferedReader transactionReader1 = new BufferedReader(new InputStreamReader(isTransactionReader1));
            BufferedReader transactionReader2 = new BufferedReader(new InputStreamReader(isTransactionReader2));
            BufferedReader transactionReader3 = new BufferedReader(new InputStreamReader(isTransactionReader3));
            BufferedReader memberReader1 = new BufferedReader(new InputStreamReader(isMemberReader1));
            BufferedReader memberReader2 = new BufferedReader(new InputStreamReader(isMemberReader2));
            BufferedReader memberReader3 = new BufferedReader(new InputStreamReader(isMemberReader3));

            this.dataset3 = new DataSet();
            dataset3.users = readMember(memberReader3);
            dataset3.items = readProduct(productReader3);
            dataset3.itemsData = readProductData(productDataReader3);
            dataset3.transactions = readTransaction(transactionReader3);
            dataset3.assignUserInterests();
            //dataset.sortUserInterestsByNumberOfItem();
            dataset3.removeNonPurchasedFromUserInterests();
            dataset3.removeUnsoldItems();
            dataset3.printStatDataSet();
            dataset3.filterUser();

            //offlineTesting();
            this.dataset2 = new DataSet();
            dataset2.users = readMember(memberReader2);
            dataset2.items = readProduct(productReader2);
            dataset2.itemsData = readProductData(productDataReader2);
            dataset2.transactions = readTransaction(transactionReader2);
            dataset2.assignUserInterests();
            dataset2.printStatDataSet();
            dataset2.removeNonPurchasedFromUserInterests();
            dataset2.removeUnsoldItems();
            dataset2.filterUser();

            this.dataset1 = new DataSet();
            dataset1.users = readMember(memberReader1);
            dataset1.items = readProduct(productReader1);
            dataset1.itemsData = readProductData(productDataReader1);
            dataset1.transactions = readTransaction(transactionReader1);
            dataset1.assignUserInterests();
            dataset1.printStatDataSet();
            dataset1.removeNonPurchasedFromUserInterests();
            dataset1.removeUnsoldItems();
            dataset1.filterUser();

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
            System.out.println("model built successfuly");
        } catch (IOException e){
            System.out.println("failed to build model");
            System.out.println(e);
            return;
        }
        System.out.println("init done");
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
