package com.broadtech.arthur.admin.rule.service.api;

import com.broadtech.arthur.admin.common.respones.ResponseVo;
import com.broadtech.arthur.admin.rule.entiry.api.dto.ApiDefinitionDTO;
import com.broadtech.arthur.admin.rule.entiry.api.vo.ApiDefinitionVo;
import com.google.common.base.Optional;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/28
 */
public interface ApiBusinessService {

    ResponseVo createApi(ApiDefinitionDTO api);
    ResponseVo deleteApi(String id,String groupId);
    ResponseVo updateApi(ApiDefinitionDTO api);
    ResponseVo queryApi(String id);

}
