package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {

    @Autowired
    private StatisticsDailyService staService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void task1(){
        System.out.println("*********task1执行了");
    }

    //在每天凌晨1点，执行方法，把前一天数据进行添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
