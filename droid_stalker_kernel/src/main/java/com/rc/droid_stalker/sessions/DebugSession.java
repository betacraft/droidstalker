package com.rc.droid_stalker.sessions;

import com.android.ddmlib.*;
import com.rc.droid_stalker.components.AppConnection;
import com.rc.droid_stalker.models.ThriftStructHelpers;
import com.rc.droid_stalker.thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
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
    public static final String START_DROID_STALKER_SERVICE = "am startservice com.rc.droid_stalker/" +
            ".service.DroidStalkerService";
    private static final String STOP_DROID_STALKER_SERVICE = "am force-stop com.rc.droid_stalker/" +
            ".service.DroidStalkerService";
    private static final String START_APP_COMMAND_FORMAT = "am start -n %s/%s";
    private static final String FORCE_STOP_APP_COMMAND_FORMAT = "am force-stop %s";
    private AndroidDebugBridge.IClientChangeListener mSessionClientDetector;
    private AppConnection mAppConnection;


    private DebugSession(final IDevice device, final AndroidAppStruct androidApp)
            throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException, DroidStalkerKernelException, TTransportException {
        mAndroidApp = androidApp;
        mDevice = device;
        mSessionId = UUID.randomUUID().toString();
        final CountDownLatch commandExecutionLatch = new CountDownLatch(1);
        mDevice.executeShellCommand(String.format(FORCE_STOP_APP_COMMAND_FORMAT, mAndroidApp.getPackageName()),
                new IShellOutputReceiver() {
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
        try {
            //ProcessKiller.killProcessRunningOnPort(11000);
            AdbHelper.createForward(AndroidDebugBridge.getSocketAddress(), device, "tcp:11000", "tcp:11000");
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAppConnection = AppConnection.get();
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

    public Set<AndroidAppStruct> getInstalledApplications() throws DroidStalkerKernelException {
        try {
            return mAppConnection.getClient().getInstalledApps();
        } catch (TException e) {
            logger.error("Error while getting installed applications",e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.APP_NOT_FOUND,
                    "Error message: " +e.getMessage());
        }
    }

    public CPUStatsStruct getCPUStats(final int span) throws DroidStalkerKernelException {
        logger.debug("Getting CPU stats for {} span {}",mClient.getClientData().getPid(),span);
        try {
            return mAppConnection.getClient().getCPUStatsFor(mClient.getClientData().getPid(), span);
        } catch (TException e) {
            logger.error("Error while getting CPU stats", e);
            throw new DroidStalkerKernelException(KernelExceptionErrorCode.KERNEL_CRASHED,
                    "Error message: "+e.getMessage());
        }
    }

    public static DebugSession startFor(final IDevice device,
                                        final AndroidAppStruct androidApp) throws TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException, DroidStalkerKernelException, TTransportException {
        return new DebugSession(device, androidApp);
    }


    public void closeSession() {

    }

}
