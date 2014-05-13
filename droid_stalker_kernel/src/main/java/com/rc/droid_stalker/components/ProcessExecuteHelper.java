package com.rc.droid_stalker.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.listener.ProcessListener;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class
 * Factory to fork processes without blocking current thread
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 5:39 PM
 */
public final class ProcessExecuteHelper {
    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ProcessExecuteHelper.class);
    /**
     * Hashmap of spawned processes
     */
    private ConcurrentHashMap<Long, Process> processMap = new ConcurrentHashMap<Long, Process>();
    /**
     * Instance
     */
    private static ProcessExecuteHelper INSTANCE;

    /**
     * Constructor
     */
    private ProcessExecuteHelper() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run () {
                INSTANCE.cleanup();
            }
        }));
    }

    /**
     * Factory
     *
     * @return
     */
    public synchronized static ProcessExecuteHelper get () {
        if (INSTANCE != null)
            return INSTANCE;
        INSTANCE = new ProcessExecuteHelper();
        return INSTANCE;
    }

    /**
     * Cleanup method
     */
    public void cleanup () {
        for (Map.Entry<Long, Process> processEntry : this.processMap.entrySet()) {
            processEntry.getValue().destroy();
        }
    }

    /**
     * Fork monitor
     */
    public interface ForkMonitor {

        void onProcessStarted(long id);

        void onProcessCrashed(int returnVal);

        void onProcessClosed();

        void onException(Throwable cause);

    }

    /**
     * Method to spawn new process without any blocking
     *
     * @param command command to be executed in different process
     * @return output of the process execution
     */
    public String execute (final String... command) throws Exception {
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(pipedInputStream);
        ProcessExecutor processExecutor = new org.zeroturnaround.exec.ProcessExecutor()
                .command(command)
                .readOutput(true)
                .redirectError(outputStream)
                .destroyOnExit();
        ProcessResult processResult = processExecutor.execute();
        if (processResult.exitValue() == 0) {
            return processResult.outputUTF8();
        }
        StringBuffer finalCommand = new StringBuffer();
        for (String commandPart : command)
            finalCommand.append(commandPart).append(" ");
        if (finalCommand.toString().contains("lsof")) {
            if (processResult.exitValue() == 1)
                return "";
        }
        byte[] error = new byte[pipedInputStream.available()];
        try {
            pipedInputStream.read(error);
        } catch (Exception ignored) {

        }
        String errorString = new String(error);
        throw new IllegalStateException("Command failed : " + finalCommand + " Output: " + processResult
                .outputString() + " Error: " + errorString + " Exit value: " + processResult.exitValue());
    }


    /**
     * Method to spawn new process without any blocking
     *
     * @param forkMonitor @ForkMonitor fork monitor for this process
     * @param commands    process params
     */
    public void executeAsynchronously (final ForkMonitor forkMonitor, final String... commands) {
        final long id = new Date().getTime();
        try {
            new ProcessExecutor()
                    .environment(System.getenv())
                    .command(commands)
                    .redirectOutputAsDebug()
                    .redirectErrorAsDebug()
                    .destroyOnExit()
                    .addListener(new ProcessListener() {
                        /**
                         * Invoked after a process has started.
                         *
                         * @param process  the process started.
                         * @param executor executor used for starting the process.
                         *                 Modifying the {@link org.zeroturnaround.exec.ProcessExecutor}
                         *                 only affects the following processes
                         *                 not the one just started.
                         */
                        @Override
                        public void afterStart (final Process process, final ProcessExecutor executor) {
                            super.afterStart(process, executor);
                            processMap.put(id, process);
                        }

                        /**
                         * Invoked after a process has exited (whether finished or cancelled).
                         *
                         * @param process process just stopped.
                         */
                        @Override
                        public void afterStop (final Process process) {
                            super.afterStop(process);
                            processMap.remove(process).destroy();
                            forkMonitor.onProcessCrashed(process.exitValue());
                        }

                        /**
                         * Invoked before a process is started.
                         *
                         * @param executor executor used for starting a process.
                         *                 Any changes made here apply to the starting process.
                         *                 Once the process has started it is not affected by the {@link
                         *                 org.zeroturnaround.exec.ProcessExecutor} any more.
                         */
                        @Override
                        public void beforeStart (ProcessExecutor executor) {
                            super.beforeStart(executor);    //To change body of overridden methods use File
                            // | Settings | File Templates.
                        }
                    })
                    .start();
        } catch (IOException e) {
            forkMonitor.onException(e.getCause());
        }
    }


    /**
     * Method to spawn new process without any blocking
     *
     * @param forkMonitor @ForkMonitor fork monitor for this process
     * @param command     process params
     */
    public void executeAsynchronously (final ForkMonitor forkMonitor, final Map<String, String> env,
            final String... command) {
        final long id = new Date().getTime();
        try {
            env.putAll(System.getenv());
            new ProcessExecutor()
                    .command(command)
                    .environment(env)
                    .redirectOutputAsDebug()
                    .redirectErrorAsDebug()
                    .destroyOnExit()
                    .addListener(new ProcessListener() {
                        /**
                         * Invoked after a process has started.
                         *
                         * @param process  the process started.
                         * @param executor executor used for starting the process.
                         *                 Modifying the {@link org.zeroturnaround.exec.ProcessExecutor}
                         *                 only affects the following processes
                         *                 not the one just started.
                         */
                        @Override
                        public void afterStart (final Process process, final ProcessExecutor executor) {
                            super.afterStart(process, executor);
                            processMap.put(id, process);
                            forkMonitor.onProcessStarted(id);
                        }

                        /**
                         * Invoked after a process has exited (whether finished or cancelled).
                         *
                         * @param process process just stopped.
                         */
                        @Override
                        public void afterStop (Process process) {
                            super.afterStop(process);
                            processMap.remove(process).destroy();
                            forkMonitor.onProcessCrashed(process.exitValue());
                        }

                    }).start();
        } catch (IOException e) {
            forkMonitor.onException(e.getCause());
        }
    }

    /**
     * Method to spawn new process without any blocking
     *
     * @param forkMonitor @ForkMonitor fork monitor for this process
     * @param logFileName push streams into this file
     * @param command     process params
     */
    public void executeAsynchronously (final ForkMonitor forkMonitor, final String logFileName,
            final Map<String, String> env,
            final String... command) {
        final long id = new Date().getTime();
        env.putAll(System.getenv());
        try {

            new ProcessExecutor()
                    .command(command)
                    .environment(env)
                    .destroyOnExit()
                    .addListener(new ProcessListener() {
                        /**
                         * Invoked after a process has started.
                         *
                         * @param process  the process started.
                         * @param executor executor used for starting the process.
                         *                 Modifying the {@link org.zeroturnaround.exec.ProcessExecutor}
                         *                 only affects the following processes
                         *                 not the one just started.
                         */
                        @Override
                        public void afterStart (final Process process, final ProcessExecutor executor) {
                            super.afterStart(process, executor);
                            processMap.put(id, process);
                            forkMonitor.onProcessStarted(id);
                        }

                        /**
                         * Invoked after a process has exited (whether finished or cancelled).
                         *
                         * @param process process just stopped.
                         */
                        @Override
                        public void afterStop (Process process) {
                            super.afterStop(process);
                            processMap.remove(process).destroy();
                            forkMonitor.onProcessCrashed(process.exitValue());
                        }

                    }).start();
        } catch (IOException e) {
            forkMonitor.onException(e.getCause());
        } catch (Exception e) {
            forkMonitor.onException(e.getCause());
        }
    }


    /**
     * Method to spawn new process without any blocking
     *
     * @param forkMonitor @ForkMonitor fork monitor for this process
     * @param command     process params
     */
    public void executeAsynchronously (final ForkMonitor forkMonitor, final ArrayList<String> command) {
        final long id = new Date().getTime();
        try {
            new ProcessExecutor()
                    .command(command)
                    .redirectOutputAsDebug()
                    .redirectErrorAsDebug()
                    .destroyOnExit()
                    .addListener(new ProcessListener() {
                        /**
                         * Invoked after a process has started.
                         *
                         * @param process  the process started.
                         * @param executor executor used for starting the process.
                         *                 Modifying the {@link org.zeroturnaround.exec.ProcessExecutor}
                         *                 only affects the following processes
                         *                 not the one just started.
                         */
                        @Override
                        public void afterStart (final Process process, final ProcessExecutor executor) {
                            super.afterStart(process, executor);
                            processMap.put(id, process);
                            forkMonitor.onProcessStarted(id);
                        }

                        /**
                         * Invoked after a process has exited (whether finished or cancelled).
                         *
                         * @param process process just stopped.
                         */
                        @Override
                        public void afterStop (Process process) {
                            super.afterStop(process);
                            processMap.remove(process).destroy();
                            forkMonitor.onProcessCrashed(process.exitValue());
                        }

                    }).start();
        } catch (IOException e) {
            forkMonitor.onException(e.getCause());
        }
    }

    /**
     * Method to kill a process with given id
     *
     * @param id
     */
    public void killProcessWithId (final long id) {
        if (processMap.containsKey(id)) {
            logger.debug("There is no process with id " + id);
            return;
        }
        try {
            ProcessKiller.killProcessWithPID(getUnixPID(processMap.remove(id)).toString());
        } catch (Exception e) {
            logger.error("Error while killing process", e);
        }
    }

    public static Integer getUnixPID (Process process) throws Exception {
        System.out.println(process.getClass().getName());
        if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
            Class cl = process.getClass();
            Field field = cl.getDeclaredField("pid");
            field.setAccessible(true);
            Object pidObject = field.get(process);
            return (Integer) pidObject;
        } else {
            throw new IllegalArgumentException("Needs to be a UNIXProcess");
        }
    }


}
