package com.broadtech.arthur.admin.rule.entiry.degrade.nc;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
public class NcDegradeRule {
    private Integer grade = 0;
    private Double count;
    private Integer timeWindow;
    private Integer minRequestAmount = 5;
    private Double slowRatioThreshold = 1.0;
    private Integer statIntervalMs = 1000;
    private String resource;
    private String limitApp;
}
