package com.arthur.boot.lifecycle;

import com.arthur.boot.core.bean.SortedBean;
import com.arthur.common.lifecycle.Ordered;

/**
 * 生命周期的排序Bean对象
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
public class SortedLifecycleBean<T extends Ordered> extends SortedBean<T> {

    public SortedLifecycleBean(T bean) {
        super(bean);
    }

    @Override
    public int getOrder() {
        return Math.min(super.getOrder(), getBean().getOrder());
    }

}
