package com.broadtech.arthur.admin.rule.controller;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.api.dto.ApiDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.api.vo.ApiDefinitionVo;
import com.broadtech.arthur.admin.rule.repository.api.ApiService;
import com.broadtech.arthur.admin.rule.service.api.ApiBusinessService;
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
@Validated
@RequestMapping("/gateway")
@Tag(name = "gateway api信息", description = "对gateway api的增删改查")
public class ApiController {

    ApiBusinessService apiService;

    @Autowired
    public void setApiService(ApiBusinessService apiService) {
        this.apiService = apiService;
    }

    @Operation(summary = "新增api"
            , description = "新增api"
            , parameters = {@Parameter(name = "apiDefinitionDTO", description = "api info"
            , schema = @Schema(implementation = ApiDefinitionDTO.class, description = "用户自定义api信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PostMapping("/api")
    public Object createApi(@RequestBody @Valid ApiDefinitionDTO apiDefinitionDTO) {
        return apiService.createApi(apiDefinitionDTO);
    }

    @Operation(summary = "删除api"
            , description = "通过指定id删除"
            , parameters = {@Parameter(name = "id", description = "api id"
            , schema = @Schema(type = "string", format = "uuid"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    public Object delApi(@RequestParam("id") @NotNull String id, @RequestParam("groupId") @NotNull String groupId) {
        return apiService.deleteApi(id, groupId);
    }


    @Operation(summary = "修改api"
            , description = "修改api"
            , parameters = {@Parameter(name = "apiDefinitionDTO", description = "api info"
            , schema = @Schema(implementation = ApiDefinitionDTO.class, description = "用户自定义api信息")),
    }
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @PutMapping("/api")
    public Object updateApi(@RequestBody @Valid ApiDefinitionDTO apiDefinitionDTO) {
        return apiService.updateApi(apiDefinitionDTO);
    }

    @Operation(summary = "查询api"
            , description = "查指定id删除"
            , parameters = {@Parameter(name = "id", description = "api id"
            , schema = @Schema(type = "string", format = "uuid"))}
            , responses = {@ApiResponse(
            responseCode = "200"
            , content = @Content(
            mediaType = "application/json"
            , schema = @Schema(implementation = ResponseVo.class)
    )
    )})
    @GetMapping("/api")
    public Object queryApi(@RequestParam("id") @NotNull String id) {
        return apiService.queryApi(id);
    }


}
