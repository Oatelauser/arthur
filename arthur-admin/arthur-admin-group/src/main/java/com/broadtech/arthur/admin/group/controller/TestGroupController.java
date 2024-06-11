package com.broadtech.arthur.admin.group.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.respones.ReturnCode;
import com.broadtech.arthur.admin.group.service.GroupBusinessService;
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
public class TestGroupController {

    @Resource
    GroupBusinessService groupBusinessService;


    @GetMapping("/service/groupInfo")
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

    @GetMapping("/service/token")
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
