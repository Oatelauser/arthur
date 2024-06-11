package com.broadtech.arthur.admin.register.application.dto;

import com.alibaba.nacos.api.common.Constants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Machenike
 * date 2022/10/18
 * @version 1.0.0
 */
@Data
@Builder
public class ServerDto {

    @NotNull
    private String  serverName;
    private String  groupName   = Constants.GROUP;
    @NotNull
    private String  ip;
    @NotNull
    private Integer port;
    private String  clusterName = Constants.DEFAULT_CLUSTER_NAME;

}



