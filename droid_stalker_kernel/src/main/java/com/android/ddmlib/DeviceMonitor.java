/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmlib;

import com.android.ddmlib.AdbHelper.AdbResponse;
import com.android.ddmlib.ClientData.DebuggerStatus;
import com.android.ddmlib.DebugPortManager.IDebugPortProvider;
import com.android.ddmlib.IDevice.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * A Device monitor. This connects to the Android Debug Bridge and get device and
 * debuggable process information from it.
 */
final class DeviceMonitor {
    private static final Logger logger = LoggerFactory.getLogger(DeviceMonitor.class);
    private byte[] mLengthBuffer = new byte[4];
    private byte[] mLengthBuffer2 = new byte[4];

    private boolean mQuit = false;

    private AndroidDebugBridge mServer;

    private SocketChannel mMainAdbConnection = null;
    private boolean mMonitoring = false;
    private int mConnectionAttempt = 0;
    private int mRestartAttemptCount = 0;
    private boolean mInitialDeviceListDone = false;

    private Selector mSelector;

    private final ArrayList<Device> mDevices = new ArrayList<Device>();

    private final ArrayList<Integer> mDebuggerPorts = new ArrayList<Integer>();

    private final HashMap<Client, Integer> mClientsToReopen = new HashMap<Client, Integer>();

    /**
     * Creates a new {@link com.android.ddmlib.DeviceMonitor} object and links it to the running
     * {@link AndroidDebugBridge} object.
     *
     * @param server the running {@link AndroidDebugBridge}.
     */
    DeviceMonitor(AndroidDebugBridge server) {
        mServer = server;

        mDebuggerPorts.add(DdmPreferences.getDebugPortBase());
    }

    /**
     * Starts the monitoring.
     */
    void start() {
        new Thread("Device List Monitor") { //$NON-NLS-1$
            @Override
            public void run() {
                deviceMonitorLoop();
            }
        }.start();
    }

    /**
     * Stops the monitoring.
     */
    void stop() {
        mQuit = true;

        // wakeup the main loop thread by closing the main connection to adb.
        try {
            if (mMainAdbConnection != null) {
                mMainAdbConnection.close();
            }
        } catch (IOException e1) {
        }

        // wake up the secondary loop by closing the selector.
        if (mSelector != null) {
            mSelector.wakeup();
        }
    }


    /**
     * Returns if the monitor is currently connected to the debug bridge server.
     *
     * @return
     */
    boolean isMonitoring() {
        return mMonitoring;
    }

    int getConnectionAttemptCount() {
        return mConnectionAttempt;
    }

    int getRestartAttemptCount() {
        return mRestartAttemptCount;
    }

    /**
     * Returns the devices.
     */
    Device[] getDevices() {
        synchronized (mDevices) {
            return mDevices.toArray(new Device[mDevices.size()]);
        }
    }

    boolean hasInitialDeviceList() {
        return mInitialDeviceListDone;
    }

    AndroidDebugBridge getServer() {
        return mServer;
    }

    void addClientToDropAndReopen(Client client, int port) {
        synchronized (mClientsToReopen) {
            Log.d("DeviceMonitor",
                    "Adding " + client + " to list of client to reopen (" + port + ").");
            if (mClientsToReopen.get(client) == null) {
                mClientsToReopen.put(client, port);
            }
        }
        mSelector.wakeup();
    }

    /**
     * Monitors the devices. This connects to the Debug Bridge
     */
    private void deviceMonitorLoop() {
        do {
            try {
                if (mMainAdbConnection == null) {
                    logger.debug("Opening ADB connection");
                    mMainAdbConnection = openAdbConnection();
                    if (mMainAdbConnection == null) {
                        mConnectionAttempt++;
                        Log.e("DeviceMonitor", "Connection attempts: " + mConnectionAttempt);
                        if (mConnectionAttempt > 10) {
                            if (!mServer.startAdb()) {
                                mRestartAttemptCount++;
                                Log.e("DeviceMonitor",
                                        "adb restart attempts: " + mRestartAttemptCount);
                            } else {
                                mRestartAttemptCount = 0;
                            }
                        }
                        waitABit();
                    } else {
                        Log.d("DeviceMonitor", "Connected to adb for device monitoring");
                        mConnectionAttempt = 0;
                    }
                }
                if (mMainAdbConnection != null && !mMonitoring) {
                    mMonitoring = sendDeviceListMonitoringRequest();
                }

                if (mMonitoring) {
                    // read the length of the incoming message
                    int length = readLength(mMainAdbConnection, mLengthBuffer);

                    if (length >= 0) {
                        // read the incoming message
                        processIncomingDeviceData(length);

                        // flag the fact that we have build the list at least once.
                        mInitialDeviceListDone = true;
                    }
                }
            } catch (AsynchronousCloseException ace) {
                // this happens because of a call to Quit. We do nothing, and the loop will break.
            } catch (TimeoutException ioe) {
                handleExpectionInMonitorLoop(ioe);
            } catch (IOException ioe) {
                handleExpectionInMonitorLoop(ioe);
            }
        } while (!mQuit);
    }

    private void handleExpectionInMonitorLoop(Exception e) {
        if (!mQuit) {
            if (e instanceof TimeoutException) {
                Log.e("DeviceMonitor", "Adb connection Error: timeout");
            } else {
                Log.e("DeviceMonitor", "Adb connection Error:" + e.getMessage());
            }
            mMonitoring = false;
            if (mMainAdbConnection != null) {
                try {
                    mMainAdbConnection.close();
                } catch (IOException ioe) {
                    // we can safely ignore that one.
                }
                mMainAdbConnection = null;

                // remove all devices from list
                // because we are going to call mServer.deviceDisconnected which will acquire this
                // lock we lock it first, so that the AndroidDebugBridge lock is always locked
                // first.
                synchronized (AndroidDebugBridge.getLock()) {
                    synchronized (mDevices) {
                        for (int n = mDevices.size() - 1; n >= 0; n--) {
                            Device device = mDevices.get(0);
                            removeDevice(device);
                            mServer.deviceDisconnected(device);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sleeps for a little bit.
     */
    private void waitABit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
        }
    }

    /**
     * @return
     * @throws java.io.IOException
     */
    private boolean sendDeviceListMonitoringRequest() throws TimeoutException, IOException {
        byte[] request = AdbHelper.formAdbRequest("host:track-devices"); //$NON-NLS-1$

        try {
            AdbHelper.write(mMainAdbConnection, request);
            AdbResponse resp = AdbHelper.readAdbResponse(mMainAdbConnection,
                    false /* readDiagString */);

            if (!resp.okay) {
                // request was refused by adb!
                Log.e("DeviceMonitor", "adb refused request: " + resp.message);
            }

            return resp.okay;
        } catch (IOException e) {
            logger.error("Sending tracking request failed", e);
            mMainAdbConnection.close();
            throw e;
        }
    }


    /**
     * Attempts to connect to the debug bridge server.
     *
     * @return a connect socket if success, null otherwise
     */
    private SocketChannel openAdbConnection() {
        logger.debug("Connecting to adb for Device List Monitoring...");

        SocketChannel adbChannel = null;
        // AMD change
        // adding 3 retries for this connection to avoid entire flow to repeat
        for (int retryNo = 1; retryNo <= 3; ++retryNo) {
            try {
                adbChannel = SocketChannel.open(AndroidDebugBridge.getSocketAddress());
                // checking if connection is proper
                while (!adbChannel.finishConnect())
                    waitABit();
                waitABit();
                break;
            } catch (IOException e) {
                if (retryNo == 3)
                    logger.error("Error while opening adb connection", e);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            logger.debug("Trying attempt to make adb channel : {}", retryNo);
        }
        return adbChannel;
    }

    /**
     * Processes an incoming device message from the socket
     *
     * @param socket
     * @param length
     * @throws java.io.IOException
     */
    private void processIncomingDeviceData(int length) throws IOException {
        ArrayList<Device> list = new ArrayList<Device>();

        if (length > 0) {
            byte[] buffer = new byte[length];
            String result = read(mMainAdbConnection, buffer);

            String[] devices = result.split("\n"); //$NON-NLS-1$

            for (String d : devices) {
                String[] param = d.split("\t"); //$NON-NLS-1$
                if (param.length == 2) {
                    // new adb uses only serial numbers to identify devices
                    Device device = new Device(this, param[0] /*serialnumber*/,
                            DeviceState.getState(param[1]));

                    //add the device to the list
                    list.add(device);
                }
            }
        }

        // now merge the new devices with the old ones.
        updateDevices(list);
    }

    /**
     * Updates the device list with the new items received from the monitoring service.
     */
    private void updateDevices(ArrayList<Device> newList) {
        // because we are going to call mServer.deviceDisconnected which will acquire this lock
        // we lock it first, so that the AndroidDebugBridge lock is always locked first.
        synchronized (AndroidDebugBridge.getLock()) {
            // array to store the devices that must be queried for information.
            // it's important to not do it inside the synchronized loop as this could block
            // the whole workspace (this lock is acquired during build too).
            ArrayList<Device> devicesToQuery = new ArrayList<Device>();
            synchronized (mDevices) {
                // For each device in the current list, we look for a matching the new list.
                // * if we find it, we update the current object with whatever new information
                //   there is
                //   (mostly state change, if the device becomes ready, we query for build info).
                //   We also remove the device from the new list to mark it as "processed"
                // * if we do not find it, we remove it from the current list.
                // Once this is done, the new list contains device we aren't monitoring yet, so we
                // add them to the list, and start monitoring them.

                for (int d = 0; d < mDevices.size(); ) {
                    Device device = mDevices.get(d);

                    // look for a similar device in the new list.
                    int count = newList.size();
                    boolean foundMatch = false;
                    for (int dd = 0; dd < count; dd++) {
                        Device newDevice = newList.get(dd);
                        // see if it matches in id and serial number.
                        if (newDevice.getSerialNumber().equals(device.getSerialNumber())) {
                            foundMatch = true;

                            // update the state if needed.
                            if (device.getState() != newDevice.getState()) {
                                device.setState(newDevice.getState());
                                device.update(Device.CHANGE_STATE);

                                // if the device just got ready/online, we need to start
                                // monitoring it.
                                if (device.isOnline()) {
                                    if (AndroidDebugBridge.getClientSupport()) {
                                        if (!startMonitoringDevice(device)) {
                                            Log.e("DeviceMonitor",
                                                    "Failed to start monitoring "
                                                            + device.getSerialNumber());
                                        }
                                    }

                                    if (device.getPropertyCount() == 0) {
                                        devicesToQuery.add(device);
                                    }
                                }
                            }

                            // remove the new device from the list since it's been used
                            newList.remove(dd);
                            break;
                        }
                    }

                    if (!foundMatch) {
                        // the device is gone, we need to remove it, and keep current index
                        // to process the next one.
                        removeDevice(device);
                        mServer.deviceDisconnected(device);
                    } else {
                        // process the next one
                        d++;
                    }
                }

                // at this point we should still have some new devices in newList, so we
                // process them.
                for (Device newDevice : newList) {
                    // add them to the list
                    mDevices.add(newDevice);
                    mServer.deviceConnected(newDevice);

                    // start monitoring them.
                    if (AndroidDebugBridge.getClientSupport()) {
                        if (newDevice.isOnline()) {
                            startMonitoringDevice(newDevice);
                        }
                    }

                    // look for their build info.
                    if (newDevice.isOnline()) {
                        devicesToQuery.add(newDevice);
                    }
                }
            }

            // query the new devices for info.
            for (Device d : devicesToQuery) {
                queryNewDeviceForInfo(d);
            }
        }
        newList.clear();
    }

    private void removeDevice(Device device) {
        device.clearClientList();
        mDevices.remove(device);

        SocketChannel channel = device.getClientMonitoringSocket();
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                // doesn't really matter if the close fails.
            }
        }
    }

    /**
     * Queries a device for its build info.
     *
     * @param device the device to query.
     */
    private void queryNewDeviceForInfo(Device device) {
        // TODO: do this in a separate thread.
        try {
            // first get the list of properties.
            device.executeShellCommand(GetPropReceiver.GETPROP_COMMAND,
                    new GetPropReceiver(device));

            queryNewDeviceForMountingPoint(device, IDevice.MNT_EXTERNAL_STORAGE);
            queryNewDeviceForMountingPoint(device, IDevice.MNT_DATA);
            queryNewDeviceForMountingPoint(device, IDevice.MNT_ROOT);

            // now get the emulator Virtual Device name (if applicable).
            if (device.isEmulator()) {
                EmulatorConsole console = EmulatorConsole.getConsole(device);
                if (console != null) {
                    device.setAvdName(console.getAvdName());
                }
            }
        } catch (TimeoutException e) {
            Log.w("DeviceMonitor", String.format("Connection timeout getting info for device %s",
                    device.getSerialNumber()));

        } catch (AdbCommandRejectedException e) {
            // This should never happen as we only do this once the device is online.
            Log.w("DeviceMonitor", String.format(
                    "Adb rejected command to get  device %1$s info: %2$s",
                    device.getSerialNumber(), e.getMessage()));

        } catch (ShellCommandUnresponsiveException e) {
            Log.w("DeviceMonitor", String.format(
                    "Adb shell command took too long returning info for device %s",
                    device.getSerialNumber()));

        } catch (IOException e) {
            Log.w("DeviceMonitor", String.format(
                    "IO Error getting info for device %s",
                    device.getSerialNumber()));
        }
    }

    private void queryNewDeviceForMountingPoint(final Device device, final String name)
            throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException,
            IOException {
        device.executeShellCommand("echo $" + name, new MultiLineReceiver() { //$NON-NLS-1$
            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public void processNewLines(String[] lines) {
                for (String line : lines) {
                    if (!line.isEmpty()) {
                        // this should be the only one.
                        device.setMountingPoint(name, line);
                    }
                }
            }
        });
    }

    /**
     * Starts a monitoring service for a device.
     *
     * @param device the device to monitor.
     * @return true if success.
     */
    private boolean startMonitoringDevice(Device device) {
        SocketChannel socketChannel = openAdbConnection();

        if (socketChannel != null) {
            try {
                boolean result = sendDeviceMonitoringRequest(socketChannel, device);
                if (result) {

                    if (mSelector == null) {
                        startDeviceMonitorThread();
                    }

                    device.setClientMonitoringSocket(socketChannel);

                    synchronized (mDevices) {
                        // always wakeup before doing the register. The synchronized block
                        // ensure that the selector won't select() before the end of this block.
                        // @see deviceClientMonitorLoop
                        mSelector.wakeup();

                        socketChannel.configureBlocking(false);
                        socketChannel.register(mSelector, SelectionKey.OP_READ, device);
                    }

                    return true;
                }
            } catch (TimeoutException e) {
                try {
                    // attempt to close the socket if needed.
                    socketChannel.close();
                } catch (IOException e1) {
                    // we can ignore that one. It may already have been closed.
                }
                logger.debug("Connection Failure when starting to monitor device '"
                        + device + "' : timeout");
            } catch (AdbCommandRejectedException e) {
                try {
                    // attempt to close the socket if needed.
                    socketChannel.close();
                } catch (IOException e1) {
                    // we can ignore that one. It may already have been closed.
                }
                logger.debug("Adb refused to start monitoring device '"
                        + device + "' : " + e.getMessage());
            } catch (IOException e) {
                try {
                    // attempt to close the socket if needed.
                    socketChannel.close();
                } catch (IOException e1) {
                    // we can ignore that one. It may already have been closed.
                }
                logger.debug("Connection Failure when starting to monitor device '"
                        + device + "' : " + e.getMessage());
            }
        }

        return false;
    }

    private void startDeviceMonitorThread() throws IOException {
        mSelector = Selector.open();
        new Thread("Device Client Monitor") { //$NON-NLS-1$
            @Override
            public void run() {
                deviceClientMonitorLoop();
            }
        }.start();
    }

    private void deviceClientMonitorLoop() {
        do {
            try {
                // This synchronized block stops us from doing the select() if a new
                // Device is being added.
                // @see startMonitoringDevice()
                synchronized (mDevices) {
                }

                int count = mSelector.select();

                if (mQuit) {
                    return;
                }

                synchronized (mClientsToReopen) {
                    if (!mClientsToReopen.isEmpty()) {
                        Set<Client> clients = mClientsToReopen.keySet();
                        MonitorThread monitorThread = MonitorThread.getInstance();

                        for (Client client : clients) {
                            Device device = client.getDeviceImpl();
                            int pid = client.getClientData().getPid();

                            monitorThread.dropClient(client, false /* notify */);

                            // This is kinda bad, but if we don't wait a bit, the client
                            // will never answer the second handshake!
                            waitABit();

                            int port = mClientsToReopen.get(client);

                            if (port == IDebugPortProvider.NO_STATIC_PORT) {
                                port = getNextDebuggerPort();
                            }
                            logger.debug("Reopening client {} with port {}", device.getSerialNumber(), port);
                            openClient(device, pid, port, monitorThread);
                            device.update(Device.CHANGE_CLIENT_LIST);
                        }

                        mClientsToReopen.clear();
                    }
                }

                if (count == 0) {
                    continue;
                }

                Set<SelectionKey> keys = mSelector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isValid() && key.isReadable()) {
                        Object attachment = key.attachment();

                        if (attachment instanceof Device) {
                            Device device = (Device) attachment;

                            SocketChannel socket = device.getClientMonitoringSocket();

                            if (socket != null) {
                                try {
                                    int length = readLength(socket, mLengthBuffer2);
                                    processIncomingJdwpData(device, socket, length);
                                } catch (IOException ioe) {
                                    logger.error("Error reading jdwp list: " + ioe.getMessage());
                                    socket.close();
                                    waitABit();
                                    waitABit();
                                    // restart the monitoring of that device
                                    synchronized (mDevices) {
                                        if (mDevices.contains(device)) {
                                            logger.debug("Restarting monitoring service for " + device);
                                            startMonitoringDevice(device);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("DeviceMonitor", "Connection error while monitoring clients.");
            }

        } while (!mQuit);
    }

    private boolean sendDeviceMonitoringRequest(SocketChannel socket, Device device)
            throws TimeoutException, AdbCommandRejectedException, IOException {

        try {
            AdbHelper.setDevice(socket, device);

            byte[] request = AdbHelper.formAdbRequest("track-jdwp"); //$NON-NLS-1$

            AdbHelper.write(socket, request);

            AdbResponse resp = AdbHelper.readAdbResponse(socket, false /* readDiagString */);

            if (!resp.okay) {
                // request was refused by adb!
                logger.error("adb refused request: {}", resp.message);
            }

            return resp.okay;
        } catch (TimeoutException e) {
            logger.error("Sending jdwp tracking request timed out!", e);
            throw e;
        } catch (IOException e) {
            logger.error("Sending jdwp tracking request failed!", e);
            throw e;
        }
    }

    private void processIncomingJdwpData(Device device, SocketChannel monitorSocket, int length)
            throws IOException {

        // This methods reads @length bytes from the @monitorSocket channel.
        // These bytes correspond to the pids of the current set of processes on the device.
        // It takes this set of pids and compares them with the existing set of clients
        // for the device. Clients that correspond to pids that are not alive anymore are
        // dropped, and new clients are created for pids that don't have a corresponding Client.

        if (length >= 0) {
            // array for the current pids.
            Set<Integer> newPids = new HashSet<Integer>();

            // get the string data if there are any
            if (length > 0) {
                byte[] buffer = new byte[length];
                String result = read(monitorSocket, buffer);

                // split each line in its own list and create an array of integer pid
                String[] pids = result.split("\n"); //$NON-NLS-1$

                for (String pid : pids) {
                    try {
                        newPids.add(Integer.valueOf(pid));
                    } catch (NumberFormatException nfe) {
                        // looks like this pid is not really a number. Lets ignore it.
                        continue;
                    }
                }
            }

            MonitorThread monitorThread = MonitorThread.getInstance();

            List<Client> clients = device.getClientList();
            Map<Integer, Client> existingClients = new HashMap<Integer, Client>();

            synchronized (clients) {
                for (Client c : clients) {
                    existingClients.put(
                            c.getClientData().getPid(),
                            c);
                }
            }

            Set<Client> clientsToRemove = new HashSet<Client>();
            for (Integer pid : existingClients.keySet()) {
                if (!newPids.contains(pid)) {
                    clientsToRemove.add(existingClients.get(pid));
                }
            }

            Set<Integer> pidsToAdd = new HashSet<Integer>(newPids);
            pidsToAdd.removeAll(existingClients.keySet());

            monitorThread.dropClients(clientsToRemove, false);

            // at this point whatever pid is left in the list needs to be converted into Clients.
            for (int newPid : pidsToAdd) {
                openClient(device, newPid, getNextDebuggerPort(), monitorThread);
            }

            if (!pidsToAdd.isEmpty() || !clientsToRemove.isEmpty()) {
                mServer.deviceChanged(device, Device.CHANGE_CLIENT_LIST);
            }
        }
    }

    /**
     * Opens and creates a new client.
     *
     * @return
     */
    private void openClient(Device device, int pid, int port, MonitorThread monitorThread) {

        SocketChannel clientSocket;
        try {
            logger.debug("Creating a pass through connection on device {} for pid {} with port {}",
                    device.getSerialNumber(),
                    pid,
                    port);

            clientSocket = AdbHelper.createPassThroughConnection(
                    AndroidDebugBridge.getSocketAddress(), device, pid);

            // required for Selector
            clientSocket.configureBlocking(false);
        } catch (UnknownHostException uhe) {
            logger.debug("Unknown Jdwp pid: " + pid, uhe);
            return;
        } catch (TimeoutException e) {
            logger.debug("Failed to connect to client '" + pid + "': timeout", e);
            return;
        } catch (AdbCommandRejectedException e) {
            logger.debug("Adb rejected connection to client '" + pid + "': ", e);
            return;

        } catch (IOException ioe) {
            logger.debug("Failed to connect to client '" + pid + "': " + ioe.getMessage(), ioe);
            return;
        }
        logger.debug("Creating client on port {}", port);
        createClient(device, pid, clientSocket, port, monitorThread);
    }


    /**
     * Creates a client and register it to the monitor thread
     *
     * @param device
     * @param pid
     * @param socket
     * @param debuggerPort  the debugger port.
     * @param monitorThread the {@link MonitorThread} object.
     */

    private void createClient(Device device, int pid, SocketChannel socket, int debuggerPort,
                              MonitorThread monitorThread) {

        /*
         * Successfully connected to something. Create a Client object, add
         * it to the list, and initiate the JDWP handshake.
         */

        Client client = new Client(device, socket, pid);
        if (client.sendHandshake()) {
            try {
                if (AndroidDebugBridge.getClientSupport()) {
                    client.listenForDebugger(debuggerPort);
                }
            } catch (IOException ioe) {
                client.getClientData().setDebuggerConnectionStatus(DebuggerStatus.ERROR);
                logger.error("Can't bind to local " + debuggerPort + " for debugger");
                // oh well
            }

            client.requestAllocationStatus();
        } else {
            logger.error("Handshake with " + client + " failed!");
            /*
             * The handshake send failed. We could remove it now, but if the
             * failure is "permanent" we'll just keep banging on it and
             * getting the same result. Keep it in the list with its "error"
             * state so we don't try to reopen it.
             */
        }

        if (client.isValid()) {
            device.addClient(client);
            monitorThread.addClient(client);
        } else {
            client = null;
        }
    }

    private int getNextDebuggerPort() {
        // get the first port and remove it
        synchronized (mDebuggerPorts) {
            if (!mDebuggerPorts.isEmpty()) {
                int port = mDebuggerPorts.get(0);

                // remove it.
                mDebuggerPorts.remove(0);

                // if there's nothing left, add the next port to the list
                if (mDebuggerPorts.isEmpty()) {
                    mDebuggerPorts.add(port + 1);
                }

                return port;
            }
        }

        return -1;
    }

    void addPortToAvailableList(int port) {
        if (port > 0) {
            synchronized (mDebuggerPorts) {
                // because there could be case where clients are closed twice, we have to make
                // sure the port number is not already in the list.
                if (mDebuggerPorts.indexOf(port) == -1) {
                    // add the port to the list while keeping it sorted. It's not like there's
                    // going to be tons of objects so we do it linearly.
                    int count = mDebuggerPorts.size();
                    for (int i = 0; i < count; i++) {
                        if (port < mDebuggerPorts.get(i)) {
                            mDebuggerPorts.add(i, port);
                            break;
                        }
                    }
                    // TODO: check if we can compact the end of the list.
                }
            }
        }
    }

    /**
     * Reads the length of the next message from a socket.
     *
     * @param socket The {@link java.nio.channels.SocketChannel} to read from.
     * @return the length, or 0 (zero) if no data is available from the socket.
     * @throws java.io.IOException if the connection failed.
     */
    private int readLength(SocketChannel socket, byte[] buffer) throws IOException {
        String msg = read(socket, buffer);

        if (msg != null) {
            try {
                return Integer.parseInt(msg, 16);
            } catch (NumberFormatException nfe) {
                // we'll throw an exception below.
            }
        }

        // we receive something we can't read. It's better to reset the connection at this point.
        throw new IOException("Unable to read length");
    }

    /**
     * Fills a buffer from a socket.
     *
     * @param socket
     * @param buffer
     * @return the content of the buffer as a string, or null if it failed to convert the buffer.
     * @throws java.io.IOException
     */
    private String read(SocketChannel socket, byte[] buffer) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(buffer, 0, buffer.length);

        while (buf.position() != buf.limit()) {
            int count;

            count = socket.read(buf);
            if (count < 0) {
                throw new IOException("EOF");
            }
        }

        try {
            return new String(buffer, 0, buf.position(), AdbHelper.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            // we'll return null below.
        }

        return null;
    }

}
