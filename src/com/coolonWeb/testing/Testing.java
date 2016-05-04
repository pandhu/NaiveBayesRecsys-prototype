package com.coolonWeb.testing;

import com.coolonWeb.model.Item;
import com.coolonWeb.model.NaiveBayesModel;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 03/05/16.
 */
public class Testing {
    public DataSet trainingDataSet;
    public DataSet testingDataSet;
    public DataSet universe;
    public NaiveBayesModel model;
    public int ratio;
    public Testing(){
        this.trainingDataSet = new DataSet();
        this.testingDataSet = new DataSet();
        this.universe = new DataSet();
        this.ratio = 100;
    }

    public void splitDataByRatio(){
        System.out.println("split data by ratio");
        //read for training data
        int totalUser = universe.userInterests.size();
        int separator = (this.ratio*totalUser)/100;
        int ii=1;
        Iterator it = universe.userInterests.entrySet().iterator();
        while (it.hasNext()) {
            if(ii == separator)
                break;
            Map.Entry pair = (Map.Entry)it.next();
            trainingDataSet.userInterests.put((String)pair.getKey(), (ArrayList<String>)pair.getValue());
            trainingDataSet.users.add((String) pair.getKey());
            for(String item: (ArrayList<String>)pair.getValue()){
                if(trainingDataSet.items.indexOf(item) == -1){
                    trainingDataSet.items.add(item);
                }
            }
            ii++;
        }
        //for testing data
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            testingDataSet.userInterests.put((String)pair.getKey(), (ArrayList<String>)pair.getValue());
            testingDataSet.users.add((String) pair.getKey());
            for(String item: (ArrayList<String>)pair.getValue()){
                if(testingDataSet.items.indexOf(item) == -1){
                    testingDataSet.items.add(item);
                }
            }
        }
        System.out.println("split data by ratio done");
    }

    public void executeTest(){
        System.out.println("execute test");

        HashMap<String, ArrayList<String>> itemsTest = new HashMap<>();
        HashMap<String, ArrayList<String>> itemsResult = new HashMap<>();

        Iterator it = testingDataSet.userInterests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<String> items = (ArrayList<String>) pair.getValue();
            ArrayList<String> temp = new ArrayList<>();

            int index = 0;
            for(int ii = 0; ii < items.size()/2;ii++){
                temp.add(items.get(ii));
                index = ii;
            }
            itemsTest.put((String) pair.getKey(), temp);

            temp = new ArrayList<>();
            for(int ii = index; ii < items.size();ii++){
                temp.add(items.get(ii));
                index = ii;
            }
            itemsResult.put((String) pair.getKey(), temp);

        }

        this.model = new NaiveBayesModel();
        this.model.setDataset(this.trainingDataSet);
        this.model.calculatePriorProb();
        this.model.calculateConditionalProb();
        HashMap<String, ArrayList<String>> newUserInterests = new HashMap<>();
        newUserInterests.putAll(this.model.getDataset().userInterests);
        newUserInterests.putAll(itemsTest);
        this.model.getDataset().userInterests = newUserInterests;

        it = itemsResult.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<Item> recommendedItems = this.model.makeTopNRecommendation((String) pair.getKey(), 10);
            ArrayList<String> resultItems = itemsResult.get((String)pair.getKey());
            int numberOfHit = 0;
            for(Item item: recommendedItems){
                if(resultItems.contains(item))
                    numberOfHit++;
            }
            System.out.println(pair.getKey()+": "+numberOfHit);
        }
    }
}
