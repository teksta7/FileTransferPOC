package com.jake.javaBatch;


import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

@Startup
@Singleton
public class Scheduler {

    static final long INITIAL_DELAY = 0;
    static final long PERIOD = 5;

    @Resource
    ManagedScheduledExecutorService scheduler;

    @PostConstruct
    public void init() {
        this.scheduler.scheduleWithFixedDelay(new MyJob(), INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

}
