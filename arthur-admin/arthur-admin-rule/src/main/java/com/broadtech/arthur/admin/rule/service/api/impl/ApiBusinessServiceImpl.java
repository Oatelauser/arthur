package com.broadtech.arthur.admin.rule.service.api.impl;

import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.IdempotentServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
import com.broadtech.arthur.admin.rule.entiry.api.dto.ApiDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.api.nc.NcApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.vo.ApiDefinitionVo;
import com.broadtech.arthur.admin.rule.repository.api.ApiRepositoryService;
import com.broadtech.arthur.admin.rule.service.api.ApiBusinessService;
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
public class ApiBusinessServiceImpl implements ApiBusinessService {

    @Resource
    ApiRepositoryService apiRepositoryService;

    @Resource
    GroupRepositoryService groupRepositoryService;

    @Resource
    IdempotentServiceImpl idempotentService;


    @Override
    public ResponseVo createApi(ApiDefinitionDTO apiDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(apiDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(apiDto.getId()));
        ApiRule   apiRule   = apiDto.dto2po();
        NcApiRule ncApiRule = apiDto.dto2nc();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(apiDto
                .getGroupId()).orNull());

        return ResponseVo.SUCCESS(apiRepositoryService.createApi(apiRule, Group.assembleConfigMetaData(group), ncApiRule));


    }

    @Override
    public ResponseVo deleteApi(String id, String groupId) {

        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(groupId).orNull());

        return ResponseVo.SUCCESS(apiRepositoryService.deleteApi(id, Group.assembleConfigMetaData(group)));


    }

    @Override
    public ResponseVo updateApi(ApiDefinitionDTO apiDto) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(apiDto.getId() + "createRoute"),
                BaseException.RepeatedSubmitException.of(apiDto.getId()));
        ApiRule   apiRule   = apiDto.dto2po();
        NcApiRule ncApiRule = apiDto.dto2nc();
        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(apiDto
                .getGroupId()).orNull());
        return ResponseVo.SUCCESS(apiRepositoryService.updateApi(apiRule, Group.assembleConfigMetaData(group), ncApiRule));


    }

    @Override
    public ResponseVo queryApi(String id) {
        Optional<ApiRule> apiOptional = apiRepositoryService.queryApi(id);
        return apiOptional.orNull() == null ? ResponseVo.SUCCESS(null) : ResponseVo.SUCCESS(apiOptional.transform(new Function<ApiRule, ApiDefinitionVo>() {
            @Override
            public @Nullable ApiDefinitionVo apply(@Nullable ApiRule apiRule) {
                ApiDefinitionVo apiDefinitionVo = new ApiDefinitionVo();
                BeanUtils.copyProperties(apiRule, apiDefinitionVo);
                return apiDefinitionVo;
            }
        }).get());

    }
}
