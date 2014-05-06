package com.rc.droid_stalker.wrappers;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.rc.droid_stalker.exceptions.NoSuchDeviceException;
import com.rc.droid_stalker.thrift.DroidStalkerKernelException;
import com.rc.droid_stalker.thrift.KernelExceptionErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom wrapper over AndroidDebugBridge
 * Singleton entity
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/24/14
 * Time  : 11:49 AM
 */
public final class ADB implements AndroidDebugBridge.IDebugBridgeChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(ADB.class);
    private static ADB INSTANCE;
    private AndroidDebugBridge mAndroidDebugBridge;

    /**
     * Constructor
     *
     * @param adbPath adb path
     */
    private ADB(final String adbPath) throws DroidStalkerKernelException {
        mAndroidDebugBridge.addDebugBridgeChangeListener(this);
        AndroidDebugBridge.init(true);
        if (mAndroidDebugBridge == null || !mAndroidDebugBridge.isConnected()) {
            logger.debug("Creating debug bridge");
            mAndroidDebugBridge = AndroidDebugBridge.createBridge
                    (adbPath, false);
        }
        if (!mAndroidDebugBridge.isConnected()) {
            logger.debug("Debug bridge is not connected so restarting it");
            mAndroidDebugBridge.restart();
        }
        if (mAndroidDebugBridge.isConnected()) {
            logger.debug("ADB bridge is connected");
        } else {
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.KERNEL_BOOT_FAILED, "ADB connection failed");
        }
    }

    /**
     * Singleton thread_factory
     *
     * @return
     */

    public static ADB initialize(final String adbPath) throws DroidStalkerKernelException {
        if (INSTANCE != null)
            return INSTANCE;
        INSTANCE = new ADB(adbPath);
        return INSTANCE;
    }


    /**
     * Method to return device associated with given serial number
     *
     * @param serialNumber serial number of the device
     * @return
     * @throws NoSuchDeviceException
     */
    public IDevice getDevice(final String serialNumber) throws NoSuchDeviceException {
        for (final IDevice device : mAndroidDebugBridge.getDevices()) {
            if (device.getSerialNumber() == serialNumber) {
                return device;
            }
        }
        //TODO add listener to device changes and retry if device is connected or not
        throw new NoSuchDeviceException(serialNumber);
    }

    public IDevice[] getDevices() {
        return mAndroidDebugBridge.getDevices();
    }

    public AndroidDebugBridge getAndroidDebugBridge() {
        return mAndroidDebugBridge;
    }

    @Override
    public void bridgeChanged(AndroidDebugBridge bridge) {
        mAndroidDebugBridge = bridge;
    }
}
