package com.broadtech.arthur.admin.register.interfaces.rest;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.register.application.dto.ServerDto;
import com.broadtech.arthur.admin.register.application.service.cmd.RegisterServerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
@RestController
@RequestMapping("/server")
@Slf4j
@Tag(name = "服务注册")
@Validated
@RequiredArgsConstructor
public class ServiceRegisterController {

    public final RegisterServerService registerServerService;

    @PostMapping("/register")
    public Object registerServiceInstance(@RequestBody @NotNull @Valid ServerDto serverDto) {
        return ResponseVo.SUCCESS(registerServerService.register(serverDto));
    }


    @PostMapping("/deregister")
    public Object unRegisterServiceInstance(@RequestBody @NotNull @Valid ServerDto serverDto) {
        return ResponseVo.SUCCESS(registerServerService.deregister(serverDto));
    }


}
