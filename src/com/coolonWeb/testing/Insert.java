package com.coolonWeb.testing;

import com.coolonWeb.DBConnect;
import com.coolonWeb.model.Transaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by root on 06/06/16.
 */
public class Insert implements Runnable {
    private String tableName;
    private Transaction transaction;
    public Insert(String tableName, Transaction trans){
        this.tableName = tableName;
        this.transaction = trans;
    }
    @Override
    public void run() {
        DBConnect db = new DBConnect();
        String query = "INSERT INTO "+this.tableName+" (ORD_MONTH, MEM_NO_ENC, PRODUCT_NUMBER_ENC, PRODUCT_NAME, LV1_CATEGORY, LV2_CATEGORY, LV3_CATEGORY, OPTIONS) values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = db.conn.prepareStatement(query);
            preparedStatement.setString(1, transaction.ordmonth);
            preparedStatement.setString(2, transaction.user);
            preparedStatement.setString(3, transaction.item);
            preparedStatement.setString(4, transaction.itemName);
            preparedStatement.setString(5, transaction.lv1cat);
            preparedStatement.setString(6, transaction.lv2cat);
            preparedStatement.setString(7, transaction.lv3cat);
            preparedStatement.setString(8, transaction.opt);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
    }
}
