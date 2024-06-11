package com.broadtech.arthur.admin.rule.repository.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.flow.dto.FlowRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.flow.nc.NcFlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.po.FlowRule;
import com.broadtech.arthur.admin.rule.entiry.flow.vo.FlowRuleVo;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/26
 */
public interface FlowRuleRepositoryService  {


    boolean createFlow(FlowRule flowRule, ConfigMetaData metaData, NcFlowRule ncFlowRule);


    boolean deleteFlow(String id, ConfigMetaData metaData);


    boolean updateFlow(FlowRule flowRule, ConfigMetaData metaData, NcFlowRule ncFlowRule);


    Optional<FlowRule> queryFlow(String id);

}
