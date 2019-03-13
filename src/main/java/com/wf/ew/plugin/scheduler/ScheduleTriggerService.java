package com.wf.ew.plugin.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/3/7.
 */
@Service
public class ScheduleTriggerService {

    @Autowired
    private Scheduler scheduler;

    @Scheduled(cron = "0/1 * * * * ?")
    public void refreshTrigger(){
        JobDetail jobDetail = null;

        // 创建JobDetail
        //CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule()

    }

}
