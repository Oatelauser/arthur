package com.broadtech.arthur.admin.auth.entity;


import lombok.Data;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/20
 */
@Data
public class UserPermissionInfo {

    private String id;

    private String userName;

    private String userPassword;

    private String menuCode;

    private String permission;

    private String endpoint;

    private Integer isDelete;

}
