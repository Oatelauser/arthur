package com.arthur.auth.upms.controller;

import com.arthur.auth.upms.service.OAuth2ClientDetailService;
import com.arthur.auth.user.api.RemoteClientDetailService;
import com.arthur.auth.user.domain.entity.OAuth2ClientDetail;
import com.arthur.web.model.GenericServerResponse;
import com.arthur.web.model.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static com.arthur.web.constant.ServerStatusEnum.EMPTY_DATA;
import static com.arthur.web.model.GenericServerResponse.ofError;
import static com.arthur.web.model.GenericServerResponse.ofSuccess;

/**
 * 用户中心查询接口
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
@Validated
@RestController
@RequiredArgsConstructor
public class OAuth2ClientController implements RemoteClientDetailService {

	private final OAuth2ClientDetailService authService;

	@Override
	public GenericServerResponse<Pageable<OAuth2ClientDetail>> getClientDetails(Integer page, Integer pageSize) {
		Pageable<OAuth2ClientDetail> clientDetails = authService.findAllClientDetails(page, pageSize);
		if (clientDetails == null) {
			return ofError(EMPTY_DATA);
		}
		return ofSuccess(clientDetails);
	}

	@Override
	public GenericServerResponse<OAuth2ClientDetail> getClientDetailById(String clientId) {
		OAuth2ClientDetail clientDetail = authService.getById(clientId);
		if (clientDetail == null) {
			return ofError(EMPTY_DATA);
		}

		return ofSuccess(clientDetail);
	}

}
