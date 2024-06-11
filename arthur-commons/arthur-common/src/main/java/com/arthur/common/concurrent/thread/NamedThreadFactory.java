package com.arthur.common.concurrent.thread;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程创建工厂
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final String LINE = "-";
    private final AtomicLong sequence = new AtomicLong(0);
    private final String threadNamePrefix;
    private boolean daemon;

    private Thread.UncaughtExceptionHandler exceptionHandler;

    public NamedThreadFactory(String prefix, boolean daemon, Thread.UncaughtExceptionHandler exceptionHandler) {
        this(prefix);
        this.daemon = daemon;
        this.exceptionHandler = exceptionHandler;
    }

    public NamedThreadFactory(String prefix, Thread.UncaughtExceptionHandler exceptionHandler) {
        this(prefix);
        this.exceptionHandler = exceptionHandler;
    }

    public NamedThreadFactory(String prefix) {
        if (!prefix.endsWith(LINE)) {
            prefix = prefix + LINE;
        }

        this.threadNamePrefix = prefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadNamePrefix + sequence.getAndIncrement());
        thread.setDaemon(daemon);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        return thread;
    }

}
