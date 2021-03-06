package com.rc.droid_stalker.view_hierarychy_tracer.models;


import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;

/**
 * Used for storing a window from the window manager service on the device.
 * These are the windows that the device selector shows.
 */
public class Window {
    private final String mTitle;
    private final int mHashCode;
    private final IHvDevice mHvDevice;
    private final Client mClient;

    public Window(IHvDevice device, String title, int hashCode) {
        mHvDevice = device;
        mTitle = title;
        mHashCode = hashCode;
        mClient = null;
    }

    public Window(IHvDevice device, String title, Client c) {
        mHvDevice = device;
        mTitle = title;
        mClient = c;
        mHashCode = c.hashCode();
    }

    public String getTitle() {
        return mTitle;
    }

    public int getHashCode() {
        return mHashCode;
    }

    public String encode() {
        return Integer.toHexString(mHashCode);
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public IHvDevice getHvDevice() {
        return mHvDevice;
    }

    public IDevice getDevice() {
        return mHvDevice.getDevice();
    }

    public Client getClient() {
        return mClient;
    }

    public static Window getFocusedWindow(IHvDevice device) {
        return new Window(device, "<Focused Window>", -1);
    }

    /*
     * After each refresh of the windows in the device selector, the windows are
     * different instances and automatically reselecting the same window doesn't
     * work in the device selector unless the equals method is defined here.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Window other = (Window) obj;
        if (mHvDevice == null) {
            if (other.mHvDevice != null)
                return false;
        } else if (!mHvDevice.getDevice().getSerialNumber().equals(
                other.mHvDevice.getDevice().getSerialNumber()))
            return false;

        if (mHashCode != other.mHashCode)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +
                ((mHvDevice == null) ? 0 : mHvDevice.getDevice().getSerialNumber().hashCode());
        result = prime * result + mHashCode;
        return result;
    }
}
