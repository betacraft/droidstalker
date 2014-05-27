package com.rc.droid_stalker.workers;

import java.util.concurrent.*;

/**
 * Timeout workers
 * Author: akshay
 * Date  : 7/10/13
 * Time  : 5:29 PM
 */
public final class ScheduledWorkers {

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);


    public static ScheduledFuture submit(final Callable<Boolean> task, final int timeout, final TimeUnit timeUnit) {
        return executorService.schedule(task, timeout, timeUnit);
    }

    public static ScheduledFuture submit(final Runnable task, final int timeout, final TimeUnit timeUnit) {
        return executorService.schedule(task, timeout, timeUnit);
    }


    public static ScheduledFuture submit(final Runnable task, final int initialDelay, final int interval, final TimeUnit timeunit) {
        return executorService.scheduleWithFixedDelay(task, initialDelay, interval, timeunit);
    }
}
