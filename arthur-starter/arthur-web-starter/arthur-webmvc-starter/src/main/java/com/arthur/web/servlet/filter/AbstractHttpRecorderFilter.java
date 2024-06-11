package com.arthur.web.servlet.filter;

import com.arthur.web.servlet.constant.WebMvcConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 记录请求和响应报文的过滤器
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractHttpRecorderFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractHttpRecorderFilter.class);
	private static final int DEFAULT_MAX_SIZE = 1024 * 512;

	private int maxSize = DEFAULT_MAX_SIZE;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain chain) throws ServletException, IOException {
		HttpServletRequest cachingRequest = request;

		boolean isCandidateHttpMethod = HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod());
		if (!this.isAsyncDispatch(request) && !(request instanceof ContentCachingRequestWrapper) && isCandidateHttpMethod) {
			cachingRequest = new ContentCachingRequestWrapper(request);
		}

		HttpServletResponse cachingResponse = response;
		if (!(response instanceof ContentCachingResponseWrapper) && isCandidateHttpMethod) {
			cachingResponse = new ContentCachingResponseWrapper(response);
		}

		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		try {
			chain.doFilter(cachingRequest, cachingResponse);
			status = response.getStatus();
		} finally {
			if (!this.isAsyncStarted(cachingRequest) && this.isCandidateStatus(status)) {
				this.recordBody(this.createRequest(cachingRequest), this.createResponse(cachingResponse));
			} else {
				this.copyBodyToResponse(cachingResponse);
			}
		}
	}

	private void copyBodyToResponse(HttpServletResponse response) {
		ContentCachingResponseWrapper cachingResponse = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
		if (cachingResponse != null) {
			try {
				cachingResponse.copyBodyToResponse();
			} catch (IOException e) {
				LOG.error("Fail to write response body back", e);
			}
		}
	}

	private String createRequest(HttpServletRequest request) {
		String payload = "";
		ContentCachingRequestWrapper cachingRequest = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (cachingRequest != null) {
			byte[] bytes = cachingRequest.getContentAsByteArray();
			payload = this.getPayload(payload, bytes, cachingRequest.getCharacterEncoding());
		}

		return payload;
	}

	private String createResponse(HttpServletResponse response) {
		String payload = "";
		ContentCachingResponseWrapper cachingResponse = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
		if (cachingResponse != null) {
			byte[] bytes = cachingResponse.getContentAsByteArray();
			try {
				cachingResponse.copyBodyToResponse();
			} catch (IOException e) {
				LOG.error("Fail to write response body back", e);
			}

			payload = this.getPayload(payload, bytes, cachingResponse.getCharacterEncoding());
		}

		return payload;

	}

	private String getPayload(String payload, byte[] bytes, String charset) {
		if (bytes.length > 0 && bytes.length < maxSize) {
			try {
				payload = new String(bytes, 0, bytes.length, charset);
			} catch (UnsupportedEncodingException e) {
				payload = WebMvcConstants.UNKNOWN_PAYLOAD;
			}
		}

		return payload;
	}


	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * 是否候选的响应码
	 *
	 * @param status 响应码
	 * @return yes or no
	 */
	protected abstract boolean isCandidateStatus(int status);

	/**
	 * 记录请求和响应内容
	 *
	 * @param request  请求内容
	 * @param response 响应内容
	 */
	protected abstract void recordBody(String request, String response);

}
