package com.broadtech.arthur.admin.route.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.common.respones.ResponseVo;

import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.broadtech.arthur.admin.route.entity.vo.RouteDefinitionV0;
import com.google.common.base.Optional;

import java.util.List;

/**
* @author Machenike
* @description 针对表【api_route(路由表)】的数据库操作Service
* @createDate 2022-08-02 16:15:08
*/
public interface RouteService extends IService<RouteDefinition> {



}
