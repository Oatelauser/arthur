package com.broadtech.arthur.admin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.Mapper;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/16
 */
public interface BaseMapper<T,M,K, V> extends Mapper<V> {

    int updateData(T template, M meta, K key, V newData);

    int deleteData(T template, M meta, K key);

    int deleteAll(T template, M meta);

    int deleteBacthData(T template, M meta, List<K> keys);

    int insertData(T template, M meta, K key, V newData);

    V selectData(T template, M meta,K key);



}
