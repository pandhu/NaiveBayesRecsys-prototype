package com.coolonWeb.model;

import com.coolonWeb.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 11/05/16.
 */
public class MemoryBasedModel {

    public ArrayList<Item> getRecommendationByProduct(String idProduct, String idUser){
        ArrayList<Item> hasil = new ArrayList<>();

        String message = "Rekomendasi Barang Ditemukan";
        boolean isError = false;
        DBConnect db = new DBConnect();
        db.setSql("SELECT PRODUCT_NAME, LV1_CATEGORY, LV2_CATEGORY, LV3_CATEGORY FROM product WHERE PRODUCT_NUMBER_ENC="+idProduct);
        ResultSet rs = db.execute();

        Item item = null;
        try {
            while(rs.next()){
                item = new Item();
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");
                item.category1 = rs.getString("LV1_CATEGORY");
                item.category2 = rs.getString("LV2_CATEGORY");
                item.category3 = rs.getString("LV3_CATEGORY");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(item == null){
            isError = true;
            message = "Barang tidak ditemukan";
        } else {
            String level1 = item.category1;
            String level2 = item.category2;
            String level3 = item.category3;
            String prodname = item.name;
            String c = item.id;

            String queryUserMirip = "SELECT member.MEM_NO_ENC FROM member, (SELECT * FROM member WHERE MEM_NO_ENC='"+idUser+"') saya WHERE saya.AGE_GROUP=member.AGE_GROUP and saya.GENDER=member.GENDER";
            String query1 = "SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, ";
            query1 += "levenshtein_ratio(PRODUCT_NAME,\""+prodname+"\")";
            query1 += "FROM purchase WHERE MEM_NO_ENC IN (SELECT DISTINCT purchase.MEM_NO_ENC FROM purchase, ($queryUserMirip) usermirip WHERE PRODUCT_NUMBER_ENC='$c' and purchase.MEM_NO_ENC=usermirip.MEM_NO_ENC) AND LV3_CATEGORY='"+level3+"' AND PRODUCT_NUMBER_ENC<>'"+c+"' GROUP BY PRODUCT_NUMBER_ENC ORDER BY 3 DESC LIMIT 10";

            String query2 = "SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, ";
            query2 += "levenshtein_ratio(PRODUCT_NAME,\""+prodname+"\")";
            query2 += "FROM purchase WHERE MEM_NO_ENC IN (SELECT DISTINCT purchase.MEM_NO_ENC FROM purchase, ($queryUserMirip) usermirip WHERE PRODUCT_NUMBER_ENC='$c' and purchase.MEM_NO_ENC=usermirip.MEM_NO_ENC) AND LV2_CATEGORY='"+level2+"' AND PRODUCT_NUMBER_ENC<>'"+c+"' GROUP BY PRODUCT_NUMBER_ENC ORDER BY 3 DESC LIMIT 10";

            String query3 = "SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, ";
            query3 += "levenshtein_ratio(PRODUCT_NAME,\""+prodname+"\")";
            query3 += "FROM purchase WHERE MEM_NO_ENC IN (SELECT DISTINCT purchase.MEM_NO_ENC FROM purchase, ($queryUserMirip) usermirip WHERE PRODUCT_NUMBER_ENC='$c' and purchase.MEM_NO_ENC=usermirip.MEM_NO_ENC) AND LV1_CATEGORY='"+level1+"' AND PRODUCT_NUMBER_ENC<>'"+c+"' GROUP BY PRODUCT_NUMBER_ENC ORDER BY 3 DESC LIMIT 10";

            String query4 = "SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, ";
            query4 += "levenshtein_ratio(PRODUCT_NAME,\""+prodname+"\")";
            query4 += "FROM purchase, ("+queryUserMirip+") usermirip WHERE LV1_CATEGORY='"+level1+"' AND PRODUCT_NUMBER_ENC<>'"+c+"' AND purchase.MEM_NO_ENC = usermirip.MEM_NO_ENC GROUP BY PRODUCT_NUMBER_ENC ORDER BY 3 DESC LIMIT 10";

            String[] query = {query1,query2,query3,query4};

            int ii= 0;
            do {
                db.setSql(query[ii]);
                rs = db.execute();
                try {
                    while(rs.next()){
                        item = new Item();
                        //Retrieve by column name
                        item.id = rs.getString("PRODUCT_NUMBER_ENC");
                        item.name = rs.getString("PRODUCT_NAME");

                        hasil.add(item);
                    }
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ii++;
            } while (hasil.size() < 10 && ii <4);

        }
        return hasil;
    }

    public ArrayList<Item> getUserHistory(String idUser){
        boolean isError = false;
        String message = "Riwayat Ditemukan";
        DBConnect db = new DBConnect();
        db.setSql("SELECT * FROM purchase WHERE MEM_NO_ENC = "+idUser);
        ResultSet rs = db.execute();
        ArrayList<Item> result = new ArrayList<>();
        try {
            while(rs.next()){
                Item item = new Item();
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");

                result.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Item> getRecommendationByUser(String idUser){
        DBConnect db = new DBConnect();
        db.setSql("SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, count(PRODUCT_NUMBER_ENC) jumlah FROM purchase, ( SELECT MEM_NO_ENC,COUNT(MEM_NO_ENC) jumlah FROM purchase WHERE PRODUCT_NUMBER_ENC in ( SELECT PRODUCT_NUMBER_ENC FROM purchase WHERE MEM_NO_ENC="+idUser+" ) AND MEM_NO_ENC <> "+idUser+" GROUP BY MEM_NO_ENC ORDER BY JUMLAH DESC LIMIT 10 ) t WHERE t.MEM_NO_ENC=purchase.MEM_NO_ENC AND purchase.PRODUCT_NUMBER_ENC not in ( SELECT PRODUCT_NUMBER_ENC FROM purchase WHERE MEM_NO_ENC="+idUser+" ) GROUP BY PRODUCT_NUMBER_ENC ORDER BY JUMLAH DESC LIMIT 10");
        ResultSet rs = db.execute();
        ArrayList<Item> result = new ArrayList<>();
        try {
            while(rs.next()){
                Item item = new Item();
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");

                result.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result.size() >0)
            return result;

        //recommendation by domographic
        db.setSql("SELECT PRODUCT_NUMBER_ENC, PRODUCT_NAME, count(PRODUCT_NUMBER_ENC) jumlah FROM purchase WHERE MEM_NO_ENC in ( SELECT MEM_NO_ENC FROM MEMBER, ( SELECT AGE_GROUP, GENDER FROM MEMBER WHERE MEM_NO_ENC = "+idUser+" ) q WHERE q.AGE_GROUP = member.AGE_GROUP and q.GENDER = member.GENDER ) GROUP BY PRODUCT_NUMBER_ENC ORDER BY JUMLAH DESC LIMIT 10");
        rs = db.execute();
        try {
            while(rs.next()){
                Item item = new Item();
                //Retrieve by column name
                item.id = rs.getString("PRODUCT_NUMBER_ENC");
                item.name = rs.getString("PRODUCT_NAME");

                result.add(item);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
