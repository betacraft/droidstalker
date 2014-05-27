package com.rc.droid_stalker.sessions.components;

import com.rc.droid_stalker.thrift.CPUStatsStruct;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains all the information about CPU stats for a session
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/23/14
 * Time  : 3:10 PM
 */
public final class CPUStats {
    private static final Logger logger = LoggerFactory.getLogger(CPUStats.class);
    private ConcurrentHashMap<DateTime, CPUStatsStruct> mStatHistory;

    {
        mStatHistory = new ConcurrentHashMap<DateTime, CPUStatsStruct>();
    }


    public void push(final CPUStatsStruct cpuStats) {
        mStatHistory.put(new DateTime(), cpuStats);
    }

}
