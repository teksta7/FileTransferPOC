package com.jake.javaBatch;


import javax.batch.runtime.BatchRuntime;
import java.util.Properties;

public class MyJob implements Runnable {

    public void run() {
        BatchRuntime.getJobOperator().start("awsBackup", new Properties());
    }

}
