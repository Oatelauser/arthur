package com.broadtech.arthur.admin.auth.respones;

import com.alibaba.fastjson2.JSON;
import com.nimbusds.jose.jwk.JWKSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/8/22
 */
@RestControllerAdvice
@Slf4j
public class GatewayResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    /**
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }



    /**
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return new ResponseVo<>(null, ReturnCode.RC101, "NO DATA");
        } else if(body instanceof String){
            return JSON.toJSONString(ResponseVo.SUCCESS(body));
        }else if (body instanceof ResponseVo){
            return body;
        } else if (body instanceof JWKSet) {
            return ((JWKSet) body).toJSONObject();
        }
        return ResponseVo.SUCCESS(body);
    }


    /**
     * 异常
     *
     * @param e e
     * @return {@link ResponseVo}<{@link String}>
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVo<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return ResponseVo.FAIL(e.getMessage());
    }



}
