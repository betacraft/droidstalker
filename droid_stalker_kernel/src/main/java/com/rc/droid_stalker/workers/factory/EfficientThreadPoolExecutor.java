package com.rc.droid_stalker.workers.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Thread pool executor
 * Author: akshay
 * Date  : 9/30/13
 * Time  : 2:55 PM
 */
public class EfficientThreadPoolExecutor extends ThreadPoolExecutor {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(EfficientThreadPoolExecutor.class);

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory and rejected execution handler.
     * It may be more convenient to use one of the {@link java.util.concurrent.Executors} factory
     * methods instead of this general purpose constructor.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue} is null
     */
    private EfficientThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param handler         the handler to use when execution is blocked
     *                        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code handler} is null
     */
    private EfficientThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default rejected execution handler.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param threadFactory   the factory to use when the executor
     *                        creates a new thread
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code threadFactory} is null
     */
    private EfficientThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even
     *                        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *                        pool
     * @param keepAliveTime   when the number of threads is greater than
     *                        the core, this is the maximum time that excess idle threads
     *                        will wait for new tasks before terminating.
     * @param unit            the time unit for the {@code keepAliveTime} argument
     * @param workQueue       the queue to use for holding tasks before they are
     *                        executed.  This queue will hold only the {@code Runnable}
     *                        tasks submitted by the {@code execute} method.
     * @param threadFactory   the factory to use when the executor
     *                        creates a new thread
     * @param handler         the handler to use when execution is blocked
     *                        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *                                  {@code corePoolSize < 0}<br>
     *                                  {@code keepAliveTime < 0}<br>
     *                                  {@code maximumPoolSize <= 0}<br>
     *                                  {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException     if {@code workQueue}
     *                                  or {@code threadFactory} or {@code handler} is null
     */
    private EfficientThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    /**
     * Method invoked upon completion of execution of the given Runnable.
     * This method is invoked by the thread that executed the task. If
     * non-null, the Throwable is the uncaught {@code RuntimeException}
     * or {@code Error} that caused execution to terminate abruptly.
     * <p/>
     * <p>This implementation does nothing, but may be customized in
     * subclasses. Note: To properly nest multiple overridings, subclasses
     * should generally invoke {@code super.afterExecute} at the
     * beginning of this method.
     * <p/>
     * <p><b>Note:</b> When actions are enclosed in tasks (such as
     * {@link java.util.concurrent.FutureTask}) either explicitly or via methods such as
     * {@code submit}, these task objects catch and maintain
     * computational exceptions, and so they do not cause abrupt
     * termination, and the internal exceptions are <em>not</em>
     * passed to this method. If you would like to trap both kinds of
     * failures in this method, you can further probe for such cases,
     * as in this sample subclass that prints either the direct cause
     * or the underlying exception if a task has been aborted:
     * <p/>
     * <pre> {@code
     * class ExtendedExecutor extends ThreadPoolExecutor {
     *   // ...
     *   protected void afterExecute(Runnable r, Throwable t) {
     *     super.afterExecute(r, t);
     *     if (t == null && r instanceof Future<?>) {
     *       try {
     *         Object result = ((Future<?>) r).get();
     *       } catch (CancellationException ce) {
     *           t = ce;
     *       } catch (ExecutionException ee) {
     *           t = ee.getCause();
     *       } catch (InterruptedException ie) {
     *           Thread.currentThread().interrupt(); // ignore/reset
     *       }
     *     }
     *     if (t != null)
     *       System.out.println(t);
     *   }
     * }}</pre>
     *
     * @param r the runnable that has completed
     * @param t the exception that caused termination, or null if
     *          execution completed normally
     */
    @Override
    protected void afterExecute (Runnable r, Throwable t) {
        super.afterExecute(r, t);
        logger.trace("Tasks completed: " + getTaskCount());
    }

    /**
     * Factory
     *
     * @param coreSize    core thread pool size
     * @param maxSize     max size of thread pool
     * @param idleTimeout idle timeout
     * @param timeUnit    idle timeout unit
     * @param queueSize   queue size
     * @param namePrefix  thread name prefix
     * @return
     */
    public static ThreadPoolExecutor get (final int coreSize, final int maxSize, final int idleTimeout,
            final TimeUnit timeUnit, final int queueSize, final String namePrefix) {
        return new EfficientThreadPoolExecutor(coreSize, // core size
                maxSize, // max size
                idleTimeout, // idle timeout
                timeUnit,
                new ArrayBlockingQueue<Runnable>(queueSize), // queue with a size
                new PriorityThreadFactory(namePrefix, Thread.NORM_PRIORITY));
    }


    /**
     * Factory
     *
     * @param coreSize       core thread pool size
     * @param maxSize        max size of thread pool
     * @param idleTimeout    idle timeout
     * @param timeUnit       idle timeout unit
     * @param queueSize      queue size
     * @param namePrefix     thread name prefix
     * @param threadPriority thread priority
     * @return
     */
    public static ThreadPoolExecutor get (final int coreSize, final int maxSize, final int idleTimeout,
            final TimeUnit timeUnit, final int queueSize, final String namePrefix,
            final int threadPriority) {
        return new EfficientThreadPoolExecutor(coreSize, // core size
                maxSize, // max size
                idleTimeout, // idle timeout
                timeUnit,
                new ArrayBlockingQueue<Runnable>(queueSize), // queue with a size
                new PriorityThreadFactory(namePrefix, threadPriority));
    }


}
