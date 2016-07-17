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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author zhangcong
 */
public class RecallTestCollect {
    
    public static void main(String[] args) {
        
        String sinceDate="2016-05-30";
        String untilDate="2016-06-04";
        String keyWord="japanese novel-filter:retweets";
        
        CollectTweets ct = new CollectTweets();
        
        ct.collectTweetsByKeyWords(keyWord,
                                   sinceDate,
                                   untilDate,
                                   "recall test: literature theme 02");
    }
    
}
