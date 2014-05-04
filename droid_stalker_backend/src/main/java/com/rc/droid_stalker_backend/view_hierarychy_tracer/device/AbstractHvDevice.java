package com.rc.droid_stalker_backend.view_hierarychy_tracer.device;


import com.android.ddmlib.IDevice;
import com.rc.droid_stalker_backend.view_hierarychy_tracer.models.IHvDevice;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractHvDevice implements IHvDevice {
    private static final String TAG = "HierarchyViewer";

    @Override
    public Image getScreenshotImage() {
        IDevice device = getDevice();
        final AtomicReference<Image> imageRef = new AtomicReference<Image>();

        return null;
    }
}

