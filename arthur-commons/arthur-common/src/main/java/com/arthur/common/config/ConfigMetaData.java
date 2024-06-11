package com.arthur.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置文件的三元组
 *
 * @author DearYang
 * @date 2022-07-26
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigMetaData {

    private String namespace;
    private String group;
    private String dataId;
    private String dataType;

    public ConfigMetaData(String group, String dataId) {
        this.group = group;
        this.dataId = dataId;
    }

    public ConfigMetaData(String namespace, String group, String dataId) {
        this.group = group;
        this.dataId = dataId;
        this.namespace = namespace;
    }

}
