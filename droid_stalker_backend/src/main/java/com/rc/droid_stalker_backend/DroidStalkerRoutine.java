package com.rc.droid_stalker_backend;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ThreadInfo;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.HierarchyViewerDirector;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.StalkerHierarchyViewerDirector;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main routine that contains the entire logic
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/27/14
 * Time  : 1:11 PM
 */
public final class DroidStalkerRoutine implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DroidStalkerRoutine.class);
    private ADB mAdb;
    private HierarchyViewerDirector mDirector;
    private IDevice mCurrentDevice;
    private Window[] mCurrentWindows;


    public DroidStalkerRoutine() {
        mAdb = ADB.get("/home/akshay/android-sdk-linux/platform-tools/adb");
        mCurrentDevice = mAdb.getDevices()[0];
        mDirector = StalkerHierarchyViewerDirector.createDirector();
        mDirector.acquireBridge(mAdb.getAndroidDebugBridge());
        mCurrentDevice = mAdb.getDevices()[0];
        mDirector.deviceConnected(mCurrentDevice);
        for (Client client : mCurrentDevice.getClients()) {
            client.setThreadUpdateEnabled(true);
            client.setHeapUpdateEnabled(true);
            client.setAsSelectedClient();
            logger.debug("Pid {} total threads {}", client.getClientData().getPid(),
                    client.getClientData().getThreads().length);
            for (ThreadInfo threadInfo : client.getClientData().getThreads()) {
                logger.debug("name {} status {}", threadInfo.getThreadName(), threadInfo.getStatus());
            }
        }


    }

    @Override
    public void run() {


    }
}
