package com.rc.droid_stalker.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.rc.droid_stalker.thrift.AndroidAppStruct;
import com.rc.droid_stalker.thrift.DroidStalkerAppException;
import com.rc.droid_stalker.thrift.DroidStalkerAppService;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/9/14
 * Time  : 11:43 PM
 */
public final class DroidStalkerAppServiceHandler implements DroidStalkerAppService.Iface {
    private final static String TAG = "###DroidStalkerAppServiceHandler###";
    private final Context mContext;


    public DroidStalkerAppServiceHandler(final Context context) {
        mContext = context;
    }

    @Override
    public Set<AndroidAppStruct> getInstalledApps() throws DroidStalkerAppException {
        final PackageManager pm = mContext.getPackageManager();
        final List<PackageInfo> installedAppPackageInformationList = pm.getInstalledPackages
                (PackageManager
                        .GET_META_DATA);
        final Set<AndroidAppStruct> installedAppStructs = new LinkedHashSet<AndroidAppStruct>();
        for (final PackageInfo installedAppPackageInfo : installedAppPackageInformationList) {
            Log.d(TAG, pm.getLaunchIntentForPackage(installedAppPackageInfo.packageName).getAction());
            final Bitmap icon = ((BitmapDrawable) pm.getApplicationIcon
                    (installedAppPackageInfo
                            .applicationInfo)).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            installedAppStructs.add(new AndroidAppStruct(installedAppPackageInfo.packageName, "",
                    installedAppPackageInfo.applicationInfo.name, new String(byteArray)));
        }
        return installedAppStructs;
    }
}
