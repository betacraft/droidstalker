package main.java.com.rc.droid_stalker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import main.java.com.rc.droid_stalker.services.BatteryStatsService;
import main.java.com.rc.droid_stalker.services.WindowManagerService;

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
