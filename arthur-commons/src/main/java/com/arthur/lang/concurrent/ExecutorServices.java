package com.arthur.lang.concurrent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 线程池工具类
 * <p>
 * 提供功能：1.线程池；2.fork/join线程池；3.线程池关闭
 *
 * @author DearYang
 * @date 2022-04-27
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ExecutorServices {

    private static final Logger log = LoggerFactory.getLogger(ExecutorServices.class);

    private static final int MAXIMUM_FORK_JOIN_CAPACITY = 1024;
    private static final int DEFAULT_SHUTDOWN_TIMEOUT = 10;
    private static final List<WeakReference<ExecutorService>> ALL_THREAD_EXECUTORS = new CopyOnWriteArrayList<>();

    public static ExecutorService createNormalThreadPool(int size, String threadNamePrefix) {
        ThreadFactory threadFactory = new NamedThreadFactory(threadNamePrefix, new ThreadUnCaughtExceptionHandler());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(50240), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        return buildExecutorService(threadPoolExecutor);
    }

    /**
     * 创建{@link ForkJoinTask} 的线程池，体检并行计算任务
     *
     * @param size             一定是2的n次方，并且最大1024
     * @param threadNamePrefix 线程名
     * @return fork/join线程池
     */
    public static ForkJoinPool createForkJoinTaskThreadPool(int size, String threadNamePrefix) {
        size = tableSizeFor(size, MAXIMUM_FORK_JOIN_CAPACITY);
        NamedForkJoinWorkerThreadFactory factory = new NamedForkJoinWorkerThreadFactory(threadNamePrefix);
        ForkJoinPool forkJoinPool = new ForkJoinPool(size, factory, new ThreadUnCaughtExceptionHandler(), false);

        return buildExecutorService(forkJoinPool);
    }

    /**
     * Shuts down the given {@link ExecutorService} in an orderly fashion. Disables new tasks from submission and then
     * waits for existing tasks to terminate. Eventually cancels running tasks if too much time elapses.
     * <p>
     * If the timeout is 0, then a plain shutdown takes place.
     *
     * @param executorService the pool to shutdown.
     * @return {@code true} if the given executor terminated and {@code false} if the timeout elapsed before
     * termination.
     */
    public static boolean shutdown(final ExecutorService executorService) {
        return shutdown(executorService, DEFAULT_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS, "");
    }

    /**
     * Shuts down the given {@link ExecutorService} in an orderly fashion. Disables new tasks from submission and then
     * waits for existing tasks to terminate. Eventually cancels running tasks if too much time elapses.
     * <p>
     * If the timeout is 0, then a plain shutdown takes place.
     * </p>
     *
     * @param executorService the pool to shutdown.
     * @param timeout         the maximum time to wait, or 0 to not wait for existing tasks to terminate.
     * @param timeUnit        the time unit of the timeout argument
     * @param source          use this string in any log messages.
     * @return {@code true} if the given executor terminated and {@code false} if the timeout elapsed before
     * termination.
     */
    public static boolean shutdown(final ExecutorService executorService, final long timeout, final TimeUnit timeUnit, final String source) {
        if (executorService == null || executorService.isTerminated()) {
            return true;
        }
        executorService.shutdown(); // Disable new tasks from being submitted
        if (timeout > 0 && timeUnit == null) {
            throw new IllegalArgumentException(
                    String.format("%s can't shutdown %s when timeout = %d.", source, executorService, timeout));
        }
        if (timeout > 0) {
            try {
                // Wait a while for existing tasks to terminate
                if (!executorService.awaitTermination(timeout, timeUnit)) {
                    executorService.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executorService.awaitTermination(timeout, timeUnit)) {
                        log.error("{} pool {} did not terminate after {} {}", source, executorService, timeout, timeUnit);
                    }
                    return false;
                }
            } catch (final InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executorService.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        } else {
            executorService.shutdown();
        }
        return true;
    }

    /**
     * 加入{@code allExecutors}，在 SpringBoot 优雅关闭关闭的时候，在 Servlet 线程池关闭后，遍历检查线程池集合，保证这些线程池做完所有任务
     * 同时防止对于临时线程池的内存泄漏，设置为 {@link WeakReference}
     *
     * @param executorService 线程池
     * @param <T>             线程池对象
     * @return 线程池
     */
    private static <T extends ExecutorService> T buildExecutorService(T executorService) {
        ALL_THREAD_EXECUTORS.add(new WeakReference<>(executorService));
        return executorService;
    }

    /**
     * 计算初始值，该数字会转换为2的n次方，并且会小于等于最大值
     * <p>
     * 参考的{@code java.util.HashMap#tableSizeFor(int)}算法
     *
     * @param cap     初始值
     * @param maximum 最大值
     * @return 符合规则的初始值
     */
    public static int tableSizeFor(int cap, int maximum) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= maximum) ? maximum : n + 1;
    }

    /**
     * 判断线程池任务是否执行完成
     * <p>
     * 1.{@link ThreadPoolExecutor} 只需要判断活跃线程数
     * 2.{@link ForkJoinPool} 需要判断：活跃线程数、running的线程数、任务队列、提交任务队列
     *
     * @param executorService 线程池
     * @return true-没有任务执行，false-有任务在执行
     */
    public static boolean isCompleted(ExecutorService executorService) {
        if (executorService instanceof ThreadPoolExecutor threadPoolExecutor) {
			return threadPoolExecutor.getActiveCount() == 0;
        } else if (executorService instanceof ForkJoinPool forkJoinPool) {
			return forkJoinPool.getActiveThreadCount() == 0
				&& forkJoinPool.getRunningThreadCount() == 0
				&& forkJoinPool.getQueuedTaskCount() == 0
				&& forkJoinPool.getQueuedSubmissionCount() == 0;
        }

        return true;
    }

    /**
     * 等待所有的线程池任务执行完成
     * <p>
     * 采用随机打乱线程池的方式，去判断线程池是否执行完成，如果三次都成功则认为是线程池没有任务执行
     *
     * @param timeout          等待超时时间，时间单位秒，如果是<0则无超时时间
     * @param executorServices 线程池集合
     */
    public static void await(long timeout, List<ExecutorService> executorServices) {
        for (int i = 0, j = 0; i < 3; j++) {
            if (j == timeout) {
                break;
            }

            Collections.shuffle(executorServices);
            if (executorServices.stream().allMatch(ExecutorServices::isCompleted)) {
                i++;
                log.info("all threads pools are completed, i: {}", i);
                continue;
            }

            i = 0;
            log.info("not all threads pools are completed, wait for 1s");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void await(List<ExecutorService> executorServices) {
        await(DEFAULT_SHUTDOWN_TIMEOUT, executorServices);
    }

    public static void awaitShutdown(List<ExecutorService> executorServices) {
        awaitShutdown(DEFAULT_SHUTDOWN_TIMEOUT, executorServices);
    }

    public static void awaitShutdown(long timeout, List<ExecutorService> executorServices) {
        if (executorServices == null || executorServices.isEmpty()) {
            return;
        }

        await(timeout, executorServices);
        executorServices.forEach(ExecutorService::shutdown);
    }

    public static void shutdownAllExecutors() {
        if (ALL_THREAD_EXECUTORS.isEmpty()) {
            return;
        }

        for (WeakReference<ExecutorService> reference : ALL_THREAD_EXECUTORS) {
            ExecutorService executor = reference.get();
            if (executor != null && !executor.isShutdown()) {
                ExecutorServices.shutdown(executor);
            }
        }
    }

    public static class NamedForkJoinWorkerThread extends ForkJoinWorkerThread {

        protected NamedForkJoinWorkerThread(String threadName, ForkJoinPool pool) {
            super(pool);
            setName(threadName);
        }
    }

    public static final class NamedForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

        private final String threadPrefixName;
        private final AtomicInteger index = new AtomicInteger(0);

        public NamedForkJoinWorkerThreadFactory(String threadPrefixName) {
            this.threadPrefixName = threadPrefixName;
        }

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new NamedForkJoinWorkerThread(threadPrefixName + "-" + index.getAndIncrement(), pool);
        }
    }

    public static final class ThreadUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error("线程【" + t.getName() + "】发生异常", e);
        }

    }

    /**
     * fork-join线程中如果任务阻塞， 会自动添加线程
     */
    public static class ManagedBlocks {

        public static <T> T callInManagedBlock(final Supplier<T> supplier) {
            final SupplierManagedBlock<T> managedBlock = new SupplierManagedBlock<>(supplier);
            try {
                ForkJoinPool.managedBlock(managedBlock);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return managedBlock.getResult();
        }

        private static class SupplierManagedBlock<T> implements ForkJoinPool.ManagedBlocker {

            private final Supplier<T> supplier;
            private T result;
            private boolean done = false;

            private SupplierManagedBlock(final Supplier<T> supplier) {
                this.supplier = supplier;
            }

            @Override
            public boolean block() {
                result = supplier.get();
                done = true;
                return true;
            }

            @Override
            public boolean isReleasable() {
                return done;
            }

            public T getResult() {
                return result;
            }
        }
    }

}
