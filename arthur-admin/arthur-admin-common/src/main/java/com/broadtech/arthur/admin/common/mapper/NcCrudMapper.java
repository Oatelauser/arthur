package com.broadtech.arthur.admin.common.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/16
 */

public class NcCrudMapper<V> implements NcBaseMapper<ConfigTemplate, ConfigMetaData, String, V> {


    /**
     * @param template
     * @param meta
     * @param key
     * @param newData
     * @return
     */
    @Override
    public int updateData(ConfigTemplate template, ConfigMetaData meta, String key, V newData) {
        return upsertData(template, meta, key, newData);
    }

    /**
     * @param template
     * @param meta
     * @return
     */
    @Override
    public int deleteAll(ConfigTemplate template, ConfigMetaData meta) {

        try {
            return template.removeConfig(meta) ? 1 : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param template
     * @param meta
     * @param key
     * @return
     */
    @Override
    public int deleteData(ConfigTemplate template, ConfigMetaData meta, String key) {
        Preconditions.checkArgument(StrUtil.isNotBlank(key));
        Map<String, V> dataMap = selectAllData(template, meta);
        dataMap.remove(key);
        try {
            return template.pushConfig(meta, JSON.toJSONString(dataMap)) ? 1 : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param template
     * @param meta
     * @param keys
     * @return
     */
    @Override
    public int deleteBacthData(ConfigTemplate template, ConfigMetaData meta, List<String> keys) {
        Preconditions.checkArgument(CollectionUtil.isNotEmpty(keys));
        Map<String, V> dataMap = selectAllData(template, meta);
        keys.forEach(dataMap::remove);
        try {
            return template.pushConfig(meta, JSON.toJSONString(dataMap)) ? keys.size() : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param template
     * @param meta
     * @param key
     * @param newData
     * @return
     */
    @Override
    public int insertData(ConfigTemplate template, ConfigMetaData meta, String key, V newData) {
        return upsertData(template, meta, key, newData);
    }

    private int upsertData(ConfigTemplate template, ConfigMetaData meta, String key, V newData) {
        Map<String, V> dataMap = selectAllData(template, meta);
//        if (StrUtil.isEmpty(key)) {
//            throw new RuntimeException("插入 nc 时, key is null");
//        }
        dataMap.put(key, newData);
        try {
            return template.pushConfig(meta, JSON.toJSONString(dataMap)) ? 1 : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param template
     * @param meta
     * @param key
     * @return
     */
    @Override
    public V selectData(ConfigTemplate template, ConfigMetaData meta, String key) {
        Map<String, V> map = selectAllData(template, meta);
        return map.get(key);

    }

    /**
     * @param template
     * @param meta
     * @return
     */
    @Override
    public Map<String, V> selectAllData(ConfigTemplate template, ConfigMetaData meta) {
        String data = getOldData(template, meta);
        Map<String, V> rs = JSON.parseObject(data, new TypeReference<Map<String, V>>() {
        });
        if (rs == null) {
            rs = MapUtil.createMap(Map.class);
        }
        return rs;
    }

    private String getOldData(ConfigTemplate template, ConfigMetaData meta) {
        try {
            return template.getConfig(meta);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
