package com.broadtech.arthur.admin.group.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.respones.ReturnCode;
import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.group.repository.GroupService;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
import com.google.common.base.Optional;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Machenike
 * @Date 2022/8/2
 * @Version 1.0.0
 * @Description
 */

@RestController
@Slf4j
@Validated
@RequestMapping("/gateway")
@Tag(name = "路由组", description = "对路由组的增删改查")
public class GroupController {

    @Resource
    GroupBusinessService groupBusinessService;

    @PostMapping("/group")
    @Operation(summary = "新增路由组"
            , description = "通过指定用户和组名新增"
            , parameters = {@Parameter(name = "uId", description = "用户id"
            , schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "groupName", description = "路由组"
                    , schema = @Schema(type = "string"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    public Object createUserRouteGroup(
            @RequestParam("groupName")
            @NotNull
            String groupName,
            @RequestParam("uId")
            @NotNull
            String uId,
            @RequestParam("groupType")
            @NotNull
            String groupType
    ) {

        return groupBusinessService.createRouteGroup(uId, groupName, groupType);

    }


    @Operation(summary = "删除路由组"
            , description = "通过指定用户和组id删除"
            , parameters = {@Parameter(name = "uId", description = "用户id"
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
    @DeleteMapping("/group")
    public Object delUserRouteGroup(
            @RequestParam("uId") @NotNull
            String uId,
            @RequestParam("groupId") @NotNull
            String groupId
    ) {

        return groupBusinessService.delRouteGroup(groupId, uId);

    }


    @Operation(summary = "修改路由组"
            , description = "修改路由组名"
            , parameters = {@Parameter(name = "uId", description = "用户id"
            , schema = @Schema(type = "string", format = "uuid"))
            , @Parameter(name = "groupName", description = "路由组"
            , schema = @Schema(type = "string"))
            , @Parameter(name = "groupId", description = "路由组Id"
            , schema = @Schema(type = "string"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PutMapping("/group")
    public Object upUserRouteGroup(
            @RequestParam("groupId") @NotNull
            String groupId,
            @RequestParam("groupName") @NotNull
            String groupName,
            String confDataId,
            String confGroupId,
            String confNameSpace,
            @RequestParam("uId") @NotNull
            String uId) {

        return groupBusinessService.updateRouteGroup(groupId
                , groupName
                , confDataId
                , confGroupId
                , confNameSpace
                , uId);

    }

    @Operation(summary = "查询路由组"
            , description = "通过指定用户和组id查询"
            , parameters = {@Parameter(name = "uId", description = "用户id"
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
    @GetMapping("/group")
    public Object queryUserRouteGroup(
            @RequestParam("uId") @NotNull String uId,
            @RequestParam("groupId") @NotNull String groupId) {
        return groupBusinessService.queryRouteGroup(groupId);
    }


    @GetMapping("/groupInfo")
    public Object queryUserRouteGroup(
            @RequestParam("uId") @NotNull String uId,
            @RequestParam("groupId") @NotNull String groupId,
            HttpServletRequest req) {

        String key = "aabb";

        try {
            String token = req.getHeader("token");
            JWT    jwt   = JWTUtil.parseToken(token);
            if (!jwt.setKey(key.getBytes()).verify()) {
                return new ResponseVo<String>(null, ReturnCode.ACCESS_DENIED, "no access");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseVo<String>(null, ReturnCode.ACCESS_DENIED, "no access");
        }


        return groupBusinessService.queryRouteGroup(groupId);
    }

    @GetMapping("/token")
    public Object getToken() {
        DateTime now     = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, 10);
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("userName", "zhangsan");
        payload.put("passWord", "666889");

        String key   = "aabb";
        String token = JWTUtil.createToken(payload, key.getBytes());
        return ResponseVo.SUCCESS(token);
    }


}
