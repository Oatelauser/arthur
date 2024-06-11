package com.broadtech.arthur.admin.rule.repository.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.system.dto.SystemRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.system.po.SystemRule;
import com.broadtech.arthur.admin.rule.entiry.system.vo.SystemRuleVo;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @description 针对表【system_rule】的数据库操作Service
 * @createDate 2022-08-15 15:18:46
 */
public interface SystemRuleService extends IService<SystemRule> {
}
