package com.coolonWeb.testing;

import com.coolonWeb.model.Item;
import com.coolonWeb.model.NaiveBayesModel;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 03/05/16.
 */
public class Testing {
    public DataSet trainingDataSet;
    public DataSet testingDataSet;
    public DataSet universe;
    public int ratio;
    public Testing(){
        this.trainingDataSet = new DataSet();
        this.testingDataSet = new DataSet();
        this.universe = new DataSet();
        this.ratio = 100;
    }

    public void splitDataByRation(){
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
    }
}
