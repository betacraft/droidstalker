package com.rc.droid_stalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.rc.droid_stalker.components.AppConnection;
import com.rc.droid_stalker.native_services.BatteryStatsService;
import com.rc.droid_stalker.native_services.NetworkStatsService;
import com.rc.droid_stalker.native_services.WindowManagerService;
import com.rc.droid_stalker.service.DroidStalkerService;
import com.rc.droid_stalker.thrift.AndroidAppStruct;
import com.rc.droid_stalker.thrift.CPUStatsStruct;
import com.rc.droid_stalker.thrift.DroidStalkerAppException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/4/14
 * Time  : 2:46 PM
 */
public class Kernel extends Activity {
    private static final String TAG = "###Kernel###";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kernel_layout);
        mHandler = new Handler();
        findViewById(R.id.start_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaseContext().startService(getDroidStalkerServiceIntent());
            }
        });
        findViewById(R.id.stop_service_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaseContext().stopService(getDroidStalkerServiceIntent());
            }
        });
        findViewById(R.id.test_get_applications_function).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGetInstalledApps();
            }
        });
        findViewById(R.id.get_network_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getNetworkStats();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.get_cpu_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGetCPUStats();
            }
        });
        findViewById(R.id.get_battery_stats_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBatterStats();
            }
        });
        getApplicationContext().startService(new Intent(getApplicationContext(), DroidStalkerService.class));
        try {
            WindowManagerService windowManagerService = new WindowManagerService();
            Log.d(TAG, "rotation " + windowManagerService.getCurrentRotation());
            BatteryStatsService serviceManager = new BatteryStatsService(getBaseContext());

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

    private void getNetworkStats() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        NetworkStatsService networkStatsService = new NetworkStatsService(getBaseContext());
    }

    private void getBatterStats() {

    }

    private void testGetCPUStats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final AppConnection appConnection = AppConnection.get();
                    final Set<CPUStatsStruct> cpuStatsStructs = appConnection.getClient().getCPUStatsFor(android.os
                            .Process
                            .myPid(), 2000);
                    try{
                    final CPUStatsStruct cpuStatsStruct = (CPUStatsStruct) cpuStatsStructs.toArray()[0];
                    if (cpuStatsStruct != null)
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), cpuStatsStruct.getPid() + " " + cpuStatsStruct
                                        .getTotalCPU() + " " + cpuStatsStruct.getPidCPU() + "" + cpuStatsStruct
                                        .getStartTimestamp() + " " + cpuStatsStruct.getEndTimestamp(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }catch (Exception ignored){

                    }
                } catch (TTransportException e) {
                    e.printStackTrace();
                } catch (DroidStalkerAppException e) {
                    e.printStackTrace();
                } catch (TException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private Intent getDroidStalkerServiceIntent() {
        return new Intent(getBaseContext(), DroidStalkerService.class);
    }

    private void testGetInstalledApps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AppConnection appConnection = AppConnection.get();
                    final StringBuffer installedAppsToast = new StringBuffer();
                    for (final AndroidAppStruct installedAndroidApp : appConnection.getClient().getInstalledApps()) {
                        installedAppsToast.append(installedAndroidApp.getApplicationName()).append("\n");
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), installedAppsToast.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (TTransportException e) {
                    e.printStackTrace();
                } catch (DroidStalkerAppException e) {
                    e.printStackTrace();
                } catch (TException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
