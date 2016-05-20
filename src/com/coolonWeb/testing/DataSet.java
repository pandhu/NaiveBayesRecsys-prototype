package com.coolonWeb.testing;

import com.coolonWeb.model.Item;
import com.coolonWeb.model.Transaction;
import com.coolonWeb.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 03/05/16.
 */
public class DataSet {
    public ArrayList<Transaction> transactions;
    public ArrayList<String> users;
    public ArrayList<String> items;
    public HashMap<String, Item> itemsData;
    public HashMap<String, ArrayList<String>> userInterests;
    public ArrayList<String> listOfPossibleUser;

    public DataSet(){
        this.transactions = new ArrayList<>();
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.userInterests = new HashMap<>();
        this.itemsData = new HashMap<>();
        this.listOfPossibleUser = new ArrayList<>();
    }

    public void sortUserInterestsByNumberOfItem(){
        System.out.println("Start sorting user interest");
        HashMap<Integer, ArrayList<String>> temp = new HashMap<>();

        Iterator it = this.userInterests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int size = ((ArrayList<String>)pair.getValue()).size();
            if(temp.get(size) == null){
                ArrayList<String> newArrList = new ArrayList<>();
                newArrList.add((String) pair.getKey());
                temp.put(size,newArrList);
            } else {
                temp.get(size).add((String) pair.getKey());
            }
        }
        System.out.println(temp.size());

        it = temp.entrySet().iterator();

        HashMap<String, ArrayList<String>> tempHash = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            for(String str: (ArrayList<String>)pair.getValue()){
                tempHash.put(str, this.userInterests.get(str));
            }
        }

        this.userInterests = tempHash;
        System.out.println("Sorting user interest done");
    }

    public void assignUserInterests(){
        System.out.println("Assign User Interest");

        //initial user interests
        for(String user : users){
            this.userInterests.put(user, new ArrayList<String>());
        }
        for(Transaction transaction : transactions){
            if(!this.userInterests.get(transaction.user).contains(transaction.item))
                this.userInterests.get(transaction.user).add(transaction.item);

        }
        System.out.println("Assign User Interest done");

    }

    public void removeNonPurchasedFromUserInterests(){
        Iterator it = this.userInterests.entrySet().iterator();
        ArrayList<String> listOfNonPurchaseUser = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(((ArrayList<String>)pair.getValue()).size() == 0){
                listOfNonPurchaseUser.add((String) pair.getKey());
            }
        }
        for(String user : listOfNonPurchaseUser){
            this.userInterests.remove(user);
        }
    }

    public void removeUnsoldItems(){
        HashMap<String, Integer> totalSold = new HashMap<>();
        for(Transaction transaction: this.transactions){
            String item = transaction.item;
            if(totalSold.get(item) == null){
                totalSold.put(item, 1);
            } else{
                totalSold.put(item, (int)totalSold.get(item)+1);
            }
        }

        ArrayList<String> removedItem = new ArrayList<>();
       for(String item: this.items){
           if(totalSold.get(item) == null){
               removedItem.add(item);
           }
       }
        for(String item: removedItem){
            if(totalSold.get(item) == null){
                this.items.remove(item);
            }
        }
    }

    public void filterUser(){
        Iterator it = this.userInterests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(((ArrayList<String>)pair.getValue()).size() == 5){
                this.listOfPossibleUser.add((String) pair.getKey());
            }
        }
    }

    public String getRandomUser(){
        int index = (int) (Math.random()* (this.listOfPossibleUser.size()+1));
        return this.listOfPossibleUser.get(index);
    }
    public void printStatDataSet(){
        System.out.println("==========================PRINT DATA STAT==============================");
        System.out.println("number of users: " + this.users.size());
        System.out.println("number of items: " + this.items.size());
        System.out.println("number of transactions: " + this.transactions.size());
        System.out.println("number of userInterests: " + this.userInterests.size());

        int min =0;
        int max =0;
        int total = 0;
        int nonPurchaseUser = 0;
        Map mp = this.userInterests;
        Iterator it = mp.entrySet().iterator();

        int[] arrCount = new int[562];
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String user = (String) pair.getKey();
            int sizeOfInterest = ((ArrayList<String>)pair.getValue()).size();
            if(sizeOfInterest > max)
                max = sizeOfInterest;
            if(sizeOfInterest < min)
                min = sizeOfInterest;
            if(sizeOfInterest == 0)
                nonPurchaseUser++;
            total = total + sizeOfInterest;

            arrCount[sizeOfInterest]++;
        }
        System.out.println("sebaran data");
        for(int ii=0; ii<arrCount.length; ii++){
            System.out.print(ii+",");
        }
        System.out.println();
        for(int ii=0; ii<arrCount.length; ii++){
            System.out.print(+arrCount[ii]+",");
        }
        System.out.println();
        System.out.println("min transaction per user: " + min);
        System.out.println("max transaction per user: " + max);
        System.out.println("total transaction: "+ total);

        System.out.println("average transaction per user: "+ total/(mp.size()-nonPurchaseUser));
        System.out.println("number of non purchase user: "+ nonPurchaseUser);

    }


}
