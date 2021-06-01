package com.pyjava.daily.thread;

import java.util.concurrent.*;

/**
 * <p>
 *     线程池
 * </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/3 14:15
 */
public class NoteFxThreadPool {

    /**
     * 工作线程池
     */
    private static final ExecutorService THREAD_POOL;

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
    }


    /**
     * <p>
     *     获取工作线程池
     * </p>
     *
     * @return java.util.concurrent.ExecutorService
     * @author zhaojj11
     * @date 2021/5/8 0:58
     * @since 1.0
     */
    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
    }
}
