package com.jake.javaBatch;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.*;
import java.util.Properties;
import java.util.Timer;


@Singleton
@Startup
public class DynamicScheduler {

    @Resource
    private TimerService timerService;

    public void setTimerService(TimerService timerService) {this.timerService = timerService; }

    @PostConstruct
    private void postConstruct() {
        timerService.createCalendarTimer(createSchedule());
    }

    @Timeout
    public void timerTimeout(Timer timer) {
        //BatchRuntime.getJobOperator().start("awsBackup", new Properties());
        System.out.println("---------timer---------");
    }

    private ScheduleExpression createSchedule(){

        ScheduleExpression expression = new ScheduleExpression();
        expression.hour("*");
        expression.minute("*");
        expression.second("10");

        return expression;
    }
}