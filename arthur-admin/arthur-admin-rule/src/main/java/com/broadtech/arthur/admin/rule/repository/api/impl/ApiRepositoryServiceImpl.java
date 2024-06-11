package com.broadtech.arthur.admin.rule.repository.api.impl;

import com.broadtech.arthur.admin.rule.entiry.api.nc.NcApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiRule;
import com.broadtech.arthur.admin.rule.repository.api.ApiRepositoryService;
import com.broadtech.arthur.admin.rule.repository.api.ApiService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import com.google.common.base.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
@Service
public class ApiRepositoryServiceImpl implements ApiRepositoryService {


    @Resource
    ApiService       apiService;
    @Resource
    NcApiServiceImpl ncApiService;
    @Resource
    ConfigTemplate   template;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean createApi(ApiRule apiRule, ConfigMetaData metaData, NcApiRule ncApiRule) {
        apiService.save(apiRule);
        ncApiService.save(template, metaData, apiRule.getId(), ncApiRule);
        return true;
    }

    @Caching(evict = {@CacheEvict(value = "api_cache", key = "#id"), @CacheEvict(value = "api_cache", key = "#id")})
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteApi(String id, ConfigMetaData metaData) {
        apiService.removeById(id);
        ncApiService.removeById(template, metaData, id);
        return true;
    }

    @Caching(evict = {@CacheEvict(value = "api_cache", key = "#apiRule.id"), @CacheEvict(value = "api_cache", key = "#apiRule.id")})
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateApi(ApiRule apiRule, ConfigMetaData metaData, NcApiRule ncApiRule) {
        apiService.updateById(apiRule);
        ncApiService.updateById(template, metaData, apiRule.getId(), ncApiRule);
        return true;
    }

    @Cacheable(value = "api_cache", key = "#id")
    @Override
    public Optional<ApiRule> queryApi(String id) {

        ApiRule apiRule = apiService.getById(id);
        if (apiRule == null) {
            return Optional.absent();
        }

        return Optional.of(apiRule);
    }
}
