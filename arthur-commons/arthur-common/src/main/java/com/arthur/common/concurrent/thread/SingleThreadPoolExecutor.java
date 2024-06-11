package com.arthur.common.concurrent.thread;

import java.util.concurrent.*;

/**
 * 单线程线程池
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class SingleThreadPoolExecutor extends ThreadPoolExecutor {

    public SingleThreadPoolExecutor(BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory) {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory);
    }

    public SingleThreadPoolExecutor(final ThreadFactory threadFactory) {
        this(Integer.MAX_VALUE, threadFactory);
    }

    public SingleThreadPoolExecutor(final int queueMaxSize, final ThreadFactory threadFactory) {
        super(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueMaxSize), threadFactory);
    }

}
