package com.coolonWeb.testing;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.MemoryBasedModel;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 06/06/16.
 */
public class CalculatePrecisionRecallMemory implements Runnable {
    private ArrayList<String> members;
    private ArrayList<Double> precisions;
    private ArrayList<Double> recalls;
    private MemoryBasedModel model;
    private String testTable;
    private int n;
    private int startIndex;
    private int endIndex;
    public CalculatePrecisionRecallMemory(ArrayList<String> members, ArrayList<Double> precisions, ArrayList<Double> recalls, MemoryBasedModel model, int n, String testTable, int startIndex, int endIndex){
        this.members = members;
        this.precisions = precisions;
        this.recalls = recalls;
        this.model = model;
        this.n = n;
        this.testTable = testTable;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
    @Override
    public void run() {
        for(int i = startIndex; i<=  endIndex; i++) {
            String user = members.get(i);
            ArrayList<Item> recommendedItems = model.getRecommendationByUser(user, n);
            ArrayList<Item> testItems = getTestItem(user);
            int hitsize = numberOfHitSet(recommendedItems, testItems);
            if(testItems.size() == 0) continue;
            //System.out.println("Recommended item:");
            //printItems(recommendedItems);
            //System.out.println("Test item:");
            //printItems(testItems);
            precisions.add(precision(recommendedItems, testItems, hitsize));
            recalls.add(recall(recommendedItems, testItems, hitsize));
            System.out.println(hitsize);
            System.out.println(user + ": precision(" + precision(recommendedItems, testItems, hitsize) + "), recall(" + recall(recommendedItems, testItems, hitsize) + ")");
        }
    }
    public double precision(ArrayList<Item> recommendedItems, ArrayList<Item> testItems, int hitsize){
        return hitsize/recommendedItems.size();

    }

    public double recall(ArrayList<Item> recommendedItems, ArrayList<Item> testItems, int hitsize){
        if(testItems.size() == 0)
            return 0;
        return hitsize/testItems.size();
    }

    public int numberOfHitSet(ArrayList<Item> recommendedItems, ArrayList<Item> testItems){
        int hitset = 0;
        for(Item recommendedItem :recommendedItems){
            if(testItems.contains(recommendedItem)){
                hitset++;
            }
        }
        return hitset;
    }
    public ArrayList<Item> getTestItem(String user){
        String query = "SELECT * FROM "+testTable+" WHERE MEM_NO_ENC = "+user+" GROUP BY PRODUCT_NUMBER_ENC";
        DBConnect db = new DBConnect(query);
        ResultSet rs = db.execute();
        ArrayList<Item> testItems = new ArrayList<>();
        try {
            while(rs.next()){
                Item item = new Item();
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                testItems.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return testItems;
    }

    public void printItems(ArrayList<Item> items){
        for(Item item : items){
            System.out.println(item.name);
        }
    }

}
