package com.rc.droid_stalker_backend;

import com.android.ddmlib.IDevice;
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
        mDirector = StalkerHierarchyViewerDirector.createDirector();
        mDirector.acquireBridge(mAdb.getAndroidDebugBridge());
        mCurrentDevice = mAdb.getDevices()[0];
        mDirector.deviceConnected(mCurrentDevice);



    }

    @Override
    public void run() {


    }
}
