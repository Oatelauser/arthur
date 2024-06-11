package com.broadtech.arthur.admin.rule.repository.degrade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.degrade.dto.DegradeRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.vo.DegradeRuleVo;
import com.google.common.base.Optional;


/**
* @author Machenike
* @description 针对表【degrade_rule】的数据库操作Service
* @createDate 2022-08-15 15:18:46
*/
public interface DegradeRuleService extends IService<DegradeRule> {
}
