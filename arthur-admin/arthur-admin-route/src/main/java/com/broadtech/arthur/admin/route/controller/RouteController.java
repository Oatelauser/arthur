package com.broadtech.arthur.admin.route.controller;


import com.broadtech.arthur.admin.common.annotation.CheckRoute;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.route.entity.dto.RouteDefinitionDTO;
import com.broadtech.arthur.admin.route.entity.po.RouteDefinition;
import com.broadtech.arthur.admin.route.service.RouteBusinessService;
import com.broadtech.arthur.common.utils.NanoIdUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author Machenike
 * @Date 2022/7/20
 * @Version 1.0.0
 * @Description
 */
@RestController
@RequestMapping("/route")
@Slf4j
@Tag(name = "路由模块")
@Validated
public class RouteController {



    @Resource
    RouteBusinessService routeService;


    @Operation(summary = "新增路由"
            , description = "新增路由"
            , parameters = {@Parameter(name = "apiRoute", description = "路由dto"
            , schema = @Schema(implementation = RouteDefinition.class, description = "用户自定义路由信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PostMapping("/create")
    public Object createRoute(@RequestBody @Valid RouteDefinitionDTO routeDefinitionDTO) {
            routeDefinitionDTO.setId(NanoIdUtils.randomNanoId());
            return routeService.createRoute(routeDefinitionDTO);

    }

    @Operation(summary = "删除路由"
            , description = "通过指定id和组id删除"
            , parameters = {@Parameter(name = "uId", description = "路由id"
            , schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "groupId", description = "路由组Id"
                    , schema = @Schema(type = "string"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @DeleteMapping("/del")
    public Object delRoute(
            @RequestParam("id") @NotNull String id
            , @RequestParam("groupId") @NotNull String groupId) {


            return routeService.delRoute(id, groupId);

    }

    @Operation(summary = "修改路由"
            , description = "修改路由"
            , parameters = {@Parameter(name = "apiRoute", description = "路由dto"
            , schema = @Schema(implementation = RouteDefinition.class, description = "用户自定义路由信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PutMapping("/update")
    public Object updateRoute(@RequestBody @Valid RouteDefinitionDTO routeDefinitionDTO) {

            return routeService.updateRoute(routeDefinitionDTO);


    }

    @Operation(summary = "查询路由"
            , description = "通过指定id和组id查询"
            , parameters = {@Parameter(name = "uId", description = "路由id"
            , schema = @Schema(type = "string", format = "uuid"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/query")
    public Object queryRoute(@RequestParam("id") @NotNull String routeId) {
            return routeService.queryRoute(routeId);
    }


    @Operation(summary = "查询路由"
            , description = "通过指定id和组id查询"
            , parameters = {@Parameter(name = "uId", description = "路由id"
            , schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "groupId", description = "路由组Id"
                    , schema = @Schema(type = "string"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/group/route")
    public Object queryRouteUnderGroup(@RequestParam("groupId") @NotNull String groupId) {
            return routeService.queryRouteUnderGroup(groupId);
    }
}
