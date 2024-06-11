package com.arthur.common.registry;

import java.util.List;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-07-25
 * @since 1.0
 */
public class Service {

    private String env;
    private String group;
    private String region;
    private String zoneId;
    private String version;

    private String serviceId;
    private String heartBeatUrl;
    private long heartBeatInterval;
    private List<Instance> instances;
    private boolean concurrencyControl;

}
