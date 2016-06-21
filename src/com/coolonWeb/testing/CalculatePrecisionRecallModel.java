package com.coolonWeb.testing;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.Item;
import com.coolonWeb.model.MemoryBasedModel;
import com.coolonWeb.model.NaiveBayesModel;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 06/06/16.
 */
public class CalculatePrecisionRecallModel implements Runnable {
    private DataSet testingSet;
    private ArrayList<Double> precisions;
    private ArrayList<Double> recalls;
    private ArrayList<Double> fscore;
    private NaiveBayesModel model;
    private int n;
    private int startIndex;
    private int endIndex;
    public CalculatePrecisionRecallModel(DataSet testingSet, ArrayList<Double> precisions, ArrayList<Double> recalls, ArrayList<Double> fscore,NaiveBayesModel model, int n, int startIndex, int endIndex){
        this.testingSet = testingSet;
        this.precisions = precisions;
        this.fscore = fscore;
        this.recalls = recalls;
        this.model = model;
        this.n = n;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
    @Override
    public void run() {
        for(int i = startIndex; i<=  endIndex; i++) {
            String user = testingSet.users.get(i);
            ArrayList<Item> recommendedItems = model.makeTopNRecommendation(user, n);
            ArrayList<Item> testItems = getTestItem(user);
            int hitsize = numberOfHitSet(recommendedItems, testItems);
            if(testItems.size() == 0) continue;
            //System.out.println("Recommended item:");
            //printItems(recommendedItems);
            //System.out.println("Test item:");
            //printItems(testItems);
            double precision = precision(recommendedItems, testItems, hitsize);
            double recall = recall(recommendedItems, testItems, hitsize);
            double f1score = calculateF1Score(precision, recall);
            precisions.add(precision);
            recalls.add(recall);
            fscore.add(f1score);
            //System.out.println(recommendedItems.size()+", "+testItems.size()+", "+hitsize+" | "+ precision+", "+recall);
            //System.out.println(user + ": precision(" + precision + "), recall(" + recall + ")");
        }
    }
    public double precision(ArrayList<Item> recommendedItems, ArrayList<Item> testItems, int hitsize){
        return hitsize/(recommendedItems.size()*1.0);

    }

    public double recall(ArrayList<Item> recommendedItems, ArrayList<Item> testItems, int hitsize){
        if(testItems.size() == 0)
            return 0.0;
        return hitsize/(testItems.size()*1.0);
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
        ArrayList<String> items = testingSet.userInterests.get(user);
        if(items == null)
            return new ArrayList<Item>();
        ArrayList<Item> testingItems = new ArrayList<>();
        for(String item: items){
            Item newItem = new Item();
            newItem.id = item;
            testingItems.add(newItem);
        }
        return testingItems;
    }

    public void printItems(ArrayList<Item> items){
        for(Item item : items){
            System.out.println(item.name);
        }
    }
    public double calculateF1Score(double precision, double recall){
        if(precision+recall == 0.0)
            return 0.0;
        return 2.0*precision*recall/(recall+precision);
    }

}
