/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author zhangcong
 */
public class CollectTweets {
    
    public static void main(String[] args) {
//        String mostInfluencedMedias1="/Users/zhangcong/dev/corpus/the-most-influenced-medias1.txt";
//        String mostFollowedTwitters="/Users/zhangcong/dev/corpus/the-most-followed-twitters.txt";
        
        String newSocialMediaList="/Users/zhangcong/dev/corpus/new-social-meida-list.txt";

//        String sinceDate="2016-06-09";
//        String untilDate="2016-06-11";
        
        CollectTweets ct = new CollectTweets();
        ct.collectTweetsByFileList(newSocialMediaList,
                                   "2016-07-11",
                                   "2016-07-14",
                                   "function test : add mining task");
        
        
//        Timer timer=new Timer(); 
//        TimerTask task=new TimerTask(){
//            public void run(){
//                System.out.println("计时任务1");
//                ct.collectTweetsByFileList(newSocialMediaList,
//                                   "2016-06-12",
//                                   "2016-06-15",
//                                   "mallet integrated 02");
//            }
//        };
//        timer.schedule(task,960000);   
        
//        Timer timer=new Timer(); 
//        TimerTask task=new TimerTask(){
//            public void run(){
//                System.out.println("计时任务2");
//                ct.collectTweetsByFileList(mostFollowedTwitters,
//                                   sinceDate,
//                                   untilDate,
//                                   "the most followed twitters");
//            }
//        };
//        timer.schedule(task,960000);   
    
    }
    
    public void collectTweetsByFileList(String path, 
                                        String sinceDate, 
                                        String untilDate,
                                        String tag){
        try {
            // The factory instance is re-useable and thread safe.
            System.out.println("Start to collect tweets from :");
            Set<String> mediaList = new HashSet<String>();
            File medias=new File(path);
            BufferedReader mdsreader = new BufferedReader(new FileReader(medias));
            String tempString = mdsreader.readLine();
            while ((tempString = mdsreader.readLine()) != null) {
                System.out.println(tempString.toLowerCase());
		mediaList.add(tempString.toLowerCase());
            }
            
            
            
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey("LuhVZOucqdHX6x0lcVgJO6QK3")
              .setOAuthConsumerSecret("6S7zbGLvHMXDMgRXq7jRIA6QmMpdI8i5IJNpnjlB55vpHpFMpj")
              .setOAuthAccessToken("861637891-kLunD37VRY8ipAK3TVOA0YKOKxeidliTqMtNb7wf")
              .setOAuthAccessTokenSecret("vcKDxs6qHnEE8fhIJr5ktDcTbPGql5o3cNtZuztZwPYl4");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            
            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
            System.out.println("connection yes");
            
            String insertSQL="INSERT INTO c_rawtext(mme_lastupdate, mme_updater, title, text, tag, text_createdate) "
                                + "VALUES (NOW(), \"AK\", ?, ?, ?, ?)";
            PreparedStatement insertPS = con.prepareStatement(insertSQL);
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for(String mediastr : mediaList){
                Query query = new Query("from:" + mediastr);
                query.setSince(sinceDate);
                query.setUntil(untilDate);
                query.setCount(100);
                query.setLang("en");
                
                QueryResult result = twitter.search(query);
                for (Status status : result.getTweets()) {
//                    System.out.println("@" + status.getUser().getScreenName() +
//                                       " | " + status.getCreatedAt().toString() +
//                                       ":" + status.getText());
//                    System.out.println("Inserting the record into the table...");
                    
                    String formattedDate = format1.format(status.getCreatedAt());
                    
                    insertPS.setString (1, status.getUser().getScreenName());
                    insertPS.setString (2, status.getText());
                    insertPS.setString (3, tag);
                    insertPS.setString (4, formattedDate);
                    insertPS.addBatch();
                }
            }
            
            System.out.println("Start to insert records...");
            insertPS.clearParameters();
            int[] results = insertPS.executeBatch();
            
        } catch (Exception te) {
            te.printStackTrace();
            System.out.println("Failed: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    public void collectTweetsByKeyWords(String keyWords,
                                        String sinceDate, 
                                        String untilDate,
                                        String tag){
        try {
            
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey("LuhVZOucqdHX6x0lcVgJO6QK3")
              .setOAuthConsumerSecret("6S7zbGLvHMXDMgRXq7jRIA6QmMpdI8i5IJNpnjlB55vpHpFMpj")
              .setOAuthAccessToken("861637891-kLunD37VRY8ipAK3TVOA0YKOKxeidliTqMtNb7wf")
              .setOAuthAccessTokenSecret("vcKDxs6qHnEE8fhIJr5ktDcTbPGql5o3cNtZuztZwPYl4");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            
            Connection con = null; //定义一个MYSQL链接对象
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
            System.out.println("connection yes");
            
            String insertSQL="INSERT INTO c_rawtext(mme_lastupdate, mme_updater, title, text, tag, text_createdate) VALUES (NOW(), \"AK\", ?, ?, ?, ?)";
            PreparedStatement insertPS = con.prepareStatement(insertSQL);
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            
            Query query = new Query(keyWords);
            query.setSince(sinceDate);
            query.setUntil(untilDate);
            query.setCount(100);
            query.setLang("en");
            
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
//                    System.out.println("@" + status.getUser().getScreenName() +
//                                       " | " + status.getCreatedAt().toString() +
//                                       ":" + status.getText());
//                    System.out.println("Inserting the record into the table...");

                String formattedDate = format1.format(status.getCreatedAt());

                insertPS.setString (1, status.getUser().getScreenName());
                insertPS.setString (2, status.getText());
                insertPS.setString (3, tag);
                insertPS.setString (4, formattedDate);
                insertPS.addBatch();
            }
            
            System.out.println("Start to insert records...");
            insertPS.clearParameters();
            int[] results = insertPS.executeBatch();
            
        } catch (Exception te) {
            te.printStackTrace();
            System.out.println("Failed: " + te.getMessage());
            System.exit(-1);
        }
    }
}
