package com.rc.droid_stalker.view_hierarychy_tracer.device;


import com.android.ddmlib.IDevice;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * This class is used for connecting to a device in debug mode running the view
 * server.
 */
public class DeviceConnection {
    private static final Logger logger = LoggerFactory.getLogger(DeviceConnection.class);
    // Now a socket channel, since socket channels are friendly with interrupts.
    private SocketChannel mSocketChannel;

    private BufferedReader mIn;

    private BufferedWriter mOut;

    public DeviceConnection(IDevice device) throws IOException {
        mSocketChannel = SocketChannel.open();
        int port = DeviceBridge.getDeviceLocalPort(device);

        if (port == -1) {
            throw new IOException();
        }
        //logger.debug("Creating socket forward with port {}", port);
        mSocketChannel.connect(new InetSocketAddress("127.0.0.1", port)); //$NON-NLS-1$
        mSocketChannel.socket().setSoTimeout(40000);
        //logger.debug("Socket channel is connected : {}", mSocketChannel.isConnected());
    }

    public BufferedReader getInputStream() throws IOException {
        if (mIn == null) {
            mIn = new BufferedReader(new InputStreamReader(
                    mSocketChannel.socket().getInputStream(), Charsets.UTF_8));
        }
        return mIn;
    }

    public BufferedWriter getOutputStream() throws IOException {
        if (mOut == null) {
            mOut = new BufferedWriter(new OutputStreamWriter(
                    mSocketChannel.socket().getOutputStream(), Charsets.UTF_8));
        }
        return mOut;
    }

    public Socket getSocket() {
        return mSocketChannel.socket();
    }

    public void sendCommand(String command) throws IOException {
        BufferedWriter out = getOutputStream();
        out.write(command);
        out.newLine();
        out.flush();
    }

    public void close() {
        try {
            if (mIn != null) {
                mIn.close();
            }
        } catch (IOException e) {
        }
        try {
            if (mOut != null) {
                mOut.close();
            }
        } catch (IOException e) {
        }
        try {
            mSocketChannel.close();
        } catch (IOException e) {
        }
    }
}
