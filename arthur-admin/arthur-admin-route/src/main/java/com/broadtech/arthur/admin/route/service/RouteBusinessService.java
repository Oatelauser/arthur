package com.broadtech.arthur.admin.route.service;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.vo.RouteDefinitionV0;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/27
 */
public interface RouteBusinessService {


    /**
     * 创建路线
     *
     * @param routeDto 路线dto
     * @return {@link ResponseVo}<{@link String}>
     */
    ResponseVo<String> createRoute(RouteDefinitionDTO routeDto);

    /**
     * 德尔路线
     *
     * @param id  id
     * @param gId g id
     * @return {@link ResponseVo}<{@link Integer}>
     */
    ResponseVo<Integer> delRoute(String id,String gId);

    /**
     * 更新路线
     *
     * @param route 路线
     * @return {@link ResponseVo}<{@link Integer}>
     */
    ResponseVo<Integer> updateRoute(RouteDefinitionDTO route);

    /**
     * 查询路线
     *
     * @param id id
     * @return {@link ResponseVo}
     */
    ResponseVo queryRoute(String id);

    /**
     * 查询路线下组
     *
     * @param gId g id
     * @return {@link ResponseVo}<{@link List}<{@link RouteDefinitionV0}>>
     */
    ResponseVo queryRouteUnderGroup(String gId);

}
