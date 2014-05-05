package main.java.com.rc.droid_stalker.services;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper over NativeServiceWrapper
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/4/14
 * Time  : 8:40 PM
 */
public abstract class NativeServiceWrapper {
    private static final String TAG = "###NativeServiceWrapper###";
    private static final String SERVICE_MANAGER_NAME = "android.os.ServiceManager";
    private static final String SERVICE_MANAGER_NATIVE_NAME = "android.os.ServiceManagerNative";

    private Class mServiceManagerClass;
    private Class mServiceManagerNativeClass;

    protected Class mServiceStubClass;
    protected Object mServiceObject;

    protected Method mGetService;
    protected IBinder mServiceBinder;

    protected abstract String getServiceClassName();

    public NativeServiceWrapper(final String serviceName) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        mServiceManagerClass = Class.forName(SERVICE_MANAGER_NAME);
        mServiceManagerNativeClass = Class.forName(SERVICE_MANAGER_NATIVE_NAME);
        for (Method method : mServiceManagerNativeClass.getMethods()) {
            Log.d(TAG, "Service manager native -> " + method.getName());
        }
        mGetService =
                mServiceManagerClass.getMethod("getService", String.class);
        mServiceBinder = (IBinder) mGetService.invoke(null, serviceName);
        mServiceStubClass = Class.forName(getServiceClassName()).getClasses()[0];
        mServiceObject = mServiceStubClass.getMethod("asInterface", IBinder.class).invoke(null, mServiceBinder);
    }


}
