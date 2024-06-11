package com.arthur.common.registry;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public class Instance {

    /**
     * instance ip.
     */
    private String ip;

    /**
     * instance port.
     */
    private int port;

    /**
     * instance weight.
     */
    private double maxWeight = 100.0;
    private double minWeight = 0.0;
    private int priority;

}
