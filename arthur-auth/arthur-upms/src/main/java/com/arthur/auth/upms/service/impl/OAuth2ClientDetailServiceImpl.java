package com.arthur.auth.upms.service.impl;

import com.arthur.auth.upms.mapper.OAuth2ClientDetailMapper;
import com.arthur.auth.upms.service.OAuth2ClientDetailService;
import com.arthur.auth.user.domain.entity.OAuth2ClientDetail;
import com.arthur.boot.mybatis.model.PagedGridResult;
import com.arthur.web.model.Pageable;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OAuth2ClientDetailServiceImpl extends ServiceImpl<OAuth2ClientDetailMapper,
		OAuth2ClientDetail> implements OAuth2ClientDetailService {

	@Override
	public Pageable<OAuth2ClientDetail> findAllClientDetails(Integer page, Integer pageSize) {
		Page<OAuth2ClientDetail> detailPage = this.page(new Page<>(page, pageSize));
		return PagedGridResult.getPage(detailPage);
	}

}
