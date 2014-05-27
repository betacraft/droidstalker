package com.rc.droid_stalker.workers.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom thread factory
 * Author: akshay
 * Date  : 9/22/13
 * Time  : 2:49 PM
 */
public final class PriorityThreadFactory implements ThreadFactory {
    private static final String GRAPHITE_COUNTER_NAME = "core_thread_count";
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private int threadPriority = Thread.NORM_PRIORITY;


    public PriorityThreadFactory (final int threadPriority) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
        this.threadPriority = threadPriority;
    }


    public PriorityThreadFactory (final String namePrefix, final int threadPriority) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix + "-" +
                poolNumber.getAndIncrement() +
                "-thread-";
        this.threadPriority = threadPriority;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     *         create a thread is rejected
     */
    @Override
    public Thread newThread (Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        t.setPriority(this.threadPriority);
        return t;
    }


}
