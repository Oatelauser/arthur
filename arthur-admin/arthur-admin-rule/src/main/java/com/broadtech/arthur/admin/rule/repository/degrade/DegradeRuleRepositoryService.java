package com.broadtech.arthur.admin.rule.repository.degrade;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface DegradeRuleRepositoryService {

    boolean createDegradeRule(DegradeRule degradeRule, ConfigMetaData metaData, NcDegradeRule ncDegradeRule);
    boolean deleteDegradeRule(String id, ConfigMetaData metaData);
    boolean updateDegradeRule(DegradeRule degradeRule, ConfigMetaData metaData, NcDegradeRule ncDegradeRule);
    Optional<DegradeRule> queryDegradeRule(String id);

}
