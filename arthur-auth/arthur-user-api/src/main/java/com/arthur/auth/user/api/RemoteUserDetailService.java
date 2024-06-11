package com.arthur.auth.user.api;

import com.arthur.auth.user.domain.entity.UserInfo;
import com.arthur.web.model.GenericServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.arthur.auth.user.constant.InternalAuthConstants.*;
import static com.arthur.auth.user.constant.UserConstants.USER_SERVICE_NAME;

/**
 * 用户详情信息
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
@Tag(name = "用户详情信息", description = "用户详情信息相关操作")
@FeignClient(value = USER_SERVICE_NAME, contextId = "RemoteUserDetailService")
public interface RemoteUserDetailService {

	@GetMapping(value = "/user/info/{username}", headers = INTERNAL_AUTH_HEADER)
	@Operation(summary = "查询客户端详情信息", description = "通过用户名查询客户端详情信息")
	@Parameter(name = "username", description = "用户名", required = true, in = ParameterIn.PATH)
	@Parameter(name = INTERNAL_AUTH_HEADER_NAME, description = "内部请求授权头", required = true,
		in = ParameterIn.HEADER, example = INTERNAL_AUTH_HEADER_VALUE)
	GenericServerResponse<UserInfo> getUserInfo(
		@PathVariable @Validated @NotBlank(message = "用户名不能为空") String username);

}
