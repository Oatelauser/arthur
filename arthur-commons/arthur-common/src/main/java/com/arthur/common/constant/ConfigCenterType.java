package com.arthur.common.constant;

/**
 * 配置中心类型枚举
 *
 * @author DearYang
 * @date 2022-07-22
 * @since 1.0
 */
@SuppressWarnings("all")
public enum ConfigCenterType {

    /**
     * 阿里nacos
     */
    NACOS("Nacos"),

    /**
     * 携程apollo
     */
    APOLLO("Apollo"),

    /**
     * zk
     */
    ZOOKEEPER("Zookeeper");

    private final String type;

    ConfigCenterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ConfigCenterType of(String type) {
        for (ConfigCenterType configCenterType : ConfigCenterType.values()) {
            if (type.equals(configCenterType.type)) {
                return configCenterType;
            }
        }

        throw new IllegalArgumentException("not matched any type: " + type);
    }
}
