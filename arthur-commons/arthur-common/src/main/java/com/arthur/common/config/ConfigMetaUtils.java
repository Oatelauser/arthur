package com.arthur.common.config;

import com.arthur.common.utils.StringUtils;

import static com.arthur.common.constant.BaseConstants.LINE_DELIMITER;

/**
 * 配置三元组工具类
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
public abstract class ConfigMetaUtils {

    /**
     * 获取配置三元组的唯一坐标
     *
     * @param configMetaData {@link ConfigMetaData}
     * @return 横线分割连接配置三元组
     */
    public static String getConfigPosition(ConfigMetaData configMetaData) {
        return configMetaData.getNamespace() + LINE_DELIMITER + configMetaData.getGroup() + LINE_DELIMITER + configMetaData.getDataId() + LINE_DELIMITER + configMetaData.getDataType();
    }

    public static ConfigMetaData getConfigMeta(String configPosition) {
        if (StringUtils.isBlank(configPosition)) {
            return null;
        }

        String[] split = configPosition.split(LINE_DELIMITER);
        if (split.length != 4) {
            return null;
        }

        return new ConfigMetaData(split[0], split[1], split[2], split[3]);
    }

}
