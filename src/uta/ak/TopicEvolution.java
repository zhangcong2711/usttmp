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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import uta.ak.usttmp.model.Topic;
import uta.ak.usttmp.model.EvolutionRelationship;
import uta.ak.usttmp.model.WordProbability;
import uta.ak.usttmp.service.impl.TopicEvolutionServiceImpl;

/**
 *
 * @author zhangcong
 */
public class TopicEvolution {
    
    public static void main(String[] args) {
        
        try {
            
            List<List<Topic>> topicGroup=new ArrayList<List<Topic>>();
            List<List<EvolutionRelationship>> evolutionGroup= new ArrayList<List<EvolutionRelationship>>(); 

            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
            System.out.println("connection yes");
            
            System.out.println("query records...");
            String querySQL="select * from c_topic where remark=?";
            PreparedStatement preparedStmt = con.prepareStatement(querySQL);
            
            for(int i=1;i<=2;i++){
                
                
                preparedStmt.setString(1, "Mallet integrated 0"+String.valueOf(i));
                ResultSet rs=preparedStmt.executeQuery();

                List<Topic> topics=new ArrayList<Topic>();

                while (rs.next()) {           

                    Topic tp= new Topic();
                    Set<WordProbability> wpset=new CopyOnWriteArraySet<WordProbability>();
                    
                    System.out.println(rs.getString("name") + "  " +
                                       rs.getString("content") + "  " +
                                       rs.getString("remark"));
                    
                    String wpstr=rs.getString("content");
                    String[] wpmaps=wpstr.split(",");
                    for(String mapstr : wpmaps){
                        String[] kvs=mapstr.split(":");
                        WordProbability wp=new WordProbability();
                        wp.setWord(kvs[0]);
                        wp.setProbability(Double.parseDouble(kvs[1]));
                        wpset.add(wp);
                    }
                    
                    tp.setId(rs.getLong("mme_eid"));
                    tp.setName(rs.getString("name"));
                    tp.setWordProbabilityMaps(wpset);
                    tp.setRemark(rs.getString("remark"));
                    
                    topics.add(tp);
                }
                
                topicGroup.add(topics);
                
                rs.close();
            }
            
            TopicEvolutionServiceImpl tei = new TopicEvolutionServiceImpl();
            evolutionGroup.add(tei.getTopicEvolutionRelationships(topicGroup.get(0), 
                                                                  topicGroup.get(1)));
//            evolutionGroup.add(tei.getTopicEvolutionRelationships(topicGroup.get(1), 
//                                                                  topicGroup.get(2)));
            
            for(List<EvolutionRelationship> tErList : evolutionGroup){
                System.out.println("============================================");
                for(EvolutionRelationship tEr : tErList){
                    System.out.println(
                            tEr.getPreTopic().getId() 
                            + " ("+ tEr.getRankAgainstPreTopicInNextGroup() +")" 
                            + " ------ "+ tEr.getSimilarity() +" ------> " 
                            + tEr.getNextTopic().getId() 
                            + " ("+ tEr.getRankAgainstNextTopicInPreGroup() +")");
                    System.out.println("    * "
                                       + tEr.getPreTopic().toString());
                    System.out.println("    * "
                                       + tEr.getNextTopic().toString());
                }
                System.out.println("============================================");
            }
            
            preparedStmt.close();
            con.close();
            
        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally{
            
            
            
        }
        
        
        
    }
}
