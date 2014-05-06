package com.rc.droid_stalker.view_hierarychy_tracer.device;


import com.android.ddmlib.*;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.HandleViewDebug.ViewDumpHandler;
import com.rc.droid_stalker.view_hierarychy_tracer.models.ViewNode;
import com.rc.droid_stalker.view_hierarychy_tracer.models.Window;
import com.rc.droid_stalker.view_hierarychy_tracer.models.WindowUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DdmViewDebugDevice extends AbstractHvDevice implements IDeviceChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(DdmViewDebugDevice.class);

    private final IDevice mDevice;
    private Map<Client, List<String>> mViewRootsPerClient = new HashMap<Client, List<String>>(40);

    public DdmViewDebugDevice(IDevice device) {
        mDevice = device;
    }

    @Override
    public boolean initializeViewDebug() {
        AndroidDebugBridge.addDeviceChangeListener(this);
        return reloadWindows();
    }

    private static class ListViewRootsHandler extends ViewDumpHandler {
        private List<String> mViewRoots = Collections.synchronizedList(new ArrayList<String>(10));

        public ListViewRootsHandler() {
            super(HandleViewDebug.CHUNK_VULW);
        }

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            int nWindows = data.getInt();

            for (int i = 0; i < nWindows; i++) {
                int len = data.getInt();
                mViewRoots.add(getString(data, len));
            }
        }

        public List<String> getViewRoots(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mViewRoots;
        }
    }

    private static class CaptureByteArrayHandler extends ViewDumpHandler {
        public CaptureByteArrayHandler(int type) {
            super(type);
        }

        private AtomicReference<byte[]> mData = new AtomicReference<byte[]>();

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            byte[] b = new byte[data.remaining()];
            data.get(b);
            mData.set(b);

        }

        public byte[] getData(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mData.get();
        }
    }


    @Override
    public boolean reloadWindows() {
        mViewRootsPerClient = new HashMap<Client, List<String>>(40);

        for (Client c : mDevice.getClients()) {
            ClientData cd = c.getClientData();
            if (cd != null && cd.hasFeature(ClientData.FEATURE_VIEW_HIERARCHY)) {
                ListViewRootsHandler handler = new ListViewRootsHandler();

                try {
                    HandleViewDebug.listViewRoots(c, handler);
                } catch (IOException e) {
                    logger.error("No connection to client: " + cd.getClientDescription(), e);
                    continue;
                }

                List<String> viewRoots = new ArrayList<String>(
                        handler.getViewRoots(200, TimeUnit.MILLISECONDS));
                mViewRootsPerClient.put(c, viewRoots);
            }
        }

        return true;
    }

    @Override
    public void terminateViewDebug() {
        // nothing to terminate
    }

    @Override
    public boolean isViewDebugEnabled() {
        return true;
    }

    @Override
    public boolean supportsDisplayListDump() {
        return true;
    }

    @Override
    public Window[] getWindows() {
        List<Window> windows = new ArrayList<Window>(10);

        for (Client c : mViewRootsPerClient.keySet()) {
            for (String viewRoot : mViewRootsPerClient.get(c)) {
                windows.add(new Window(this, viewRoot, c));
            }
        }

        return windows.toArray(new Window[windows.size()]);
    }

    @Override
    public int getFocusedWindow() {
        // TODO: add support for identifying view in focus
        return -1;
    }

    @Override
    public IDevice getDevice() {
        return mDevice;
    }

    @Override
    public ViewNode loadWindowData(Window window) {
        Client c = window.getClient();
        if (c == null) {
            return null;
        }

        String viewRoot = window.getTitle();
        CaptureByteArrayHandler handler = new CaptureByteArrayHandler(HandleViewDebug.CHUNK_VURT);
        try {
            HandleViewDebug.dumpViewHierarchy(c, viewRoot,
                    false /* skipChildren */,
                    true  /* includeProperties */,
                    handler);
        } catch (IOException e) {
            logger.error("Error while loading window data", e);
            return null;
        }

        byte[] data = handler.getData(20, TimeUnit.SECONDS);
        if (data == null) {
            return null;
        }

        String viewHierarchy = new String(data, Charset.forName("UTF-8"));
        return DeviceBridge.parseViewHierarchy(new BufferedReader(new StringReader(viewHierarchy)),
                window);
    }

    @Override
    public void loadProfileData(Window window, ViewNode viewNode) {
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        CaptureByteArrayHandler handler = new CaptureByteArrayHandler(HandleViewDebug.CHUNK_VUOP);
        try {
            HandleViewDebug.profileView(c, viewRoot, viewNode.toString(), handler);
        } catch (IOException e) {
            logger.error("Error while loading profile data", e);
            return;
        }

        byte[] data = handler.getData(30, TimeUnit.SECONDS);
        if (data == null) {
            logger.error("Timed out waiting for profile data");
            return;
        }

        try {
            boolean success = DeviceBridge.loadProfileDataRecursive(viewNode,
                    new BufferedReader(new StringReader(new String(data))));
            if (success) {
                viewNode.setProfileRatings();
            }
        } catch (IOException e) {
            logger.error("Error while loading profile data recursively", e);
            return;
        }
    }

    @Override
    public Image loadCapture(Window window, ViewNode viewNode) {
        return null;
    }


    @Override
    public void invalidateView(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.invalidateView(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            logger.error("Error while invalidating view {}", viewNode.toString(), e);
        }
    }

    @Override
    public void requestLayout(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.requestLayout(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            logger.error("Error while requesting layout for {}", viewNode.toString(), e);
        }
    }

    @Override
    public void outputDisplayList(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.dumpDisplayList(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            logger.error("Error while dumping display list {}", viewNode.toString(), e);
        }
    }

    @Override
    public void addWindowChangeListener(WindowUpdater.IWindowChangeListener l) {
        // TODO: add support for listening to view root changes
    }

    @Override
    public void removeWindowChangeListener(WindowUpdater.IWindowChangeListener l) {
        // TODO: add support for listening to view root changes
    }

    @Override
    public void deviceConnected(IDevice device) {
        // pass
    }

    @Override
    public void deviceDisconnected(IDevice device) {
        // pass
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        if ((changeMask & IDevice.CHANGE_CLIENT_LIST) != 0) {
            reloadWindows();
        }
    }

    @Override
    public boolean isViewUpdateEnabled() {
        return true;
    }

    @Override
    public void invokeViewMethod(Window window, ViewNode viewNode, String method,
                                 List<?> args) {
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.invokeMethod(c, viewRoot, viewNode.toString(), method, args.toArray());
        } catch (IOException e) {
            logger.error("Error while invoking view method {} on view {}", method, viewNode.toString(), e);
        }
    }

    @Override
    public boolean setLayoutParameter(Window window, ViewNode viewNode, String property,
                                      int value) {
        Client c = window.getClient();
        if (c == null) {
            return false;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.setLayoutParameter(c, viewRoot, viewNode.toString(), property, value);
        } catch (IOException e) {
            logger.error("Error while setting layout parameter for view {}", viewNode.toString(), e);
            return false;
        }

        return true;
    }
}
