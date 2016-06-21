package com.coolonWeb.testing;

import com.coolonWeb.model.NaiveBayesModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 06/06/16.
 */
public class TestingModelBased {
    private NaiveBayesModel model;
    public DataSet trainingSet;
    public DataSet testingSet;
    private int n;
    private int numberOfThread;
    public TestingModelBased(DataSet trainingSet, DataSet testingSet, int n, NaiveBayesModel model){
        this.model = model;
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.n = n;
        this.numberOfThread = 7;

    }

    public void runTesting(){
        ArrayList<String> members = model.getDataset().users;

        ArrayList<Double> precisions = new ArrayList<>();
        ArrayList<Double> recalls = new ArrayList<>();
        ArrayList<Double> f1Score = new ArrayList<>();
        Collections.synchronizedList(precisions);
        Collections.synchronizedList(f1Score);
        Collections.synchronizedList(recalls);
        ArrayList<Thread> threads = new ArrayList<>();
        int residu = members.size()%numberOfThread;
        int chunkSize = (members.size() - residu)/numberOfThread;
        for(int i = 1; i<= numberOfThread; i++){
            int endIndex = i*chunkSize;
            int startIndex = endIndex-(chunkSize)+1;
            Thread thread = new Thread(new CalculatePrecisionRecallModel(testingSet,precisions,recalls,f1Score,model,n,startIndex,endIndex));
            threads.add(thread);
            thread.start();
        }
        //calculate residu
        Thread threadResidu = new Thread(new CalculatePrecisionRecallModel(testingSet,precisions,recalls,f1Score,model,n,members.size()-residu-1,members.size()-1));
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
        System.out.println("threads to finish.");

        calculatePrecisionRecall(precisions, recalls, f1Score);

    }

    public void calculatePrecisionRecall(ArrayList<Double> precisions, ArrayList<Double> recalls, ArrayList<Double> f1Score){

        System.out.println("calculate precision");
        double precisionAvg = 0.0;
        for(double precision: precisions){
            precisionAvg += precision;
        }
        precisionAvg = precisionAvg/precisions.size();
        System.out.println("precision memory based for n:"+n+" is "+ precisionAvg);

        System.out.println("calculate recall");
        double recallAvg = 0.0;
        for(double recall: recalls){
            recallAvg += recall;
        }
        recallAvg = recallAvg/recalls.size();
        System.out.println("recall memory based for n:"+n+" is "+ recallAvg);

        System.out.println("calculate f1score");
        double f1scoreAvg = 0.0;
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
        String content = "n:"+this.n+" precision:"+precision+" recall:"+recall+" f1score:"+f1score+"\n";

        File file = new File("/home/pandhu/TA/model_test_result.txt");

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
