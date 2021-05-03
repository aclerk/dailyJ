package com.pyjava.notefx.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>描述: [功能描述] </p>
 *
 * @author zhaojj11
 * @version v1.0
 * @date 2021/5/2 21:51
 */
public class CommonThreadFactory implements ThreadFactory {
    /**
     * 线程池线程个数
     */
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    /**
     *
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 线程名称前缀
     */
    private final String namePrefix;

    /**
     * 线程名称
     */
    private String name;

    public CommonThreadFactory() {
        this.namePrefix = "notefx-pool-" + POOL_NUMBER.getAndIncrement() + "-thread";
    }

    public CommonThreadFactory(String name) {
        this();
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r,
                (this.name == null || "".equals(this.name)) ? this.namePrefix + threadNumber.getAndIncrement() :
                        this.name + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
