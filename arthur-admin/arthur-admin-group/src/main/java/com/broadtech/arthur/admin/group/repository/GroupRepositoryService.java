package com.broadtech.arthur.admin.group.repository;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.group.entity.dto.GroupDto;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
public interface GroupRepositoryService {


    /**
     * 创建路由组
     *
     * @param routeGroup    路由组
     * @param meta          元
     * @param groupMetadata 组元数据
     * @return {@link ResponseVo}
     */
    String createRouteGroup(Group routeGroup, ConfigMetaData meta, ConfigMetaData groupMetadata);

    /**
     * 德尔路线组
     *
     * @param groupId          集团
     * @param metaData       元数据
     * @param configMetaData 配置元数据
     * @return {@link ResponseVo}
     */
    boolean delRouteGroup(String id,ConfigMetaData metaData,ConfigMetaData configMetaData);


    /**
     * 更新路由组
     *
     * @param routeGroup    路由组
     * @param meta          元
     * @param groupMetadata 组元数据
     * @return {@link ResponseVo}
     */
    boolean updateRouteGroup(Group routeGroup,ConfigMetaData meta,ConfigMetaData groupMetadata);


    /**
     * 查询路线组
     *
     * @param id 组id
     * @return {@link Optional}<{@link Group}>
     */
    Optional<Group> queryRouteGroup(String id);

}
