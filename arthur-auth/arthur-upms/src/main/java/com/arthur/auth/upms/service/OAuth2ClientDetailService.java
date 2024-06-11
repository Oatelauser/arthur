package com.arthur.auth.upms.service;

import com.arthur.auth.user.domain.entity.OAuth2ClientDetail;
import com.arthur.web.model.Pageable;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OAuth2ClientDetailService extends IService<OAuth2ClientDetail> {

    Pageable<OAuth2ClientDetail> findAllClientDetails(Integer page, Integer pageSize);

}
