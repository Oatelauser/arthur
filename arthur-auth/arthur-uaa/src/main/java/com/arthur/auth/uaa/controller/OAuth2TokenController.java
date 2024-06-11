package com.arthur.auth.uaa.controller;

import com.arthur.common.response.ServerResponse;
import com.arthur.oauth2.annotation.AuthPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2令牌请求
 *
 * @author DearYang
 * @date 2022-08-18
 * @since 1.0
 */
@RestController
@RequestMapping("/token")
@Tag(name = "OAuth2令牌", description = "OAuth2令牌请求")
public class OAuth2TokenController {

	@DeleteMapping("/logout")
	@Operation(summary = "登出")
	@Parameter(name = HttpHeaders.AUTHORIZATION, description = "授权头", in = ParameterIn.HEADER)
	public ServerResponse logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth) {
		if (!StringUtils.hasText(auth)) {
			return ServerResponse.ofSuccess();
		}

		String token = auth.replace(TokenType.BEARER.getValue(), "").trim();
		return this.deleteToken(token);
	}

	@AuthPermission
	@PostMapping("/list")
	@Operation(summary = "令牌列表")
	public ServerResponse listToken() {
		return ServerResponse.ofSuccess();
	}

	@AuthPermission
	@DeleteMapping("/{token}")
	@Operation(summary = "删除令牌")
	@Parameter(name = "token", description = "令牌", in = ParameterIn.PATH)
	public ServerResponse deleteToken(@PathVariable String token) {
		return ServerResponse.ofSuccess();
	}

}
