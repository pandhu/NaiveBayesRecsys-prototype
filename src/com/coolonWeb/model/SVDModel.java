package com.coolonWeb.model;

import com.coolonWeb.testing.DataSet;
import weka.core.matrix.Matrix;
import weka.core.matrix.SingularValueDecomposition;

import java.util.ArrayList;

/**
 * Created by root on 13/05/16.
 */
public class SVDModel {
    DataSet dataset;
    Matrix matrixUS;
    Matrix matrixSV;

    public SVDModel(DataSet dataset){
        this.dataset = dataset;
        Matrix matrixR = createMatrixR(1000);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrixR);
        Matrix matrixU = svd.getU();
        Matrix matrixSquareRootS = squareRoot(svd.getS());
        Matrix matrixV = svd.getV();
        System.out.println("dimession of u:"+matrixU.getRowDimension()+"x"+matrixU.getColumnDimension());
        System.out.println("dimession of s:"+matrixSquareRootS.getRowDimension()+"x"+matrixSquareRootS.getColumnDimension());
        System.out.println("dimession of v:"+matrixV.getRowDimension()+"x"+matrixV.getColumnDimension());
        matrixUS = matrixU.times(matrixSquareRootS);
        matrixSV = matrixSquareRootS.times(matrixV);
        System.out.println("Rank U: "+matrixU.rank()+" | Rank S:"+matrixSquareRootS.rank()+" | Rank V:"+matrixV.rank()+" | SVD rank:"+svd.rank());
    }

    public Matrix squareRoot(Matrix matrix){
        for(int ii = 0; ii< matrix.getArray().length; ii++){
            for(int jj = 0; jj<matrix.getArray()[0].length;jj++){
                matrix.set(ii,jj, Math.sqrt(matrix.get(ii,jj)));
            }
        }
        return matrix;
    }

    public Matrix createMatrixR(int numberOfTransaction){
        ArrayList<Transaction> transactions = dataset.transactions;
        ArrayList<String> users = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        int ii = 0;
        for(Transaction transaction:transactions){
            if(ii++> numberOfTransaction)
                break;
            if(users.indexOf(transaction.user) == -1)
                users.add(transaction.user);
            if(items.indexOf(transaction.item) == -1)
                items.add(transaction.item);
        }
        System.out.println(users.size());
        System.out.println(items.size());

        double[][] matrix = new double[users.size()][items.size()];
        System.out.println("init matrix: "+users.size()+"x"+items.size());

        int count = 0;
        for(Transaction transaction: transactions){
            int userIndex = users.indexOf(transaction.user);
            int itemIndex = items.indexOf(transaction.item);
            if(userIndex == -1 || itemIndex == -1)
                continue;

            matrix[userIndex][itemIndex] = 1;

        }
        return new Matrix(matrix);
    }

}