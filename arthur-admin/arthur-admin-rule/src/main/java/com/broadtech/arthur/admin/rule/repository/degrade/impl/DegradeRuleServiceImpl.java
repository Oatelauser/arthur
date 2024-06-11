package com.broadtech.arthur.admin.rule.repository.degrade.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.common.template.CacheTypeEnum;

import com.broadtech.arthur.admin.group.entity.po.Group;
import com.broadtech.arthur.admin.rule.entiry.degrade.dto.DegradeRuleDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.degrade.nc.NcDegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.po.DegradeRule;
import com.broadtech.arthur.admin.rule.entiry.degrade.vo.DegradeRuleVo;
import com.broadtech.arthur.admin.rule.mapper.degrade.DegradeRuleMapper;
import com.broadtech.arthur.admin.rule.repository.degrade.DegradeRuleService;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.broadtech.arthur.common.utils.NanoIdUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Machenike
 * @description 针对表【degrade_rule】的数据库操作Service实现
 * @createDate 2022-08-15 15:18:46
 */
@Service
public class DegradeRuleServiceImpl extends ServiceImpl<DegradeRuleMapper, DegradeRule>
        implements DegradeRuleService {
}




