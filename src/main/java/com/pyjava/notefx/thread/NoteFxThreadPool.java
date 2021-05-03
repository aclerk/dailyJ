package com.pyjava.notefx.thread;

import java.util.concurrent.*;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/3 14:15
 */
public class NoteFxThreadPool {

    private static final ExecutorService THREAD_POOL;
    private static final ExecutorService FILE_MONITOR_POOL;

    static {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 5;
        TimeUnit keepAliveTimeUnit = TimeUnit.MINUTES;
        int queSize = 100_000;
        ThreadFactory threadFactory = new CommonThreadFactory();

        THREAD_POOL = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, keepAliveTimeUnit, new ArrayBlockingQueue<>(queSize)
                , threadFactory);

        FILE_MONITOR_POOL = Executors.newSingleThreadExecutor(r -> new Thread(r, "file-monitor-thread"));
    }


    /**
     * 获取线程池
     * @return 线程池
     */
    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
    }

    public static ExecutorService getFileMonitorPool() {
        return FILE_MONITOR_POOL;
    }

}
