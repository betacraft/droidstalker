package com.rc.droid_stalker_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kernel of the backend
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/23/14
 * Time  : 10:05 PM
 */
public final class Kernel {
    private static final Logger logger = LoggerFactory.getLogger(Kernel.class);


    public static void main(final String[] args) {

        new Thread(new DroidStalkerRoutine()).start();

    }
}
