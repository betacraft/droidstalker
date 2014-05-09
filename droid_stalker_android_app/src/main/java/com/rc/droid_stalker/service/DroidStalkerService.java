package com.rc.droid_stalker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    private int mServerPort;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mInitialized.getAndSet(true))
            return START_STICKY;
        Log.d(TAG, "Starting service");
        mContext = getBaseContext();
        startServer(intent.getIntExtra(SERVICE_PORT, 11000));
        return START_STICKY;
    }

    private void startServer(final int port) {

    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
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
