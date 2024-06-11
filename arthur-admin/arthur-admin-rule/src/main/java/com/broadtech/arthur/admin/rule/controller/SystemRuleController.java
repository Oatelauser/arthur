package com.broadtech.arthur.admin.rule.controller;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.system.dto.SystemRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.system.vo.SystemRuleVo;
import com.broadtech.arthur.admin.rule.repository.system.SystemRuleService;
import com.broadtech.arthur.admin.rule.service.system.SystemRuleBusinessService;
import com.google.common.base.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@RestController
@RequestMapping("/gateway")
@Validated
@Slf4j
@Tag(name = "系统规则", description = "系统规则的crud")
@Repository
public class SystemRuleController {


    SystemRuleBusinessService systemRuleService;

    @Autowired
    public void setSystemRuleService(SystemRuleBusinessService systemRuleService) {
        this.systemRuleService = systemRuleService;
    }

    @Operation(summary = "新增系统规则"
            , description = "新增系统规则"
            , parameters = {@Parameter(name = "systemRuleDefinitionDTO", description = "system info"
            , schema = @Schema(implementation = SystemRuleDefinitionDTO.class, description = "用户自定义系统信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PostMapping("/systemRule")
    public Object createSystemRule(@RequestBody @Valid SystemRuleDefinitionDTO systemRuleDefinitionDTO) {
        return systemRuleService.createSystemRule(systemRuleDefinitionDTO);
    }


    @Operation(summary = "删除system"
            , description = "删除system"
            , parameters = {@Parameter(name = "id", description = "删除system id"
            , schema = @Schema(implementation = String.class, description = "id")),
            @Parameter(name = "groupId", description = "删除system groupId"
                    , schema = @Schema(implementation = String.class, description = "groupId"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @DeleteMapping("/systemRule")
    public Object delSystemRule(@RequestParam("id") @NotNull String id, @RequestParam("groupId") @NotNull String groupId) {
        return systemRuleService.deleteSystemRule(id, groupId);
    }

    @Operation(summary = "查询system"
            , description = "查询system"
            , parameters = {@Parameter(name = "id", description = "查询system id"
            , schema = @Schema(implementation = String.class, description = "id"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/systemRule")
    public Object querySystemRule(@RequestParam("id") @NotNull String id) {
        return systemRuleService.querySystemRule(id);
    }

}
