package com.rc.droid_stalker.models;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.ThreadInfo;
import com.rc.droid_stalker.thrift.DeviceStruct;
import com.rc.droid_stalker.thrift.ThreadInfoStruct;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/7/14
 * Time  : 7:27 PM
 */
public final class ThriftStructHelpers {
    public static DeviceStruct prepareDeviceStruct(final IDevice device) {
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

    public static ThreadInfoStruct prepareThreadInfoStruct(final ThreadInfo threadInfo) {
        final ThreadInfoStruct threadInfoStruct = new ThreadInfoStruct(threadInfo.getThreadId(),
                threadInfo.getThreadName(), threadInfo.getStatus(), threadInfo.getTid(), threadInfo.getUtime(),
                threadInfo.getStime(), threadInfo.isDaemon(), threadInfo.getStackCallTime());
        if (threadInfo.getStackTrace() != null) {
            final StringBuilder stackTrace = new StringBuilder();

            for (final StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
                stackTrace.append(stackTraceElement.toString()).append("\n");
            }
            threadInfoStruct.setStackTrace(stackTrace.toString());
        }
        return threadInfoStruct;

    }
}
