package com.broadtech.arthur.admin.common.detect;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
public abstract class RouteDetector<T extends CheckAble> extends AbstractDetector<T> implements DetectBehavior<T> {


    @Override
    protected boolean support(T t) {
        return CheckAble.class.isAssignableFrom(t.getClass());
    }

}
