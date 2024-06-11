package com.broadtech.arthur.admin.rule.entiry.system.nc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NcSystemRule {
    private Double highestSystemLoad = -1.0;
    private Double highestCpuUsage = -1.0;
    private Double qps = -1.0;
    private Long avgRt = -1L;
    private Long maxThread = -1L;
    private String resource;
    private String limitApp;
}
