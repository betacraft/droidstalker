package com.rc.droid_stalker.native_services;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper over system window manager service
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/4/14
 * Time  : 8:35 PM
 */
public final class WindowManagerService extends NativeServiceWrapper {
    private static final String TAG = "###WindowManagerService###";
    private static final String CLASS_NAME = "android.view.IWindowManager";


    public WindowManagerService() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        super("window");
        for (Method method : mServiceStubClass.getMethods()) {
            StringBuilder params = new StringBuilder();
            for (Class type : method.getParameterTypes()) {
                params.append(type.getCanonicalName()).append(", ");
            }
            Log.d(TAG, "Window manager -> " + method.getName() + "(" + params + "):" + method
                    .getReturnType()
                    .getCanonicalName());
        }
    }

    @Override
    protected String getServiceClassName() {
        return CLASS_NAME;
    }

    public int getCurrentRotation() {
        try {
            return (Integer) mServiceStubClass.getMethod("getRotation").invoke(mServiceObject);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
