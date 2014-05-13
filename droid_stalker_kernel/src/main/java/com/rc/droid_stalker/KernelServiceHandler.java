package com.rc.droid_stalker;

import com.android.ddmlib.*;
import com.rc.droid_stalker.models.ThriftStructHelpers;
import com.rc.droid_stalker.sessions.DebugSession;
import com.rc.droid_stalker.thrift.*;
import com.rc.droid_stalker.wrappers.ADB;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/6/14
 * Time  : 5:21 PM
 */
public class KernelServiceHandler implements DroidStalkerKernelService.Iface, AndroidDebugBridge.IDeviceChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(KernelServiceHandler.class);
    private static final String ANDROID_SDK_LOCATION_FORMAT = "%s/platform-tools/adb";
    private ADB mADB;
    private ConcurrentHashMap<String, IDevice> mConnectedDevices;
    private DebugSession mCurrentDebugSession;


    /**
     * Initialization block
     */ {
        mConnectedDevices = new ConcurrentHashMap<String, IDevice>();
    }

    public KernelServiceHandler() {
        try {
            mADB = ADB.initialize(String.format(ANDROID_SDK_LOCATION_FORMAT, "/home/akshay/android-sdk-linux"), this);
            for (final IDevice device : mADB.getDevices()) {
                addDevice(device);
            }
        } catch (DroidStalkerKernelException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String startDebugSessionFor(final DeviceStruct device, final AndroidAppStruct androidApp) throws
            DroidStalkerKernelException {
        logger.debug("Starting debug session for {}",device.getSerialNumber());
        if (device == null) {
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.APP_COULD_NOT_START, "Device is null");
        }
        final IDevice debugDevice = mConnectedDevices.get(device.getSerialNumber());
        if (debugDevice == null)
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.DEVICE_NOT_FOUND_CONNECTED,
                    "Device with serial number " + device.getSerialNumber() + " is not found connected");
        if (mCurrentDebugSession != null)
            mCurrentDebugSession.closeSession();
        try {
            mCurrentDebugSession = DebugSession.startFor(debugDevice, androidApp);
        } catch (TimeoutException e) {
            logger.error("Error while creating debug session",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.FAILED_TO_START_APP, e.getMessage());
        } catch (AdbCommandRejectedException e) {
            logger.error("Error while creating debug session",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.FAILED_TO_START_APP, e.getMessage());
        } catch (ShellCommandUnresponsiveException e) {
            logger.error("Error while creating debug session",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.FAILED_TO_START_APP, e.getMessage());
        } catch (IOException e) {
            logger.error("Error while creating debug session",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.FAILED_TO_START_APP, e.getMessage());
        } catch (TTransportException e) {
            logger.error("Error while creating debug session",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.FAILED_TO_START_APP, e.getMessage());
        }
        logger.debug("Debug session created successfully");
        return mCurrentDebugSession.getSessionId();
    }


    @Override
    public Set<DeviceStruct> getDevices() throws DroidStalkerKernelException {
        logger.debug("get devices");
        return getConnectedDevicesStructList();
    }

    @Override
    public Set<AndroidAppStruct> getInstalledAppsOn(final DeviceStruct device) throws DroidStalkerKernelException {
        return mCurrentDebugSession.getInstalledApplications();
    }


    @Override
    public Set<ThreadInfoStruct> getThreadsRunningIn(final String debugSession) throws DroidStalkerKernelException {
        if (mCurrentDebugSession == null || !mCurrentDebugSession.getSessionId().equals(debugSession)) {
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.NO_SUCH_DEBUG_SESSION_RUNNING,
                    "Illegal debug session");
        }
        return mCurrentDebugSession.getAllThreads();
    }

    @Override
    public CPUStatsStruct getCPUStatsFor(final String debugSession,
                                         final int span) throws DroidStalkerKernelException {
        return mCurrentDebugSession.getCPUStats(span);
    }

    private Set<DeviceStruct> getConnectedDevicesStructList() {
        final Set<DeviceStruct> deviceStructArrayList = new HashSet<DeviceStruct>();
        for (Map.Entry<String, IDevice> connectedDeviceEntry : mConnectedDevices.entrySet()) {
            deviceStructArrayList.add(ThriftStructHelpers.prepareDeviceStruct(connectedDeviceEntry.getValue()));
        }
        return deviceStructArrayList;
    }


    // IDeviceChangeListener implementation

    @Override
    public void deviceConnected(final IDevice device) {
        logger.debug("{} device got connected", device.getSerialNumber());
        addDevice(device);
    }

    private void addDevice(final IDevice device) {
        logger.debug("adding {} device", device.getSerialNumber());
        try {
            //ProcessKiller.killProcessRunningOnPort(11000);
            AdbHelper.createForward(AndroidDebugBridge.getSocketAddress(), device, "tcp:11000", "tcp:11000");
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mConnectedDevices.put(device.getSerialNumber(), device);
    }

    @Override
    public void deviceDisconnected(final IDevice device) {
        logger.debug("{} device got disconnected", device.getSerialNumber());
        mConnectedDevices.remove(device.getSerialNumber());
    }

    @Override
    public void deviceChanged(final IDevice device, final int changeMask) {

    }


}
