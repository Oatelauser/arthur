package com.broadtech.arthur.admin.rule.service.degrade;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.degrade.dto.DegradeRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.degrade.vo.DegradeRuleVo;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface DegradeBusinessService {
    /**
     * 创建降级规则
     *
     * @param degradeRuleDefinitionDTO 降低规则定义dto
     * @return {@link ResponseVo}
     */
    ResponseVo createDegradeRule(DegradeRuleDefinitionDTO degradeRuleDefinitionDTO);

    /**
     * 删除降解规律
     *
     * @param id      id
     * @param groupId 组id
     * @return {@link ResponseVo}
     */
    ResponseVo deleteDegradeRule(String id,String groupId);

    /**
     * 降低更新规则
     *
     * @param degradeRuleDefinitionDTO 降低规则定义dto
     * @return {@link ResponseVo}
     */
    ResponseVo updateDegradeRule(DegradeRuleDefinitionDTO degradeRuleDefinitionDTO);

    /**
     * 查询降级规则
     *
     * @param id id
     * @return {@link Optional}<{@link DegradeRuleVo}>
     */
    ResponseVo queryDegradeRule(String id);

}
