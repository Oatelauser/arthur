package com.arthur.auth.user.api;

import com.arthur.auth.user.domain.entity.OAuth2ClientDetail;
import com.arthur.web.model.GenericServerResponse;
import com.arthur.web.model.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.arthur.auth.user.constant.InternalAuthConstants.*;
import static com.arthur.auth.user.constant.UserConstants.USER_SERVICE_NAME;


/**
 * 查询OAuth2客户端信息
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
@Tag(name = "查询客户端信息", description = "OAuth2查询客户端信息")
@FeignClient(value = USER_SERVICE_NAME, contextId = "RemoteClientDetailService")
public interface RemoteClientDetailService {

	/**
	 * 客户端详情列表
	 *
	 * @return {@link OAuth2ClientDetail}
	 */
	@Operation(summary = "查询客户端详情列表")
	@GetMapping(value = "/client/details", headers = INTERNAL_AUTH_HEADER)
	@Parameter(name = "page", description = "当前页数", allowEmptyValue = true, in = ParameterIn.QUERY)
	@Parameter(name = "pageSize", description = "每页数据量", allowEmptyValue = true, in = ParameterIn.QUERY)
	@Parameter(name = INTERNAL_AUTH_HEADER_NAME, description = "内部请求授权头", required = true,
		in = ParameterIn.HEADER, example = INTERNAL_AUTH_HEADER_VALUE)
	GenericServerResponse<Pageable<OAuth2ClientDetail>> getClientDetails(
		@RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "10") Integer pageSize);

	/**
	 * 查询客户端详情
	 *
	 * @param clientId 客户端ID
	 * @return {@link OAuth2ClientDetail}
	 */
	@Operation(summary = "查询客户端详情")
	@Parameter(name = "clientId", description = "客户端ID", required = true)
	@GetMapping(value = "/client/{clientId}", headers = INTERNAL_AUTH_HEADER)
	@Parameter(name = "clientId", description = "客户端ID", required = true, in = ParameterIn.PATH)
	@Parameter(name = INTERNAL_AUTH_HEADER_NAME, description = "内部请求授权头", required = true,
		in = ParameterIn.HEADER, example = INTERNAL_AUTH_HEADER_VALUE)
	GenericServerResponse<OAuth2ClientDetail> getClientDetailById(
		@PathVariable @Validated @NotBlank(message = "clientId不能为空") String clientId);

}
