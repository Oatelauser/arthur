package com.broadtech.arthur.admin.rule.repository.degrade.impl;

import com.broadtech.arthur.admin.common.template.CacheTemplate;
import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleRepositoryService;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DegradeRuleRepositoryServiceImpl implements DegradeRuleRepositoryService {


    @Resource
    ConfigTemplate template;

    @Resource
    CacheTemplate cacheTemplate;


    @Resource
    NcDegradeRuleServiceImpl ncDegradeRuleService;



    @Resource
    DegradeRuleService degradeRuleService;


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean createDegradeRule(DegradeRule degradeRule, ConfigMetaData metaData, NcDegradeRule ncDegradeRule) {
        return degradeRuleService.save(degradeRule) && ncDegradeRuleService
                .save(template, metaData, degradeRule.getId(), ncDegradeRule);

    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Caching(evict = @CacheEvict(value = "degrade", key = "#id"))
    @Override
    public boolean deleteDegradeRule(String id, ConfigMetaData metaData) {
        return degradeRuleService.removeById(id) && ncDegradeRuleService.removeById(template, metaData, id);
    }

    @Caching(evict = @CacheEvict(value = "degrade", key = "#degradeRule.id"))
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateDegradeRule(DegradeRule degradeRule, ConfigMetaData metaData, NcDegradeRule ncDegradeRule) {
        return degradeRuleService.updateById(degradeRule) &&
                ncDegradeRuleService.updateById(template, metaData, degradeRule.getId(), ncDegradeRule);

    }

    @Cacheable(value = "degrade", key = "#id")
    @Override
    public Optional<DegradeRule> queryDegradeRule(String id) {
        DegradeRule degradeRule = degradeRuleService.getById(id);
        return degradeRule == null ? Optional.absent() : Optional.of(degradeRule);
    }


}
