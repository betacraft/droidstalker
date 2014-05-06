package com.rc.droid_stalker.wrappers;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;

/**
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 5/6/14
 * Time  : 7:25 PM
 */
public final class Device implements AndroidDebugBridge.IClientChangeListener{



    @Override
    public void clientChanged(Client client, int changeMask) {

    }
}
