package com.jake.javaBatch;


import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.Properties;


@Singleton
public class MyScheduler {

    @Schedule(hour = "*", minute = "*", second = "0")
    public void myJob() {
        BatchRuntime.getJobOperator().start("awsBackup", new Properties());
    }
}