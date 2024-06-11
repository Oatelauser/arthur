package com.arthur.web.servlet.filter;

import com.arthur.web.servlet.utils.ServletRequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.arthur.web.constant.RemoteIpConstants.REMOTE_FILTER_ORDER;
import static com.arthur.web.constant.RemoteIpConstants.REMOTE_IP_ATTR;

/**
 * 请求IP地址解析过滤器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-01
 * @since 1.0
 */
public class ServletRemoteIpRequestFilter extends OncePerRequestFilter implements Ordered {

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		String remoteIp = ServletRequestUtils.resolveRemoteIp(request);
		request.setAttribute(REMOTE_IP_ATTR, remoteIp);
		filterChain.doFilter(request, response);
	}

	@Override
	public int getOrder() {
		return REMOTE_FILTER_ORDER;
	}

}
