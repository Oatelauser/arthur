package com.arthur.lang.concurrent;

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
	private boolean daemon;
	private final String threadNamePrefix;
	private Thread.UncaughtExceptionHandler exceptionHandler;
    private final AtomicLong sequence = new AtomicLong(0);

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
	@SuppressWarnings("all")
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
		thread.setDaemon(daemon);
        thread.setName(threadNamePrefix + sequence.getAndIncrement());
        thread.setUncaughtExceptionHandler(exceptionHandler);
        return thread;
    }

	public static class Builder {

		private boolean daemon = false;
		private String prefixName;
		private Thread.UncaughtExceptionHandler handler;

		public Builder daemon(boolean daemon) {
			this.daemon = daemon;
			return this;
		}

		public Builder prefixName(String prefixName) {
			this.prefixName = prefixName;
			return this;
		}

		public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
			this.handler = handler;
			return this;
		}

		public NamedThreadFactory build() {
			return new NamedThreadFactory(prefixName, daemon, handler);
		}

	}

}
