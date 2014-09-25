package com.rc.droid_stalker.components;

import com.rc.droid_stalker.thrift.DroidStalkerAppService;
import org.apache.thrift.transport.TTransportException;

/**
 * Connection helper for socket io manager
 * Author: akshay
 * Date  : 10/9/13
 * Time  : 12:28 AM
 */
public class AppConnection extends ComponentConnection {
    /**
     * Constructor
     *
     * @throws org.apache.thrift.transport.TTransportException
     */
    private AppConnection() throws TTransportException {
        super(11000);
    }


    public static AppConnection get() throws TTransportException {
        return new AppConnection();
    }

    @Override
    public DroidStalkerAppService.Client getClient() {
        return (DroidStalkerAppService.Client) this.client;
    }

    @Override
    protected void prepareClient() {
        this.client = new DroidStalkerAppService.Client(this.protocol);
    }
}
