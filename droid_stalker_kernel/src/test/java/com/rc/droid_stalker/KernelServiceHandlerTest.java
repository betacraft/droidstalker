package com.rc.droid_stalker;

import com.rc.droid_stalker.components.KernelConnection;
import com.rc.droid_stalker.thrift.AndroidAppStruct;
import com.rc.droid_stalker.thrift.CPUStatsStruct;
import com.rc.droid_stalker.thrift.DeviceStruct;
import com.rc.droid_stalker.thrift.ThreadInfoStruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/6/14
 * Time  : 7:35 PM
 */
public class KernelServiceHandlerTest {


    private static final String ADB_LOCATION = "/home/akshay/android-sdk-linux/platform-tools/adb";
    private KernelConnection mKernelConnection;

    @Before
    public void setUp() throws Exception {
        mKernelConnection = KernelConnection.get();
    }


    @Test
    public void testGetDevices() throws Exception {
        Set<DeviceStruct> deviceStructSet = mKernelConnection.getClient().getDevices();
        for (DeviceStruct deviceStruct : deviceStructSet) {
            System.out.println(String.format("%s is connected", deviceStruct.getSerialNumber()));
        }
        assert true;
    }


    @Test
    public void testStartDebugSessionFor() throws Exception {

        Set<DeviceStruct> deviceStructSet = mKernelConnection.getClient().getDevices();
        DeviceStruct device = null;
        for (DeviceStruct deviceStruct : deviceStructSet) {
            device = deviceStruct;
            System.out.println(String.format("%s is connected", deviceStruct.getSerialNumber()));
        }
        if (device == null) {
            assert true;
        }
        String debugSessionId = mKernelConnection.getClient().startDebugSessionFor(device,
                new AndroidAppStruct("com.obeat" +
                        ".advertdisplay",
                        "com.obeat.advertdisplay.activities.Boot", "asda", ""));
        for (ThreadInfoStruct threadInfoStruct : mKernelConnection.getClient().getThreadsRunningIn(debugSessionId)) {
            System.out.println(threadInfoStruct.getName());
        }
        for (AndroidAppStruct app : mKernelConnection.getClient().getInstalledAppsOn(device)) {
            System.out.println(app.getApplicationName());
        }
        for (int i = 0; i < 100; ++i) {
            CPUStatsStruct cpuStatsStruct = mKernelConnection.getClient().getCPUStatsFor(debugSessionId, 2000);
            System.out.println(String.format("CPU stas %s %s %s", cpuStatsStruct.getPid(), cpuStatsStruct.getTotalCPU(),
                    cpuStatsStruct.getPidCPU()));

        }

        assert true;
    }


    @After
    public void tearDown() throws Exception {
        mKernelConnection.close();
    }

}
