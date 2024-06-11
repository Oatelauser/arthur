package com.broadtech.arthur.admin.group.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.broadtech.arthur.admin.common.config.MetaConfigProperties;
import com.broadtech.arthur.admin.common.execption.BaseException;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.IdempotentServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.entity.vo.GroupVo;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.utils.NanoIdUtils;
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
 * @date 2022/9/27
 */
@Service
@Slf4j
public class GroupBusinessServiceImpl implements GroupBusinessService {
    @Resource
    MetaConfigProperties   metaConfigProperties;
    @Resource
    GroupRepositoryService groupRepositoryService;
    @Resource
    IdempotentServiceImpl  idempotentService;


    @Override
    public ResponseVo createRouteGroup(String uId, String groupName, String groupType) {

        String routeGroupId = NanoIdUtils.randomNanoId();
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(routeGroupId + "createRoute"),
                BaseException.RepeatedSubmitException.of(routeGroupId));
        //获取全局的元数据
        ConfigMetaData meta = metaConfigProperties.getNcMeta();
        //获取分组类型对应的元数据
        ConfigMetaData groupMetadata = metaConfigProperties.getNcGroupMeta(routeGroupId, groupName, groupType);

        Group routeGroup = Group.builder()
                .id(routeGroupId)
                .uid(uId)
                .groupName(groupName)
                .confDataId(groupMetadata.getDataId())
                .confGroupId(groupMetadata.getGroup())
                .confNameSpace(groupMetadata.getNamespace())
                .confDataType(groupMetadata.getDataType())
                .build();
        return ResponseVo.SUCCESS(groupRepositoryService.createRouteGroup(routeGroup, meta, groupMetadata));
    }

    @Override
    public ResponseVo delRouteGroup(String groupId, String uId) {

        Group group = Preconditions.checkNotNull(groupRepositoryService.queryRouteGroup(groupId).orNull(),"获取分组信息失败");

        ConfigMetaData metaData = metaConfigProperties.getNcMeta();

        ConfigMetaData configMetaData = ConfigMetaData.builder()
                .dataId(group.getConfDataId())
                .group(group.getConfGroupId())
                .namespace(group.getConfNameSpace())
                .dataType(group.getConfDataType()).build();

        return ResponseVo.SUCCESS(groupRepositoryService.delRouteGroup(groupId, metaData, configMetaData));
    }

    @Override
    public ResponseVo updateRouteGroup(String groupId, String groupName, String confDataId, String confGroupId, String confNameSpace, String uId) {
        Preconditions.checkState(
                !idempotentService.checkDupSubmit(groupId + "createRoute"),
                BaseException.RepeatedSubmitException.of(groupId));

        Group group = new Group();
        group.setId(groupId);
        group.setGroupName(groupName);
        return ResponseVo.SUCCESS(groupRepositoryService.updateRouteGroup(group, null, null));
    }




    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public static ResponseVo helloFallback(String groupId) {
        return ResponseVo.SUCCESS(String.format("Halooooo %s", groupId));
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public static ResponseVo exceptionHandler(String groupId, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return ResponseVo.SUCCESS("Oops, error occurred at " + groupId);
    }
    @SentinelResource(value = "hello", blockHandler = "exceptionHandler", fallback = "helloFallback")
    @Override
    public ResponseVo queryRouteGroup(String groupId) {
        Optional<Group> groupOptional = groupRepositoryService.queryRouteGroup(groupId);
        return groupOptional.orNull() == null ? ResponseVo.SUCCESS(null) : ResponseVo.SUCCESS(groupOptional.transform(new Function<Group, GroupVo>() {
            @Override
            public @Nullable GroupVo apply(@Nullable Group group) {

                GroupVo groupVo = new GroupVo();
                BeanUtils.copyProperties(group,groupVo);
                return groupVo;

            }
        }).get());
    }
}
