package com.forward.januswebrtc.util;

import com.forward.januswebrtc.domain.Request;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadExecutorUtil {
    public static volatile Executor pool;
    private static AtomicLong threadId;
    private static final int CORE_SIZE = 20;
    private static int limit_size = 100;

    static
    {
        try
        {
            limit_size = Integer.parseInt(System.getProperty("pool.limit", "100"));
        } catch (Exception e)
        {
        }
        if (limit_size < CORE_SIZE)
            limit_size = CORE_SIZE;

        threadId = new AtomicLong();
        pool = new ThreadPoolExecutor(CORE_SIZE, limit_size, 120, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(200), new ThreadFactory()
        {

            @Override
            public Thread newThread(Runnable r)
            {
                Thread t = new Thread(r, "JanusClientPool-thread-" + threadId.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        });
    }
}
