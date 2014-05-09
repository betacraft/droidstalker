package com.rc.droid_stalker.sessions;

import com.android.ddmlib.*;
import com.rc.droid_stalker.models.ThriftStructHelpers;
import com.rc.droid_stalker.thrift.AndroidAppStruct;
import com.rc.droid_stalker.thrift.DroidStalkerKernelException;
import com.rc.droid_stalker.thrift.KernelExceptionErrorCode;
import com.rc.droid_stalker.thrift.ThreadInfoStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Currently running debug session
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/7/14
 * Time  : 1:10 PM
 */
public final class DebugSession {
    private static final Logger logger = LoggerFactory.getLogger(DebugSession.class);
    private AndroidAppStruct mAndroidApp;
    private IDevice mDevice;
    private Client mClient;
    private String mSessionId;
    private AndroidDebugBridge mDebugBridge;
    private static final String START_APP_COMMAND_FORMAT = "am start -n %s/%s";
    private AndroidDebugBridge.IClientChangeListener mSessionClientDetector;

    private DebugSession(final AndroidDebugBridge debugBridge, final IDevice device, final AndroidAppStruct androidApp)
            throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException, DroidStalkerKernelException {
        mDebugBridge = debugBridge;
        mAndroidApp = androidApp;
        mDevice = device;
        mSessionId = UUID.randomUUID().toString();
        final CountDownLatch commandExecutionLatch = new CountDownLatch(1);
        mSessionClientDetector = new AndroidDebugBridge.IClientChangeListener() {
            @Override
            public void clientChanged(Client client, int changeMask) {
                if (client.getClientData().getClientDescription() == null)
                    return;
                logger.debug("Client changed with description {}", client.getClientData().getClientDescription());
                if (client.getClientData().getClientDescription().equals(mAndroidApp.getPackageName())) {
                    AndroidDebugBridge.removeClientChangeListener(mSessionClientDetector);
                    mClient = client;
                    mClient.setAsSelectedClient();
                    commandExecutionLatch.countDown();
                }
            }
        };
        AndroidDebugBridge.addClientChangeListener(mSessionClientDetector);
        mDevice.executeShellCommand(String.format(START_APP_COMMAND_FORMAT, mAndroidApp.getPackageName(),
                mAndroidApp.getActivityName()), new IShellOutputReceiver() {
            @Override
            public void addOutput(byte[] data, int offset, int length) {
                logger.debug(new String(data));
            }

            @Override
            public void flush() {

            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
        try {
            commandExecutionLatch.await();
        } catch (InterruptedException ignored) {
        }
        if (mClient == null)
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.APP_COULD_NOT_START,
                    "Failed to get hold of the client");

    }

    public String getSessionId() {
        return mSessionId;
    }

    public Set<ThreadInfoStruct> getAllThreads() {
        Set<ThreadInfoStruct> runningThreads = new LinkedHashSet<ThreadInfoStruct>();
        for (final ThreadInfo threadInfo : mClient.getClientData().getThreads()) {
            runningThreads.add(ThriftStructHelpers.prepareThreadInfoStruct(threadInfo));
        }
        return runningThreads;
    }

    public static DebugSession startFor(final AndroidDebugBridge debugBridge, final IDevice device,
                                        final AndroidAppStruct androidApp) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException, DroidStalkerKernelException {
        return new DebugSession(debugBridge, device, androidApp);
    }

    public void closeSession() {

    }

}
