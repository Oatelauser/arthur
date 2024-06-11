package com.broadtech.arthur.admin.common.service.impl;

import com.broadtech.arthur.admin.common.mapper.BaseMapper;
import com.broadtech.arthur.admin.common.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/17
 */
public class ServiceImpl <Mapper extends BaseMapper<T,M,K,V>,T,M,K,V> implements IService<T,M,K,V>{

    @Autowired
    protected Mapper baseMapper;
    /**
     * @return
     */
    @Override
    public BaseMapper<T, M, K, V> getBaseMapper() {
        return baseMapper;
    }
}
