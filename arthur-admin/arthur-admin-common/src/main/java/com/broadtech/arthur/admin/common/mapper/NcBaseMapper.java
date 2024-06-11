package com.broadtech.arthur.admin.common.mapper;

import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;

import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/16
 */
public interface NcBaseMapper<T extends ConfigTemplate, M extends ConfigMetaData, K extends String, V> extends BaseMapper<T,M,K,V> {

    Map<K,V> selectAllData(T template, M meta);




}
