package com.rc.droid_stalker.wrappers;

import com.android.ddmlib.*;
import com.rc.droid_stalker.exceptions.NoSuchDeviceException;
import com.rc.droid_stalker.thrift.DroidStalkerKernelException;
import com.rc.droid_stalker.thrift.KernelExceptionErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Custom wrapper over AndroidDebugBridge
 * Singleton entity
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/24/14
 * Time  : 11:49 AM
 */
public final class ADB implements AndroidDebugBridge.IDebugBridgeChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(ADB.class);
    private static final int MIN_PORT_NUMBER = 10000;
    private static final int MAX_PORT_NUMBER = 10050;
    private static ADB INSTANCE;
    private AndroidDebugBridge mAndroidDebugBridge;
    private AtomicBoolean mIsInitialized = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param adbPath adb path
     */
    private ADB(final String adbPath, final AndroidDebugBridge.IDeviceChangeListener deviceChangeListener)
            throws DroidStalkerKernelException {
        if (mIsInitialized.getAndSet(true))
            return;
        //TODO generate available ports and then use them
        //DdmPreferences.setDebugPortBase(9015);
        //DdmPreferences.setSelectedDebugPort(9016);
        DdmPreferences.setInitialHeapUpdate(true);
        DdmPreferences.setInitialThreadUpdate(true);
        logger.debug("Connecting adb");
        try {
            AndroidDebugBridge.init(true);
        } catch (IllegalStateException ex) {
            if (!ex.getMessage().equals("AndroidDebugBridge.init() has already been called.")) {
                throw ex;
            }
        }
        if (mAndroidDebugBridge == null || !mAndroidDebugBridge.isConnected()) {
            logger.debug("Creating debug bridge");
            mAndroidDebugBridge = AndroidDebugBridge.createBridge
                    (adbPath, true);
        }
        /*if (!mAndroidDebugBridge.isConnected()) {
            logger.debug("Debug bridge is not connected so restarting it");
            mAndroidDebugBridge.restart();
        }     */
        //TODO add a check after 10 retries throw an error
        while (!mAndroidDebugBridge.isConnected())
            waitABit();
        if (deviceChangeListener != null)
            AndroidDebugBridge.addDeviceChangeListener(deviceChangeListener);
        if (mAndroidDebugBridge.isConnected()) {
            logger.debug("ADB bridge is connected");
        } else {
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.KERNEL_BOOT_FAILED, "ADB connection failed");
        }
        AndroidDebugBridge.addDebugBridgeChangeListener(this);
    }

    private void waitABit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Singleton thread_factory
     *
     * @return
     */

    public static ADB  initialize(final String adbPath, final AndroidDebugBridge.IDeviceChangeListener deviceChangeListener) throws
            DroidStalkerKernelException {
        if (INSTANCE != null)
            return INSTANCE;
        INSTANCE = new ADB(adbPath, deviceChangeListener);
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

    public static int getAvailablePort() {
        for (int port = MIN_PORT_NUMBER; port <= MAX_PORT_NUMBER; ++port) {
            if (available(port))
                return port;
        }
        throw new RuntimeException("Not available");
    }

    public static boolean available(final int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }
}
