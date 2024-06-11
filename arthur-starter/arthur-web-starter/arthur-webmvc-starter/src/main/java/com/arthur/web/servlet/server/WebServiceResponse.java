package com.arthur.web.servlet.server;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * 自定义响应{@link HttpServletResponse}返回信息
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-20
 * @since 1.0
 */
public class WebServiceResponse {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public WebServiceResponse(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * @see #writeTo(Object, MediaType, HttpServletResponse)
     */
    public void writeTo(Object body, HttpServletResponse response) throws IOException {
        String content = response.getContentType();
        if (content == null) {
            content = response.getHeader(HttpHeaders.CONTENT_TYPE);
        }
        MediaType contentType = MediaType.parseMediaType(content);
        this.writeTo(body, contentType, response);
    }

    /**
     * 写JSON数据到响应
     *
     * @see #writeTo(Object, MediaType, HttpServletResponse)
     */
    public void jsonWriteTo(Object body, HttpServletResponse response) throws IOException {
        this.writeTo(body, MediaType.APPLICATION_JSON, response);
    }

    /**
     * 写消息到响应
     *
     * @param body        相应内容
     * @param contentType 请求类型
     * @param response    {@link HttpServletResponse}
     * @throws IOException IO异常
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void writeTo(Object body, MediaType contentType, HttpServletResponse response) throws IOException {
        List<HttpMessageConverter<?>> converters = messageConverters.getObject().getConverters();
        for (HttpMessageConverter converter : converters) {
            if (converter.canWrite(body.getClass(), contentType)) {
                converter.write(body, contentType, new ServletServerHttpResponse(response));
                return;
            }
        }
    }

}
