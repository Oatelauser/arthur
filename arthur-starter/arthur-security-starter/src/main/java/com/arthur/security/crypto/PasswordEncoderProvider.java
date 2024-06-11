package com.arthur.security.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-29
 * @since 1.0
 */
public interface PasswordEncoderProvider {

	String encodingId();

	PasswordEncoder obtainsEncoder();

}
