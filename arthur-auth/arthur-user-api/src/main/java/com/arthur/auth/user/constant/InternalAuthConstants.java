package com.arthur.auth.user.constant;

/**
 * 内部授权请求头信息常量
 *
 * @author DearYang
 * @date 2022-08-17
 * @since 1.0
 */
public interface InternalAuthConstants {

    /**
     * 请求头中的特殊标志头
     */
    String INTERNAL_AUTH_HEADER_NAME = "X-Internal-Authorization";

    /**
     * 内部特殊标志的值
     */
    String INTERNAL_AUTH_HEADER_VALUE = "Y";

	/**
	 * 完整的验证头信息，k=v形式
	 */
	String INTERNAL_AUTH_HEADER = INTERNAL_AUTH_HEADER_NAME + "=" + INTERNAL_AUTH_HEADER_VALUE;

}
