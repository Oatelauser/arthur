package com.arthur.plugin.ssl.support;

import com.arthur.common.invoke.LambdaMetafactorys;
import com.arthur.common.invoke.Lookups;
import org.springframework.boot.web.embedded.netty.SslServerCustomizer;
import org.springframework.util.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;

import static java.lang.invoke.MethodType.methodType;

/**
 * 配置具有别名的key store
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-03-28
 * @since 1.0
 */
public abstract class KeyManagerFactoryProvider {

	/**
	 * 创建具有alias的key store方法
	 */
	private static final BiFunction<String, String, KeyManagerFactory> methodInvoker;

	static {
		try {
			methodInvoker = createLambdaFunction();
		} catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	public static KeyManagerFactory getInstance(String alias, String algorithm) throws NoSuchAlgorithmException {
		if (!StringUtils.hasText(alias)) {
			return KeyManagerFactory.getInstance(algorithm);
		}
		return methodInvoker.apply(alias, algorithm);
	}

	/**
	 * {@code SslServerCustomizer.ConfigurableAliasKeyManagerFactory#ConfigurableAliasKeyManagerFactory(String, String)}
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	private static BiFunction<String, String, KeyManagerFactory> createLambdaFunction() throws Throwable {
		Class<KeyManagerFactory> keyManagerClass = (Class<KeyManagerFactory>) Class.forName(SslServerCustomizer.class.getName()
			+ "$ConfigurableAliasKeyManagerFactory");
		MethodHandles.Lookup lookup = Lookups.trustedLookup(keyManagerClass);
		MethodHandle methodHandle = lookup.findConstructor(keyManagerClass, methodType(void.class, String.class, String.class));
		return (BiFunction<String, String, KeyManagerFactory>) LambdaMetafactorys.createLambda(BiFunction.class, lookup, methodHandle)
			.getTarget()
			.invokeExact();
	}

}
