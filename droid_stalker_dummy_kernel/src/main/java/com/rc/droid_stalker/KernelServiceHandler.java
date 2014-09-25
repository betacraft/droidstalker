package com.rc.droid_stalker;

import com.rc.droid_stalker.thrift.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Kernel service handler
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/6/14
 * Time  : 5:21 PM
 */
public class KernelServiceHandler implements DroidStalkerKernelService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(KernelServiceHandler.class);
    private static final String DEBUG_SESSION_ID = "dummy_session_id";
    private static final Set<AndroidAppStruct> INSTALLED_APPS = new HashSet<>();
    private static final Set<ThreadInfoStruct> RUNNING_THREADS = new HashSet<>();
    private static final Set<CPUStatsStruct> CPU_STATS = new HashSet<>();
    private static final Set<DeviceStruct> CONNECTED_DEVICES = new HashSet<>();

    /**
     * Initialization block
     */ {
        INSTALLED_APPS.add(new AndroidAppStruct("com.dummy1", "dummy_activity1", "Dummy1", ""));
        INSTALLED_APPS.add(new AndroidAppStruct("com.dummy2", "dummy_activity2", "Dummy2", ""));
        INSTALLED_APPS.add(new AndroidAppStruct("com.dummy3", "dummy_activity3", "Dummy3", ""));
        INSTALLED_APPS.add(new AndroidAppStruct("com.dummy4", "dummy_activity4", "Dummy4", ""));

        RUNNING_THREADS.add(new ThreadInfoStruct(0, "dummy_thread1", 0, 100, 25000, 1552, false, 100));
        RUNNING_THREADS.add(new ThreadInfoStruct(0, "dummy_thread2", 0, 100, 25000, 1552, false, 100));
        RUNNING_THREADS.add(new ThreadInfoStruct(0, "dummy_thread3", 0, 100, 25000, 1552, false, 100));
        RUNNING_THREADS.add(new ThreadInfoStruct(0, "dummy_thread4", 0, 100, 25000, 1552, false, 100));

        CPU_STATS.add(new CPUStatsStruct(0, 11, 1, 1256, "", ""));
        CPU_STATS.add(new CPUStatsStruct(1, 11, 1, 1256, "", ""));
        CPU_STATS.add(new CPUStatsStruct(2, 11, 1, 1256, "", ""));
        CPU_STATS.add(new CPUStatsStruct(3, 11, 1, 1256, "", ""));

        CONNECTED_DEVICES.add(new DeviceStruct("serial_dummy1", "state_dummy1", false));
        CONNECTED_DEVICES.add(new DeviceStruct("serial_dummy2", "state_dummy2", false));
        CONNECTED_DEVICES.add(new DeviceStruct("serial_dummy3", "state_dummy3", false));
        CONNECTED_DEVICES.add(new DeviceStruct("serial_dummy4", "state_dummy4", false));
    }

    public KernelServiceHandler() {

    }

    @Override
    public String startDebugSessionFor(final DeviceStruct device, final AndroidAppStruct androidApp) throws
            DroidStalkerKernelException {
        logger.debug("Starting debug session");
        return DEBUG_SESSION_ID;
    }


    @Override
    public Set<DeviceStruct> getDevices() throws DroidStalkerKernelException {
        logger.debug("get devices");
        return getConnectedDevicesStructList();
    }

    @Override
    public Set<AndroidAppStruct> getInstalledAppsOn(final DeviceStruct device) throws DroidStalkerKernelException {
        logger.debug("Get installed apps on device called");
        return INSTALLED_APPS;
    }


    @Override
    public Set<ThreadInfoStruct> getThreadsRunningIn(final String debugSession) throws DroidStalkerKernelException {
        logger.debug("Get threads running called");
        return RUNNING_THREADS;
    }

    @Override
    public Set<CPUStatsStruct> getCPUStatsFor(final String debugSession,
                                              final int lastPacketId) throws DroidStalkerKernelException {
        logger.debug("Get cpu stats for called");
        return CPU_STATS;
    }

    private Set<DeviceStruct> getConnectedDevicesStructList() {
        return CONNECTED_DEVICES;
    }


    // IDeviceChangeListener implementation


}
