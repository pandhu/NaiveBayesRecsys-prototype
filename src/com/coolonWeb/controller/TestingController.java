package com.coolonWeb.controller;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.testing.Insert;
import com.coolonWeb.testing.Testing;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 04/06/16.
 */
public class TestingController extends HttpServlet implements Runnable{
    public String tablename;
    public int testScale;
    private Thread t;
    public TestingController(String tablename,  int testScale){
        this.tablename = tablename;
        this.testScale = testScale;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String path = request.getPathInfo();
        if(path == null) path = "";
        switch (path){
            case "/memorybased":
                memoryBasedTest(request, response);
                return;
            default:
                return;
        }

    }
    public void memoryBasedTest(HttpServletRequest request, HttpServletResponse response){
        System.out.print("test begin....");
        splitDataset("purchase", 30);
    }

    public void memoryBasedTest(){
        System.out.print("test begin....");
        splitDataset(this.tablename, testScale);
    }
    public void splitDataset(String table, int testScale){
        System.out.print("drop table..");
        dropTable(table+"_training");
        dropTable(table+"_testing");
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

        System.out.print("create table....");
        createTable(table+"_training");
        createTable(table+"_testing");
        System.out.print("splitting db");
        for(String member : members){
            splitInterestUser(table, member, testScale);
        }
        System.out.print("splitting done...");


    }

    public void splitInterestUser(String tableName, String idUser, int testScale){
        String query =  "SELECT * FROM "+tableName+" where MEM_NO_ENC = "+idUser;
        DBConnect db = new DBConnect(query);
        ResultSet rs = db.execute();
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            while(rs.next()){
                Transaction trans = new Transaction();
                trans.ordmonth = rs.getString("ORD_MONTH");
                trans.user = rs.getString("MEM_NO_ENC");
                trans.item = rs.getString("PRODUCT_NUMBER_ENC");
                trans.itemName = rs.getString("PRODUCT_NAME");
                trans.lv1cat = rs.getString("LV1_CATEGORY");
                trans.lv2cat = rs.getString("LV2_CATEGORY");
                trans.lv3cat = rs.getString("LV3_CATEGORY");
                trans.opt = rs.getString("OPTIONS");
                //Retrieve by column name
                transactions.add(trans);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        int count = 0;
        int numberOfTestData = testScale*transactions.size()/100;
        int numberOfTrainData = transactions.size() - numberOfTestData;
        ArrayList<Transaction> training = new ArrayList<>();
        ArrayList<Transaction> testing = new ArrayList<>();
        for(Transaction trans : transactions){
            if(count < numberOfTrainData){
                new Thread(new Insert(tableName+"_training", trans)).start();
            } else {
                new Thread(new Insert(tableName+"_testing", trans)).start();
            }
            count++;
        }
    }

    public void createTable(String tablename){
        String query =  "CREATE TABLE IF NOT EXISTS `"+tablename+"` ( `ORD_MONTH` int(6) DEFAULT NULL, `MEM_NO_ENC` bigint(10) DEFAULT NULL, `PRODUCT_NUMBER_ENC` bigint(10) DEFAULT NULL, `PRODUCT_NAME` varchar(260) DEFAULT NULL, `LV1_CATEGORY` varchar(30) DEFAULT NULL, `LV2_CATEGORY` varchar(39) DEFAULT NULL, `LV3_CATEGORY` varchar(57) DEFAULT NULL, `OPTIONS` varchar(469) DEFAULT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
        DBConnect db = new DBConnect();
        db.setSql(query);
        db.executeUpdate();
        db.closeConnection();
    }
    public void dropTable(String tablename){
        String query = "DROP TABLE "+tablename;
        DBConnect db =  new DBConnect();
        db.setSql(query);
        db.executeUpdate();
        db.closeConnection();
    }

    public void insert(String table, Transaction transaction){
        DBConnect db = new DBConnect();
        String query = "INSERT INTO "+table+" (ORD_MONTH, MEM_NO_ENC, PRODUCT_NUMBER_ENC, PRODUCT_NAME, LV1_CATEGORY, LV2_CATEGORY, LV3_CATEGORY, OPTIONS) values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, transaction.ordmonth);
            preparedStatement.setString(2, transaction.user);
            preparedStatement.setString(3, transaction.item);
            preparedStatement.setString(4, transaction.itemName);
            preparedStatement.setString(5, transaction.lv1cat);
            preparedStatement.setString(6, transaction.lv2cat);
            preparedStatement.setString(7, transaction.lv3cat);
            preparedStatement.setString(8, transaction.opt);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
    }

    public void createSql(ArrayList<Transaction> transactions, String tableName){
        String queryBuilder= "";
        int limit = 10;
        int counter = 0;
        for(Transaction trans: transactions){
            if(counter >= limit)
                break;
            String query = "INSERT INTO "+tableName+" (ORD_MONTH, MEM_NO_ENC, PRODUCT_NUMBER_ENC, PRODUCT_NAME, LV1_CATEGORY, LV2_CATEGORY, LV3_CATEGORY, OPTIONS) values ("+trans.ordmonth+","+trans.user+","+trans.item+",'"+trans.itemName+"','"+trans.lv1cat+"','"+trans.lv2cat+"','"+trans.lv3cat+"','"+trans.opt+"');\n";
            queryBuilder+= query;
            counter++;
        }
        try {
            PrintWriter out = new PrintWriter("../"+tableName+".sql");
            out.println(queryBuilder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        memoryBasedTest();
    }

    public void start ()
    {
        System.out.println("Starting splitting data for " +  tablename );
        if (t == null)
        {
            t = new Thread (this, tablename);
            t.start ();
        }
    }
}
