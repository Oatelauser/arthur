package com.arthur.security.crypto;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Security加密工厂
 * <p>
 * 拓展{@link DelegatingPasswordEncoder}无法动态加载{@link PasswordEncoder}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-29
 * @see PasswordEncoder
 * @see PasswordEncoderProvider
 * @since 1.0
 */
public class PasswordEncoderFactory {

	/**
	 * 创建{@link DelegatingPasswordEncoder}加密类
	 *
	 * @param provider 加密提供者
	 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
	 */
	public static PasswordEncoder createDelegatingPasswordEncoder(ObjectProvider<List<PasswordEncoderProvider>> provider) {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> passwordEncoderMappings = new HashMap<>(20);
		loadDefaultPasswordEncoders(encodingId, passwordEncoderMappings);
		provider.ifAvailable(encoders -> encoders.forEach(encoder ->
			passwordEncoderMappings.put(encoder.encodingId(), encoder.obtainsEncoder())));
		return new DelegatingPasswordEncoder(encodingId, passwordEncoderMappings);
	}

	/**
	 * copy by {@link org.springframework.security.crypto.factory.PasswordEncoderFactories#createDelegatingPasswordEncoder()}
	 */
	@SuppressWarnings("deprecation")
	private static void loadDefaultPasswordEncoders(String encodingId, Map<String, PasswordEncoder> passwordEncoderMappings) {
		passwordEncoderMappings.put(encodingId, new BCryptPasswordEncoder());
		passwordEncoderMappings.put("ldap", new LdapShaPasswordEncoder());
		passwordEncoderMappings.put("MD4", new Md4PasswordEncoder());
		passwordEncoderMappings.put("MD5", new MessageDigestPasswordEncoder("MD5"));
		passwordEncoderMappings.put("noop", NoOpPasswordEncoder.getInstance());
		passwordEncoderMappings.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		//encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		passwordEncoderMappings.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
		//encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
		passwordEncoderMappings.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
		passwordEncoderMappings.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
		//encoders.put("sha256", new StandardPasswordEncoder());
		passwordEncoderMappings.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		//encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
	}

}
