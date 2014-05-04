package com.rc.droid_stalker_backend;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.rc.droid_stalker_backend.exceptions.NoSuchDeviceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom wrapper over AndroidDebugBridge
 * Singleton entity
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/24/14
 * Time  : 11:49 AM
 */
public final class ADB {

    private static final Logger logger = LoggerFactory.getLogger(ADB.class);
    private static ADB INSTANCE;
    private AndroidDebugBridge mAndroidDebugBridge;

    /**
     * Constructor
     *
     * @param adbPath adb path
     */
    private ADB(final String adbPath) {
        AndroidDebugBridge.init(false);
        if (mAndroidDebugBridge == null || !mAndroidDebugBridge.isConnected()) {
            logger.debug("Creating debug bridge");
            mAndroidDebugBridge = AndroidDebugBridge.createBridge
                    (adbPath, true);
        }
        if (!mAndroidDebugBridge.isConnected()) {
            logger.debug("Debug bridge is not connected so restarting it");
            mAndroidDebugBridge.restart();
        }
        if (mAndroidDebugBridge.isConnected()) {
            logger.debug("ADB bridge is connected");
        } else {
            throw new RuntimeException("Could not connect to ADB");
        }
    }

    /**
     * Singleton factory
     *
     * @return
     */

    public static ADB get(final String adbPath) {
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
}
