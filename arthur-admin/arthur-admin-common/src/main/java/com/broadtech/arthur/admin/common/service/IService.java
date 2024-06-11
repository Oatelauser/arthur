package com.broadtech.arthur.admin.common.service;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.broadtech.arthur.admin.common.mapper.BaseMapper;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
public interface IService<T,M,K, V> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param newData 实体对象
     */
    default boolean save(T template, M meta, K key, V newData) {
        return SqlHelper.retBool(getBaseMapper().insertData(template,meta,key,newData));
    }

    /**
     * 根据 ID 删除
     *
     * @param key 主键ID
     */
    default boolean removeById(T template, M meta, K key) {
        return SqlHelper.retBool(getBaseMapper().deleteData(template,meta,key));
    }
    default boolean removeAll(T template, M meta) {
        return SqlHelper.retBool(getBaseMapper().deleteAll(template,meta));
    }

    default boolean removeByIds(T template, M meta, List<K> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return SqlHelper.retBool(getBaseMapper().deleteBacthData(template,meta,list));
    }


    /**
     * 根据 ID 选择修改
     *
     * @param newData 实体对象
     */
    default boolean updateById(T template, M meta, K key, V newData) {
        return SqlHelper.retBool(getBaseMapper().updateData(template, meta, key, newData));
    }


    /**
     * 根据 ID 查询
     *
     * @param key 主键ID
     */
    default V getById(T template, M meta, K key) {
        return getBaseMapper().selectData(template, meta, key);
    }



    BaseMapper<T,M,K, V> getBaseMapper();

}
