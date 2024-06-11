package com.broadtech.arthur.admin.rule.mapper.degrade;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Machenike
* @description 针对表【degrade_rule】的数据库操作Mapper
* @createDate 2022-08-15 15:18:46
* @Entity com.broadtech.arthur.admin.entity.rule2.DegradeRuleDefinitionDTO
*/
@Mapper
public interface DegradeRuleMapper extends BaseMapper<DegradeRule> {

}




