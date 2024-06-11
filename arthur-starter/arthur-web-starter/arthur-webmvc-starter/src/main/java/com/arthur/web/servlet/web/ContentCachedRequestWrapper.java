package com.arthur.web.servlet.web;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 实现缓存请求内容的{@link org.springframework.http.server.ServletServerHttpRequest}
 * <p>
 * 解决请求内容只能读取一次的问题
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @see ContentCachingRequestWrapper
 * @since 1.0
 */
public class ContentCachedRequestWrapper extends HttpServletRequestWrapper {

	protected byte[] cachedContent;
	protected ContentCachedInputStream cachedInputStream;

	public ContentCachedRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@NonNull
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (cachedInputStream == null) {
			this.initializeCachedInputStream();
		}
		return cachedInputStream;
	}

	@Override
	public BufferedReader getReader() {
		try {
			return new BufferedReader(new InputStreamReader(this.getInputStream()));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void initializeCachedInputStream() throws IOException {
		byte[] cachedContent = StreamUtils.copyToByteArray(getRequest().getInputStream());
		this.cachedContent = ObjectUtils.isEmpty(cachedContent) ?
			cachedContent : this.handleRequestContent(cachedContent);
		this.cachedInputStream = new ContentCachedInputStream(this.cachedContent);
	}

	/**
	 * 处理请求内容
	 *
	 * @param cachedContent 请求内容字节数组
	 * @return 处理后的请求字节数组
	 * @throws UnsupportedEncodingException 未知的编码
	 */
	protected byte[] handleRequestContent(byte[] cachedContent) throws UnsupportedEncodingException {
		return cachedContent;
	}

	/**
	 * 获取缓存的请求内容
	 */
	@Nullable
	public byte[] getCachedContent() {
		return cachedContent;
	}

}
