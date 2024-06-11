package com.broadtech.arthur.admin.group.repository.Impl;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.service.impl.NcMetaServiceImpl;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupRepositoryService;
import com.broadtech.arthur.admin.group.repository.GroupService;
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
 * @date 2022/9/27
 */
@Service
@Slf4j
public class GroupRepositoryServiceImpl implements GroupRepositoryService {


    @Resource
    NcMetaServiceImpl ncMetaService;

    @Resource
    GroupService groupService;

    @Resource
    ConfigTemplate template;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String createRouteGroup(Group routeGroup, ConfigMetaData meta, ConfigMetaData groupMetadata) {
        groupService.save(routeGroup);
//        //生成分组类型元数据文件是否成功
        ncMetaService.save(template, groupMetadata, null, null);
        //生成全局元数据文件是否成功
        ncMetaService.save(template, meta, routeGroup.getId(), groupMetadata);
        return routeGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "group_cache", key = "#routeGroup.id")
    public boolean updateRouteGroup(Group routeGroup, ConfigMetaData meta, ConfigMetaData groupMetadata) {
        //查看是否成功

        return groupService.updateById(routeGroup);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(value = "group_cache", key = "#id")
    public boolean delRouteGroup(String id, ConfigMetaData metaData, ConfigMetaData configMetaData) {

        //删除全局元数据信息                                                      //删除元数据对应文件
        return ncMetaService.removeById(template, metaData, id) && ncMetaService.removeAll(template, configMetaData);


    }


    @Override
    @Cacheable(value = "group_cache", key = "#id")
    public Optional<Group> queryRouteGroup(String id) {
        Group group = groupService.getById(id);
        return group == null ? Optional.absent() : Optional.of(group);
    }
}
