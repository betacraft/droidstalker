package com.rc.droid_stalker.view_hierarychy_tracer.device;


import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;
import com.rc.droid_stalker.view_hierarychy_tracer.HierarchyViewerDirector;
import com.rc.droid_stalker.view_hierarychy_tracer.models.IHvDevice;


public class HvDeviceFactory {
    public static IHvDevice create(IDevice device) {
        // default to old mechanism until the new one is fully tested
        if (!HierarchyViewerDirector.isUsingDdmProtocol()) {
            return new ViewServerDevice(device);
        }

        // Wait for a few seconds after the device has been connected to
        // allow all the clients to be initialized. Specifically, we need to wait
        // until the client data is filled with the list of features supported
        // by the client.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore
        }

        boolean ddmViewHierarchy = false;

        // see if any of the clients on the device support view hierarchy via DDMS
        for (Client c : device.getClients()) {
            ClientData cd = c.getClientData();
            if (cd != null && cd.hasFeature(ClientData.FEATURE_VIEW_HIERARCHY)) {
                ddmViewHierarchy = true;
                break;
            }
        }

        return ddmViewHierarchy ? new DdmViewDebugDevice(device) : new ViewServerDevice(device);
    }
}
