package com.rc.droid_stalker;

import com.rc.droid_stalker.components.KernelConnection;
import com.rc.droid_stalker.thrift.AndroidAppStruct;
import com.rc.droid_stalker.thrift.DeviceStruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 7/4/14
 * Time  : 8:35 PM
 */
public class KernelServiceHandlerTest {

    private static final String ADB_LOCATION = "/home/akshay/android-sdk-linux/platform-tools/adb";
    private KernelConnection mKernelConnection;
    private DeviceStruct mDevice;
    private Set<AndroidAppStruct> mInstalledApplications;


    @Before
    public void setUp() throws Exception {
        mKernelConnection = KernelConnection.get();
    }


    @Test
    public void testGetDevices() throws Exception {
        mDevice = (DeviceStruct) mKernelConnection.getClient().getDevices().toArray()[0];
        assert mDevice != null;
    }

    @Test
    public void testGetInstalledApplications() throws Exception {
        mInstalledApplications = mKernelConnection.getClient().getInstalledAppsOn(mDevice);
        assert mInstalledApplications.size() != 0;

    }

    @Test
    public void testStartDebugSessionFor() throws Exception {
        if(mInstalledApplications == null){
            assert true;
            return;
        }
        final AndroidAppStruct app = (AndroidAppStruct) mInstalledApplications.toArray()[0];
        assert (mKernelConnection.getClient().startDebugSessionFor(mDevice, app) != null);
    }


    @After
    public void tearDown() throws Exception {
        mKernelConnection.close();
    }
}
