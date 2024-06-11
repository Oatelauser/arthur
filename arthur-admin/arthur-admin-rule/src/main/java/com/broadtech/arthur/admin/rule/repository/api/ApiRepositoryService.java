package com.broadtech.arthur.admin.rule.repository.api;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.api.nc.NcApiRule;
import com.broadtech.arthur.admin.rule.entiry.api.po.ApiRule;
import com.broadtech.arthur.common.config.ConfigMetaData;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface ApiRepositoryService {

    boolean createApi(ApiRule apiRule, ConfigMetaData metaData, NcApiRule ncApiRule);
    boolean deleteApi(String id, ConfigMetaData metaData);
    boolean updateApi(ApiRule apiRule, ConfigMetaData metaData, NcApiRule ncApiRule);
    Optional<ApiRule> queryApi(String id);


}
