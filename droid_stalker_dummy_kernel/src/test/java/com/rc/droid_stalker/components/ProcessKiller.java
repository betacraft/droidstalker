package com.rc.droid_stalker.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: akshay
 * Date  : 10/28/13
 * Time  : 9:10 PM
 */
public final class ProcessKiller {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ProcessKiller.class);

    /**
     * Method to kill process with id
     *
     * @param pid
     */
    public static void killProcessWithPID (final String pid) throws Exception {
        try {
            logger.debug("Kill returned " + ProcessExecuteHelper.get().execute("/bin/bash", "-c",
                    "kill " + pid));
            return;
        } catch (Exception exception) {
            logger.error("Error while killing process with process id " + pid, exception);
        }
        logger.debug("Trying with -9");
        try {
            logger.debug("Kill -9 returned " + ProcessExecuteHelper.get().execute("/bin/bash", "-c",
                    "kill -9 " + pid));
            return;
        } catch (Exception exception) {
            logger.error("Error while killing process with process id " + pid, exception);
        }
        throw new IllegalStateException("Can not kill process with id " + pid);
    }

    /**
     * @param port
     * @throws Exception
     */
    public static void killProcessRunningOnPort (final int port) throws Exception {
        String processId = "";
        try {
            processId = ProcessExecuteHelper.get().execute("lsof", "-t", "-i", ":" + port);
        } catch (Exception exception) {
            logger.error("Exception in executing lsof", exception);
        }
        if (processId.equals("")) {
            logger.debug("No process is running on port " + port);
            return;
        }
        logger.debug("Process id for process running on port " + port + " is " + processId);
        killProcessWithPID(processId);
        logger.debug("Process killed running on port " + port + " successfully");
    }
}
