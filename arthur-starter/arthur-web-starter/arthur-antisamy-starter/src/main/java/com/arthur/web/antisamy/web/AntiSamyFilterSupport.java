package com.arthur.web.antisamy.web;

import com.arthur.web.antisamy.context.AntiSamyConfig;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.context.AntiSamyService;

/**
 * AntiSamy过滤器支持
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-07
 * @since 1.0
 */
public interface AntiSamyFilterSupport {

	default AntiSamyService getAntiSamyService(AntiSamyConfig config, AntiSamyPolicyService antiSamyService) {
		return AntiSamyService.create(antiSamyService.getDefaultPolicy(), config);
	}

}
