package com.rc.droid_stalker.components;

import com.rc.droid_stalker.thrift.DroidStalkerKernelService;
import org.apache.thrift.transport.TTransportException;

/**
 * Connection helper for socket io manager
 * Author: akshay
 * Date  : 10/9/13
 * Time  : 12:28 AM
 */
public class KernelConnection extends ComponentConnection {
    /**
     * Constructor
     *
     * @throws org.apache.thrift.transport.TTransportException
     */
    private KernelConnection() throws TTransportException {
        super(10000);
    }


    public static KernelConnection get() throws TTransportException {
        return new KernelConnection();
    }

    @Override
    public DroidStalkerKernelService.Client getClient() {
        return (DroidStalkerKernelService.Client) this.client;
    }

    @Override
    protected void prepareClient() {
        this.client = new DroidStalkerKernelService.Client(this.protocol);
    }
}
