package com.coolonWeb.testing;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.MemoryBasedModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 06/06/16.
 */
public class TestingMemoryBased {
    private MemoryBasedModel model;
    private String testTable;
    private int n;
    private int flag;
    private int numberOfThread;
    public TestingMemoryBased(MemoryBasedModel model, String testTable, int n){
        this.model = model;
        this.testTable = testTable;
        this.n = n;
        this.flag =0;
        this.numberOfThread = 8;
    }

    public void runTesting(){
        System.out.print("Run Testing for memorybased on "+testTable);
        String query = "SELECT * FROM member";
        DBConnect db = new DBConnect();
        db.setSql(query);
        ResultSet rs = db.execute();
        ArrayList<String> members = new ArrayList<>();

        System.out.println("retrieve members...");
        try {
            while(rs.next()){
               members.add(rs.getString("MEM_NO_ENC"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        ArrayList<Double> precisions = new ArrayList<>();
        ArrayList<Double> recalls = new ArrayList<>();
        Collections.synchronizedList(precisions);
        Collections.synchronizedList(recalls);
        ArrayList<Thread> threads = new ArrayList<>();
        for(int i = 1; i<= numberOfThread; i++){
            int endIndex = i*members.size()/numberOfThread;
            int startIndex = endIndex-(members.size()/numberOfThread)+1;
            Thread thread = new Thread(new CalculatePrecisionRecallMemory(members, precisions, recalls, model, n, testTable, startIndex, endIndex));
            threads.add(thread);
            thread.start();
        }

        try {
            System.out.println("Waiting for threads to finish.");
            for(Thread thread : threads){
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        calculatePrecisionRecall(precisions, recalls);

    }

    public void calculatePrecisionRecall(ArrayList<Double> precisions, ArrayList<Double> recalls){

        System.out.println("calculate precision");
        double precisionAvg = 0;
        for(double precision: precisions){
            precisionAvg += precision;
        }
        precisionAvg = precisionAvg/precisions.size();
        System.out.println("precision memory based for n:"+n+" datasettest:"+testTable+" is "+ precisionAvg);
        System.out.println("calculate recall");
        double recallAvg = 0;
        for(double recall: recalls){
            recallAvg += recall;
        }
        recallAvg = recallAvg/recalls.size();
        System.out.println("recall memory based for n:"+n+" datasettest:"+testTable+" is "+ recallAvg);
    }

}
