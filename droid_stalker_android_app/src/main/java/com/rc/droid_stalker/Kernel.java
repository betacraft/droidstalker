package com.rc.droid_stalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.rc.droid_stalker.native_services.BatteryStatsService;
import com.rc.droid_stalker.native_services.WindowManagerService;
import com.rc.droid_stalker.service.DroidStalkerService;

import java.lang.reflect.InvocationTargetException;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/4/14
 * Time  : 2:46 PM
 */
public class Kernel extends Activity {
    private static final String TAG = "###Kernel###";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().startService(new Intent(getApplicationContext(), DroidStalkerService.class));
        try {
            WindowManagerService windowManagerService = new WindowManagerService();
            Log.d(TAG, "rotation " + windowManagerService.getCurrentRotation());
            BatteryStatsService serviceManager = new BatteryStatsService();

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "", e);
        } catch (NoSuchMethodException e) {

            Log.e(TAG, "", e);
        } catch (InvocationTargetException e) {

            Log.e(TAG, "", e);
        } catch (IllegalAccessException e) {

            Log.e(TAG, "", e);
        }
    }
}
