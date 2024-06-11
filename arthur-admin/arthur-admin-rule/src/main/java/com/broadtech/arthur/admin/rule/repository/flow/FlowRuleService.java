package com.broadtech.arthur.admin.rule.repository.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.flow.dto.FlowRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.vo.FlowRuleVo;
import com.google.common.base.Optional;


/**
* @author Machenike
* @description 针对表【gateway_flow_rule】的数据库操作Service
* @createDate 2022-08-15 15:18:46
*/
public interface FlowRuleService extends IService<FlowRule> {

}
