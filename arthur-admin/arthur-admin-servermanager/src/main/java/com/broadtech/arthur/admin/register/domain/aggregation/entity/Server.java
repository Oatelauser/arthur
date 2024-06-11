package com.broadtech.arthur.admin.register.domain.aggregation.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 聚合根
 * @author Machenike
 * date 2022/10/19
 * @version 1.0.0
 */
@Data
public class Server {
    private Integer id;
    private String  serverName;
    private String  groupName;
    private String  ip;
    private Integer port;
    private String  clusterName;

}
