package com.broadtech.arthur.admin.rule.repository.system;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.admin.rule.entiry.system.nc.NcSystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.po.SystemRule;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface SystemRuleRepositoryService {
    boolean createSystemRule(SystemRule systemRule, ConfigMetaData metaData, NcSystemRule ncSystemRule);
    boolean deleteSystemRule(String id, ConfigMetaData metaData);
    boolean updateSystemRule(SystemRule systemRule, ConfigMetaData metaData, NcSystemRule ncSystemRule);
    Optional<SystemRule> querySystemRule(String id);
}
