package com.rc.droid_stalker.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.rc.droid_stalker.components.AndroidOSUtils;
import com.rc.droid_stalker.components.CPUStats;
import com.rc.droid_stalker.thrift.*;
import com.rc.droid_stalker.workers.ScheduledWorker;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * CPU stats hub
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/9/14
 * Time  : 11:43 PM
 */
public final class DroidStalkerAppServiceHandler implements DroidStalkerAppService.Iface {
    private final static String TAG = "###DroidStalkerAppServiceHandler###";
    private static final int CPU_CALCULATION_SPAN = 1;
    private final Context mContext;
    private final CPUStats mCPUStats;
    private ScheduledWorker mScheduledWorkers;
    private int mCurrentSessionPid;

    {
        mCPUStats = new CPUStats();
        mScheduledWorkers = new ScheduledWorker();
    }

    public DroidStalkerAppServiceHandler(final Context context) {
        mContext = context;
        mScheduledWorkers.submit(new Runnable() {
            @Override
            public void run() {
                final CPUStatsStruct cpuStatsStruct = new CPUStatsStruct();
                cpuStatsStruct.setPid(mCurrentSessionPid);
                final AndroidOSUtils androidOSUtils = new AndroidOSUtils();
                String cpuStat1 = androidOSUtils.readSystemStat();
                String pidStat1 = androidOSUtils.readProcessStat(mCurrentSessionPid);

                try {
                    Thread.sleep(CPU_CALCULATION_SPAN);
                } catch (Exception ignored) {
                }

                String cpuStat2 = androidOSUtils.readSystemStat();
                String pidStat2 = androidOSUtils.readProcessStat(mCurrentSessionPid);

                float cpu = androidOSUtils
                        .getSystemCpuUsage(cpuStat1, cpuStat2);

                if (cpu >= 0.0f) {
                    cpuStatsStruct.setTotalCPU(cpu);
                } else {
                    Log.e(TAG, "Error while getting total cpu");
                }

                String[] toks = cpuStat1.split(" ");
                long cpu1 = androidOSUtils.getSystemUptime(toks);

                toks = cpuStat2.split(" ");
                long cpu2 = androidOSUtils.getSystemUptime(toks);

                cpu = androidOSUtils.getProcessCpuUsage(pidStat1, pidStat2,
                        cpu2 - cpu1);
                if (cpu >= 0.0f) {
                    cpuStatsStruct.setPidCPU(cpu);
                } else {
                    Log.e(TAG, "Error while getting total cpu");
                }
                mCPUStats.push(cpuStatsStruct);
            }
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public Set<AndroidAppStruct> getInstalledApps() throws DroidStalkerAppException {
        final PackageManager pm = mContext.getPackageManager();
        final List<PackageInfo> installedAppPackageInformationList = pm.getInstalledPackages
                (PackageManager.GET_ACTIVITIES);
        final Set<AndroidAppStruct> installedAppStructSet = new LinkedHashSet<AndroidAppStruct>();
        for (final PackageInfo installedAppPackageInfo : installedAppPackageInformationList) {
            if (pm.getLaunchIntentForPackage(installedAppPackageInfo.packageName) == null)
                continue;
            if ((installedAppPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                continue;
            final Bitmap icon = ((BitmapDrawable) pm.getApplicationIcon
                    (installedAppPackageInfo
                            .applicationInfo)).getBitmap();
            if (icon == null)
                continue;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            installedAppStructSet.add(new AndroidAppStruct(installedAppPackageInfo.packageName,
                    pm.getLaunchIntentForPackage(installedAppPackageInfo.packageName).getComponent().getClassName(),
                    pm.getApplicationLabel(installedAppPackageInfo.applicationInfo).toString(),
                    new String(byteArray)));
        }
        return installedAppStructSet;
    }



    @Override
    public Set<CPUStatsStruct> getCPUStatsFor(final int pid, final int lastPacketId) throws DroidStalkerAppException {
        return mCPUStats.getAllPacketsAfterId(lastPacketId);
    }
}
