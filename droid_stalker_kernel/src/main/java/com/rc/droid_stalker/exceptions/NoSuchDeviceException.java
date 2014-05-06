package com.rc.droid_stalker.exceptions;

/**
 * Exception to be thrown if the requested device is not connected
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/24/14
 * Time  : 12:23 PM
 */
public final class NoSuchDeviceException extends DroidStalkerBaseException {


    /**
     * Constructor
     *
     * @param serialNumber
     */
    public NoSuchDeviceException(final String serialNumber) {
        super("Failed to detect device with serial number " + serialNumber,
                "No device with serial number " + serialNumber + " is connected to the computer");
    }
}
