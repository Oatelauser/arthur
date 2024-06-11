package com.arthur.common.notify.event;

/**
 * 事件传输的载体
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
public class DataEvent<T> {

    private T delegator;

    public T getDelegator() {
        return delegator;
    }

    public void setDelegator(T delegator) {
        this.delegator = delegator;
    }
}
