package com.rc.droid_stalker.view_hierarychy_tracer;

import com.rc.droid_stalker.view_hierarychy_tracer.device.DeviceConnection;
import com.rc.droid_stalker.view_hierarychy_tracer.models.IHvDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/28/14
 * Time  : 11:12 AM
 */
public final class StalkerHierarchyViewerDirector extends HierarchyViewerDirector {
    private static final Logger logger = LoggerFactory.getLogger(StalkerHierarchyViewerDirector.class);
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private IHvDevice mCurrentDevice;
    private DeviceConnection mDevicePermanentConnection;




    public static HierarchyViewerDirector createDirector() {
        return sDirector = new StalkerHierarchyViewerDirector();
    }

    @Override
    public void terminate() {
        super.terminate();
        mExecutor.shutdown();
    }

    @Override
    public String getAdbLocation() {
        return "/home/akshay/android-sdk-linux/platform-tools/adb";
    }

    @Override
    public void executeInBackground(final String taskName, final Runnable task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                task.run();
            }
        });
    }
}
