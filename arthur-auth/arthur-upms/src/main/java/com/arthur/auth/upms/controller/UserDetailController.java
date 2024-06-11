package com.arthur.auth.upms.controller;

import com.arthur.auth.user.api.RemoteUserDetailService;
import com.arthur.auth.user.domain.entity.UserInfo;
import com.arthur.web.model.GenericServerResponse;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户详情
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-18
 * @since 1.0
 */
@RestController
public class UserDetailController implements RemoteUserDetailService {

	@Override
	public GenericServerResponse<UserInfo> getUserInfo(String username) {
		return null;
	}

}
