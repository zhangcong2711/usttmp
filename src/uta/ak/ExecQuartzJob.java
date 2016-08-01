/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.ak;

import java.text.SimpleDateFormat;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uta.ak.usttmp.quartz.CollectTwitterJob;

/**
 *
 * @author zhangcong
 */
public class ExecQuartzJob {
    
    
    
    public static void main(String[] args) throws Exception {
        
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        ApplicationContext applicationContext = 
            new ClassPathXmlApplicationContext("quartzContext.xml");
        Scheduler quartzScheduler =
            (Scheduler) 
            applicationContext.getBean("quartzScheduler");
        
        JobDetail jobDetail = JobBuilder.newJob(CollectTwitterJob.class)
                .withIdentity("qrtz_job_collecttwitter", "qrtz_job_collecttwitter")
                .build();  
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule()
                .repeatSecondlyForTotalCount(1000).withIntervalInHours(24);  
        
        
        Trigger trigger = TriggerBuilder.newTrigger()  
                .withIdentity("qrtz_trigger_collecttwitter", 
                              "qrtz_trigger_collecttwitter").startAt(format1.parse("2016-08-03 00:05:00"))
                .withSchedule(builder).build();  
        
//        quartzScheduler.scheduleJob(jobDetail, trigger);  
    }
    
}
