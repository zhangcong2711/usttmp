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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author zhangcong
 */
public class TestNodejsInterface {
    
    public static void main(String[] args) throws Exception {
        
        
        new TestNodejsInterface().testFromDB();
        
        
//        String interfaceMsg="<message> " +
//                                "    <title> " +
//                                "7788"+
//                                "    </title> " +
//                                "    <text> " +
//                                StringEscapeUtils.escapeXml10("afefefefefe///7777$$%#") +
//                                "    </text> " +
//                                "    <textCreatetime> " +
//                                "2016-07-22 12:00:00" +
//                                "    </textCreatetime> " +
//                                "    <tag> " +
//                                "afjeof" +
//                                "    </tag> " +
//                                "</message>";
//                    
//    String restUrl="http://192.168.0.103:8991/usttmp_textreceiver/rest/addText";
//    
//    RestTemplate restTemplate = new RestTemplate();
//     
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.TEXT_XML);
//    headers.setAccept(Arrays.asList(MediaType.TEXT_XML));
//    HttpEntity<String> entity = new HttpEntity<String>(interfaceMsg, headers);
//     
//    ResponseEntity<String> result = restTemplate.exchange(restUrl, HttpMethod.POST, entity, String.class);
    
      

    }
    
    private void testFromDB() throws Exception{
        
        Connection con = null; //定义一个MYSQL链接对象
        Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/USTTMP", "root", "root.123"); //链接本地MYSQL
        System.out.println("connection yes");
        
        System.out.println("query records...");
        String querySQL="SELECT" +
                            "	* " +
                            "FROM " +
                            "	c_rawtext " +
                            "WHERE " +
                            "tag like 'function%'";

        PreparedStatement preparedStmt = con.prepareStatement(querySQL);
        ResultSet rs=preparedStmt.executeQuery();

        Set<String> filterDupSet=new HashSet<>();
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (rs.next()) {           

            System.out.println(rs.getString("title") + "  " +
                               rs.getString("text") + "  " +
                               rs.getString("tag"));
            
            String formattedDate = format1.format(new Date());
            
            String interfaceMsg="<message> " +
                                "    <title> " +
                                rs.getString("title")+
                                "    </title> " +
                                "    <text> " +
                                StringEscapeUtils.escapeXml10(rs.getString("text")) +
                                "    </text> " +
                                "    <textCreatetime> " +
                                formattedDate +
                                "    </textCreatetime> " +
                                "    <tag> " +
                                rs.getString("tag") +
                                "    </tag> " +
                                "</message>";
                    
//            String restUrl="http://192.168.0.103:8991/usttmp_textreceiver/rest/addText";
            String restUrl="http://127.0.0.1:8991/usttmp_textreceiver/rest/addText";
            
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            headers.setAccept(Arrays.asList(MediaType.TEXT_XML));
//            headers.setContentLength();
            HttpEntity<String> entity = new HttpEntity<String>(interfaceMsg, headers);

            ResponseEntity<String> result = restTemplate.exchange(restUrl, HttpMethod.POST, entity, String.class);
            
            System.out.println(result.getBody());

        }
        
        
    }
    
}
