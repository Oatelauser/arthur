package com.arthur.common.concurrent.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.*;

import static com.arthur.common.algorithms.HashAlgorithm.fnv132Hash;

/**
 * 复合线程池
 * <p>
 * 组合普通线程池和单线程池，底层采用一致性哈希算法
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class CompositeSingleThreadPoolExecutor extends ThreadPoolExecutor {

    private final SortedMap<Integer, SingleThreadPoolExecutor> singleExecutors = new ConcurrentSkipListMap<>();

    /**
     * Instantiates a new Composite executor.
     *
     * @param corePoolSize    the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime   the keep alive time
     * @param unit            the unit
     * @param workQueue       the work queue
     * @param threadFactory   the thread factory
     * @param handler         the handler
     * @param singled         the singled Whether to execute sequentially.
     */
    public CompositeSingleThreadPoolExecutor(boolean singled, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        if (singled) {
            createSingleExecutors(corePoolSize, threadFactory);
        }
    }

    public CompositeSingleThreadPoolExecutor(boolean singled, int corePoolSize, ThreadFactory threadFactory) {
        this(singled, corePoolSize, corePoolSize, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory, new AbortPolicy());
    }

    /**
     * 基于一致性哈希算法，创建多个单线程池
     *
     * @param corePoolSize  核心线程池数
     * @param threadFactory 线程工厂
     */
    public void createSingleExecutors(int corePoolSize, ThreadFactory threadFactory) {
        for (int i = 0; i < corePoolSize; i++) {
            SingleThreadPoolExecutor singleExecutor = new SingleThreadPoolExecutor(threadFactory);
            String hash = singleExecutor.hashCode() + "-" + i;
            for (int j = 0; j < 4; j++) {
                singleExecutors.put(fnv132Hash(hash + "&&VN" + j), singleExecutor);
            }
        }
    }

    /**
     * 基于一致性哈希算法路由线程池
     *
     * @param node 路由节点
     * @return 单线程线程池
     */
    public SingleThreadPoolExecutor getSingleExecutor(String node) {
        int hash = fnv132Hash(node);
        SortedMap<Integer, SingleThreadPoolExecutor> tailMap = singleExecutors.tailMap(hash);
        Integer selectNode = tailMap.firstKey();
        return singleExecutors.get(selectNode);
    }

    public void close() {
        List<ExecutorService> services = new ArrayList<>();
        if (!singleExecutors.isEmpty()) {
            services.addAll(singleExecutors.values());
        }
        services.add(this);

        ExecutorServices.awaitShutdown(services);
    }

}
