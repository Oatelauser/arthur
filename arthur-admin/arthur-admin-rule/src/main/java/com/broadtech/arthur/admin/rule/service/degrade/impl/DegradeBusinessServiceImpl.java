package com.broadtech.arthur.admin.rule.service.degrade.impl;

import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.IdempotentServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
import com.broadtech.arthur.admin.rule.entiry.degrade.dto.DegradeRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.vo.DegradeRuleVo;
import com.broadtech.arthur.admin.rule.repository.api.ApiRepositoryService;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleRepositoryService;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleService;
import com.broadtech.arthur.admin.rule.service.degrade.DegradeBusinessService;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
@Service
@Slf4j
public class DegradeBusinessServiceImpl implements DegradeBusinessService {


    @Resource
    DegradeRuleRepositoryService degradeRuleRepositoryService;

    @Resource
    GroupRepositoryService groupRepositoryService;

    @Resource
    IdempotentServiceImpl idempotentService;

    @Override
    public ResponseVo createDegradeRule(DegradeRuleDefinitionDTO degradeRuleDefinitionDTO) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(degradeRuleDefinitionDTO.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(degradeRuleDefinitionDTO.getId()));

        NcDegradeRule ncDegradeRule = degradeRuleDefinitionDTO.dto2nc();
        DegradeRule   degradeRule   = degradeRuleDefinitionDTO.dto2po();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(degradeRuleDefinitionDTO
                .getGroupId()).orNull());


            return ResponseVo.SUCCESS(degradeRuleRepositoryService.createDegradeRule(degradeRule, Group.assembleConfigMetaData(group), ncDegradeRule));



    }

    @Override
    public ResponseVo deleteDegradeRule(String id, String groupId) {
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(groupId).orNull());


            return ResponseVo.SUCCESS(degradeRuleRepositoryService.deleteDegradeRule(id, Group.assembleConfigMetaData(group)));

    }

    @Override
    public ResponseVo updateDegradeRule(DegradeRuleDefinitionDTO degradeRuleDefinitionDTO) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(degradeRuleDefinitionDTO.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(degradeRuleDefinitionDTO.getId()));

        NcDegradeRule ncDegradeRule = degradeRuleDefinitionDTO.dto2nc();
        DegradeRule   degradeRule   = degradeRuleDefinitionDTO.dto2po();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(degradeRuleDefinitionDTO
                .getGroupId()).orNull());


            return ResponseVo.SUCCESS(degradeRuleRepositoryService.updateDegradeRule(degradeRule, Group.assembleConfigMetaData(group), ncDegradeRule));

    }

    @Override
    public ResponseVo queryDegradeRule(String id) {
        Optional<DegradeRule> degradeRuleOptional = degradeRuleRepositoryService.queryDegradeRule(id);

        return degradeRuleOptional.orNull() == null ? ResponseVo.FAIL(null) : ResponseVo.SUCCESS(degradeRuleOptional.transform(
                new Function<DegradeRule, DegradeRuleVo>() {
                    @Override
                    public @Nullable DegradeRuleVo apply(@Nullable DegradeRule degradeRule) {
                        DegradeRuleVo degradeRuleVo = new DegradeRuleVo();
                        BeanUtils.copyProperties(degradeRule, degradeRuleVo);
                        return degradeRuleVo;
                    }
                }).get());
    }
}
