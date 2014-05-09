package com.rc.droid_stalker.components;


import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that maintains persistent component connection
 * Author: akshay
 * Date  : 9/17/13
 * Time  : 7:37 PM
 */
public abstract class ComponentConnection {
    /**
     * Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(ComponentConnection.class);
    /**
     * Transport
     */
    private TTransport transport;
    /**
     * Protocol for the connection
     */
    protected TProtocol protocol;
    /**
     * Service client
     */
    protected TServiceClient client;
    /**
     * Maximum retries
     */
    private static final int MAX_RETRIES = 3;


    /**
     * Constructor
     *
     * @param port
     * @throws org.apache.thrift.transport.TTransportException
     */
    protected ComponentConnection(final int port) throws TTransportException {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                tryForConnection(port);
                return;
            } catch (TTransportException e) {
                logger.error("Error while connecting to a component", e);
                if (retries == MAX_RETRIES - 1) {
                    throw e;
                }
            }
            ++retries;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
        throw new TTransportException("Could not connect to a component");
    }


    private void tryForConnection(final int port) throws TTransportException {
        this.transport = new TSocket("localhost", port);
        this.transport.open();
        this.protocol = new TBinaryProtocol(this.transport);
        prepareClient();
    }

    public void close() {
        this.transport.close();
    }

    public abstract TServiceClient getClient();

    protected abstract void prepareClient();
}
