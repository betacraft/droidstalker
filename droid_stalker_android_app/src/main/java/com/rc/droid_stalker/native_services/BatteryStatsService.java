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
public final class BatteryStatsService extends NativeServiceWrapper {


    private static final String TAG = "###BatteryStatsService###";
    private Context mContext;

    public BatteryStatsService(final Context context) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        super("batterystats");
        mContext = context;
        for (Method method : mServiceStubClass.getMethods()) {
            StringBuilder params = new StringBuilder();
            for (Class type : method.getParameterTypes()) {
                params.append(type.getCanonicalName()).append(", ");
            }
            Log.d(TAG, "Battery Stats -> " + method.getName() + "(" + params + "):" + method
                    .getReturnType()
                    .getCanonicalName());
        }

       // mServiceStubClass.getMethod("publish", Context.class).invoke(mServiceObject,mContext);
       /* byte[] stats = (byte[]) mServiceStubClass.getMethod("getStatistics").invoke(mServiceObject);
        Log.d(TAG, "stats " + new String(stats));  */
    }


    @Override
    protected String getServiceClassName() {
        return "com.android.internal.app.IBatteryStats";
    }
}