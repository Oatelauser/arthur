package com.broadtech.arthur.admin.rule.repository.api.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiRule;
import com.broadtech.arthur.admin.rule.mapper.api.ApiMapper;
import com.broadtech.arthur.admin.rule.repository.api.ApiService;
import org.springframework.stereotype.Service;

/**
 * @author Machenike
 * @description 针对表【api】的数据库操作Service实现
 * @createDate 2022-08-15 15:18:46
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiRule>
        implements ApiService {

}




