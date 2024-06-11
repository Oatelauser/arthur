package com.arthur.common.lifecycle;

/**
 * 优先顺序接口
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface Ordered {

    /**
     * 最高优先级
     */
    int MAXIMUM_ORDER = Integer.MIN_VALUE;

    /**
     * 最小优先级
     */
    int MINIMUM_ORDER = Integer.MAX_VALUE;

    /**
     * 优先级
     *
     * @return 顺序
     */
    default int getOrder() {
        return MINIMUM_ORDER;
    }

}
