package com.broadtech.arthur.admin.common.detect;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
public abstract class AbstractDetector<T> {
    AbstractDetector next;


    /**
     * 支持
     *
     * @param t t
     * @return boolean
     */
    protected abstract boolean support(T t);


    /**
     * 检测
     *
     * @param t t
     */
    protected void detection(T t) throws RuntimeException {
        if (support(t)) {
            doDetection(t);
        } else {
            if (next == null) {
                throw new RuntimeException("no suitable detector");
            }
            next.detection(t);
        }
    }

    /**
     * 做检测
     *
     * @param t t
     */
    protected abstract void doDetection(T t);

}
