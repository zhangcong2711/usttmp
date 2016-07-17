/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author zhangcong
 */
public class FilterStopWordStemming {
    
    public static void main(String[] args) {
        
        try {
            
            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
            System.out.println("connection yes");
            
            System.out.println("query records...");
//            String querySQL="select * from c_rawtext";
            String querySQL="SELECT\n" +
                                "	* " +
                                "FROM " +
                                "	c_rawtext " +
                                "WHERE " +
                                "tag like 'mallet integrated%'";
            
            PreparedStatement preparedStmt = con.prepareStatement(querySQL);
            ResultSet rs=preparedStmt.executeQuery();
            
            FileExcludeStopWord fesw=new FileExcludeStopWord();
            List<Map> text_lines=new ArrayList<Map>();
            
            Set<String> filterDupSet=new HashSet<>();
            
            while (rs.next()) {           
                
                System.out.println(rs.getString("title") + "  " +
                                   rs.getString("text") + "  " +
                                   rs.getString("tag"));
                String filteredText=fesw.doExForTweet(rs.getString("text"));
                if(filteredText!=null && (!filteredText.trim().isEmpty())){
                    Map<String,String> hs=new HashMap<>();
                    hs.put("title", rs.getString("title"));
                    hs.put("text", filteredText);
                    hs.put("tag", rs.getString("tag"));
                    hs.put("rawtext_id", rs.getString("mme_eid"));
                    
                    if(!filterDupSet.contains(filteredText)){
                        filterDupSet.add(filteredText);
                        text_lines.add(hs);
                    }
                }
                
            }
            
            PreparedStatement insertPS = con.prepareStatement("INSERT INTO c_text(mme_lastupdate, mme_updater, title, text, tag, rawtext_id) VALUES (NOW(), \"AK\", ?, ?, ?, ?)");
//            PreparedStatement queryDupPS=con.prepareStatement("select * from c_text where text=? and tag like 'recall tweet%'");
            
            for(Map<String,String> hmap : text_lines){
                
                
//                queryDupPS.clearParameters();
//                queryDupPS.setString(1, hmap.get("text"));
//                ResultSet duprs=queryDupPS.executeQuery();
//                if(duprs.next()){
//                    continue;
//                }
                
//                System.out.println("Start to insert records... "+hmap.get("text"));
//                insertPS.clearParameters();
                insertPS.setString(1, hmap.get("title"));
                insertPS.setString(2, hmap.get("text"));
                insertPS.setString(3, hmap.get("tag"));
//                insertPS.setString(3, "wiki cate");
                insertPS.setString(4, hmap.get("rawtext_id"));
                insertPS.addBatch();
            }

            System.out.println("Start to insert records...");
            insertPS.clearParameters();
            int[] results = insertPS.executeBatch();
            
        }catch(
                ClassNotFoundException | InstantiationException | 
                IllegalAccessException | SQLException e){
            e.printStackTrace();
        }
    }
    
}
