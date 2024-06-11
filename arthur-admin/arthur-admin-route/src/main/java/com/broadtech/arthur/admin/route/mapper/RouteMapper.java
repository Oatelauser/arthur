package com.broadtech.arthur.admin.route.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import org.apache.ibatis.annotations.Mapper;

/**
 *
* @author Machenike
* @description 针对表【api_route(路由表)】的数据库操作Mapper
* @createDate 2022-08-02 16:15:08
* @Entity com.broadtech.arthur.admin.entity.route.definition.ApiRouteDefinition
*/
@Mapper
public interface RouteMapper extends BaseMapper<RouteDefinition> {



}




