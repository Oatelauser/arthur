package com.broadtech.arthur.admin.rule.service.flow.impl;

import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.IdempotentServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.rule.entiry.flow.dto.FlowRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.vo.FlowRuleVo;
import com.broadtech.arthur.admin.rule.repository.flow.FlowRuleRepositoryService;
import com.broadtech.arthur.admin.rule.service.flow.FlowRuleBusinessService;
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
public class FlowRuleBusinessServiceImpl implements FlowRuleBusinessService {


    @Resource
    FlowRuleRepositoryService flowRuleRepositoryService;

    @Resource
    GroupRepositoryService groupRepositoryService;

    @Resource
    IdempotentServiceImpl idempotentService;

    @Override
    public ResponseVo createFlow(FlowRuleDefinitionDTO flowDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(flowDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(flowDto.getId()));
        FlowRule flowRule = flowDto.dto2po();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(flowDto
                .getGroupId()).orNull());
        NcFlowRule ncFlowRule = flowDto.dto2nc();



            return ResponseVo.SUCCESS(flowRuleRepositoryService.createFlow(flowRule, Group.assembleConfigMetaData(group), ncFlowRule));

    }

    @Override
    public ResponseVo deleteFlow(String id, String groupId) {
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(groupId).orNull());

            return ResponseVo.SUCCESS(flowRuleRepositoryService.deleteFlow(id, Group.assembleConfigMetaData(group)));

    }

    @Override
    public ResponseVo updateFlow(FlowRuleDefinitionDTO flowDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(flowDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(flowDto.getId()));
        FlowRule flowRule = flowDto.dto2po();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(flowDto
                .getGroupId()).orNull());
        NcFlowRule ncFlowRule = flowDto.dto2nc();



            return ResponseVo.SUCCESS(flowRuleRepositoryService.updateFlow(flowRule, Group.assembleConfigMetaData(group), ncFlowRule));

    }

    @Override
    public ResponseVo queryFlow(String id) {
        Optional<FlowRule> flowRuleOptional = flowRuleRepositoryService.queryFlow(id);

        return flowRuleOptional.orNull() == null ? ResponseVo.SUCCESS(null
        ) : ResponseVo.SUCCESS(flowRuleOptional.transform(
                new Function<FlowRule, FlowRuleVo>() {
                    @Override
                    public @Nullable FlowRuleVo apply(@Nullable FlowRule flowRule) {
                        FlowRuleVo flowRuleVo = new FlowRuleVo();
                        BeanUtils.copyProperties(flowRule, flowRuleVo);
                        return flowRuleVo;
                    }
                }
        ).get());
    }
}
