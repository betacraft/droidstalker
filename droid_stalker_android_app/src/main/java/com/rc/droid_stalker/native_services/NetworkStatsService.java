package com.rc.droid_stalker.native_services;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.rc.droid_stalker.components.NetworkStats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
        Parcel statsParcel = Parcel.obtain();
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : services) {
            Log.d(TAG, "Process name -" + services.get(0).processName);
            ((Parcelable) mServiceStubClass.getMethod("getDataLayerSnapshotForUid",
                    int.class).invoke(mServiceObject,
                    android.os.Process.getUidForName(services.get(0).processName))).writeToParcel(statsParcel, 0);
            NetworkStats networkStats = new NetworkStats(statsParcel);
            Log.d(TAG, "" + networkStats.getTotalBytes());

        }


    }


    @Override
    protected String getServiceClassName() {
        return "android.net.INetworkStatsService";
    }
}