package com.rc.droid_stalker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.rc.droid_stalker.thrift.DroidStalkerAppService;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.HashedWheelTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Droid stalker service
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/9/14
 * Time  : 7:08 PM
 */
public class DroidStalkerService extends Service {

    private static final String TAG = "###DroidStalkerService###";
    private static final String SERVICE_PORT = "service_port";
    private Context mContext;
    private AtomicBoolean mInitialized = new AtomicBoolean(false);
    private ScheduledExecutorService mStalkerExecutor = Executors.newScheduledThreadPool(4);
    private Thread mServerThread;
    private int mServerPort;
    private Handler mHandler;
    private CountDownLatch mDroidStalkerAppServiceLatch;
    private NettyServerTransport mDroidStalkerThriftServer;
    private ExecutorService mBossExecutor;
    private ExecutorService mWorkerExecutor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mInitialized.getAndSet(true))
            return START_STICKY;
        Log.d(TAG, "Starting service");
        mContext = getBaseContext();
        mHandler = new Handler();
        mServerPort = intent.getIntExtra(SERVICE_PORT, 11000);
        mServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startServer(mServerPort);
            }
        });
        mServerThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        Toast.makeText(mContext, "Service stopper", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    private void startServer(final int port) {
        mDroidStalkerAppServiceLatch = new CountDownLatch(1);
        DroidStalkerAppService.Processor<DroidStalkerAppServiceHandler>
                processor = new DroidStalkerAppService.Processor<DroidStalkerAppServiceHandler>
                (new DroidStalkerAppServiceHandler(mContext));
        ThriftServerDef serverDef = null;
        // Thirft eventManagerThriftServer definition builder
        serverDef = new ThriftServerDefBuilder().withProcessor(processor)
                .listen(port)
                .build();
        // Create session boss and executor thread pools
        mBossExecutor = Executors.newFixedThreadPool(5);
        mWorkerExecutor = Executors.newFixedThreadPool(5);
        //bootstrap options
        Map<String, Object> bootstrapOptions = new HashMap<String, Object>();
        bootstrapOptions.put("reuseAddress", true);
        NettyServerConfig nettyServerConfig = new NettyServerConfig(bootstrapOptions,
                new HashedWheelTimer(),
                mBossExecutor, 1, mWorkerExecutor, 1);
        // Create the session transport
        mDroidStalkerThriftServer = new NettyServerTransport(serverDef,
                nettyServerConfig,
                new DefaultChannelGroup());

        Log.d(TAG, "session starting");
        // start session
        mDroidStalkerThriftServer.start();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "Thrift server started", Toast.LENGTH_LONG).show();
            }
        });
        // Arrange to stop the session at shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    mDroidStalkerThriftServer.stop();
                    mDroidStalkerAppServiceLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        try {
            mDroidStalkerAppServiceLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void stopServer() {
        try {
            mDroidStalkerThriftServer.stop();
        } catch (InterruptedException ignored) {
        }
        mBossExecutor.shutdownNow();
        mWorkerExecutor.shutdownNow();
        mDroidStalkerAppServiceLatch.countDown();
        mServerThread.interrupt();
    }


    private static final class ApplicationListStalker implements Runnable {

        private static final String TAG = "###ApplicationListStalker###";
        private Context mContext;
        private ConcurrentLinkedDeque<String> stalkList;

        @Override
        public void run() {

        }
    }
}
