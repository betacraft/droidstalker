package com.rc.droid_stalker.components;

import com.rc.droid_stalker.thrift.CPUStatsStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains all the information about CPU stats for a session
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/23/14
 * Time  : 3:10 PM
 */
public final class CPUStats {
    private static final Logger logger = LoggerFactory.getLogger(CPUStats.class);
    private ConcurrentHashMap<Integer, CPUStatsStruct> mStatHistory;
    private int mCurrentPacketId = 0;

    {
        mStatHistory = new ConcurrentHashMap<Integer, CPUStatsStruct>();
    }


    public void push(final CPUStatsStruct cpuStats) {
        mStatHistory.put(++mCurrentPacketId, cpuStats);
    }

    public Set<CPUStatsStruct> getAllPacketsAfterId(final int lastPacketId) {
        if (mCurrentPacketId <= lastPacketId)
            return new HashSet<CPUStatsStruct>();
        Set<CPUStatsStruct> resultSet = new LinkedHashSet<CPUStatsStruct>();
        for (int packet = lastPacketId + 1; packet < mCurrentPacketId; ++packet) {
            resultSet.add(mStatHistory.get(packet));
        }
        return resultSet;
    }

}
