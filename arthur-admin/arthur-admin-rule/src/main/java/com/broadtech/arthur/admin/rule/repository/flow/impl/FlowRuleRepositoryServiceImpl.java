package com.broadtech.arthur.admin.rule.repository.flow.impl;

import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import com.broadtech.arthur.admin.rule.repository.flow.FlowRuleRepositoryService;
import com.broadtech.arthur.admin.rule.repository.flow.FlowRuleService;
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
 * @date 2022/9/26
 */
@Service
@Slf4j
public class FlowRuleRepositoryServiceImpl implements FlowRuleRepositoryService {

    @Resource
    FlowRuleService flowRuleService;
    @Resource
    NcFlowRuleServiceImpl ncFlowRuleService;

    @Resource
    ConfigTemplate template;



    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean createFlow(FlowRule flowRule, ConfigMetaData metaData, NcFlowRule ncFlowRule) {
        return flowRuleService.save(flowRule)&&
        ncFlowRuleService.save(template, metaData, flowRule.getId(), ncFlowRule);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "flow_cache",key = "#id")
    @Override
    public boolean deleteFlow(String id, ConfigMetaData metaData) {

       return flowRuleService.removeById(id)&&
        ncFlowRuleService.removeById(template, metaData, id);


    }

    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "flow_cache",key = "#flowRule.id")
    @Override
    public boolean updateFlow(FlowRule flowRule, ConfigMetaData metaData, NcFlowRule ncFlowRule) {

        return  flowRuleService.updateById(flowRule)&&
        ncFlowRuleService.updateById(template, metaData, flowRule.getId(), ncFlowRule);
    }

    @Cacheable(value = "flow_cache",key = "#flowRule.id")
    @Override
    public Optional<FlowRule> queryFlow(String id) {
        FlowRule rule = flowRuleService.getById(id);
        return rule == null ? Optional.absent() : Optional.of(rule);
    }
}
