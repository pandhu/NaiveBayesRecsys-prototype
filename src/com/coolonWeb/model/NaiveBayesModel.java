package com.coolonWeb.model;

import java.util.*;

/**
 * Created by pandhu on 30/03/16.
 */
public class NaiveBayesModel {
    private ArrayList<String> users;
    private HashMap<String, Item> itemsData;
    private ArrayList<String> items;
    private HashMap<String, Double> priorProbs;
    private ArrayList<Transaction> transactions;
    private HashMap<String, ArrayList<String>> userInterests;
    private HashMap<String, Double> conditionalProbs;
    private static final int CN= 3;
    public NaiveBayesModel() {
        System.out.println("init model");
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.priorProbs = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.userInterests = new HashMap<>();
        this.conditionalProbs = new HashMap<>();
        this.itemsData = new HashMap<>();
        System.out.println("init model done");
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
    public void calculatePriorProb(){
        System.out.println("Start calculate prior probabilistic");
        HashMap<String, HashMap<String, Integer>> hasVote = new HashMap<>();
        HashMap<String, Integer> countProbs = new HashMap<>();

        for(Transaction transaction : this.transactions){
            //kalo si item baru muncul di transaksi
            if(hasVote.get(transaction.item) == null){
                hasVote.put(transaction.item, new HashMap<String, Integer>());
            }
            //kalo user belom vote
            if(hasVote.get(transaction.item).get(transaction.user) == null){
                //kalo belom ada hasnya
                if(countProbs.get(transaction.item) == null)
                    countProbs.put(transaction.item, 1);
                else
                    countProbs.put(transaction.item, countProbs.get(transaction.item)+1);

            }
        }

        Iterator it = countProbs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double priorProbability = ((int) pair.getValue()/1.0)/users.size();
            this.priorProbs.put(""+pair.getKey(), priorProbability);
        }
        System.out.println("calculate prior probabilistic done");
    }

    public void calculateConditionalProb(){
        System.out.println("Start calculate conditional probabilistic");

        HashMap<String, Integer> count = new HashMap<>();
        for(String user : users){
            for(String itemA: userInterests.get(user)){
                for(String itemB: userInterests.get(user)) {
                    if (!itemA.equals(itemB)) {
                        //kalo belom ada
                        if(count.get(itemA+","+itemB) == null){
                            count.put(itemA+","+itemB, 1);
                        } else {
                            count.put(itemA+","+itemB, count.get(itemA+","+itemB)+1);
                        }
                    }
                }
            }
        }

        Iterator it = count.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double jointProb = ((int) pair.getValue()/1.0)/users.size();
            double conditionalProb = jointProb/this.priorProbs.get(pair.getKey().toString().split(",")[1]);
            this.conditionalProbs.put(pair.getKey().toString(), conditionalProb);
        }
        System.out.println("calculate conditional probabilistic done");
    }

    public ArrayList<Item> makeTopNRecommendation(String user, int n){
        HashMap<String, Double> recommendedItems = new HashMap<>();
        int cn = CN;
        if(this.userInterests.get(user).size() < cn);
        cn = this.userInterests.get(user).size();

        for(String item: this.items){
            //System.out.println("new item");
            double priorProbs;
            if (this.priorProbs.get(item) == null){
                //priorProbs = 0;
                continue;
            } else{
                priorProbs = this.priorProbs.get(item);
            }
            double recProbs = priorProbs;
            for(String itemInterest: this.userInterests.get(user)){
                double conditionalProbs;
                if(this.conditionalProbs.get(itemInterest+","+item) == null){
                    continue;
                } else {
                    conditionalProbs = this.conditionalProbs.get(itemInterest+","+item);
                }
                double value;
                if(this.priorProbs.get(itemInterest)!=null)
                    value = Math.pow(conditionalProbs/this.priorProbs.get(itemInterest), cn / this.userInterests.get(user).size());
                else
                    value = 0;
                recProbs = recProbs * value;
                //System.out.println(conditionalProbs+"|"+priorProbs+"|"+value+"|"+recProbs);
            }

            recommendedItems.put(item, recProbs);
        }
        HashMap<String, Double> sortedRecommendedItems = sortByComparator(recommendedItems);
        ArrayList<Item> nRecommendedItems = new ArrayList<>();

        Iterator it = sortedRecommendedItems.entrySet().iterator();
        for (int ii = 0; ii < n; ii++) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getValue());
            Item recommendedItem = this.itemsData.get((String)pair.getKey());
            nRecommendedItems.add(recommendedItem);
        }
        return nRecommendedItems;
    }
    public void printUsers(){
        for(String user: this.users){
            System.out.println(user);
        }
    }

    public void printItems(){
        for(String item: this.items){
            System.out.println(item);
        }
    }

    public void printTransaction(){
        for(Transaction transaction : transactions){
            System.out.println(transaction);
        }
    }

    public void printUserInterests(){
        Map mp = this.userInterests;
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String user = (String) pair.getKey();
            System.out.print(user+": ");
            for(String item : (ArrayList<String>)pair.getValue()){
                System.out.print(item+" ");
            }
            System.out.println();
        }
    }

    public void setUsers(ArrayList<String> users){
        this.users = users;
    }
    public void setItems(ArrayList<String> items){
        this.items = items;
    }
    public void setTransactions(ArrayList<Transaction> transasctons){
        this.transactions = transasctons;
    }
    public void setConditionalProbs(HashMap<String, Double> conditionalProbs) {
        this.conditionalProbs = conditionalProbs;
    }
    public void setItemsData(HashMap<String, Item> itemsData) {
        this.itemsData = itemsData;
    }

    public void printPriorProbs() {
        Map mp = this.priorProbs;
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(!pair.getValue().equals("0.0"))
                System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    public void printConditionalProbs() {
        Map mp = this.conditionalProbs;
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(!pair.getValue().equals("0.0"))
                System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }
    public ArrayList<Item> getUserHistoryTransaction(String idUser){
        ArrayList<String> items =  this.userInterests.get(idUser);
        ArrayList<Item> transItems = new ArrayList<>();
        for(String item : items){
            Item transItem = this.itemsData.get(item);
            transItems.add(transItem);
        }
        return transItems;
    }

    public void buy(String idUser, String idItem){
        System.out.println(idUser+" buy "+idItem);
        if(!this.userInterests.get(idUser).contains(idItem)){
            this.userInterests.get(idUser).add(idItem);
        }
        return;
    }
    /*
    * UTILITIES
    * */

    private static HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Convert sorted map back to a Map
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Double> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public double getPriorProbs(String idItem){
        return this.priorProbs.get(idItem);
    }

    public void registerNewUser(String id){
        this.users.add(id);
        this.userInterests.put(id, new ArrayList<String>());
        System.out.println("new user registered with id "+id);
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public HashMap<String, Double> getPriorProbs() {
        return priorProbs;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public HashMap<String, ArrayList<String>> getUserInterests() {
        return userInterests;
    }

    public HashMap<String, Double> getConditionalProbs() {
        return conditionalProbs;
    }

    public HashMap<String, Item> getItemsData() {
        return itemsData;
    }
}

