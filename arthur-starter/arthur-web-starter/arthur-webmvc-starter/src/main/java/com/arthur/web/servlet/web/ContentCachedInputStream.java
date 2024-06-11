package com.arthur.web.servlet.web;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;

/**
 * 缓存请求内容的Servlet输入流
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-13
 * @see ServletInputStream
 * @see ContentCachedRequestWrapper
 * @since 1.0
 */
public class ContentCachedInputStream extends ServletInputStream {

	private final ByteArrayInputStream is;

	public ContentCachedInputStream(byte[] content) {
		this.is = new ByteArrayInputStream(content);
	}

	@Override
	public boolean isFinished() {
		return is.available() == 0;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
	}

	@Override
	public int read() {
		return this.is.read();
	}

}
