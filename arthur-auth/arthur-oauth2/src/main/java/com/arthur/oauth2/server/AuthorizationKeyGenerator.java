package com.arthur.oauth2.server;

import com.arthur.common.constant.BaseConstants;
import com.arthur.oauth2.constant.OAuth2Constants;

/**
 * 生成{@link RedisOAuth2AuthorizationService}的redis key
 *
 * @author DearYang
 * @date 2022-09-05
 * @since 1.0
 */
public class AuthorizationKeyGenerator implements RedisKeyGenerator<String[]> {

	@Override
	public String generate(String[] data) {
		return OAuth2Constants.TOKEN_KEY_PREFIX + BaseConstants.COLON + data[0] + BaseConstants.COLON + data[1];
	}

}
