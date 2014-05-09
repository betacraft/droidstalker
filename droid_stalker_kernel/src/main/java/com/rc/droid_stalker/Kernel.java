package com.rc.droid_stalker;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.rc.droid_stalker.thread_factory.EfficientThreadPoolExecutor;
import com.rc.droid_stalker.thrift.DroidStalkerKernelService;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Kernel of the DroidStalker
 * Author: akshay deo (akshay@rainingclouds.com)
 * Date  : 4/23/14
 * Time  : 10:05 PM
 */
public final class Kernel {
    private static final Logger logger = LoggerFactory.getLogger(Kernel.class);


    public static void main(final String[] args) {
        final CountDownLatch kernelServiceLatch = new CountDownLatch(1);
        DroidStalkerKernelService.Processor<KernelServiceHandler>
                processor = new DroidStalkerKernelService.Processor<KernelServiceHandler>(new KernelServiceHandler());
        ThriftServerDef serverDef = null;
        // Thirft eventManagerThriftServer definition builder
        serverDef = new ThriftServerDefBuilder().withProcessor(processor)
                .listen(10000)
                .build();
        // Create session boss and executor thread pools
        ExecutorService bossExecutor = EfficientThreadPoolExecutor.get(3, 15, 1,
                TimeUnit.MINUTES, 5,
                "droid_stalker_boss");
        ExecutorService workerExecutor = EfficientThreadPoolExecutor.get(5, 15, 1,
                TimeUnit.MINUTES, 5,
                "droid_stalker_worker");
        //bootstrap options
        Map<String, Object> bootstrapOptions = new HashMap<String, Object>();
        bootstrapOptions.put("reuseAddress", true);
        NettyServerConfig nettyServerConfig = new NettyServerConfig(bootstrapOptions,
                new HashedWheelTimer(),
                bossExecutor, 1, workerExecutor, 1);
        // Create the session transport
        final NettyServerTransport eventManagerThriftServer = new NettyServerTransport(serverDef,
                nettyServerConfig,
                new DefaultChannelGroup());

        logger.debug("session starting");
        // start session
        eventManagerThriftServer.start();

        // Arrange to stop the session at shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    eventManagerThriftServer.stop();
                    kernelServiceLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        try {
            kernelServiceLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
