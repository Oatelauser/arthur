package com.arthur.common.concurrent.thread;

import java.util.concurrent.Executor;

/**
 * thread pool process factory
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public interface ThreadExecutorFactory {

    /**
     * process thread pool
     *
     * @param executor original thread pool
     * @param <T>      thread pool
     * @return new thread pool
     */
    <T extends Executor> T newExecutor(Executor executor);

}
