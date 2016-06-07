package com.coolonWeb.testing;

import com.coolonWeb.model.NaiveBayesModel;

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
    public TestingModelBased(DataSet trainingSet, DataSet testingSet, int n){
        this.model = new NaiveBayesModel();
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.n = n;
        this.numberOfThread = 32;

        this.model.setDataset(this.trainingSet);
        this.model.calculatePriorProb();
        this.model.calculateConditionalProb();
    }

    public void runTesting(){
        ArrayList<String> members = model.getDataset().users;

        ArrayList<Double> precisions = new ArrayList<>();
        ArrayList<Double> recalls = new ArrayList<>();
        Collections.synchronizedList(precisions);
        Collections.synchronizedList(recalls);
        ArrayList<Thread> threads = new ArrayList<>();
        int residu = members.size()%numberOfThread;
        int chunkSize = (members.size() - residu)/numberOfThread;
        for(int i = 1; i<= numberOfThread; i++){
            int endIndex = i*chunkSize;
            int startIndex = endIndex-(chunkSize)+1;
            Thread thread = new Thread(new CalculatePrecisionRecallModel(testingSet,precisions,recalls,model,n,startIndex,endIndex));
            threads.add(thread);
            thread.start();
        }
        //calculate residu
        Thread threadResidu = new Thread(new CalculatePrecisionRecallModel(testingSet,precisions,recalls,model,n,members.size()-residu-1,members.size()-1));
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
        calculatePrecisionRecall(precisions, recalls);

    }

    public void calculatePrecisionRecall(ArrayList<Double> precisions, ArrayList<Double> recalls){

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
    }

}
