package com.rc.droid_stalker_backend.view_hierarychy_tracer.device;


import com.android.ddmlib.IDevice;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.ViewNode;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.Window;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.WindowUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class ViewServerDevice extends AbstractHvDevice {
    private static final Logger logger = LoggerFactory.getLogger(ViewServerDevice.class);

    final IDevice mDevice;
    private DeviceBridge.ViewServerInfo mViewServerInfo;
    private Window[] mWindows;

    public ViewServerDevice(IDevice device) {
        mDevice = device;
    }

    @Override
    public boolean initializeViewDebug() {
        logger.debug("Initializing view debug");
        if (!mDevice.isOnline()) {
            return false;
        }
        DeviceBridge.stopViewServer(mDevice);
        DeviceBridge.startViewServer(mDevice);
        DeviceBridge.setupDeviceForward(mDevice);
        return reloadWindows();
    }

    @Override
    public boolean reloadWindows() {
        if (!DeviceBridge.isViewServerRunning(mDevice)) {
            if (!DeviceBridge.startViewServer(mDevice)) {
                logger.error("Unable to debug device: " + mDevice.getName());
                DeviceBridge.removeDeviceForward(mDevice);
                return false;
            }
        }
        logger.debug("Loading view server info for {}", mDevice.getSerialNumber());
        mViewServerInfo = DeviceBridge.loadViewServerInfo(mDevice);
        if (mViewServerInfo == null) {
            return false;
        }

        mWindows = DeviceBridge.loadWindows(this, mDevice);
        return true;
    }

    @Override
    public boolean supportsDisplayListDump() {
        return mViewServerInfo != null && mViewServerInfo.protocolVersion >= 4;
    }

    @Override
    public void terminateViewDebug() {
        DeviceBridge.removeDeviceForward(mDevice);
        DeviceBridge.removeViewServerInfo(mDevice);
    }

    @Override
    public boolean isViewDebugEnabled() {
        return mViewServerInfo != null;
    }

    @Override
    public Window[] getWindows() {
        return mWindows;
    }

    @Override
    public int getFocusedWindow() {
        return DeviceBridge.getFocusedWindow(mDevice);
    }

    @Override
    public IDevice getDevice() {
        return mDevice;
    }

    @Override
    public ViewNode loadWindowData(Window window) {
        return DeviceBridge.loadWindowData(window);
    }

    @Override
    public void loadProfileData(Window window, ViewNode viewNode) {
        DeviceBridge.loadProfileData(window, viewNode);
    }

    @Override
    public Image loadCapture(Window window, ViewNode viewNode) {
        return DeviceBridge.loadCapture(window, viewNode);
    }


    @Override
    public void invalidateView(ViewNode viewNode) {
        DeviceBridge.invalidateView(viewNode);
    }

    @Override
    public void requestLayout(ViewNode viewNode) {
        DeviceBridge.requestLayout(viewNode);
    }

    @Override
    public void outputDisplayList(ViewNode viewNode) {
        DeviceBridge.outputDisplayList(viewNode);
    }

    @Override
    public void addWindowChangeListener(WindowUpdater.IWindowChangeListener l) {
        if (mViewServerInfo != null && mViewServerInfo.protocolVersion >= 3) {
            WindowUpdater.startListenForWindowChanges(l, mDevice);
        }
    }

    @Override
    public void removeWindowChangeListener(WindowUpdater.IWindowChangeListener l) {
        if (mViewServerInfo != null && mViewServerInfo.protocolVersion >= 3) {
            WindowUpdater.stopListenForWindowChanges(l, mDevice);
        }
    }

    @Override
    public boolean isViewUpdateEnabled() {
        return false;
    }

    @Override
    public void invokeViewMethod(Window window, ViewNode viewNode, String method,
                                 List<?> args) {
        // not supported
    }

    @Override
    public boolean setLayoutParameter(Window window, ViewNode viewNode, String property,
                                      int value) {
        // not supported
        return false;
    }
}
