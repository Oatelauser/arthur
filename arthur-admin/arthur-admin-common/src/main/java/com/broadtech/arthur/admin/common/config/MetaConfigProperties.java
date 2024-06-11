package com.broadtech.arthur.admin.common.config;

import com.broadtech.arthur.admin.common.config.enums.MetaTypeEnum;
import com.broadtech.arthur.common.config.ConfigMetaData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Machenike
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "arthur.meta")
@Data
public class MetaConfigProperties {
    String DATA_TYPE = "json";
    String NAME_SPACE = "public";


    private ConfigMetaData group;
    private ConfigMetaData api;
    private ConfigMetaData flow;
    private ConfigMetaData degrade;
    private ConfigMetaData system;

    public ConfigMetaData getNcMeta() {
        return getGroup();
    }

    public ConfigMetaData getNcGroupMeta(String groupId, String groupName, String groupType) {

        switch (MetaTypeEnum.valueOf(groupType)) {
            case API:
                return getApi();
            case FLOW:
                return getFlow();
            case SYSTEM:
                return getSystem();
            case DEGRADE:
                return getDegrade();
            case ROUTE:
                return getRoute(groupId, groupName, groupType);
            default:
                throw new RuntimeException("暂时不支持该类型");
        }
    }

    private ConfigMetaData getRoute(String groupId, String groupName, String groupType) {
        ConfigMetaData item = new ConfigMetaData();
        item.setDataId(groupId + "_" + groupName +"."+ DATA_TYPE);
        item.setGroup(groupName);
        item.setNamespace(NAME_SPACE);
        item.setDataType(DATA_TYPE);
        return item;

    }

}
