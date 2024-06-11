package com.broadtech.arthur.admin.rule.controller;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.flow.dto.FlowRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.repository.flow.FlowRuleService;
import com.broadtech.arthur.admin.rule.service.flow.FlowRuleBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RequestMapping("/gateway")
@Tag(name = "网关限流规则", description = "网关限流规则crud")
@Validated
public class FlowRuleController {

    FlowRuleBusinessService flowRuleService;

    @Autowired
    public void setGatewayFlowRuleService(FlowRuleBusinessService flowRuleService) {
        this.flowRuleService = flowRuleService;
    }

    @Operation(summary = "新增限流规则"
            , description = "新增限流规则"
            , parameters = {@Parameter(name = "flowRule", description = "flow info"
            , schema = @Schema(implementation = FlowRuleDefinitionDTO.class, description = "用户自定义限流信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PostMapping("/flow")
    public Object createGwFlowRule(@RequestBody @Valid FlowRuleDefinitionDTO flowRule) {
        return flowRuleService.createFlow(flowRule);
    }


    @Operation(summary = "修改限流规则"
            , description = "修改限流规则"
            , parameters = {@Parameter(name = "flowRule", description = "flow info"
            , schema = @Schema(implementation = FlowRuleDefinitionDTO.class, description = "用户自定义限流信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PutMapping("/flow")
    public Object updateGwFlowRule(@RequestBody @Valid FlowRuleDefinitionDTO flowRule) {
        return flowRuleService.updateFlow(flowRule);
    }


    @Operation(summary = "删除Flow规则"
            , description = "删除Flow规则"
            , parameters = {@Parameter(name = "id", description = "删除Flow id"
            , schema = @Schema(implementation = String.class, description = "id")),
            @Parameter(name = "groupId", description = "Flow groupId"
                    , schema = @Schema(implementation = String.class, description = "groupId"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @DeleteMapping("/flow")
    public Object delGwFlowRule(@RequestParam("id") @NotNull String id, @RequestParam("groupId") @NotNull String groupId) {
        return flowRuleService.deleteFlow(id, groupId);
    }

    @Operation(summary = "查询Flow规则"
            , description = "查询Flow规则"
            , parameters = {@Parameter(name = "id", description = "查询Flow id"
            , schema = @Schema(implementation = String.class, description = "id"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/flow")
    public Object queryGwFlowRule(@RequestParam("id") @NotNull String id) {
        return flowRuleService.queryFlow(id);
    }

}
