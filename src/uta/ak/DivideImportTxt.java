/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;

/**
 *
 * @author zhangcong
 */
public class DivideImportTxt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            
            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
            System.out.println("yes");
            
            StringBuffer sb = new StringBuffer();
            FileUtils.readToBuffer(sb, "/Users/zhangcong/dev/corpus/c_001.txt");
            
            int segmentSize=2048;
            
            int maxLength=sb.length();
            int segmentNum=(int)Math.floor(maxLength/segmentSize)+1;
            
            System.out.println("Inserting records into the table...");
            String insertSQL="INSERT INTO c_rawtext(mme_lastupdate, mme_updater, title, text, tag) VALUES (NOW(), \"AK\", ?, ?, ?)";
            PreparedStatement preparedStmt = con.prepareStatement(insertSQL);
               
            for(int i=0;i<segmentNum;i++){
                boolean breakSign=false;
                int startIdx=segmentSize * i;
                int endIdx=segmentSize * (i+1);
                if(endIdx>maxLength){
                    endIdx=maxLength;
                }
                String text=sb.substring(startIdx, endIdx);
                
                preparedStmt.setString (1, "c_001_"+(i+1));
                preparedStmt.setString (2, text);
                preparedStmt.setString (3, "wiki cate");
                preparedStmt.execute();
            }
            
            System.out.println("Successfully finish");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    
}
