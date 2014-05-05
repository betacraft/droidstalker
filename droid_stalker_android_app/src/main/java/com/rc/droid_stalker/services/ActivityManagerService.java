package main.java.com.rc.droid_stalker.services;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Power manager class
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/5/14
 * Time  : 7:56 PM
 */
public final class ActivityManagerService extends NativeServiceWrapper {
    private static final String TAG = "###ActivityManagerService###";


    public ActivityManagerService() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        super("batterystats");
        for (Method method : mServiceStubClass.getMethods()) {
            StringBuilder params = new StringBuilder();
            for (Class type : method.getParameterTypes()) {
                params.append(type.getCanonicalName()).append(", ");
            }
            Log.d(TAG, "Battery Stats -> " + method.getName() + "(" + params + "):" + method
                    .getReturnType()
                    .getCanonicalName());
        }

    }


    @Override
    protected String getServiceClassName() {
        return "com.android.internal.app.IBatteryStats";
    }
}
