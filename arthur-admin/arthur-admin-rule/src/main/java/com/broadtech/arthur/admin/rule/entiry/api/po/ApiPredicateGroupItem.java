package com.broadtech.arthur.admin.rule.entiry.api.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPredicateGroupItem  {
    private  Set<ApiPathPredicateItem> items = new HashSet<>();
}
