package com.rc.droid_stalker_backend.view_hierarychy_tracer;


import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.device.DeviceBridge;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.device.HvDeviceFactory;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.*;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;

/**
 * This is the class where most of the logic resides.
 */
public abstract class HierarchyViewerDirector implements IDeviceChangeListener,
        WindowUpdater.IWindowChangeListener {
    private static final boolean sIsUsingDdmProtocol;

    static {
        //String sHvProtoEnvVar = System.getenv("ANDROID_HVPROTO"); //$NON-NLS-1$
        sIsUsingDdmProtocol = true;//"ddm".equalsIgnoreCase(sHvProtoEnvVar);
    }

    protected static HierarchyViewerDirector sDirector;

    private static final Logger logger = LoggerFactory.getLogger(HierarchyViewerDirector.class);


    private Timer mPixelPerfectRefreshTimer = new Timer();

    private boolean mAutoRefresh = true;


    private String mFilterText = ""; //$NON-NLS-1$

    private static final Object mDevicesLock = new Object();
    private Map<IDevice, IHvDevice> mDevices = new HashMap<IDevice, IHvDevice>(10);

    public static boolean isUsingDdmProtocol() {
        return sIsUsingDdmProtocol;
    }

    public void terminate() {
        WindowUpdater.terminate();
        mPixelPerfectRefreshTimer.cancel();
    }

    public abstract String getAdbLocation();

    public static HierarchyViewerDirector getDirector() {
        return sDirector;
    }

    /**
     * Init the DeviceBridge with an existing {@link AndroidDebugBridge}.
     *
     * @param bridge the bridge object to use
     */
    public void acquireBridge(final AndroidDebugBridge bridge) {
        DeviceBridge.acquireBridge(bridge);
    }


    public void populateDeviceSelectionModel() {
        IDevice[] devices = DeviceBridge.getDevices();
        for (IDevice device : devices) {
            deviceConnected(device);
        }
    }

    public void startListenForDevices() {
        DeviceBridge.startListenForDevices(this);
    }

    public void stopListenForDevices() {
        DeviceBridge.stopListenForDevices(this);
    }

    public abstract void executeInBackground(String taskName, Runnable task);

    @Override
    public void deviceConnected(final IDevice device) {
        logger.debug("Connecting to the device {}", device.getSerialNumber());
        if (!device.isOnline()) {
            logger.debug("Device {} is not online", device.getSerialNumber());
            return;
        }

        IHvDevice hvDevice;
        synchronized (mDevicesLock) {
            hvDevice = mDevices.get(device);
            if (hvDevice == null) {
                hvDevice = HvDeviceFactory.create(device);
                hvDevice.initializeViewDebug();
                hvDevice.addWindowChangeListener(getDirector());
                mDevices.put(device, hvDevice);
            } else {
                // attempt re-initializing view server if device state has changed
                hvDevice.initializeViewDebug();
            }
        }

        DeviceSelectionModel.getModel().addDevice(hvDevice);
        focusChanged(device);
    }

    @Override
    public void deviceDisconnected(final IDevice device) {
        executeInBackground("Disconnecting device", new Runnable() {
            @Override
            public void run() {
                IHvDevice hvDevice;
                synchronized (mDevicesLock) {
                    hvDevice = mDevices.get(device);
                    if (hvDevice != null) {
                        mDevices.remove(device);
                    }
                }

                if (hvDevice == null) {
                    return;
                }

                hvDevice.terminateViewDebug();
                hvDevice.removeWindowChangeListener(getDirector());
                DeviceSelectionModel.getModel().removeDevice(hvDevice);
            }
        });
    }

    @Override
    public void windowsChanged(final IDevice device) {
        executeInBackground("Refreshing windows", new Runnable() {
            @Override
            public void run() {
                logger.debug("Device changed so refreshing windows");
                IHvDevice hvDevice = getHvDevice(device);
                hvDevice.reloadWindows();
                DeviceSelectionModel.getModel().updateDevice(hvDevice);
            }
        });
    }

    @Override
    public void focusChanged(final IDevice device) {
        executeInBackground("Updating focus", new Runnable() {
            @Override
            public void run() {
                IHvDevice hvDevice = getHvDevice(device);
                int focusedWindow = hvDevice.getFocusedWindow();
                DeviceSelectionModel.getModel().updateFocusedWindow(hvDevice, focusedWindow);
            }
        });
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        if ((changeMask & IDevice.CHANGE_STATE) != 0 && device.isOnline()) {
            deviceConnected(device);
        }
    }


    public IHvDevice getHvDevice(IDevice device) {
        synchronized (mDevicesLock) {
            return mDevices.get(device);
        }
    }

    private Image getScreenshotImage(IHvDevice hvDevice) {
        return (hvDevice == null) ? null : hvDevice.getScreenshotImage();
    }

    public void loadViewTreeData(final Window window) {
        executeInBackground("Loading view hierarchy", new Runnable() {
            @Override
            public void run() {
                mFilterText = ""; //$NON-NLS-1$

                IHvDevice hvDevice = window.getHvDevice();
                ViewNode viewNode = hvDevice.loadWindowData(window);
                if (viewNode != null) {
                    viewNode.setViewCount();
                }
            }
        });
    }


    public void refreshWindows() {
        executeInBackground("Refreshing windows", new Runnable() {
            @Override
            public void run() {
                IHvDevice[] hvDevicesA = DeviceSelectionModel.getModel().getDevices();
                IDevice[] devicesA = new IDevice[hvDevicesA.length];
                for (int i = 0; i < hvDevicesA.length; i++) {
                    devicesA[i] = hvDevicesA[i].getDevice();
                }
                IDevice[] devicesB = DeviceBridge.getDevices();
                HashSet<IDevice> deviceSet = new HashSet<IDevice>();
                for (int i = 0; i < devicesB.length; i++) {
                    deviceSet.add(devicesB[i]);
                }
                for (int i = 0; i < devicesA.length; i++) {
                    if (deviceSet.contains(devicesA[i])) {
                        windowsChanged(devicesA[i]);
                        deviceSet.remove(devicesA[i]);
                    } else {
                        deviceDisconnected(devicesA[i]);
                    }
                }
                for (IDevice device : deviceSet) {
                    deviceConnected(device);
                }
            }
        });
    }

    public void loadViewHierarchy() {
        Window window = DeviceSelectionModel.getModel().getSelectedWindow();
        if (window != null) {
            loadViewTreeData(window);
        }
    }


    private void loadViewRecursive(ViewNode viewNode) {
        IHvDevice hvDevice = getHvDevice(viewNode.window.getDevice());
        //Image image = hvDevice.loadCapture(viewNode.window, viewNode);
        /*if (image == null) {
            return;
        }
        viewNode.image = image; */
        final int N = viewNode.children.size();
        for (int i = 0; i < N; i++) {
            loadViewRecursive(viewNode.children.get(i));
        }
    }


    public String getFilterText() {
        return mFilterText;
    }

}
