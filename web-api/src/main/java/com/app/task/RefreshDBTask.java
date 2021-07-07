package com.app.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshDBTask  implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RefreshDBTask.class);
    @Override
    public void run() {

        //System.out.print("\n\n" + Thread.currentThread().getName()+" START: "+new Date());
        //System.out.print(" ...... " + Thread.currentThread().getName()+" DONE: "+new Date());
        if (Thread.interrupted()){
            log.warn("Refresh DB Task Interrupted");
            return;
        }
    }
}
