package com.coolonWeb.testing;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.MemoryBasedModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        String query = "SELECT * FROM "+testTable+" GROUP BY MEM_NO_ENC";
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
        ArrayList<Double> f1Score = new ArrayList<>();

        Collections.synchronizedList(precisions);
        Collections.synchronizedList(recalls);
        ArrayList<Thread> threads = new ArrayList<>();
        int residu = members.size()%numberOfThread;
        int chunkSize = (members.size() - residu)/numberOfThread;
        for(int i = 1; i<= numberOfThread; i++){
            DBConnect dbc = new DBConnect();
            int endIndex = i*chunkSize;
            int startIndex = endIndex-(chunkSize)+1;
            Thread thread = new Thread(new CalculatePrecisionRecallMemory(members, precisions, recalls,f1Score, model, n, testTable, startIndex, endIndex, dbc ));
            threads.add(thread);
            thread.start();
        }
        //calculate residu
        DBConnect dbc = new DBConnect();
        Thread threadResidu = new Thread(new CalculatePrecisionRecallMemory(members,precisions,recalls,f1Score,model,n,testTable, members.size()-residu-1,members.size()-1, dbc));
        threads.add(threadResidu);
        threadResidu.start();

        try {
            System.out.println("Waiting for threads to finish.");
            for(Thread thread : threads){
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        calculatePrecisionRecall(precisions, recalls, f1Score);

    }

    public void calculatePrecisionRecall(ArrayList<Double> precisions, ArrayList<Double> recalls, ArrayList<Double> f1Score){

        System.out.println("calculate precision");
        double precisionAvg = 0;
        for(double precision: precisions){
            precisionAvg += precision;
        }
        precisionAvg = precisionAvg/precisions.size();
        System.out.println("precision memory based for n:"+n+" is "+ precisionAvg);

        System.out.println("calculate recall");
        double recallAvg = 0;
        for(double recall: recalls){
            recallAvg += recall;
        }
        recallAvg = recallAvg/recalls.size();
        System.out.println("recall memory based for n:"+n+" is "+ recallAvg);

        System.out.println("calculate f1score");
        double f1scoreAvg = 0;
        for(double f1: f1Score){
            f1scoreAvg+= f1;
        }
        f1scoreAvg = f1scoreAvg/f1Score.size();
        System.out.println("f1score memory based for n:"+n+" is "+ f1scoreAvg);

        try {
            printToFile(precisionAvg, recallAvg, f1scoreAvg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printToFile(double precision, double recall, double f1score) throws IOException {
        String content = testTable+" n:"+this.n+" precision:"+precision+" recall:"+recall+" f1score:"+f1score+"\n";

        File file = new File("/home/pandhu/TA/memory_test_result.txt");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
    }

}
