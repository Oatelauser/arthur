package com.broadtech.arthur.admin.user.controller;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/13
 */
@RestController
@Slf4j
@Tag(name = "User", description = "User Home")
@RequestMapping("/gateway")
@Validated
public class UserController {


    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
//
    public Object createUser(
            @RequestParam(value = "userName") @NotNull String userName
            , @RequestParam(value = "userPassword") @NotNull String userPassword
            , @RequestParam(value = "msisdn") @NotNull int msisdn
            , @RequestParam(value = "email") @NotNull String email
            , @RequestParam(value = "gender") @NotNull int gender
            , @RequestParam(value = "isDelete", required = false, defaultValue = "0") @NotNull int isDelete
    ) {
        return userService.createUser(userName, userPassword
                , msisdn, email
                , gender, isDelete);
    }

    @GetMapping("/query/user")
    public Object queryUser(@RequestParam("id") @NotNull Integer id) {
        return ResponseVo.SUCCESS(userService.queryUserById(id));
    }

    @GetMapping("/query/currentUser")
    public Object queryUser(
            @RequestParam("current") @NotNull Integer current,
            @RequestParam("size") @NotNull Integer size) {
        return userService.queryUserByPage(current, size);
    }


}
