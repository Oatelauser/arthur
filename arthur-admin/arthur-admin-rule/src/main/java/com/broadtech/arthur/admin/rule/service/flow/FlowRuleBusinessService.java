package com.broadtech.arthur.admin.rule.service.flow;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.flow.dto.FlowRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.vo.FlowRuleVo;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface FlowRuleBusinessService {



    ResponseVo createFlow(FlowRuleDefinitionDTO flowDto);

    ResponseVo deleteFlow(String id,String groupId);

    ResponseVo updateFlow(FlowRuleDefinitionDTO flowDto);

    ResponseVo queryFlow(String id);

}
