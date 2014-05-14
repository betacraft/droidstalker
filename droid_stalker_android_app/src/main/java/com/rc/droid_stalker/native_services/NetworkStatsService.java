package com.rc.droid_stalker.native_services;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Power manager class
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/5/14
 * Time  : 7:56 PM
 */
public final class NetworkStatsService extends NativeServiceWrapper {


    private static final String TAG = "###NetworkStatsService###";
    private Context mContext;

    public NetworkStatsService(final Context context) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        super("netstats");
        mContext = context;
        for (Method method : mServiceStubClass.getMethods()) {
            StringBuilder params = new StringBuilder();
            for (Class type : method.getParameterTypes()) {
                params.append(type.getCanonicalName()).append(", ");
            }
            Log.d(TAG, "Network Stats -> " + method.getName() + "(" + params + "):" + method
                    .getReturnType()
                    .getCanonicalName());
        }

    }


    @Override
    protected String getServiceClassName() {
        return "android.net.INetworkStatsService";
    }
}