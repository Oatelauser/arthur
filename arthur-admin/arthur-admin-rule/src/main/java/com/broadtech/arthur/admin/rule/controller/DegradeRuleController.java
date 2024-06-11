package com.broadtech.arthur.admin.rule.controller;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.api.dto.ApiDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.degrade.dto.DegradeRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleService;
import com.broadtech.arthur.admin.rule.service.degrade.DegradeBusinessService;
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
@Validated
@Tag(name = "网关降级规则", description = "网关降级规则的crud")
public class DegradeRuleController {


    DegradeBusinessService degradeRuleService;

    @Autowired
    public void setDegradeRuleService(DegradeBusinessService degradeRuleService) {
        this.degradeRuleService = degradeRuleService;
    }


    @Operation(summary = "新增降级规则"
            , description = "新增降级规则"
            , parameters = {@Parameter(name = "DegradeRuleDefinitionDTO", description = "degrade info"
            , schema = @Schema(implementation = ApiDefinitionDTO.class, description = "用户自定义降级信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PostMapping("/degrade")
    public Object createGatewayDegrade(@RequestBody @Valid DegradeRuleDefinitionDTO degradeRuleDefinitionDTO) {
        return degradeRuleService.createDegradeRule(degradeRuleDefinitionDTO);
    }


    @Operation(summary = "删除degrade"
            , description = "删除degrade"
            , parameters = {@Parameter(name = "id", description = "删除degrade id"
            , schema = @Schema(implementation = String.class, description = "id")),
            @Parameter(name = "groupId", description = "删除degrade groupId"
                    , schema = @Schema(implementation = String.class, description = "groupId"))
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @DeleteMapping("/degrade")
    public Object delGatewayDegrade(@RequestParam("id") @NotNull String id, @RequestParam("groupId") @NotNull String groupId) {
        return degradeRuleService.deleteDegradeRule(id, groupId);
    }

    @Operation(summary = "查询降级规则"
            , description = "查询降级规则"
            , parameters = {@Parameter(name = "id", description = "degrade id"
            , schema = @Schema(implementation = String.class, description = "id"))}
            , responses = {@ApiResponse(responseCode = "200"
            , content = @Content(mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/degrade")
    public Object queryGatewayDegrade(@RequestParam("id") @NotNull String id) {
        return degradeRuleService.queryDegradeRule(id);
    }


}
