package com.broadtech.arthur.admin.group.service;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
public interface GroupBusinessService {
     /**
      * 创建路由组
      *
      * @param uId       你id
      * @param groupName 组名称
      * @param groupType 组类型
      * @return {@link ResponseVo}
      */
     ResponseVo createRouteGroup(String uId, String groupName, String groupType);

     /**
      * 德尔路线组
      *
      * @param groupId 组id
      * @param uId     你id
      * @return {@link ResponseVo}
      */
     ResponseVo delRouteGroup(String groupId,String uId);

     /**
      * 更新路由组
      *
      * @param groupId       组id
      * @param groupName     组名称
      * @param confDataId    配置数据id
      * @param confGroupId   会议组id
      * @param confNameSpace 配置名称空间
      * @param uId           你id
      * @return {@link ResponseVo}
      */
     ResponseVo updateRouteGroup(String groupId, String groupName, String confDataId, String confGroupId, String confNameSpace, String uId);

     /**
      * 查询路线组
      *
      * @param groupId 组id
      * @return {@link Optional}<{@link Group}>
      */
     ResponseVo queryRouteGroup(String groupId);
}
