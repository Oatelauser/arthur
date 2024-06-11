package com.broadtech.arthur.admin.rule.entiry.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemRuleVo {
    private String id;
    private String groupId;
    private Double highestSystemLoad = -1.0;
    private Double highestCpuUsage = -1.0;
    private Double qps = -1.0;
    private Long avgRt = -1L;
    private Long maxThread = -1L;
    private String resource;
    private String limitApp;
}
