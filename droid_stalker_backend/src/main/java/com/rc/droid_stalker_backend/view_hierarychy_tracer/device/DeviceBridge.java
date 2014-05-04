package com.rc.droid_stalker_backend.view_hierarychy_tracer.device;


import com.android.ddmlib.*;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.IHvDevice;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.ViewNode;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bridge to the device.
 */
public class DeviceBridge {

    private static final Logger logger = LoggerFactory.getLogger(DeviceBridge.class);
    private static final int DEFAULT_SERVER_PORT = 4939;

    // These codes must match the auto-generated codes in IWindowManager.java
    // See IWindowManager.aidl as well
    private static final int SERVICE_CODE_START_SERVER = 1;

    private static final int SERVICE_CODE_STOP_SERVER = 2;

    private static final int SERVICE_CODE_IS_SERVER_RUNNING = 3;
    private static final int MAX_RETRIES = 3;

    private static AndroidDebugBridge sBridge;

    private static final HashMap<IDevice, Integer> sDevicePortMap = new HashMap<IDevice, Integer>();

    private static final HashMap<IDevice, DeviceConnection> sDeviceConnectionMap = new HashMap<IDevice,
            DeviceConnection>();

    private static final HashMap<IDevice, ViewServerInfo> sViewServerInfo =
            new HashMap<IDevice, ViewServerInfo>();

    private static int sNextLocalPort = DEFAULT_SERVER_PORT;

    public static class ViewServerInfo {
        public final int protocolVersion;

        public final int serverVersion;

        ViewServerInfo(int serverVersion, int protocolVersion) {
            this.protocolVersion = protocolVersion;
            this.serverVersion = serverVersion;
        }
    }

    /**
     * Init the DeviceBridge with an existing {@link AndroidDebugBridge}.
     *
     * @param bridge the bridge object to use
     */
    public static void acquireBridge(AndroidDebugBridge bridge) {
        sBridge = bridge;
    }


    /**
     * Disconnects the current {@link AndroidDebugBridge}.
     */
    public static void terminate() {
        AndroidDebugBridge.terminate();
    }

    public static IDevice[] getDevices() {
        if (sBridge == null) {
            return new IDevice[0];
        }
        return sBridge.getDevices();
    }

    /*
     * This adds a listener to the debug bridge. The listener is notified of
     * connecting/disconnecting devices, devices coming online, etc.
     */
    public static void startListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    /**
     * Sets up a just-connected device to work with the view server.
     * <p/>
     * This starts a port forwarding between a local port and a port on the
     * device.
     *
     * @param device
     */
    public static void setupDeviceForward(final IDevice device) {
        logger.debug("Setting up device forward for device {}", device.getSerialNumber());
        synchronized (sDevicePortMap) {
            if (device.getState() == IDevice.DeviceState.ONLINE) {
                int localPort = getNextAvailablePort();
                try {
                    device.createForward(localPort, DEFAULT_SERVER_PORT);
                    sDevicePortMap.put(device, localPort);
                } catch (TimeoutException e) {
                    logger.error("Error while setup device forward", e);
                } catch (AdbCommandRejectedException e) {
                    logger.error(String.format("Adb rejected forward command for device %1$s: %2$s",
                            device, e.getMessage()), e);
                } catch (IOException e) {
                    logger.error(String.format("Failed to create forward for device %1$s: %2$s",
                            device, e.getMessage()), e);
                }
            }
        }
    }

    private static int getNextAvailablePort() {
        int port = sNextLocalPort++;
        while (!available(port)) {
            port = sNextLocalPort++;
        }
        return port;
    }

    private static boolean available(int port) {
        logger.debug("Testing port {} for device bridge", port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            logger.debug("Port {} is not available", port);
            return false;
        } catch (IOException e) {
            logger.debug("Port {} is available", port);
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }

    public static void removeDeviceForward(IDevice device) {
        synchronized (sDevicePortMap) {
            final Integer localPort = sDevicePortMap.get(device);
            if (localPort != null) {
                try {
                    device.removeForward(localPort, DEFAULT_SERVER_PORT);
                    sDevicePortMap.remove(device);
                } catch (TimeoutException e) {
                    logger.error("Timeout removing port forwarding for " + device, e);
                } catch (AdbCommandRejectedException e) {
                    // In this case, we want to fail silently.
                } catch (IOException e) {
                    logger.error(String.format("Failed to remove forward for device %1$s: %2$s",
                            device, e.getMessage()), e);
                }
            }
        }
    }

    public static int getDeviceLocalPort(IDevice device) {
        synchronized (sDevicePortMap) {
            Integer port = sDevicePortMap.get(device);
            if (port != null) {
                return port;
            }

            logger.error("Missing forwarded port for " + device.getSerialNumber());
            return -1;
        }

    }

    public static boolean isViewServerRunning(IDevice device) {
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildIsServerRunningShellCommand(),
                        new BooleanResultReader(result));
                if (!result[0]) {
                    ViewServerInfo serverInfo = loadViewServerInfo(device);
                    if (serverInfo != null && serverInfo.protocolVersion > 2) {
                        result[0] = true;
                    }
                }
            }
        } catch (TimeoutException e) {
            logger.error("Timeout checking status of view server on device " + device, e);
        } catch (IOException e) {
            logger.error("Unable to check status of view server on device " + device, e);
        } catch (AdbCommandRejectedException e) {
            logger.error("Adb rejected command to check status of view server on device " + device, e);
        } catch (ShellCommandUnresponsiveException e) {
            logger.error("Unable to execute command to check status of view server on device "
                    + device, e);
        }
        return result[0];
    }

    public static boolean startViewServer(IDevice device) {
        return startViewServer(device, DEFAULT_SERVER_PORT);
    }

    public static boolean startViewServer(IDevice device, int port) {
        logger.debug("Starting view server on device {} on port {}", device.getSerialNumber(), port);
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildStartServerShellCommand(port),
                        new BooleanResultReader(result));
            }
        } catch (TimeoutException e) {
            logger.error("Timeout starting view server on device " + device, e);
        } catch (IOException e) {
            logger.error("Unable to start view server on device " + device, e);
        } catch (AdbCommandRejectedException e) {
            logger.error("Adb rejected command to start view server on device " + device, e);
        } catch (ShellCommandUnresponsiveException e) {
            logger.error("Unable to execute command to start view server on device " + device, e);
        }
        return result[0];
    }

    public static boolean stopViewServer(IDevice device) {
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildStopServerShellCommand(), new BooleanResultReader(
                        result));
            }
        } catch (TimeoutException e) {
            logger.error("Timeout stopping view server on device " + device, e);
        } catch (IOException e) {
            logger.error("Unable to stop view server on device " + device, e);
        } catch (AdbCommandRejectedException e) {
            logger.error("Adb rejected command to stop view server on device " + device, e);
        } catch (ShellCommandUnresponsiveException e) {
            logger.error("Unable to execute command to stop view server on device " + device, e);
        }
        return result[0];
    }

    private static String buildStartServerShellCommand(int port) {
        return String.format("service call window %d i32 %d", SERVICE_CODE_START_SERVER, port); //$NON-NLS-1$
    }

    private static String buildStopServerShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_STOP_SERVER); //$NON-NLS-1$
    }

    private static String buildIsServerRunningShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_IS_SERVER_RUNNING); //$NON-NLS-1$
    }

    private static class BooleanResultReader extends MultiLineReceiver {
        private final boolean[] mResult;

        public BooleanResultReader(boolean[] result) {
            mResult = result;
        }

        @Override
        public void processNewLines(String[] strings) {
            if (strings.length > 0) {
                Pattern pattern = Pattern.compile(".*?\\([0-9]{8} ([0-9]{8}).*"); //$NON-NLS-1$
                Matcher matcher = pattern.matcher(strings[0]);
                if (matcher.matches()) {
                    if (Integer.parseInt(matcher.group(1)) == 1) {
                        mResult[0] = true;
                    }
                }
            }
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }

    public static ViewServerInfo loadViewServerInfo(final IDevice device) {
        logger.debug("Loading view server info from device {}", device.getSerialNumber());
        int server = -1;
        int protocol = -1;
        DeviceConnection connection = getConnectionForDevice(device);
        try {
            connection.sendCommand("SERVER"); //$NON-NLS-1$
            String line = connection.getInputStream().readLine();
            if (line != null) {
                server = Integer.parseInt(line);
            }
            connection.sendCommand("PROTOCOL"); //$NON-NLS-1$
            line = connection.getInputStream().readLine();
            if (line != null) {
                protocol = Integer.parseInt(line);
            }
        } catch (Exception e) {
            logger.error("Unable to get view server protocol version from device " + device, e);
        }
        if (server == -1 || protocol == -1) {
            return null;
        }
        ViewServerInfo returnValue = new ViewServerInfo(server, protocol);
        synchronized (sViewServerInfo) {
            sViewServerInfo.put(device, returnValue);
        }
        return returnValue;
    }

    private static DeviceConnection getConnectionForDevice(final IDevice device) {
        if (sDeviceConnectionMap.containsKey(device)) {
            DeviceConnection connection = sDeviceConnectionMap.get(device);
            if (!connection.getSocket().isClosed())
                return connection;
            else {
                connection.close();
            }
        }
        return createNewDeviceConnectionFor(device);
    }

    public static DeviceConnection createNewDeviceConnectionFor(final IDevice device) {
        DeviceConnection deviceConnection = null;
        int retries = 0;
        while (retries++ < MAX_RETRIES) {
            try {
                deviceConnection = new DeviceConnection(device);
                break;
            } catch (IOException e) {
                logger.error("Error while creating device connection for device {}", device.getSerialNumber(), e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
        }
        if (deviceConnection == null)
            throw new RuntimeException("Could not connect to the device " + device.getSerialNumber());
        sDeviceConnectionMap.put(device, deviceConnection);
        return deviceConnection;
    }

    public static ViewServerInfo getViewServerInfo(IDevice device) {
        synchronized (sViewServerInfo) {
            return sViewServerInfo.get(device);
        }
    }

    public static void removeViewServerInfo(IDevice device) {
        synchronized (sViewServerInfo) {
            sViewServerInfo.remove(device);
        }
    }

    /*
     * This loads the list of windows from the specified device. The format is:
     * hashCode1 title1 hashCode2 title2 ... hashCodeN titleN DONE.
     */
    public static Window[] loadWindows(final IHvDevice hvDevice, final IDevice device) {
        logger.debug("Loading windows from device {}", device.getSerialNumber());
        ArrayList<Window> windows = new ArrayList<Window>();
        DeviceConnection connection = getConnectionForDevice(device);
        ViewServerInfo serverInfo = getViewServerInfo(device);
        try {
            connection.sendCommand("LIST"); //$NON-NLS-1$
            BufferedReader in = connection.getInputStream();
            String line;
            while ((line = in.readLine()) != null) {
                //logger.debug("{} read from view server for {}", line, device.getSerialNumber());
                if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
                    break;
                }

                int index = line.indexOf(' ');
                if (index != -1) {
                    String windowId = line.substring(0, index);

                    int id;
                    if (serverInfo.serverVersion > 2) {
                        id = (int) Long.parseLong(windowId, 16);
                    } else {
                        id = Integer.parseInt(windowId, 16);
                    }

                    Window w = new Window(hvDevice, line.substring(index + 1), id);
                    logger.debug("Adding window from {} with title {}", device.getSerialNumber(), w.getTitle());
                    windows.add(w);
                }
            }
            // Automatic refreshing of windows was added in protocol version 3.
            // Before, the user needed to specify explicitly that he wants to
            // get the focused window, which was done using a special type of
            // window with hash code -1.
            if (serverInfo.protocolVersion < 3) {
                windows.add(Window.getFocusedWindow(hvDevice));
            }
        } catch (Exception e) {
            logger.error("Unable to load the window list from device " + device, e);
        }
        // The server returns the list of windows from the window at the bottom
        // to the top. We want the reverse order to put the top window on top of
        // the list.
        Window[] returnValue = new Window[windows.size()];
        for (int i = windows.size() - 1; i >= 0; i--) {
            returnValue[returnValue.length - i - 1] = windows.get(i);
        }
        return returnValue;
    }

    /*
     * This gets the hash code of the window that has focus. Only works with
     * protocol version 3 and above.
     */
    public static int getFocusedWindow(IDevice device) {
        DeviceConnection connection = getConnectionForDevice(device);
        try {

            connection.sendCommand("GET_FOCUS"); //$NON-NLS-1$
            String line = connection.getInputStream().readLine();
            if (line == null || line.length() == 0) {
                return -1;
            }
            return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
        } catch (Exception e) {
            logger.error("Unable to get the focused window from device " + device, e);
        }
        return -1;
    }

    public static ViewNode loadWindowData(Window window) {
        DeviceConnection connection = getConnectionForDevice(window.getDevice());
        try {
            connection.sendCommand("DUMP " + window.encode()); //$NON-NLS-1$
            BufferedReader in = connection.getInputStream();
            ViewNode currentNode = parseViewHierarchy(in, window);
            ViewServerInfo serverInfo = getViewServerInfo(window.getDevice());
            if (serverInfo != null) {
                currentNode.protocolVersion = serverInfo.protocolVersion;
            }
            return currentNode;
        } catch (Exception e) {
            logger.error("Unable to load window data for window " + window.getTitle() + " on device "
                    + window.getDevice(), e);
        }
        return null;
    }

    public static ViewNode parseViewHierarchy(BufferedReader in, Window window) {
        ViewNode currentNode = null;
        int currentDepth = -1;
        String line;
        try {
            while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) {
                    break;
                }
                int depth = 0;
                while (line.charAt(depth) == ' ') {
                    depth++;
                }
                while (depth <= currentDepth) {
                    if (currentNode != null) {
                        currentNode = currentNode.parent;
                    }
                    currentDepth--;
                }
                currentNode = new ViewNode(window, currentNode, line.substring(depth));
                currentDepth = depth;
            }
        } catch (IOException e) {
            logger.error("Error reading view hierarchy stream: " + e.getMessage(), e);
            return null;
        }
        if (currentNode == null) {
            return null;
        }
        while (currentNode.parent != null) {
            currentNode = currentNode.parent;
        }

        return currentNode;
    }

    public static boolean loadProfileData(Window window, ViewNode viewNode) {
        DeviceConnection connection = getConnectionForDevice(window.getDevice());
        try {
            connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString()); //$NON-NLS-1$
            BufferedReader in = connection.getInputStream();
            int protocol;
            synchronized (sViewServerInfo) {
                protocol = sViewServerInfo.get(window.getDevice()).protocolVersion;
            }
            if (protocol < 3) {
                return loadProfileData(viewNode, in);
            } else {
                boolean ret = loadProfileDataRecursive(viewNode, in);
                if (ret) {
                    viewNode.setProfileRatings();
                }
                return ret;
            }
        } catch (Exception e) {
            logger.error("Unable to load profiling data for window " + window.getTitle()
                    + " on device " + window.getDevice(), e);
        }
        return false;
    }

    private static boolean loadProfileData(ViewNode node, BufferedReader in) throws IOException {
        String line;
        if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1") //$NON-NLS-1$
                || line.equalsIgnoreCase("DONE.")) { //$NON-NLS-1$
            return false;
        }
        String[] data = line.split(" ");
        node.measureTime = (Long.parseLong(data[0]) / 1000.0) / 1000.0;
        node.layoutTime = (Long.parseLong(data[1]) / 1000.0) / 1000.0;
        node.drawTime = (Long.parseLong(data[2]) / 1000.0) / 1000.0;
        return true;
    }

    public static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
            throws IOException {
        if (!loadProfileData(node, in)) {
            return false;
        }
        for (int i = 0; i < node.children.size(); i++) {
            if (!loadProfileDataRecursive(node.children.get(i), in)) {
                return false;
            }
        }
        return true;
    }

    public static Image loadCapture(Window window, ViewNode viewNode) {
        /*DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(window.getDevice());
            connection.getSocket().setSoTimeout(5000);
            connection.sendCommand("CAPTURE " + window.encode() + " " + viewNode.toString()); //$NON-NLS-1$
            return new Image(connection.getSocket().getInputStream());
        } catch (Exception e) {
            Log.e(TAG, "Unable to capture data for node " + viewNode + " in window "
                    + window.getTitle() + " on device " + window.getDevice());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }     */
        return null;
    }


    public static void invalidateView(ViewNode viewNode) {
        DeviceConnection connection = getConnectionForDevice(viewNode.window.getDevice());
        try {
            connection.sendCommand("INVALIDATE " + viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
        } catch (Exception e) {
            logger.error("Unable to invalidate view " + viewNode + " in window " + viewNode.window
                    + " on device " + viewNode.window.getDevice(), e);
        }
    }

    public static void requestLayout(ViewNode viewNode) {
        DeviceConnection connection = getConnectionForDevice(viewNode.window.getDevice());
        try {
            connection.sendCommand("REQUEST_LAYOUT " + viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
        } catch (Exception e) {
            logger.error("Unable to request layout for node " + viewNode + " in window "
                    + viewNode.window + " on device " + viewNode.window.getDevice(), e);
        }
    }

    public static void outputDisplayList(ViewNode viewNode) {
        DeviceConnection connection = getConnectionForDevice(viewNode.window.getDevice());
        try {
            connection.sendCommand("OUTPUT_DISPLAYLIST " +
                    viewNode.window.encode() + " " + viewNode); //$NON-NLS-1$
        } catch (Exception e) {
            logger.error("Unable to dump displaylist for node " + viewNode + " in window "
                    + viewNode.window + " on device " + viewNode.window.getDevice(), e);
        }
    }

}
