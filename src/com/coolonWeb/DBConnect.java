package com.coolonWeb;

import java.sql.*;
/**
 * Created by root on 11/04/16.
 */
public class DBConnect {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/elevenia-new";

    static final String USER = "root";
    static final String PASS = "password0!";
    public Connection conn;
    private String sql;
    private Statement stmt;

    public DBConnect(){
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e){

        }
        this.stmt = null;
    }
    public DBConnect(String sql){
        this.sql = sql;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e){

        }
        this.stmt = null;

    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ResultSet execute(){
        ResultSet rs = null;
        try {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(this.sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int executeUpdate(){
        try {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            return stmt.executeUpdate(this.sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void closeConnection() {
        try {
            if(stmt != null)
                stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void connect() {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM member";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                String id  = rs.getString("MEM_NO_ENC");
                String age = rs.getString("AGE_GROUP");
                String gender = rs.getString("GENDER");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Age: " + age);
                System.out.print(", First: " + gender);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end FirstExample

