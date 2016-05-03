package com.coolonWeb;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.NaiveBayesModel;
import com.coolonWeb.model.Transaction;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pandhu on 30/03/16.
 */
public class Main extends HttpServlet{
    public static NaiveBayesModel model;

    public void init() throws ServletException {

        try {
            ServletContext context = getServletContext();

            System.out.println("building model");
            InputStream isProductReader = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isProductDataReader = context.getResourceAsStream("/WEB-INF/data/product.csv");
            InputStream isTransactionReader = context.getResourceAsStream("/WEB-INF/data/purchase.csv");
            InputStream isMemberReader = context.getResourceAsStream("/WEB-INF/data/member.csv");
            InputStream isConditionalProb = context.getResourceAsStream("/WEB-INF/data/conditionalProb.txt");


            BufferedReader productReader = new BufferedReader(new InputStreamReader(isProductReader));
            BufferedReader productDataReader = new BufferedReader(new InputStreamReader(isProductDataReader));
            BufferedReader transactionReader = new BufferedReader(new InputStreamReader(isTransactionReader));
            BufferedReader memberReader = new BufferedReader(new InputStreamReader(isMemberReader));
            BufferedReader conditionalProbReader = new BufferedReader(new InputStreamReader(isConditionalProb));

            model = new NaiveBayesModel();
            model.setItems(readProduct(productReader));
            model.setItemsData(readProductData(productDataReader));
            model.setUsers(readMember(memberReader));
            model.setTransactions(readTransaction(transactionReader));
            model.calculatePriorProb();
            model.assignUserInterests();
            //model.calculateConditionalProb();
            model.setConditionalProbs(readConditionalProbs(conditionalProbReader));
            //model.setConditionalProbs(new HashMap<String, Double>());

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
}
