package com.rc.droid_stalker_backend.view_hierarychy_tracer.models;


import com.android.ddmlib.IDevice;

import java.awt.*;
import java.util.List;

/**
 * Represents a device that can perform view debug operations.
 */
public interface IHvDevice {
    /**
     * Initializes view debugging on the device.
     *
     * @return true if the on device component was successfully initialized
     */
    boolean initializeViewDebug();

    boolean reloadWindows();

    void terminateViewDebug();

    boolean isViewDebugEnabled();

    boolean supportsDisplayListDump();

    Window[] getWindows();

    int getFocusedWindow();

    IDevice getDevice();


    ViewNode loadWindowData(Window window);

    void loadProfileData(Window window, ViewNode viewNode);

    Image loadCapture(Window window, ViewNode viewNode);

    public Image getScreenshotImage();

    void invalidateView(ViewNode viewNode);

    void requestLayout(ViewNode viewNode);

    void outputDisplayList(ViewNode viewNode);

    boolean isViewUpdateEnabled();

    void invokeViewMethod(Window window, ViewNode viewNode, String method, List<?> args);

    boolean setLayoutParameter(Window window, ViewNode viewNode, String property, int value);

    void addWindowChangeListener(WindowUpdater.IWindowChangeListener l);

    void removeWindowChangeListener(WindowUpdater.IWindowChangeListener l);
}
