package com.broadtech.arthur.admin.rule.service.system.impl;


import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.IdempotentServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
import com.broadtech.arthur.admin.rule.entiry.system.dto.SystemRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.system.nc.NcSystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.po.SystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.vo.SystemRuleVo;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleRepositoryService;
import com.broadtech.arthur.admin.rule.repository.system.SystemRuleRepositoryService;
import com.broadtech.arthur.admin.rule.service.system.SystemRuleBusinessService;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
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
public class SystemRuleBusinessServiceImpl implements SystemRuleBusinessService {

    @Resource
    SystemRuleRepositoryService systemRuleRepositoryService;

    @Resource
    GroupRepositoryService groupRepositoryService;

    @Resource
    IdempotentServiceImpl idempotentService;

    @Override
    public ResponseVo createSystemRule(SystemRuleDefinitionDTO systemDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(systemDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(systemDto.getId()));

        SystemRule   systemRule   = systemDto.dto2po();
        NcSystemRule ncSystemRule = systemDto.dto2nc();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(systemDto
                .getGroupId()).orNull());
        return ResponseVo.SUCCESS(systemRuleRepositoryService.createSystemRule(systemRule, Group.assembleConfigMetaData(group), ncSystemRule));
    }

    @Override
    public ResponseVo deleteSystemRule(String id, String groupId) {
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(groupId).orNull());
        return ResponseVo.SUCCESS(systemRuleRepositoryService.deleteSystemRule(id, Group.assembleConfigMetaData(group)));
    }

    @Override
    public ResponseVo updateSystemRule(SystemRuleDefinitionDTO systemDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(systemDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(systemDto.getId()));

        SystemRule   systemRule   = systemDto.dto2po();
        NcSystemRule ncSystemRule = systemDto.dto2nc();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(systemDto
                .getGroupId()).orNull());

        return ResponseVo.SUCCESS(systemRuleRepositoryService.updateSystemRule(systemRule, Group.assembleConfigMetaData(group), ncSystemRule));
    }

    @Override
    public ResponseVo querySystemRule(String id) {
        Optional<SystemRule> systemRuleOptional = systemRuleRepositoryService.querySystemRule(id);

        return systemRuleOptional.orNull() == null
                ? ResponseVo.SUCCESS(null) : ResponseVo.SUCCESS(systemRuleOptional.transform(
                new Function<SystemRule, SystemRuleVo>() {
                    @Override
                    public @Nullable SystemRuleVo apply(@Nullable SystemRule systemRule) {
                        SystemRuleVo systemRuleVo = new SystemRuleVo();
                        BeanUtils.copyProperties(systemRule, systemRuleVo);
                        return systemRuleVo;
                    }
                }).get());
    }
}
