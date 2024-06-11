package com.broadtech.arthur.admin.rule.service.system;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.system.dto.SystemRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.system.vo.SystemRuleVo;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface SystemRuleBusinessService {

    ResponseVo createSystemRule(SystemRuleDefinitionDTO systemDto);

    ResponseVo deleteSystemRule(String id, String groupId);

    ResponseVo updateSystemRule(SystemRuleDefinitionDTO systemDto);

    ResponseVo querySystemRule(String id);
}
