package com.broadtech.arthur.admin.rule.repository.system.impl;

import com.broadtech.arthur.admin.common.template.CacheTemplate;
import com.broadtech.arthur.admin.rule.entiry.system.nc.NcSystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.po.SystemRule;
import com.broadtech.arthur.admin.rule.repository.system.SystemRuleRepositoryService;
import com.broadtech.arthur.admin.rule.repository.system.SystemRuleService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.config.ConfigTemplate;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class SystemRuleRepositoryServiceImpl implements SystemRuleRepositoryService {

    @Resource
    ConfigTemplate template;

    @Resource
    CacheTemplate cacheTemplate;


    @Resource
    NcSystemRuleServiceImpl ncSystemRuleService;



    @Resource
    SystemRuleService systemRuleService;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean createSystemRule(SystemRule systemRule, ConfigMetaData metaData, NcSystemRule ncSystemRule) {
        return systemRuleService.save(systemRule) &&
                ncSystemRuleService.save(template, metaData, systemRule.getId(), ncSystemRule);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "sys_cache", key = "#id")
    public boolean deleteSystemRule(String id, ConfigMetaData metaData) {
        return systemRuleService.removeById(id) &&
                ncSystemRuleService.removeById(template, metaData, id);

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "sys_cache", key = "#systemRule.id")
    public boolean updateSystemRule(SystemRule systemRule, ConfigMetaData metaData, NcSystemRule ncSystemRule) {

        return systemRuleService.save(systemRule) &&
                ncSystemRuleService.save(template, metaData, systemRule.getId(), ncSystemRule);

    }

    @Cacheable(value = "sys_cache", key = "#id")
    @Override
    public Optional<SystemRule> querySystemRule(String id) {
        SystemRule systemRule = systemRuleService.getById(id);
        return systemRule == null ? Optional.absent() : Optional.of(systemRule);
    }
}
