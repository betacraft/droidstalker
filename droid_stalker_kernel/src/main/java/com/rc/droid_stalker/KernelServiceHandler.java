package com.rc.droid_stalker;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.rc.droid_stalker.thrift.DeviceStruct;
import com.rc.droid_stalker.thrift.DroidStalkerKernelException;
import com.rc.droid_stalker.thrift.DroidStalkerKernelService;
import com.rc.droid_stalker.wrappers.ADB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private ADB mADB;
    private ConcurrentHashMap<String, IDevice> mConnectedDevices;

    /**
     * Initialization block
     */ {
        mConnectedDevices = new ConcurrentHashMap<String, IDevice>();
    }

    // DroidStalkerKernelService implementation
    @Override
    public void boot(final String adbLocation) throws DroidStalkerKernelException {
        mADB = ADB.initialize(adbLocation);
        for (final IDevice connectedDevice : mADB.getDevices()) {
            logger.debug("Adding {} as connected device", connectedDevice.getSerialNumber());
            mConnectedDevices.put(connectedDevice.getSerialNumber(), connectedDevice);
        }
    }

    @Override
    public boolean isRunning() throws DroidStalkerKernelException {
        return false;
    }

    @Override
    public Set<DeviceStruct> getDevices() throws DroidStalkerKernelException {
        return getConnectedDevicesStructList();
    }

    private Set<DeviceStruct> getConnectedDevicesStructList() {
        final Set<DeviceStruct> deviceStructArrayList = new HashSet<DeviceStruct>();
        for (Map.Entry<String, IDevice> connectedDeviceEntry : mConnectedDevices.entrySet()) {
            deviceStructArrayList.add(prepareDeviceStruct(connectedDeviceEntry.getValue()));
        }
        return deviceStructArrayList;
    }


    // IDeviceChangeListener implementation

    @Override
    public void deviceConnected(final IDevice device) {
        logger.debug("{} device got connected", device.getSerialNumber());
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


    private DeviceStruct prepareDeviceStruct(final IDevice device) {
        final DeviceStruct deviceStruct = new DeviceStruct();
        deviceStruct.setSerialNumber(device.getSerialNumber());
        deviceStruct.setAvdName(device.getAvdName());
        deviceStruct.setDeviceState(device.getState().toString());
        deviceStruct.setIsEmulator(device.isEmulator());
        try {
            deviceStruct.setBatteryPercentage(device.getBatteryLevel().shortValue());
        } catch (Exception ignored) {
            deviceStruct.setBatteryPercentage((short) -1);
        }
        return deviceStruct;
    }
}
