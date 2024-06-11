package com.broadtech.arthur.admin.common.template;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/18
 */
@NoArgsConstructor
@AllArgsConstructor
public enum CacheTypeEnum {
    GROUP("GROUP"),
    API("API"),
    FLOW("FLOW"),
    SYSTEM("SYSTEM"),
    DEGRADE("DEGRADE"),
    ROUTE("ROUTE");
    String type;

}
