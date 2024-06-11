package com.arthur.plugin.result;

/**
 * WebFluxResultFactory构建工厂
 *
 * @param <T> 结果对象泛型
 * @author DearYang
 * @date 2022-08-15
 * @see WebFluxResult
 * @since 1.0
 */
public interface WebFluxResultFactory<T> {

    WebFluxResult<T> newInstance();

}
