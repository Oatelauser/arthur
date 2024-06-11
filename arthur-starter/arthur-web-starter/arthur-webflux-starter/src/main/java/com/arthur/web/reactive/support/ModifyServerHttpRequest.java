package com.arthur.web.reactive.support;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;

/**
 * 请求装饰器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-10
 * @since 1.0
 */
public class ModifyServerHttpRequest extends ServerHttpRequestDecorator {

    private final HttpHeaders headers;
    private final CachedBodyHttpOutputMessage outputMessage;


    public ModifyServerHttpRequest(HttpHeaders headers, ServerHttpRequest delegate,
            CachedBodyHttpOutputMessage outputMessage) {
        super(delegate);
        this.headers = headers;
        this.outputMessage = outputMessage;
    }

    @NonNull
    @Override
    public HttpHeaders getHeaders() {
        long contentLength = headers.getContentLength();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        if (contentLength > 0) {
            httpHeaders.setContentLength(contentLength);
        } else {
            httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
        }
        return httpHeaders;
    }

    @NonNull
    @Override
    public Flux<DataBuffer> getBody() {
        return outputMessage.getBody();
    }

}
